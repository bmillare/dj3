(try
  (clojure.core/require 'clojure.tools.namespace.file)
  (clojure.core/require 'clojure.tools.namespace.parse)
  (catch java.io.FileNotFoundException e
    (require 'dj.codeloading.maven)
    (dj.codeloading.maven/load {:coordinates '[[org.clojure/tools.namespace "0.2.11" :exclusions [[org.clojure/clojure]]]]})))

(ns
    ^{:dj.codeloading/dependencies '[[org.clojure/tools.namespace "0.2.11"
                                     :exclusions [[org.clojure/clojure]]]]}
    dj.codeloading.namespace
  (:require [dj]
            [dj.classloader :as dc]
            [dj.codeloading.maven :as dcm]
            [clojure.tools.namespace.file :as ctnf]
            [clojure.tools.namespace.parse :as ctnp]
            [clojure.set :as cs]))

(defn ns-name->repo-relative-path [n]
  (str (dj/replace-map n {"." "/"})
       ".clj"))

(defn find-ns-form
  "get namespace dependencies from ns form"
  [ns-name]
  (-> ns-name
      ns-name->repo-relative-path
      dc/find-resource
      ctnf/read-file-ns-decl))

(defn analyze-ns [ns-name]
  (let [raw-ns-form (find-ns-form ns-name)]
    {:dependent-namespaces (ctnp/deps-from-ns-decl raw-ns-form)
     :dependent-coordinates (set (-> raw-ns-form
                                     second
                                     meta
                                     :dj.codeloading/dependencies
                                     second))}))

(defn analyze-ns-recursive [ns-name]
  (loop [ret-dependent-namespaces #{}
         ret-dependent-coordinates #{}
         unvisited-namespaces #{ns-name}]
    (if (empty? unvisited-namespaces)
      {:dependent-namespaces ret-dependent-namespaces
       :dependent-coordinates ret-dependent-coordinates}
      (let [this-ns (first unvisited-namespaces)
            {:keys [dependent-namespaces
                    dependent-coordinates]} (try
                                              (analyze-ns this-ns)
                                              (catch Exception e
                                                {:dependent-namespaces #{}
                                                 :dependent-coordinates #{}}))]
        (recur (conj ret-dependent-namespaces
                     this-ns)
               (cs/union ret-dependent-coordinates
                         dependent-coordinates)
               (cs/union (disj unvisited-namespaces this-ns)
                         (cs/difference dependent-namespaces
                                        ret-dependent-namespaces)))))))

(defn load-ns
  "loads namespace with specified coordinates in the namespace
  metadata :dj.codeloading/dependencies and well as recursively for
  its dependent namespaces"
  [ns-name]
  (dcm/load {:coordinates (-> ns-name
                              analyze-ns-recursive
                              :dependent-coordinates
                              vec)}))
