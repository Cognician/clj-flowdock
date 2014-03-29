(ns clj-flowdock.api.push
  (:require [clj-flowdock.api :as api]))

(defn push-api-url [flow-token path]
  (str "v1/messages/" path "/" flow-token))

;; API Reference https://flowdock.com/api/team-inbox

(defn to-team-inbox [flow-token source from-address subject content
                     & {:keys [from-name reply-to project format tags link]}]
  (api/http-post (push-api-url flow-token "team_inbox")
                 (cond-> {:source source
                          :from_address from-address
                          :subject subject
                          :content content}
                         from-name (assoc :from_name from-name)
                         reply-to  (assoc :reply_to reply-to)
                         project   (assoc :project project)
                         format    (assoc :format format)
                         tags      (assoc :tags tags)
                         link      (assoc :link link))))

;; API Reference https://flowdock.com/api/chat

(defn to-chat [flow-token content external-user-name & {:keys [tags message-id]}]
  (api/http-post (push-api-url flow-token "chat")
                 (cond-> {:content content
                          :external_user_name external-user-name}
                         tags       (assoc :tags tags)
                         message-id (assoc :message_id message-id))))
