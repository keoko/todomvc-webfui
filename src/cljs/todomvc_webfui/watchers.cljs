(ns todomvc-webfui.watchers
  (:use [webfui.framework :only [launch-app]]
        [webfui.utilities :only [get-attribute clicked]]
        [todomvc-webfui.state :only [add-todo! refresh-data my-state edit-todo! reset-mark-edit-todo! remove-todo-by-id! clear-todos! toggle-completed! toggle-all-completed! mark-edit-todo!]])
  (:use-macros [webfui.framework.macros :only [add-dom-watch add-mouse-watch]]))

(add-dom-watch :new-todo [state new-element]
	       (let [title  (get-attribute new-element :value)]
                 (add-todo! title)
                 (refresh-data)
                 @my-state))

(add-dom-watch :edit-todo [state new-element]
               (let [id-name (get-attribute new-element :id)
                     id (.slice id-name 6)
                     title (get-attribute new-element :value)]
                 (edit-todo! id title)
                 (reset-mark-edit-todo!)
                 (refresh-data)
               	 @my-state)) 

(add-mouse-watch :delete-todo [state first-element last-element points]
                 (when (clicked first-element last-element)
                   (let [id (get-attribute first-element :data-todo-id)]
                     (remove-todo-by-id! id)
                     (refresh-data))
                   @my-state))

(add-mouse-watch :clear-todos [state first-element last-element points]
                 (when (clicked first-element last-element)
                   (clear-todos!)
                   (refresh-data)
                   @my-state))

(add-dom-watch :toggle-all-completed [state new-element]
               (let [completed (get-attribute new-element :checked)]
                 (toggle-all-completed! completed)
                 (refresh-data)
                 @my-state))

(add-dom-watch :toggle-completed [state new-element]
                   (let [id (get-attribute new-element :data-todo-id)
                         completed (get-attribute new-element :checked)]
                     (toggle-completed! id completed)
                     (refresh-data))
                     @my-state)

(add-mouse-watch :enable-content [state first-element last-element points]
                 (when (clicked first-element last-element)
                   (let [id (get-attribute first-element :data-todo-id)]
                     (mark-edit-todo! id)
                     (refresh-data)
                     @my-state)))
