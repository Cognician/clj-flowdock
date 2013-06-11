(ns clj-flowdock.streaming
  (:require [clj-flowdock.api :as api]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log])
  (:refer-clojure :exclude [read]))

(defprotocol Connection
  (read [this] "Read from connection, returning a json map")
  (close [this] "Close connection"))

(deftype FlowConnection [flow-id reader]
  Connection
  (read [this]
    (json/parse-string (.readLine reader)))
  (close [this]
    (.close reader)))

(defn- flow-stream-url [flow-id]
  (str "http://stream.flowdock.com/flows/" flow-id "?active=true"))

(defn- user-stream-url []
  (str "http://stream.flowdock.com/flows/?active=true&user=1"))

(defn streaming-url [flow-id]
  (if (s/blank? flow-id)
    (user-stream-url)
    (flow-stream-url flow-id)))

(defn open [flow-id]
  (let [response (client/get (streaming-url flow-id) {:as :stream :basic-auth api/basic-auth-token})]
    (log/info "Streaming messages from:" flow-id)
    (FlowConnection. flow-id (io/reader (:body response)))))

