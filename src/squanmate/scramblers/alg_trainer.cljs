(ns squanmate.scramblers.alg-trainer
  (:require [clojure.string :as str]
            [reagent.core :as reagent]
            [squanmate.scramblers.algsets.edge-permutation :as ep]
            [squanmate.ui.color-chooser :as color-chooser]
            [squanmate.ui.common :as common]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]))

(defn- puzzle-preview [state]
  (when-let [puzzle (:puzzle @state)]
    (let [draw-settings (assoc (:draw-settings @state)
                               :size 180)]
      [newmonochrome/monochrome-puzzle puzzle draw-settings])))

(defn- scramble-preview [s]
  (when (not (str/blank? s))
    [:div.col-xs-10.col-md-6.col-lg-6.scramble [common/well s]]))

(defn- new-scramble-button [state]
  [:div
   (if (empty? (:selected-algs @state))
     [common/alert "Select some algs below to get started."]
     [common/button {:on-click identity} "New scramble"])])

(defn- alg-selections [algs]
  [:div
   [:div.container-fluid
    (for [[case-name alg] algs]
      ^{:key case-name}
      [:div.col-xs-4
       [common/checkbox {:inline true :checked false}
        case-name]])]
   [:div.center
    [common/button "Select all"]
    [common/button "Select none"]]])

(defn- alg-selection-settings [state]
  [:div
   [common/accordion {:default-active-key 1}
    [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :th}]
                                                " Edge permutation (EP)"])
                   :event-key 1}
     [:div
      [:div "With parity"
       [alg-selections ep/cases-with-parity]]

      [:div "With no parity"
       [alg-selections ep/cases-with-no-parity]]]]
    [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :th}]
                                                " Permute last layer (PLL)"])
                   :event-key 2}
     [:div "todo"]]]])

(defn- settings [state]
  [common/accordion {:default-active-key 1}
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :cog}]
                                               " Settings"])
                  :event-key 1}
    [alg-selection-settings state]]
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :tint}]
                                               " Colors"])
                  :event-key 2}
    [color-chooser/color-chooser (reagent/cursor state [:draw-settings])]]])

(defn new-default-state []
  (reagent/atom {:selected-algs #{}}))

(defn trainer-component [state]
  [:div
   [:div.center
    [new-scramble-button state]
    [puzzle-preview state]]
   [:div.center.top17
    [scramble-preview (:scramble-algorithm @state)]]
   [:div.center
    [settings state]]])
