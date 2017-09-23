(ns squanmate.pages.scramble-inspector
  (:require [squanmate.ui.scramble :as scramble]
            [squanmate.alg.manipulation :as manipulation]))

(defonce state (scramble/default-state))

(defn content []
  [:div.container
   [:div.center
    [:div.col-xs-8
     [scramble/component state]]]])

(defn content-from-args [{:keys [scramble-algorithm]}]
  (let [local-state (scramble/default-state)
        scramble-algorithm (manipulation/format-alg scramble-algorithm)]
    (swap! local-state assoc-in [:scramble :scramble-algorithm] scramble-algorithm)
    (scramble/mark-alg-imported local-state)
    [scramble/component local-state]))
