(ns quickstart.core
  (:require [clojure.string :as str]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [quickstart.ajax :refer [load-interceptors!]]
            [quickstart.events])
  (:import goog.History))

(defn nav-link [uri title page]
  [:li.nav-item
   {:class (when (= page @(rf/subscribe [:page])) "active")}
   [:a.nav-link {:href uri} title]])

(defn navbar []
  [:nav.navbar.navbar-dark.bg-primary.navbar-expand-md
   {:role "navigation"}
   [:button.navbar-toggler.hidden-sm-up
    {:type "button"
     :data-toggle "collapse"
     :data-target "#collapsing-navbar"}
    [:span.navbar-toggler-icon]]
   [:a.navbar-brand {:href "#/"} "quickstart"]
   [:div#collapsing-navbar.collapse.navbar-collapse
    [:ul.nav.navbar-nav.mr-auto
     [nav-link "#/hanzi" "Hanzi" :hanzi]
     [nav-link "#/emoji" "Emoji" :emoji]
     [nav-link "#/about" "About" :about]]]])

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:img {:src (str js/context "/img/warning_clojure.png")}]]]])

(defn home-page []
  [:div.container
   [:h1 "Home"]
   [:ul
    [:li
     [:a {:href "#/hanzi"} "Random Hanzi"]]
    [:li
     [:a {:href "#/emoji"} "Random Emoji"]]]])

(defn hanzi-page []
  [:div.container
   [:h1 "Random Hanzi"]
   [:div.button-box
    [:button.btn.btn-primary {:on-click #(rf/dispatch [:generate-hanzi])}
                            "Next"]
    [:button.btn.btn-success {:on-click #(rf/dispatch [:mark-as-correct])}
                             "I know this one!"]
    [:span "Score: "
           [:span @(rf/subscribe [:my-score])]
           " of "
           [:span @(rf/subscribe [:total-points])]]]
   [:p.hanzi (-> [:hanzi] rf/subscribe deref :val)]
   [:div.hanzi-history
    (for [{:keys [id val correct]} @(rf/subscribe [:hanzi-history])]
      (let [attrs {:title val}
            attrs' (conj attrs (when correct [:className "correct"]))]
        ^{:key id} [:span attrs' val]))]])


(def pages
  {:home #'home-page
   :hanzi #'hanzi-page
   :about #'about-page})

(defn page []
  [:div
   [navbar]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

(secretary/defroute "/hanzi" []
  (rf/dispatch [:set-active-page :hanzi]))

(secretary/defroute "/about" []
  (rf/dispatch [:set-active-page :about]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET "/docs" {:handler #(rf/dispatch [:set-docs %])}))

(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))
