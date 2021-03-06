(ns squanmate.scramblers.alg-trainer
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [reagent.core :as reagent]
            [squanmate.pages.links :as links]
            [squanmate.scramblers.alg-trainer.algset-scrambler :as algset-scrambler]
            [squanmate.scramblers.alg-trainer.case-selection :as selection]
            [squanmate.scramblers.alg-trainer.scramble-generation :as scramble-generation]
            [squanmate.scramblers.algsets.edge-permutation :as ep]
            [squanmate.ui.alg-display :as alg-display]
            [squanmate.ui.case-counter :as case-counter]
            [squanmate.ui.common :as common]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.ui.middle-layer-controls :as middle-layer-controls]
            [squanmate.ui.shortcut-cheat-sheet :as shortcut-cheat-sheet]))

(defn- puzzle-preview [state draw-settings-map]
  (when-let [puzzle (:puzzle @state)]
    (let [draw-settings (assoc draw-settings-map
                               :size 180)]
      [newmonochrome/monochrome-puzzle puzzle draw-settings])))

(defn- scramble-preview [s]
  (when (not (str/blank? s))
    [:div.col-xs-10.col-md-6.col-lg-6.scramble
     [common/well (when s
                    [alg-display/rich-scramble-display s])]]))

(defn- case-selection-component [state case]
  (let [[case-name alg] case
        selected? (selection/case-selected? state case)]
    [:div.col-xs-4
     [:div.selected-wrapper
      {:class (when selected? "selected-case")}
      [common/checkbox {:inline true
                        :checked selected?
                        :on-change #(selection/select-or-deselect! state case)}
       case-name]]]))

(defn- case-group [cases state]
  [:div
   (for [[case-name alg :as c] cases]
     ^{:key case-name}
     [case-selection-component state c])])

(defn- alg-group-selected-cases [cases state]
  (->> cases
       (into (hash-set))
       (set/intersection (:selected-cases @state))
       count))

(defn- alg-group-selection-counter [cases state]
  (let [selected-count (alg-group-selected-cases cases state)]
    (when (pos-int? selected-count)
      [case-counter/selected-cases-counter selected-count (count cases)])))

(defn- case-selections [state alg-set]
  (let [odd-cases-present? (not-empty (:odd-cases alg-set))]
    [:div

     [:div.container-fluid [case-group (:even-cases alg-set) state]]
     [:div.center.top10
      [alg-group-selection-counter (:even-cases alg-set) state]]
     (when odd-cases-present?
       [:div
        [:hr]
        [:div.container-fluid [case-group (:odd-cases alg-set) state]]])
     [:div.center.top10
      [alg-group-selection-counter (:odd-cases alg-set) state]]

     [:hr]
     [:div.center.vertical
      [:div "Select:"]
      [:div
       [common/button
        {:on-click #(selection/select-cases! state (algset-scrambler/all-cases alg-set))}
        "All"]
       [common/button
        {:on-click #(selection/deselect-cases! state (algset-scrambler/all-cases alg-set))}
        "None"]
       [common/button
        {:on-click #(selection/select-cases! state (:even-cases alg-set))}
        "All even parity"]
       (when odd-cases-present?
         [common/button
          {:on-click #(selection/select-cases! state (:odd-cases alg-set))}
          "All odd parity"])]
      [:div.center.top10
       [alg-group-selection-counter (algset-scrambler/all-cases alg-set) state]]]]))

(defn- algset-header [title]
  (reagent/as-element [:span [common/glyphicon {:glyph :th}]
                       " " title]))

(defn- alg-selection-settings [state]
  [common/accordion
   (for [[index a] (zipmap (range) scramble-generation/algsets)
         :let [{:keys [name algset]} a]]
     ^{:key (str "algset-" name)}
     [common/panel {:header (algset-header name)
                    :event-key index}
      [case-selections state algset]])])

(defn- settings [state]
  [common/accordion {:default-active-key 1}
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :cog}]
                                               " Algorithm sets"])
                  :event-key 1}
    [alg-selection-settings state]]
   [common/panel {:header (reagent/as-element [:span [common/glyphicon {:glyph :wrench}]
                                               " Scramble options"])
                  :event-key 2}
    [middle-layer-controls/controls (reagent/cursor state [:middle-layer-settings])]]])

(defn new-default-state [& {:keys [keybindings]}]
  (reagent/atom {:selected-cases #{}
                 :middle-layer-settings (deref (middle-layer-controls/default-state))
                 ;; optional
                 :keybindings keybindings}))

(defn- new-scramble-button [state]
  [:div
   (if (empty? (:selected-cases @state))
     [common/alert "Select some cases below to get started."]
     [common/button {:on-click #(scramble-generation/set-new-scramble! state)
                     :bs-style :success}
      "New scramble"])])

(defn- inspect-scramble-button [state]
  (when (:chosen-case @state)
    [common/button {:on-click #(links/set-link-to-scramble (:scramble-algorithm @state))}
     [common/glyphicon {:glyph :search}]
     " Inspect"]))

(defn- repeat-scramble-button [state]
  (when (:chosen-case @state)
    [common/button {:on-click #(scramble-generation/new-scramble state (:chosen-case @state))}
     [:span [common/glyphicon {:glyph :repeat}]]
     " Repeat case"]))

(defn- cheat-sheet-button [state]
  [shortcut-cheat-sheet/cheat-sheet-button (:keybindings @state)])

(defn- action-buttons [state]
  [:div.center
   [repeat-scramble-button state]
   [new-scramble-button state]
   [inspect-scramble-button state]
   [cheat-sheet-button state]])

(defn trainer-component
  ([state]
   [trainer-component state newmonochrome/default-settings])
  ([state draw-settings-map]
   [:div
    [:div.center.vertical [action-buttons state]]
    [:div.center.top17 [puzzle-preview state draw-settings-map]]
    [:div.center.top17 [scramble-preview (:scramble-algorithm @state)]]
    [settings state]]))
