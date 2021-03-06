(ns quickstart.views
  (:require [clojure.string :as string]
            [re-frame.core :refer [dispatch subscribe]]
            [quickstart.emoji :as emoji]))


(defn hanzi-page []
  [:div.container
   [:h1 "Random Hanzi Quiz"]
   [:div "Do you know this word?"]
   (when-let [{:keys [status uri]} @(subscribe [:hanzi/error])]
     [:div.error (str "Error with code " status " at " uri)])
   (let [{:keys [word pinyin gloss]} @(subscribe [:hanzi/current])]
     [:div.word
      [:span.word {:on-click #(dispatch [:hanzi/show-meta true])}
                  word]
      [:div.meta (when @(subscribe [:hanzi/show-meta]) {:class "show"})
       [:span.pinyin pinyin]
       [:span.gloss gloss]]])
   [:p.hanzi-controls
    [:button.btn.btn-success {:on-click #(dispatch [:hanzi/mark true])}
                             "Yes"]
    [:button.btn.btn-danger {:on-click #(dispatch [:hanzi/mark false])}
                            "No"]

    [:span "Score: "
           [:span @(subscribe [:hanzi/score])]
           " of "
           [:span @(subscribe [:hanzi/total-points])]]]
   [:div.hanzi-history
    (for [{:keys [word gloss pinyin correct]} @(subscribe [:hanzi/history])]
      (let [attrs {:title (str pinyin " - " gloss)}
            span-attrs (conj attrs (when correct [:class "correct"]))]
        ^{:key word} [:span span-attrs word]))]])

(defn form-group [label child]
  [:div.form-group.row
   [:label.col-sm-2.col-form-label label]
   child])

(defn emoji-page []
  [:div.container
   [:h1 "Random Emoji Generator"]
   [:form.emoji-controls
    (form-group "Number:"
     [:div.col-sm-2
      [:input.form-control
       {:type "number"
        :value @(subscribe [:emoji/count])
        :on-change #(dispatch [:emoji/set-count (.. % -target -value)])}]])
    (form-group "Category:"
     [:div.col-sm-3
      [:select.form-control
       {:value @(subscribe [:emoji/category])
        :on-change #(dispatch [:emoji/set-category (.. % -target -value)])}
       [:option {:value ""} "All"]
       (for [cat emoji/categories]
         ^{:key cat} [:option {:value cat} (string/capitalize cat)])]])
    (form-group "Include:"
     [:div.col-sm-4
      [:input.form-control
       {:value @(subscribe [:emoji/include])
        :on-change #(dispatch [:emoji/set-include (.. % -target -value)])}]])
    (form-group "Exclude:"
     [:div.col-sm-4
      [:input.form-control
       {:value @(subscribe [:emoji/exclude])
        :on-change #(dispatch [:emoji/set-exclude (.. % -target -value)])}]])

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

(defn fonts-page []
  [:div.container
   [:h1 "Fonts"]
   [:div
    [:input {:class "form-control"
             :placeholder "Sample text"
             :value @(subscribe [:fonts/text])
             :on-change #(dispatch [:fonts/set-text (.. % -target -value)])}]
    [:button.btn.btn-primary {:on-click #(dispatch [:fonts/load])} "Load fonts"]]
   [:table.table
    [:thead
     [:tr
      [:th "Font"]
      [:th "Sample"]]]
    [:tbody
     (let [text @(subscribe [:fonts/text])]
       (for [font @(subscribe [:fonts/fonts])]
         ^{:key font}
         [:tr
          [:td font]
          [:td {:style {:font-family font
                        :font-size "3rem"}}
               text]]))]]])

(defn voice-option [{:keys [idx name lang]}]
  ^{:key idx}
  [:option {:value name} (str name " (" lang ")")])

(defn voices-page []
  [:div.container
   [:h1 "Voices"]
   (form-group "Voices"
     [:div.col-sm-3
      [:select.form-control
       {:value @(subscribe [:voices/current])
        :on-change #(dispatch [:voices/set-voice (.. % -target -value)])}
       (for [voice @(subscribe [:voices/voices])]
        (voice-option voice))]])
   (form-group "Phrase"
     [:div.col-sm-8
      [:input.form-control
       {:value @(subscribe [:voices/phrase])
        :on-change #(dispatch [:voices/set-phrase (.. % -target -value)])}]])
   [:div.form-group.row
    [:div.col-sm-10
     [:button.btn.btn-primary
       {:type "submit"
        :on-click #(dispatch [:voices/speak])}
        ; :on-click #(.speak js/speechSynthesis (js/SpeechSynthesisUtterance. "what the hell"))}
       "Speak"]]]])
