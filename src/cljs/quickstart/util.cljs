(ns quickstart.util)

(defn random-hanzi []
  (let [start 0x4e00
        end 0x9fff
        ordinal (-> (- end start)
                    (inc)
                    (rand-int)
                    (+ start))
        hanzi (str (char ordinal))]
    (println "Random hanzi" hanzi)
    hanzi))
