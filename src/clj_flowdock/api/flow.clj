(ns clj-flowdock.api.flow
  (:require [clj-flowdock.api :as api])
  (:refer-clojure :exclude [get list find]))

(def route "flows/")

(defn list []
  (api/http-get route))

(defn list-all []
  (api/http-get (str route "all")))

(defn find [key value]
  (first (filter #(.contains value (% key)) (list-all))))

(defn get [id]
  (api/http-get (str route id)))

(defn add-user [flow-id user-id]
  (api/http-post (str route flow-id "/users") {:id user-id}))

(defn block-user [flow-id user-id]
  (api/http-put (str route flow-id "/users/" user-id) {:id user-id :disabled true}))

(defn create [org-id name]
  (api/http-post (str route org-id) {:name name}))

(defn update [flow-id attributes]
  (api/http-put (str route flow-id) attributes))