(ns clj-flowdock.api.flow
  (:require [clj-flowdock.api :as api]
            [clojure.string :as s]
            [clj-flowdock.api.user :as user])
  (:refer-clojure :exclude [get list find]))

(declare flow->flow-id)

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

(defn add-myself [flow]
  (add-user (flow->flow-id flow) (clojure.core/get (user/me) "id")))

(defn flow->flow-id [flow]
  (str (clojure.core/get-in flow ["organization" "parameterized_name"]) "/" (clojure.core/get flow "parameterized_name")))
