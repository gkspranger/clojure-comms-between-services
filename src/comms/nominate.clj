(ns comms.nominate
  (:require [clojure.core.async :refer [chan go-loop >!!]]
            [mount.core :refer [defstate]]
            [clojure.pprint :refer [pprint]]))

(def nominate? (atom true))
(def nominations (chan 100))

(defn evaluate
  [cnt]
  (let [_ (Thread/sleep 1000)
        output (str "nominating" cnt)
        _ (pprint output)]
    (>!! nominations output)))

(defn nominate
  []
  (go-loop [cnt 1]
    (if-not @nominate?
      nil
      (do
        (evaluate cnt)
        (recur (inc cnt))))))

(defstate process
  :start (do
           (pprint "starting nomination service")
           (nominate))
  :stop (do
          (pprint "stopping nomination service")
          (reset! nominate? false)
          (Thread/sleep 1000)))
