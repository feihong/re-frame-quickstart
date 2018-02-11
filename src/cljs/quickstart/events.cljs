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
    (util/generate-hanzi db false)))
  
(reg-event-db
  :mark-as-correct
  (fn [db _]
    (util/generate-hanzi db true)))

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
