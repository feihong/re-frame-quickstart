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
                    :val (random-hanzi)}]
        (swap! current-id inc)
        result))))
