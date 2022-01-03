(ns comms.srvc1
  (:require [clojure.core.async :refer [chan go-loop >!!]]
            [mount.core :refer [defstate start]]
            [clojure.pprint :refer [pprint]]))

(def run-service? (atom true))
(def srvc-channel (chan 100))

(defn do-service
  [cnt]
  (Thread/sleep 1000)
  (>!! srvc-channel (str "srvc1 iteration" cnt)))

(defn run-service
  []
  (go-loop [cnt 1]
    (if-not @run-service?
      nil
      (do
        (do-service cnt)
        (recur (inc cnt))))))

(defstate process
  :start (do
           (pprint "starting service 1")
           (run-service))
  :stop (do
          (pprint "stopping service 1")
          (reset! run-service? false)
          (Thread/sleep 2000)))

(comment
  
  (start #'process)
  (reset! run-service? false)

  )
