(ns squanmate.alg.serialization
  (:require [clojure.string :as str]
            [squanmate.alg.prettification :as prettification]
            [squanmate.alg.types :as types]
            [squanmate.alg.rotation :as rotation]
            [squanmate.services.shapes :as shapes]))

(defn step-to-str [s]
  (condp = (type s)
    types/Slice "/"
    types/Rotations (str "(" (prettification/prettify-value
                              (:top-amount s))
                         ","
                         (prettification/prettify-value
                          (:bottom-amount s))
                         ")")))

(defn alg-to-str [steps]
  (str/join ""
            (map step-to-str steps)))

(defn rotation-specification
  "Returns the key (programmatical name) of the layer and its initial rotation
  (from the default rotation) as a vector."
  [layer]
  (let [layer-key (shapes/layer-shape-name-key layer)
        rotations (rotation/possible-rotations (get shapes/all-shapes layer-key))
        same-shape? (fn [[l rotation-amount]]
                      (shapes/same-shape-and-orientation? layer l))
        all-same-shapes (filter same-shape? rotations)
        [_ rotation-amount] (first (filter same-shape? rotations))]
    [layer-key rotation-amount]))

(defn puzzle-specification [p]
  (let [[top-name top-rotation] (-> p :top-layer rotation-specification)
        [bottom-name bottom-rotation] (-> p :bottom-layer rotation-specification)
        initial-rotation (alg-to-str [(types/->Rotations top-rotation
                                                         bottom-rotation)])]
    {:top-name top-name
     :bottom-name bottom-name
     :initial-rotation initial-rotation}))
