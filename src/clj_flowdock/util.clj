(ns clj-flowdock.util
  (:import [java.util.concurrent LinkedBlockingQueue]))

(defn- env-variable [name]
  (-> (System/getenv)
    (get name)))

(defn config-property [name]
  (or (env-variable name) (System/getProperty name)))

(defn pipe
  "Returns a vector containing a sequence that will read from the
   queue, and a function that inserts items into the queue.

   Based upon:
   http://clj-me.cgrand.net/2010/04/02/pipe-dreams-are-not-necessarily-made-of-promises/
   http://oobaloo.co.uk/clojure-from-callbacks-to-sequences"
  [queue-size]
  (let [q (LinkedBlockingQueue. queue-size)
        EOQ (Object.)
        NIL (Object.)
        s (fn queue-seq []
            (lazy-seq
              (let [x (.take q)]
                (when-not (= EOQ x)
                  (cons (when-not (= NIL x) x) (queue-seq))))))]
    [(s) (fn queue-insert ([] (queue-insert EOQ)) ([x] (.offer q (or x NIL))))]))