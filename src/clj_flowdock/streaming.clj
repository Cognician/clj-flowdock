(ns clj-flowdock.streaming
  (:require [clj-flowdock.api :as api]
            [clj-flowdock.api.flow :as flow]
            [clj-flowdock.util :as u]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [clojure.string :as s]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]))

(def stream-url "http://stream.flowdock.com/flows")

(defn- open-message-stream []
  (let [flows (map #(% "id") (flow/list))
        url (str stream-url "?active=true&filter=" (s/join "," flows))
        response (client/get url {:as :stream :basic-auth api/basic-auth-token})]
    (log/info "Streaming messages from:" url)
    (io/reader (:body response))))

(defn- message-seq [reader]
  (->> (line-seq reader)
    (remove s/blank?)
    (map #(json/parse-string %))))

(defn- map-messages [f]
  (with-open [message-reader (open-message-stream)]
    (log/info "Message Stream Open")
    (doseq [message (message-seq message-reader)]
      (f message))))

(defn messages []
  (let [[s queue-insert] (u/pipe 10000)
        message-thread (Thread. #(map-messages queue-insert) "FlowDock Streaming Thread")]
    (.start message-thread)
    s))