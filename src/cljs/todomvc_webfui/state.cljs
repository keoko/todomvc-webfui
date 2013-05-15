(ns todomvc-webfui.state
  (:use [todomvc-webfui.utils :only [get-uuid]]
        [clojure.walk :only [keywordize-keys]]))

(declare my-state)

(def local-storage-name "todos-webfui")

(defn compute-stats [todos]
  (let [total (count todos)
        completed (count (filter :completed todos))]
    {:total-todo (count todos)
     :todo-left (- total completed)
     :todo-completed completed}))

(defn compute-stats! []
  (swap! my-state update-in [:stats] #(compute-stats (:todos @my-state))))


(defn test-state []
  (reset! my-state [{:id 1 :title "test 1" :completed false}
                    {:id 2 :title "test 2" :completed true}
                    {:id 3 :title "test 3" :completed true}]))

(defn init-state []
  (let [state (.getItem js/localStorage local-storage-name)]
    (if (not state)
      (do
        {:todos []
         :stats (compute-stats [])})
      (let [todos (keywordize-keys (js->clj (.parse js/JSON state)))]
        {:todos todos
         :stats (compute-stats todos)}))))


(def my-state (atom (init-state)))


(defn add-todo! [title]
    (swap! my-state update-in [:todos] conj {:id (get-uuid) :title title :completed false}))


(defn save-todos []
  (let [todos (.stringify js/JSON (clj->js (get-in @my-state [:todos])))]
    (.setItem js/localStorage local-storage-name todos)))


(defn edit-todo! [id title]
  (swap! my-state update-in [:todos]
         (fn [todos] (map #(if (= id (:id %)) (assoc % :title title) %) todos))))

(defn remove-todo-by-id! [id]
  (swap! my-state update-in [:todos]
         (fn [todos] (remove #(= id (:id  %)) todos))))


(defn clear-todos! []
  (swap! my-state update-in [:todos]
         #(remove :completed %)))


(defn toggle-all-completed! [completed]
  (swap! my-state update-in [:todos] (fn [todos] (map #(assoc % :completed completed) todos))))

(defn toggle-completed! [id completed]
  (swap! my-state update-in [:todos]
         (fn [todos]
           (for [todo todos]
             (if (not= id (:id todo))
               todo
               (assoc todo :completed completed))))))

(defn refresh-data []
  ;; (load-todos) ;; this is part of the init-state, and then state
  (comment repl/connect "http://localhost:9000/repl")
  (save-todos)
  (compute-stats!)
  ;; (redraw-todos-ui) ;; this is part of the render-all function
  ;; (redraw-stats-ui) ;; this is part of the render-all function
)


(defn reset-mark-edit-todo! []
  (swap! my-state assoc :edit nil))

(defn mark-edit-todo! [id]
  (swap! my-state assoc :edit id))

