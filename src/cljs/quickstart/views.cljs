(ns quickstart.views
  (:require [re-frame.core :refer [dispatch subscribe]]))


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
     [:label.col-sm-2.col-form-label "Include:"]
     [:div.col-sm-4
      [:input.form-control
       {:value @(subscribe [:emoji-include])
        :on-change #(dispatch [:set-emoji-include (-> % .-target .-value)])}]]]
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
