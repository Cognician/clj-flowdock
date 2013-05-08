(ns clj-flowdock.api.message
  (:require [clj-flowdock.api :as api]
            [clojure.string :as s])
  (:refer-clojure :exclude [list]))

(declare email nick command? parent-message-id flow create-message influx-tag?)

(def user #(get % "user"))
(def content #(get % "content"))
(def event #(get % "event"))
(def id #(get % "id"))
(def parent #(get % "parent"))
(def users #(get % "users"))
(def tags #(get % "tags"))
(def comment? #(= "comment" (event %)))
(def message? #(= "message" (event %)))

(defn list [flow-id]
  (api/http-get (str "flows/" flow-id "/messages")))

(defn get-message [flow-id message-id]
  (api/http-get (str "flows/" flow-id "/messages/" message-id)))

(defn edit [flow-id message-id message]
  (api/http-put (str "flows/" flow-id "/messages/" message-id) message))

(defn delete [flow-id message-id]
  (api/http-delete (str "flows/" flow-id "/messages/" message-id)))

(defn send-message [flow-id message]
  (api/http-post (str "flows/" flow-id "/messages") message))

(defn parent-message [child-message]
  (when-let [parent-id (parent-message-id child-message)]
    (get (flow child-message) parent-id)))

(defn send-private-message [user-id content]
  (api/http-post (str "private/" user-id "/messages") (create-message content)))

(defn chat [flow chat-string]
  (send-message flow (create-message chat-string)))

(defn reply
  ([reply-packet] (reply (:original reply-packet) (:response reply-packet)))
  ([message content]
    (let [message-content (str "@" (nick message) ", " content)]
      (send-message (flow message) (create-message message-content)))))

(defn email [message]
  (get-in message ["user" "email"]))

(defn nick [message]
  (get-in message ["user" "nick"]))

(defn parent-message-id [message]
  (when (comment? message)
    (->> message
      tags
      (filter influx-tag?)
      first
      (re-find #"(.+):(.+)")
      last)))

(defn command? [message command]
  (and (message? message)
    (.startsWith (content message) (str "@Jarvis $" command))))

(defn flow [message]
  (-> message
    (get "flow")
    (s/split #":")
    second))

(defn create-message [content]
  {:event "message"
   :content content
   :external_user_name "Jarvis"})

(defn- influx-tag? [tag]
  (.startsWith tag "influx"))
