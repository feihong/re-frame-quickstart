(ns quickstart.events
  (:require [re-frame.core :refer [dispatch reg-event-db reg-event-fx trim-v path]]
            day8.re-frame.http-fx
            [ajax.core :as ajax]
            [quickstart.db :as db]
            [quickstart.util :as util]
            [quickstart.emoji :as emoji]))


(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :set-active-page
  [trim-v]
  (fn [db [page]]
    (assoc db :page page)))

(reg-event-fx
  :hanzi/load-word
  [(path :hanzi)]
  (fn [_]
    {:http-xhrio {:method :get
                  :uri "/api/random-word"
                  :timeout 3000
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:hanzi/good-result]
                  :on-failure [:hanzi/bad-result]}}))

(reg-event-db
  :hanzi/good-result
  [(path :hanzi) trim-v]
  (fn [db [result]]
    (let [item (assoc result :correct false)]
      (assoc db :current item
                :error nil))))

(reg-event-db
  :hanzi/bad-result
  [(path :hanzi) trim-v]
  (fn [db [result]]
    (assoc db :error result)))

(reg-event-db
  :hanzi/show-meta
  [(path :hanzi) trim-v]
  (fn [db [value]]
    (assoc db :show-meta value)))

(reg-event-fx
  :hanzi/mark
  [(path :hanzi) trim-v]
  (fn [{:keys [db]} [correct]]
    {:db (util/move-current-to-history db correct)
     :dispatch [:hanzi/load-word]}))

(reg-event-db
  :emoji/set-count
  [(path :emoji) trim-v]
  (fn [db [value]]
    (assoc db :count (js/parseInt value))))

(reg-event-db
  :emoji/generate
  [(path :emoji)]
  (fn [{:keys [count] :as db} _]
    (assoc db :emojis (emoji/random-emojis count db))))

(reg-event-db
  :emoji/replace-one
  [(path :emoji) trim-v]
  (fn [db [index]]
    (->> (emoji/random-emoji db)
         (assoc-in db [:emojis index]))))

(reg-event-db
  :emoji/set-include
  [(path :emoji) trim-v]
  (fn [db [value]]
    (assoc db :include-text value
              :include-keywords (util/get-keywords value))))

(reg-event-db
  :emoji/set-exclude
  [(path :emoji) trim-v]
  (fn [db [value]]
    (assoc db :exclude-text value
              :exclude-keywords (util/get-keywords value))))

(reg-event-fx
  :emoji/set-category
  [(path :emoji) trim-v]
  (fn [{:keys [db]} [value]]
    {:dispatch [:emoji/generate]
     :db (assoc db :category value)}))

; Source: http://clarkonium.net/2016/07/speech-synthesis-in-clojurescript/
(reg-event-db
  :voices/load-voices
  [(path :voices)]
  (fn [db _]
    (let [get-voices (fn [] (.getVoices js/speechSynthesis))]
      (dispatch [:voices/voices-loaded (get-voices)])
      ; Chrome won't load voices until onvoiceschanged fires
      (set! (.-onvoiceschanged js/speechSynthesis)
            #(dispatch [:voices/voices-loaded (get-voices)]))
      db)))

(reg-event-db
  :voices/voices-loaded
  [(path :voices) trim-v]
  (fn [db [voice-objects]]
    (let [voices (->> voice-objects
                      (map (fn [v] {:name (.-name v)
                                    :lang (.-lang v)
                                    :obj v}))
                      (map-indexed (fn [i m] (assoc m :idx i))))]
      (println "Got" (count voice-objects) "voices")
      (assoc db :voices voices
                :current
                (cond
                  (some #(= (:lang %) "zh-CN") voices)
                  (->> voices (filter #(= (:lang %) "zh-CN")) first :name)
                  (> (count voices) 0)
                  (-> voices first :name)
                  :else
                  "")))))

(reg-event-db
  :voices/speak
  [(path :voices)]
  (fn [{:keys [phrase current voices] :as db} _]
    ; Is there a way to turn this into a pure function?
    (let [voice-obj (-> (filter #(= current (% :name)) voices)
                        first
                        :obj)
          utterance (doto (js/SpeechSynthesisUtterance.)
                          (aset "text" phrase)
                          (aset "voice" voice-obj))]
      (.speak js/speechSynthesis utterance))
    db))

(reg-event-db
  :voices/set-phrase
  [(path :voices) trim-v]
  (fn [db [value]]
    (assoc db :phrase value)))

(reg-event-db
  :voices/set-voice
  [(path :voices) trim-v]
  (fn [db [name]]
    (assoc db :current name)))

(reg-event-db
  :fonts/set-text
  [(path :fonts) trim-v]
  (fn [db [text]]
    (assoc db :text text)))

(reg-event-fx
  :fonts/load
  (fn [_]
    {:http-xhrio {:method :get
                  :uri "/api/fonts"
                  :timeout 3000
                  :response-format (ajax/json-response-format)
                  :on-success [:fonts/good-result]
                  :on-failure [:fonts/bad-result]}}))

(reg-event-db
  :fonts/good-result
  [(path :fonts) trim-v]
  (fn [db [result]]
    (assoc db :fonts result)))
