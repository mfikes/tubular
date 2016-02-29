(ns tubular.core
  (:require [clj-sockets.core :refer [create-socket write-line read-char]]))

(defonce connected (atom false))

(defn- start-reader
  [socket]
  (.start
    (Thread.
      (fn []
        (loop []
          (when-let [c (try
                         (read-char socket)
                         (catch Throwable _
                           nil))]
            (print c)
            (flush)
            (recur)))
        (reset! connected false)))))

(defn- start-writer
  [socket]
  ((fn []
     (let [line (clojure.core/read-line)]
       (when @connected
         (write-line socket line)
         (when (not= line ":cljs/quit")
           (recur)))))))

(defn connect
  ([port]
    (connect "localhost" port))
  ([hostname port]
   (let [socket (create-socket hostname port)]
     (reset! connected true)
     (start-reader socket)
     (start-writer socket))))
