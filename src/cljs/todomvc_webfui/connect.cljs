(ns todo-webfui.connect
  (:require [clojure.browser.repl :as repl]))


(defn connect []
  (.log js/console "about to connect to brepl")
  (repl/connect "http://localhost:9000/repl"))