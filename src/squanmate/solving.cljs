(ns squanmate.solving
  (:require [cljs.core.async :refer [<!]]
            [cljs-workers.core :as main]
            [cljs-workers.worker :as worker])
  (:require-macros [cljs.core.async.macros :as m :refer [go]]))

;; Setup the browser path (handling both in one file)
(defn app
  []
  (let [;; you can create one worker or a pool (async channel of workers)
        worker-pool
        (main/create-pool 2 "js/compiled/webworkers.js")

        ;; a "do-with-pool" or "-worker" (see below) will return immediately and give you a result channel. So to print the result you have to handle the channel
        print-result
        (fn [result-chan]
          (go
            (let [result (<! result-chan)]
              (.debug js/console
                      (str (:state result))
                      result))))]

    ;; Copy all simple values
    (print-result (main/do-with-pool! worker-pool {:handler :mirror, :arguments {:a "Hallo" :b "Welt" :c 10}}))
    ;; Copy the simple values and transfer the ArrayBuffer
    (print-result (main/do-with-pool! worker-pool {:handler :mirror, :arguments {:a "Hallo" :b "Welt" :c 10 :d (js/ArrayBuffer. 10) :transfer [:d]} :transfer [:d]}))
    ;; Copy the simple values and transfer the ArrayBuffer, but transfer (browser thread) will fail cause the wrong value and the wrong type is marked to do so
    (print-result (main/do-with-pool! worker-pool {:handler :mirror, :arguments {:a "Hallo" :b "Welt" :c 10 :d (js/ArrayBuffer. 10) :transfer [:d]} :transfer [:c]}))
    ;; Copy the simple values and transfer the ArrayBuffer, but transfer mirroring (worker thread) will fail cause the wrong value and the wrong type is marked to do so
    (print-result (main/do-with-pool! worker-pool {:handler :mirror, :arguments {:a "Hallo" :b "Welt" :c 10 :d (js/ArrayBuffer. 10) :transfer [:c]} :transfer [:d]}))))

;; Setup the worker path (handling both in one file)
(defn worker
  []
  (worker/register
   :mirror
   (fn [arguments]
     arguments))

  (worker/bootstrap))

;; Decide which path to setup
(if (main/main?)
  (app)
  (worker))
