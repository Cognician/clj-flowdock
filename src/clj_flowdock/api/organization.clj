(ns clj-flowdock.api.organization
  (:require [clj-flowdock.api :as api])
  (:refer-clojure :exclude [list get]))

(def route "organizations")

(defn list []
  (api/http-get route))

(defn get-users []
  ((first (list)) "users"))