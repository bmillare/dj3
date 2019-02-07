(ns dj.codeloading.maven
  (:require [cemerick.pomegranate :as cp]
            [cemerick.pomegranate.aether :as a])
  (:refer-clojure :exclude [load]))

(let [default
      {:repositories
       (merge a/maven-central
              {"clojars" "https://clojars.org/repo"})}]
  (defn load
    "Usage:

  (load {:coordinates '[[org.clojure/tools.namespace \"0.2.11\" :exclusions [[org.clojure/clojure]]]]})"
    [args]
    (apply cp/add-dependencies
           (apply concat
                  (merge default
                         args)))))
