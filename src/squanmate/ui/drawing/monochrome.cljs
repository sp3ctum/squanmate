(ns squanmate.ui.drawing.monochrome
  (:require [reagent.core :as reagent]
            [squanmate.puzzle :as p]
            [squanmate.slicing :as slicing]
            [clojure.string :as string]
            [squanmate.shapes :as shapes]
            [cljsjs.react-bootstrap]))

;; Note: cubeshape is the app that draws the puzzle. So this is for building a
;; cubeshape app specific request string.
(defprotocol CubeshapePiecesString
  (cubeshape-app-string [layer]))

(extend-protocol CubeshapePiecesString
  shapes/Shape
  (cubeshape-app-string [shape]
    (p/pieces-str shape))

  p/TopLayer
  (cubeshape-app-string [top-layer]
    (p/pieces-str top-layer))

  p/BottomLayer
  (cubeshape-app-string [bottom-layer]
    (p/pieces-str bottom-layer)))

(def tooltip (reagent/adapt-react-class js/ReactBootstrap.Tooltip))
(def overlay-trigger (reagent/adapt-react-class js/ReactBootstrap.OverlayTrigger))

(defn layer-component [layer & {:keys [size]
                                :or {size 100}}]
  (when layer
    (let [url (str "//localhost:9292/cubeshape/"
                   (cubeshape-app-string layer)
                   "?"
                   "size=" size)
          shape-name (shapes/layer-shape-name layer)]
      [overlay-trigger
       {:overlay (reagent/as-element [tooltip {:id "test"}
                                      shape-name])
        :placement "top"}
       [:img {:src url}]])))

(defn monochrome-puzzle [puzzle & debug?]
  (let [top-img (layer-component (:top-layer puzzle))
        bottom-img (layer-component (:bottom-layer puzzle))]
    [:div.puzzle
     [:span.layer.top top-img]
     [:span.layer.bottom {:style {:margin-left "-20px"}} bottom-img]
     (when debug?
       [:div
        "Top:" (p/pieces-str (:top-layer puzzle))
        ", "
        "Bottom: " (p/pieces-str (:bottom-layer puzzle))])]))
