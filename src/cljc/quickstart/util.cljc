(ns quickstart.util)

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

(defn generate-hanzi [db correct]
  "Generate new hanzi and put the old hanzi in the history, marking it correct
  if appropriate"
  (let [old-hanzi (-> (:hanzi db)
                      (assoc :correct correct))
        new-hanzi (new-hanzi-item)
        new-history (conj (:hanzi-history db) old-hanzi)]
    (assoc db :hanzi new-hanzi
              :hanzi-history new-history)))
