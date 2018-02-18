(ns quickstart.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf :refer [subscribe, dispatch]]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [quickstart.ajax :refer [load-interceptors!]]
            [quickstart.events]
            [quickstart.subs]
            [quickstart.views :as views])
  (:import goog.History))

(defn nav-link [uri title page]
  [:li.nav-item
   {:class (when (= page @(subscribe [:page])) "active")}
   [:a.nav-link {:href uri} title]])

(defn navbar []
  [:nav.navbar.navbar-dark.bg-primary.navbar-expand-md
   {:role "navigation"}
   [:button.navbar-toggler.hidden-sm-up
    {:type "button"
     :data-toggle "collapse"
     :data-target "#collapsing-navbar"}
    [:span.navbar-toggler-icon]]
   [:a.navbar-brand {:href "#/"} "Quickstart"]
   [:div#collapsing-navbar.collapse.navbar-collapse
    [:ul.nav.navbar-nav.mr-auto
     [nav-link "#/hanzi" "Hanzi" :hanzi]
     [nav-link "#/emoji" "Emoji" :emoji]
     [nav-link "#/voices" "Voices" :emoji]
     [nav-link "#/about" "About" :about]]]])

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:img {:src (str js/context "/img/warning_clojure.png")}]
     [:p "Using EmojiOne version " (.-emojiVersion js/emojione)]
     (let [url "https://github.com/feihong/re-frame-quickstart"]
       [:p "Source: "
        [:a {:href url :target "_blank"} url]])]]])

(defn home-page []
  [:div.container
   [:h1 "Home"]
   [:ul
    [:li
     [:a {:href "#/hanzi"} "Random Hanzi Quiz"]]
    [:li
     [:a {:href "#/emoji"} "Random Emoji Generator"]]
    [:li
     [:a {:href "#/voices"} "Voices"]]
    [:li
     [:a {:href (str js/context "/swagger-ui")} "Swagger UI"]]]])

(def pages
  {:home #'home-page
   :about #'about-page
   :hanzi #'views/hanzi-page
   :emoji #'views/emoji-page
   :voices #'views/voices-page})

(defn page []
  [:div
   [navbar]
   [(pages @(subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

(secretary/defroute "/hanzi" []
  (rf/dispatch [:set-active-page :hanzi]))

(secretary/defroute "/emoji" []
  (rf/dispatch [:set-active-page :emoji]))

(secretary/defroute "/about" []
  (rf/dispatch [:set-active-page :about]))

(secretary/defroute "/voices" []
  (rf/dispatch [:set-active-page :voices]))

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
(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (js/document.getElementById "app")))

(defn ^:export init []
  (rf/dispatch-sync [:initialize-db])
  (rf/dispatch [:hanzi/load-word])
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
