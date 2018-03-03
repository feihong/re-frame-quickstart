(ns quickstart.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
  :hanzi/current
  (fn [db _]
    (-> db :hanzi :current)))

(reg-sub
  :hanzi/history
  (fn [db _]
    (-> db :hanzi :history)))

(reg-sub
  :hanzi/total-points
  (fn [_]
    (subscribe [:hanzi/history]))
  (fn [items _]
    (count items)))

(reg-sub
  :hanzi/score
  (fn [_]
    (subscribe [:hanzi/history]))
  (fn [items _]
    (->> items (filter :correct) count)))

(reg-sub
  :hanzi/show-meta
  (fn [db _]
    (-> db :hanzi :show-meta)))

(reg-sub
  :hanzi/error
  (fn [db _]
    (-> db :hanzi :error)))

(reg-sub
  :emoji/count
  (fn [db _]
    (-> db :emoji :count)))

(reg-sub
  :emoji/emojis
  (fn [db _]
    (->> db
         :emoji
         :emojis
         ; Add indexes to each map
         (map-indexed #(assoc %2 :idx %1)))))

(reg-sub
  :emoji/include
  (fn [db _]
    (-> db :emoji :include-text)))

(reg-sub
  :emoji/exclude
  (fn [db _]
    (-> db :emoji :exclude-text)))

(reg-sub
  :emoji/category
  (fn [db _]
    (-> db :emoji :category)))

(reg-sub
  :voices/voices
  (fn [db _]
    (-> db :voices :voices)))

(reg-sub
  :voices/current
  (fn [db _]
    (-> db :voices :current)))

(reg-sub
  :voices/phrase
  (fn [db _]
    (-> db :voices :phrase)))

(reg-sub
  :fonts/fonts
  (fn [db _]
    (:fonts db)))
