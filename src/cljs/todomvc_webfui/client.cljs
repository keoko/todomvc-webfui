(ns todomvc-webfui.client
  (:use [webfui.framework :only [launch-app]]
        [webfui.plugin.core :only [register-plugin active-plugins]]
        [webfui.plugin.mouse :only [mouse]]
        [todomvc-webfui.state :only [my-state]]
        [todomvc-webfui.dom :only [render-all]]
        [todomvc-webfui.plugin.input-text :only [input-text]]
        [todomvc-webfui.plugin.checkbox :only [checkbox]])
  (:require [clojure.browser.repl :as repl]))


;; reset plugins as "form-values" is pre-loaded and it does not work with "enter-key" events
(reset! active-plugins [])
(register-plugin (input-text.))
(register-plugin (mouse.))
(register-plugin (checkbox.))


(launch-app my-state render-all)
