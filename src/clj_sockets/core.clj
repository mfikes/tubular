;; Same as https://github.com/atroche/clj-sockets/blob/master/src/clj_sockets/core.clj
;; but without clojure.core.typed
(ns clj-sockets.core
  "Contains all of the functions that make up the public API of this project."
  (:require [clojure.java.io :refer [writer reader]])
  (:refer-clojure :exclude [read-line])
  (:import (java.net Socket ServerSocket)
           (java.io BufferedWriter BufferedReader)
           (clojure.lang Seqable)))

(defn create-socket
  "Connect a socket to a remote host. The call blocks until
   the socket is connected."
  [^String hostname ^Integer port]
  (Socket. hostname port))

(defn close-socket
  "Close the socket, and also closes its input and output streams."
  [^Socket socket]
  (.close socket))

(defn ^:private write-to-buffer
  "Write a string to a BufferedWriter, then flush it."
  [^BufferedWriter output-stream ^String string]
  (.write output-stream string)
  (.flush output-stream))

(defn write-to
  "Send a string over the socket."
  [socket message]
  (write-to-buffer (writer socket) message))

(defn write-line
  "Send a line over the socket."
  [socket message]
  (write-to socket (str message "\n")))

; this is memoized so that we always get the same reader for
; a given socket. otherwise the temporary readers could have text
; loaded into their buffers and then never used
(def ^:private get-reader
  "Get the BufferedReader for a socket.
  This is memoized so that we always get the same reader for a given socket.
  If we didn't do this, every time we did e.g. read-char on a socket we'd
  get back a new reader, and that last one would be thrown away despite having
  loaded input into its buffer."
  (memoize (fn [^Socket socket]
             (reader socket))))

(defn read-char
  "Read a single character from a socket."
  [socket]
  (let [read-from-buffer (fn [^BufferedReader input-stream]
                           (.read input-stream))]
    (-> socket
      get-reader
      read-from-buffer
      char)))

(defn read-lines
  "Read all the lines currently loaded into the input stream of a socket."
  [socket]
  (line-seq (get-reader socket)))

(defn read-line
  "Read a line from the given socket"
  [^Socket socket]
  (let [read-line-from-reader (fn [^BufferedReader reader]
                                (.readLine reader))]
    (read-line-from-reader (get-reader socket))))

(defn create-server
  "Initialise a ServerSocket on localhost using a port.
  Passing in 0 for the port will automatically assign a port based on what's
  available."
  [^Integer port]
  (ServerSocket. port))

(defn listen
  "Waits for a connection from another socket to come through, then
   returns the server's now-connected Socket."
  [^ServerSocket server-socket]
  (.accept server-socket))