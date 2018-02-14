(ns quickstart.events
  (:require [quickstart.db :as db]
            [quickstart.util :as util]
            [quickstart.emoji :as emoji]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx trim-v path]]))


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
  :hanzi/mark-incorrect
  [(path :hanzi)]
  (fn [db _]
    (util/generate-hanzi db false)))

(reg-event-db
  :hanzi/mark-correct
  [(path :hanzi)]
  (fn [db _]
    (util/generate-hanzi db true)))

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
