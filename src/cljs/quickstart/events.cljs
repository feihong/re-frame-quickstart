(ns quickstart.events
  (:require [quickstart.db :as db]
            [quickstart.util :as util]
            [re-frame.core :refer [dispatch reg-event-db reg-sub]]))

;;dispatchers

(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :set-active-page
  (fn [db [_ page]]
    (assoc db :page page)))

(reg-event-db
  :set-docs
  (fn [db [_ docs]]
    (assoc db :docs docs)))

(reg-event-db
  :generate-hanzi
  (fn [db _]
    (let [old-hanzi (:hanzi db)
          new-hanzi (util/new-hanzi-item)
          new-history (conj (:hanzi-history db) old-hanzi)]
      (assoc db :hanzi new-hanzi
                :hanzi-history new-history))))

;;subscriptions

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
  :docs
  (fn [db _]
    (:docs db)))

(reg-sub
  :hanzi
  (fn [db _]
    (:hanzi db)))

(reg-sub
  :hanzi-history
  (fn [db _]
    (:hanzi-history db)))
