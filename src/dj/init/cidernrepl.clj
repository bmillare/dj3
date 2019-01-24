(ns dj.init.cidernrepl)

(require '[cemerick.pomegranate :as cp]
         '[cemerick.pomegranate.aether :as a]
         '[dynapath.dynamic-classpath :as dcp])

(-> (Thread/currentThread)
    (.setContextClassLoader (clojure.lang.RT/baseLoader)))

;; Not sure if this is necessary
#_ (extend jdk.internal.loader.ClassLoaders$AppClassLoader
  dcp/DynamicClasspath
  (assoc dcp/base-readable-addable-classpath
    :classpath-urls #(seq (.getURLs %))
    :can-add? (constantly false)))

(cp/add-dependencies
 :coordinates '[[refactor-nrepl "2.4.0"]
                [cider/cider-nrepl "0.20.0"]]
 :repositories (merge a/maven-central
                      {"clojars" "https://clojars.org/repo"}))

(require '[nrepl.server]
         '[cider.nrepl])

(let [port 9001]
  (println "starting cider nrepl server on port" port)
  (nrepl.server/start-server
   :port port :handler cider.nrepl/cider-nrepl-handler)
  (println "running cider nrepl server on port" port))
