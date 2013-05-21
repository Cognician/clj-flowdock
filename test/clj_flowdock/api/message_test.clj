(ns clj-flowdock.api.message-test
  (:use clojure.test)
  (:require [clj-flowdock.api.message :as message]))

(def sample-comment {"app" "chat", "edited" nil, "user" 29990, "flow" "rally-software:the-fellowship", "attachments" [], "sent" 1360870381666, "tags" [":user:35899" "influx:600555"], "content" {"title" "updated branch master with a commit", "text" "test comment"}, "event" "comment", "id" 603062, "uuid" "iAykQpTMoyErW6P7"})

;{app chat, user 29983, attachments [], sent 1369155254035, tags [:unread:35899], content TEST MESSAGE, event message, id 192485891, to 35899, uuid e3woxd4Id8BJ5C4r}
;{app chat, user 29983, flow rally-software:jarvish, attachments [], sent 1369155660434, tags [:user:35899 :unread:35899], content @Jarvis, TEST MESSAGE, event message, id 53967, uuid -WJMThzC-3uaYn8i}


(deftest flow-id
  (is (= "rally-software/the-fellowship" (message/flow-id {"flow" "rally-software:the-fellowship"})))
  (is (= "rally-software/alm" (message/flow-id {"flow" "rally-software:alm"}))))

(deftest comment?
  (is (= true (message/comment? sample-comment)))
  (is (= false (message/comment? {"event" "message"}))))

(deftest parent-message-id
  (is (= "600555" (message/parent-message-id sample-comment)))
  (is (nil? (message/parent-message-id {"event" "message"}))))
