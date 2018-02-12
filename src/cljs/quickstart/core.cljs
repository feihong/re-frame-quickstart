(ns quickstart.core
  (:require [clojure.string :as str]
            [reagent.core :as r]
            [re-frame.core :as rf :refer [subscribe, dispatch]]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [quickstart.ajax :refer [load-interceptors!]]
            [quickstart.events]
            [quickstart.subs])
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
     [:a {:href "#/hanzi"} "Random Hanzi Quiz"]]
    [:li
     [:a {:href "#/emoji"} "Random Emoji Generator"]]]])

(defn hanzi-page []
  [:div.container
   [:h1 "Random Hanzi Quiz"]
   [:div "Do you know this character?"]
   [:p.hanzi (-> [:hanzi] subscribe deref :val)]
   [:p.hanzi-controls
    [:button.btn.btn-success {:on-click #(dispatch [:mark-as-correct])}
                             "Yes"]
    [:button.btn.btn-danger {:on-click #(dispatch [:mark-as-incorrect])}
                            "No"]

    [:span "Score: "
           [:span @(subscribe [:my-score])]
           " of "
           [:span @(subscribe [:total-points])]]]
   [:div.hanzi-history
    (for [{:keys [id val correct]} @(subscribe [:hanzi-history])]
      (let [attrs {:title val}
            span-attrs (conj attrs (when correct [:class "correct"]))]
        ^{:key id} [:span span-attrs val]))]])

(defn emoji-page []
  [:div.container
   [:h1 "Random Emoji"]
   [:form.emoji-controls
    [:div.form-group.row
     [:label.col-sm-2.col-form-label "Number of emoji:"]
     [:div.col-sm-3
      [:input.form-control
       {:type "number"
        :value @(subscribe [:emoji-count])
        :on-change #(dispatch [:set-emoji-count (-> % .-target .-value)])}]]]
    [:div.form-group.row
     [:label.col-sm-2.col-form-label "Exclude:"]
     [:div.col-sm-4
      [:input.form-control
       {:value @(subscribe [:emoji-exclude])
        :on-change #(dispatch [:set-emoji-exclude (-> % .-target .-value)])}]]]

    [:div.form-group.row
     [:div.col-sm-10
      [:button.btn.btn-primary
        {:type "submit"
         :on-click #(dispatch [:generate-emojis])}
        "Generate"]]]]
   [:p.emojis
    (for [{:keys [idx text shortname]} @(subscribe [:emojis])]
      ^{:key idx} [:span {:title shortname
                          :on-click #(dispatch [:replace-emoji idx])}
                        text])]])

(def pages
  {:home #'home-page
   :hanzi #'hanzi-page
   :emoji #'emoji-page
   :about #'about-page})

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

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
