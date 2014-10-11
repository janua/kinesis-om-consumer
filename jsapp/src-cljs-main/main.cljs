(ns kinesis-om-template.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [kinesis-om-template.events]))

(def items (atom []))

(defn pushToItems [e]
  (let [updateItems (js->clj (.parse js/JSON (.-data e)) :keywordize-keys true)]
  (.log js/console (str updateItems))
  (swap! items (fn [l] (vec (concat updateItems l))))))

(defn itemsList [data owner]
  (reify
    om/IRender
    (render [this]
      (apply dom/div #js {:className "items"}
        (vec
          (map (fn [i] (dom/div #js {:className "item"} (pr-str i))) data))))))

(om/root itemsList items {:target (. js/document (getElementById "mainapp"))})

(kinesis-om-template.events/attachToEventStream #(pushToItems %))

