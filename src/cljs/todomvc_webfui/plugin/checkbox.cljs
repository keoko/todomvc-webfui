(ns todomvc-webfui.plugin.checkbox
  (:use [webfui.plugin.core :only [Plugin]]
        [webfui.dom-manipulation :only [select-path-html parsed-html unparse-html path-dom resolve-target]]))


(defn input [dom-watchers parsed-html event]
  (let [target (.-target event)
        [tagname attributes :as element] (resolve-target @parsed-html target)
        event (@dom-watchers (keyword (:watch attributes)))]
    (when (and event (contains? #{:input} tagname) (= "checkbox" (:type attributes)))
      (let [checked (.-checked target)
            new-element (update-in element
                                   [1 :checked]
                                   (fn [old]
                                     (set! (.-checked target) old)
                                     checked))]
        (event element new-element)))))

(deftype checkbox []
  Plugin
  (declare-events [this body dom-watchers parsed-html]
    (.addEventListener body "change" (partial input dom-watchers parsed-html)))
  (fix-dom [this]
    nil))
