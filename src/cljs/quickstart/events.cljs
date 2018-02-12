(ns quickstart.events
  (:require [quickstart.db :as db]
            [quickstart.util :as util]
            [quickstart.emoji :as emoji]
            [re-frame.core :refer [dispatch reg-event-db trim-v path]]))


(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :set-active-page
  [trim-v]
  (fn [db [page]]
    (assoc db :page page)))

(reg-event-db
  :mark-as-incorrect
  [(path :hanzi)]
  (fn [db _]
    (util/generate-hanzi db false)))

(reg-event-db
  :mark-as-correct
  [(path :hanzi)]
  (fn [db _]
    (util/generate-hanzi db true)))

(reg-event-db
  :set-emoji-count
  [(path :emoji) trim-v]
  (fn [db [value]]
    (assoc db :count (js/parseInt value))))

(reg-event-db
  :generate-emojis
  [(path :emoji)]
  (fn [{:keys [count exclude-keywords] :as db} _]
    (assoc db :emojis (emoji/random-emojis count exclude-keywords))))

(reg-event-db
  :replace-emoji
  [(path :emoji)]
  (fn [{:keys [emojis exclude-keywords] :as db} [_ index]]
    (let [new-item (->> (repeatedly #(emoji/random-emoji-with-conditions exclude-keywords))
                        (drop-while #(some #{%} emojis))
                        first)]
      (assoc-in db [:emojis index] new-item))))

(reg-event-db
  :set-emoji-include
  [(path :emoji) trim-v]
  (fn [db [value]]
    (assoc db :include-text value
              :include-keywords (util/get-keywords value))))

(reg-event-db
  :set-emoji-exclude
  [(path :emoji) trim-v]
  (fn [db [value]]
    (assoc db :exclude-text value
              :exclude-keywords (util/get-keywords value))))
