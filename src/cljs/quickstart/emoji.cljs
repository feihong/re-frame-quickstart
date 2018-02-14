(ns quickstart.emoji
  (:require [clojure.string :as string]))

(def emojis
  (let [emap (.-emojioneList js/emojione)]
    (->> emap
         js/Object.keys
         (map (fn [shortname]
                (let [item (aget emap shortname)]
                  {:shortname shortname
                   :text (-> (aget item "uc_output") js/emojione.convert)
                   :category (aget item "category")}))))))

(def categories
  (->> emojis
       (reduce #(conj %1 (:category %2)) #{})
       (into [])))

(defn includes-keywords? [text keywords]
  "Return true if text includes one of the keywords"
  (some #(string/includes? text %) keywords))

(defn meets-criteria [{:keys [shortname] :as emoji} {:keys [category include-keywords exclude-keywords]}]
  (and
    (or (string/blank? category)
        (= category (:category emoji)))
    (or
      (empty? include-keywords)
      (includes-keywords? shortname include-keywords))
    (not (includes-keywords? shortname exclude-keywords))))

(defn get-emojis [options]
  (->> emojis
       (filter #(meets-criteria % options))))

(defn random-emoji [options]
  (-> (get-emojis options)
      (rand-nth)))

(defn random-emojis [num options]
  "Generate `num` unique emojis"
  (->> (get-emojis options)
       shuffle
       (take num)
       (into [])))
