(ns squanmate.core
  (:require [reagent.core :as reagent]
            [squanmate.http.routing :as routing]
            [squanmate.pages.main-ui :as main-ui]))

(enable-console-print!)

(defonce app-state-atom (reagent/atom {:page {:name nil
                                              :route-args nil}}))

(defn- try-remove-node [id]
  (when-let [element (js/document.getElementById id)]
    (-> element .-parentElement (.removeChild element))))

(defn main []
  (try-remove-node "loading-area")

  (routing/app-routes app-state-atom)

  ;; conditionally start the app based on whether the #main-app-area
  ;; node is on the page
  (if-let [node (.getElementById js/document "main-app-area")]
    (reagent/render [main-ui/main-ui app-state-atom]
                    node)))

(main)
