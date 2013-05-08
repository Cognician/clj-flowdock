(ns clj-flowdock.message
  (:require [clojure.string :as s]))

(defn- influx-tag? [tag]
  (.startsWith tag "influx"))

(def user #(get % "user"))
(def content #(get % "content"))
(def event #(get % "event"))
(def id #(get % "id"))

(defn user-email [message]
  (get-in message ["user" "email"]))

(defn user-nick [message]
  (get-in message ["user" "nick"]))

(def parent #(get % "parent"))

(def users #(get % "users"))
(def tags #(get % "tags"))

(def comment? #(= "comment" (event %)))
(def message? #(= "message" (event %)))

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
    (.startsWith (content message) (str "$" command))))

(defn flow [message]
  (-> message
    (get "flow")
    (s/split #":")
    second))

(defn create-message [content]
  {:event "message"
   :content content
   :external_user_name "Jarvis"})

