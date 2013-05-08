(ns clj-flowdock.api.flow
  (:require [clj-flowdock.api :as api])
  (:refer-clojure :exclude [get list find]))

(defn list []
  (api/http-get "flows"))

(defn list-all []
  (api/http-get "flows/all"))

(defn find [key value]
  (first (filter #(.contains value (% key)) (list-all))))

(defn get [id]
  (api/http-get (str "flows/" id)))

(defn add-user [flow-id user-id]
  (api/http-post (str "flows/" flow-id "/users") {:id user-id}))

(defn block-user [flow-id user-id]
  (api/http-put (str "flows/" flow-id "/users/" user-id) {:id user-id :disabled true}))
