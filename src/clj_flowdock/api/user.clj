(ns clj-flowdock.api.user
  (:require [clj-flowdock.api :as api]
            [clj-flowdock.api.organization :as organization])
  (:refer-clojure :exclude [get find list]))

(def route "users/")

(defn list []
  (api/http-get route))

(defn find [key value]
   (first (filter #(.contains value (% key)) (list))))

(defn get [id]
  (api/http-get (str route id)))

(defn me []
  (first (list)))