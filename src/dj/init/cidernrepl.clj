(ns dj.init.cidernrepl)

(require '[cemerick.pomegranate :as cp]
         '[cemerick.pomegranate.aether :as a])

(cp/add-dependencies
 :coordinates '[[refactor-nrepl "2.3.1"]
                [cider/cider-nrepl "0.15.1"]]
 :repositories (merge a/maven-central
                      {"clojars" "https://clojars.org/repo"}))

(require '[clojure.tools.nrepl.server]
         '[cider.nrepl])

(println "starting cider nrepl server on port 9000")
(clojure.tools.nrepl.server/start-server :port 9000 :handler cider.nrepl/cider-nrepl-handler)
(println "running cider nrepl server on port 9000")
