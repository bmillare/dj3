(ns dj.init.cidernrepl)

(require '[cemerick.pomegranate :as cp]
         '[cemerick.pomegranate.aether :as a])

(cp/add-dependencies
 :coordinates '[[refactor-nrepl "2.3.1"]
                [cider/cider-nrepl "0.14.0"]]
 :repositories (merge a/maven-central
                      {"clojars" "https://clojars.org/repo"}))

(require '[clojure.tools.nrepl.server]
         '[cider.nrepl])

(clojure.tools.nrepl.server/start-server :port 9000 :handler cider.nrepl/cider-nrepl-handler)
