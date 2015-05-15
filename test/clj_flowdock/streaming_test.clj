(ns clj-flowdock.streaming-test
  (:use clojure.test)
  (:require [clj-flowdock.streaming :as streaming]))

(deftest streaming-url
  (is (= "http://stream.flowdock.com/flows/test-flow?active=true"
        (streaming/streaming-url "test-flow")))
  (is (= "http://stream.flowdock.com/flows/?active=true&user=1"
         (streaming/streaming-url ""))))

(deftest streaming
  (testing "that you can start streaming like this"
    (streaming/connect "" #(println %) :timeout 1000))

  (testing "that it fails when no authentication token provided"))
