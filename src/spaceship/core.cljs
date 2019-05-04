(ns ^:figwheel-hooks spaceship.core
  (:require
   [goog.dom :as gdom]
   [rum.core :as rum]))

(defn multiply [a b] (* a b))


(defn get-app-element []
  (gdom/getElement "app"))

(def width 200)
(def height 400)
(def plane-width 50)
(def plane-height 50)
(def speed 1)
(defonce state
  (atom {:y (/ height 2)
         :moving false
         :vy 0}))

(defn next-frame [state]
  (update state :y + (if (:moving state)
                       (:vy state)
                       0)))

(defn update-vy [state mouse-event]
  (assoc state :vy
         (if (< (.-pageY mouse-event) (/ height 2))
           (- speed)
           speed)))

(rum/defc screen < rum/reactive [state]
  [:svg {:x "0px"
         :y "0px"
         :width (str width "px")
         :height (str height "px")
         :view-box (str "0 0 " width " " height)
         :on-mouse-down (fn [ev]
                          (swap! state assoc :moving true)
                          (swap! state #(update-vy % ev)))
         :on-mouse-move (fn [ev]
                          (swap! state #(update-vy % ev)))
         :on-mouse-up #(swap! state assoc :vy 0 :moving false)}
   [:image {:href "/images/plane.svg"
            :x (/ width 4)
            :y (- (:y (rum/react state)) (/ plane-height 2))
            :width plane-width
            :height plane-height}]])

(defn mount-app []
  (when-let [el (get-app-element)]
    (rum/mount (screen state) el)))

(mount-app)

(defonce initialize-fps
  (js/setInterval
    #(swap! state next-frame)
    (/ 1000 300)))

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  (mount-app)
)
