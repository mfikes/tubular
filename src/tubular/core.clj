(ns tubular.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clj-sockets.core :refer [create-socket write-line read-char]]))

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
    (if-let [line (read-line)]
      (do (write-line socket line)
          (when (= line ":repl/quit")
            (reset! running false)))
      (reset! running false))))

(defn connect
  ([port]
    (connect "localhost" port))
  ([hostname port]
   (let [socket (create-socket hostname port)
         running (atom true)]
     (doto (Thread. #(run-reader socket running))
       (.setDaemon true)
       (.start))
     (run-writer socket running))))

(def cli-options
  [["-H" "--host HOST" "Address to bind"
    :default "localhost"]
   ["-p" "--port PORT" "Port number"
    :default 5555
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-h" "--help"]])

(defn -main [& args]
  (let [{:keys [options] :as parsed} (parse-opts args cli-options)]
    (if (:help options)
      (println (:summary parsed))
      (connect (:host options) (:port options)))))
