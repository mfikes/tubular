(ns tubular.core
  (:require [clj-sockets.core :refer [create-socket write-line read-char]]))

(defn- run-reader
  [socket running]
  (while @running
    (try
      (print (read-char socket))
      (flush)
      (catch Throwable _
        (reset! running false)))))

(defn- run-writer
  [socket running]
  (while @running
    (let [line (read-line)]
      (write-line socket line)
      (when (= line ":cljs/quit")
        (reset! running false)))))

(defn connect
  ([port]
    (connect "localhost" port))
  ([hostname port]
   (let [socket (create-socket hostname port)
         running (atom true)]
     (.start (Thread. #(run-reader socket running)))
     (run-writer socket running))))
