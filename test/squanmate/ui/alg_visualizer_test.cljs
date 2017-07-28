(ns squanmate.ui.alg-visualizer-test
  (:require [squanmate.ui.alg-visualizer :as v]
            [clojure.test :as t]
            [squanmate.puzzle :as p]
            [squanmate.puzzle :as puzzle]
            [reagent.core :as reagent])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg]]))

(defcard-rg turn-and-slice
  [v/algorithm-visualization
   puzzle/square-square
   "3/"
   {:monochrome? false}])

(defcard-rg square-square-alg
  [:div
   "this is the lin method algorithm for the CP+DF opposite corners case"
   [v/algorithm-visualization
    puzzle/square-square
    "1,0 / -4,-3 / -3,0 / -3,-3 / -3,0 / -2,-3 /"
    {:monochrome? false}]])

(defonce alg-visualizer-state (v/default-alg-visualizer-state))

(defcard-rg alg-visualizer
  [v/alg-visualizer alg-visualizer-state {:monochrome? false}]
  alg-visualizer-state
  {:inspect-data true})
