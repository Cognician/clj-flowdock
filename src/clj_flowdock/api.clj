(ns clj-flowdock.api
  (:require [clj-flowdock.message :as m]
            [clj-http.client :as client]
            [clojure.string :as s]
            [cheshire.core :as json]))

(def basic-auth-token "9920ebbdac4ddde778fe5173348ebdb6")
(def api-url "https://api.flowdock.com/")

(defn http-get
  ([path] (http-get path {}))
  ([path query-params]
    (:body (client/get (str api-url path) {:basic-auth basic-auth-token
                                           :query-params query-params
                                           :as :json-string-keys}))))

(defn http-post [path params]
  (client/post (str api-url path) {:body (json/generate-string params)
                                   :basic-auth basic-auth-token
                                   :content-type :json
                                   :throw-exceptions false}))

(defn http-put [path params]
  (client/put (str api-url path) {:body (json/generate-string params)
                                  :basic-auth basic-auth-token
                                  :content-type :json
                                  :throw-exceptions false}))

(defn http-delete [path]
  (client/delete (str api-url path) {:basic-auth basic-auth-token
                                     :content-type :json
                                     :throw-exceptions false}))

(def organization (memoize #(-> (http-get "organizations") first m/id)))
(def user-info (memoize #(http-get (str "users/" %))))
(def message (memoize #(http-get (str "flows/" (organization) "/" %1 "/messages/" %2))))

(defn flows []
  (let [flows (http-get "flows?users=1")]
    (map m/id flows)))

(defn send-message [flow message]
  (http-post
    (str "flows/" (organization) "/" flow "/messages")
    message))

(defn parent-message [child-message]
  (when-let [parent-id (m/parent-message-id child-message)]
    (message (m/flow child-message) parent-id)))

(defn send-private-message [user-id content]
  (http-post
    (str "private/" user-id "/messages")
    (m/create-message content)))

(defn chat [flow chat-string]
  (send-message flow (m/create-message chat-string)))

(defn reply
  ([reply-packet] (reply (:original reply-packet) (:response reply-packet)))
  ([message content]
    (let [message-content (str "@" (m/user-nick message) ", " content)]
      (send-message (m/flow message) (m/create-message message-content)))))
