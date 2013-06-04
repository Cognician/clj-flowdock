(ns clj-flowdock.streaming
  (:require [clj-flowdock.api :as api]
            [clj-flowdock.api.flow :as flow]
            [clj-flowdock.util :as u]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [clojure.string :as s]
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

(defn- streaming-url [flow]
  (str "http://stream.flowdock.com/flows/" flow "?active=true&user=1"))

(defn open [flow]
  (println (streaming-url flow))
  (let [response (client/get (streaming-url flow) {:as :stream :basic-auth api/basic-auth-token})]
    (log/info "Streaming messages from:" flow)
    (FlowConnection. flow (io/reader (:body response)))))

