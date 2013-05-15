(ns todomvc-webfui.plugin.input-text
  (:use [webfui.plugin.core :only [Plugin]]
        [webfui.dom-manipulation :only [select-path-html parsed-html unparse-html path-dom resolve-target]]))

(def enter-key 13)

(defn input [dom-watchers parsed-html event]
  (let [target (.-target event)
        key-code (.-keyCode event)
        [tagname attributes :as element] (resolve-target @parsed-html target)
        event (@dom-watchers (keyword (:watch attributes)))]
    (when (and event (contains? #{:input :textarea} tagname) (= enter-key key-code))
      (let [value (.-value target)
            new-element (update-in element
                                   [1 :value]
                                   (fn [old]
                                     (set! (.-value target) old)
                                     value))]
        (event element new-element)))))

(defn input1 []
  (.log js/console "removing listener"))

(deftype input-text []
  Plugin
  (declare-events [this body dom-watchers parsed-html]
    (comment .removeEventListener body "input" input1 false)
    (.addEventListener body "keypress" (partial input dom-watchers parsed-html)))
  (fix-dom [this]
           nil))