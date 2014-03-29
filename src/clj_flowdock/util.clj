(ns clj-flowdock.util)

(defn config-property [name]
  (or (System/getenv name) (System/getProperty name)))
