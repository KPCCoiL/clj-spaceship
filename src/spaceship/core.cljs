(ns ^:figwheel-hooks spaceship.core
  (:require
   [goog.dom :as gdom]
   [rum.core :as rum]))

(println "This text is printed from src/spaceship/core.cljs. Go ahead and edit it and see reloading in action.")

(defn multiply [a b] (* a b))


;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:text "Here comes the game."}))

(defn get-app-element []
  (gdom/getElement "app"))

(rum/defc hello < rum/reactive []
  [:div
   (:text (rum/react app-state))])

(defn mount-app []
  (when-let [el (get-app-element)]
    (rum/mount (hello) el)))

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  (mount-app)
)
