(ns clj-flowdock.streaming
  (:require [clj-flowdock.api :as api]
            [clj-flowdock.settings :refer :all]
            [clj-http.client :as client]
            [http.async.client :as http]
            [cheshire.core :as json]
            [clojure.java.io :as io]
            [clojure.string :as s]
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
  (str "https://stream.flowdock.com/flows/" flow-id "?active=true"))

(defn- user-stream-url []
  (str "https://stream.flowdock.com/flows/?active=true&user=1"))

(defn streaming-url [flow-id]
  (if (s/blank? flow-id)
    (user-stream-url)
    (flow-stream-url flow-id)))

(defn open [flow-id]
  (let [response (client/get (streaming-url flow-id) {:as :stream :basic-auth basic-auth-token})]
    (log/info "Streaming messages from:" flow-id)
    (FlowConnection. flow-id (io/reader (:body response)))))

(def ^{:private true} never -1)

(defn- with-defaults [opts]
  (merge {:timeout never} opts))

(defn connect [flow-id callback & args]
  "Opens a streaming connection on <flow-id>, invoking <callback> whenever a message arrive. See <http://neotyk.github.io/http.async.client/docs.html#sec-2-3-7>"
  (let [opts (with-defaults (apply hash-map args))]
    (with-open [client (http/create-client)]
      (let [resp (http/stream-seq client :get (streaming-url flow-id) :auth {:user basic-auth-token :password ""} :timeout (:timeout opts))]
        (doseq [s (http/string resp)]
          (apply callback [s]))))))

