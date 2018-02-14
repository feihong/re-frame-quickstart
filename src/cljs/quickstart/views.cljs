(ns quickstart.views
  (:require [clojure.string :as string]
            [re-frame.core :refer [dispatch subscribe]]
            [quickstart.emoji :as emoji]))


(defn hanzi-page []
  [:div.container
   [:h1 "Random Hanzi Quiz"]
   [:div "Do you know this character?"]
   [:p.hanzi (-> [:hanzi/current] subscribe deref :val)]
   [:p.hanzi-controls
    [:button.btn.btn-success {:on-click #(dispatch [:hanzi/mark-correct])}
                             "Yes"]
    [:button.btn.btn-danger {:on-click #(dispatch [:hanzi/mark-incorrect])}
                            "No"]

    [:span "Score: "
           [:span @(subscribe [:hanzi/score])]
           " of "
           [:span @(subscribe [:hanzi/total-points])]]]
   [:div.hanzi-history
    (for [{:keys [id val correct]} @(subscribe [:hanzi/history])]
      (let [attrs {:title val}
            span-attrs (conj attrs (when correct [:class "correct"]))]
        ^{:key id} [:span span-attrs val]))]])

(defn emoji-page []
  [:div.container
   [:h1 "Random Emoji"]
   [:form.emoji-controls
    [:div.form-group.row
     [:label.col-sm-2.col-form-label "Number of emoji:"]
     [:div.col-sm-2
      [:input.form-control
       {:type "number"
        :value @(subscribe [:emoji/count])
        :on-change #(dispatch [:emoji/set-count (-> % .-target .-value)])}]]]
    [:div.form-group.row
     [:label.col-sm-2.col-form-label "Category:"]
     [:div.col-sm-2
      [:select.form-control
       {:value @(subscribe [:emoji/category])
        :on-change #(dispatch [:emoji/set-category (-> % .-target .-value)])}
       [:option {:value ""} "All"]
       (for [cat emoji/categories]
         ^{:key cat} [:option {:value cat} (string/capitalize cat)])]]]
    [:div.form-group.row
     [:label.col-sm-2.col-form-label "Include:"]
     [:div.col-sm-4
      [:input.form-control
       {:value @(subscribe [:emoji/include])
        :on-change #(dispatch [:emoji/set-include (-> % .-target .-value)])}]]]
    [:div.form-group.row
     [:label.col-sm-2.col-form-label "Exclude:"]
     [:div.col-sm-4
      [:input.form-control
       {:value @(subscribe [:emoji/exclude])
        :on-change #(dispatch [:emoji/set-exclude (-> % .-target .-value)])}]]]

    [:div.form-group.row
     [:div.col-sm-10
      [:button.btn.btn-primary
        {:type "submit"
         :on-click #(dispatch [:emoji/generate])}
        "Generate"]]]]
   [:p.emojis
    (for [{:keys [idx text shortname]} @(subscribe [:emoji/emojis])]
      ^{:key idx} [:span {:title shortname
                          :on-click #(dispatch [:emoji/replace-one idx])}
                        text])]])
