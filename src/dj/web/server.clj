(ns
    ^{:dj.codeloading/dependencies '[[http-kit "2.2.0"
                                      :exclusions [[org.clojure/clojure]]]]}
    dj.web.server
  (:require [org.httpkit.server :as server]))

;; Requires [http-kit "*"]

(defn ->dynamic-server
  "server that appends functionality while running

note, changing port after server is started will do nothing

Required keys:
:port
:handler

returns atom of opts and server
"
  [opts]
  (let [{:keys [port]} opts
        db (atom opts)
        server (server/run-server (fn [request]
                                    ((:handler @db) request))
                                  {:port port})]
    (swap! db
           assoc
           :server
           server)
    db))

(defn stop [server]
  ((:server @server)))
