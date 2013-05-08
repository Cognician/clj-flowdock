(ns clj-flowdock.api.user
  (:require [clj-flowdock.api :as api]
            [clj-flowdock.api.organization :as organization])
  (:refer-clojure :exclude [get list]))

(defn get-user [key value]
  (let [users (organization/get-users)]
    (first (filter #(.contains value (% key)) users))))

(defn get [id]
  (api/http-get (str "users/" id)))