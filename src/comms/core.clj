(ns comms.core
  (:gen-class)
  (:require [comms.nominate :as nominate]
            [comms.verify :as verify]
            [mount.core :as mount]))

(defn start
  []
  (mount/start #'nominate/process)
  (mount/start #'verify/process))

(defn stop
  []
  (mount/stop #'nominate/process)
  (Thread/sleep 1000)
  (mount/stop #'verify/process))

(defn -main
  [& _args]
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop))
  (start)
  (doseq [_ (range)] (Thread/sleep 1000)))
