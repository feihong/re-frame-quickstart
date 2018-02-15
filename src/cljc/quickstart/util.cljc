(ns quickstart.util
  (:require [clojure.string :as string]))


(defn random-hanzi []
  (let [start 0x4e00
        end 0x9fff
        ; Generate random int between start and end (inclusive)
        ordinal (-> (- end start)
                    (inc)
                    (rand-int)
                    (+ start))]
    (str (char ordinal))))

(defonce new-hanzi-item
  (let [current-id (atom 0)]
    (fn []
      (let [result {:id @current-id
                    :val (random-hanzi)
                    :correct false}]
        (swap! current-id inc)
        result))))

(defn move-current-to-history [db correct]
  "tbd"
  (let [old-word (-> (:current db)
                     (assoc :correct correct))
        new-history (conj (:history db) old-word)]
    (assoc db :history new-history)))

(defn get-keywords [text]
  (as-> (string/lower-case text) $
        (string/split $ #"\s+")
        (remove string/blank? $)))
