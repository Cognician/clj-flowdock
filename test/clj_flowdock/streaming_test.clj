(ns clj-flowdock.streaming-test
  (:use clojure.test)
  (:require [clj-flowdock.streaming :as streaming]))

(deftest streaming-url
  (is (= "http://stream.flowdock.com/flows/test-flow?active=true"
        (streaming/streaming-url "test-flow")))
  (is (= "http://stream.flowdock.com/flows/?active=true&user=1"
        (streaming/streaming-url ""))))