(ns comms.verify
  (:require [comms.nominate :refer [nominations]]
            [clojure.core.async :refer [chan go-loop <!! >!!]]
            [mount.core :refer [defstate]]
            [clojure.pprint :refer [pprint]]))

(def verify? (atom true))
(def verifications (chan 100))

(defn evaluate
  []
  (let [nomination (<!! nominations)
        output (str nomination "; verifying")
        _ (pprint output)]
    (>!! verifications output)))

(defn verify
  []
  (go-loop []
    (if-not @verify?
      nil
      (do
        (evaluate)
        (recur)))))

(defstate process
  :start (do
           (pprint "starting verify service")
           (verify))
  :stop (do
          (pprint "stopping verify service")
          (reset! verify? false)
          (Thread/sleep 1000)))
