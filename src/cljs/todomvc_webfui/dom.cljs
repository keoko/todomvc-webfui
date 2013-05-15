(ns todomvc-webfui.dom)

(defn redraw-todo [todo is-editable]
  (let [{:keys [id title completed]} todo
        checkbox-extra-params (if (:completed todo) {:checked "checked"} {})
        li-extra-params (if (:completed todo) {:class "completed"} {})]
    [:li (merge li-extra-params (if is-editable {:class "editing"} {}))
     [:div.view {:data-todo-id id}
      [:input.toggle (merge {:data-todo-id id
                             :type "checkbox"
                             :watch :toggle-completed} checkbox-extra-params)]
      [:label {:data-todo-id id :mouse :enable-content} (str (:title todo))]
      [:button.destroy {:data-todo-id id
                        :mouse :delete-todo}]]
     [:input.edit {:id (str "input_" id) :value title :watch :edit-todo}]]))



(defn redraw-todo-clear [stats]
  (if (< 0 (:todo-completed stats))
      [:button#clear-completed {:mouse :clear-todos} (str "Clear completed (" (:todo-completed stats) ")")]
      [:span]))

(defn redraw-todo-count [stats]
  (if (< 0 (:todo-left stats))
    [:span#todo-count [:strong (str (:todo-left stats))] (str " " 
                                                              (if (== 1 (:todo-left stats)) "item" "items") 
                                                              " left")]
    [:span]))



(defn render-all [state]
  (let [{:keys [todos stats edit checked-all]} state
        edit-todo-id edit
        checked-all (if (= (count todos) (:todo-completed stats)) {:checked "checked"} {})]
    (.log js/console "todos->" (pr-str state))
       [:div
        [:section#todoapp
         [:header#header
          [:h1 "todos"]
          [:input#new-todo {:placeholder "What needs to be done?"
                            :autofocus true
                            :watch :new-todo}]]
         [:section#main
          [:input#toggle-all (merge {:type "checkbox" :watch :toggle-all-completed} checked-all)]
          [:label {:for "toggle-all"} "Mark all as complete"]
          [:ul#todo-list (for [todo todos] (redraw-todo todo (== edit-todo-id (:id todo))))]]
         [:footer#footer {:style {:display (if (< 0 (count todos)) "block" "none")}}
          (redraw-todo-count stats)
          (redraw-todo-clear stats)
          ]]
        [:footer#info
         [:p "Double-click to edit a todo"]
         [:p "Created by " [:a {:href "http://twitter.com/keoko"} "Natxo Cabré" ]]
         [:p "Port of " [:a {:href "http://todomvc.com/vanilla-examples/vanillajs/"} "VanillaJS TodoMVC"]]]]))
