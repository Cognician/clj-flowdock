(ns clj-flowdock.api
  (:require [clj-http.client :as client]
            [cheshire.core :as json]))

(defn- config-property [name]
  (or (System/getenv name) (System/getProperty name)))

(def basic-auth-token (config-property "FLOWDOCK_TOKEN"))
(def api-url (or (config-property "FLOWDOCK_API_URL") "https://api.flowdock.com/"))

(defn http-get
  ([path] (http-get path {}))
  ([path query-params]
    (:body (client/get
             (str api-url path)
             {:basic-auth basic-auth-token
              :query-params query-params
              :as :json-string-keys}))))

(defn http-post [path params]
  (client/post (str api-url path) {:body (json/generate-string params)
                                   :basic-auth basic-auth-token
                                   :content-type :json
                                   :throw-exceptions false}))

(defn http-put [path params]
  (client/put (str api-url path) {:body (json/generate-string params)
                                  :basic-auth basic-auth-token
                                  :content-type :json
                                  :throw-exceptions false}))

(defn http-delete [path]
  (client/delete (str api-url path) {:basic-auth basic-auth-token
                                     :content-type :json
                                     :throw-exceptions false}))
