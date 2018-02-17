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

(defn move-current-to-history [db correct]
  "Move the current word to history"
  (let [old-word (-> (:current db)
                     (assoc :correct correct))
        new-history (conj (:history db) old-word)]
    (assoc db :history new-history
              :show-meta false)))

(defn get-keywords [text]
  (as-> (string/lower-case text) $
        (string/split $ #"\s+")
        (remove string/blank? $)))
