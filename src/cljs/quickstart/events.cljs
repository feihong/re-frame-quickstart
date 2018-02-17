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
  (fn [db _]
    {:http-xhrio {:method :get
                  :uri (str js/context "/api/random-word")
                  :timeout 3000
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:hanzi/good-result]
                  :on-failure [:hanzi/bad-result]}}))

(reg-event-db
  :hanzi/good-result
  [(path :hanzi) trim-v]
  (fn [db [result]]
    (let [item (assoc result :correct false)]
      (assoc db :current item))))

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
