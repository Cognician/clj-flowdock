(ns clj-flowdock.settings)

(defn- config-property [name]
  (or (System/getenv name) (System/getProperty name)))

(def basic-auth-token (config-property "FLOWDOCK_TOKEN"))
(def api-url (or (config-property "FLOWDOCK_API_URL") "https://api.flowdock.com/"))
