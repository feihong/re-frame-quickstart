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

(defn includes-keywords? [text keywords]
  "Return true if text includes one of the keywords"
  (some #(string/includes? text %) keywords))

(defn meets-criteria [{:keys [shortname]} include-keywords exclude-keywords]
  (and
    (or
      (empty? include-keywords)
      (includes-keywords? shortname include-keywords))
    (not (includes-keywords? shortname exclude-keywords))))

(defn get-emojis [include-keywords exclude-keywords]
  (->> emojis
       (filter #(meets-criteria % include-keywords exclude-keywords))))

(defn random-emoji [include-keywords exclude-keywords]
  (-> (get-emojis include-keywords exclude-keywords)
      (rand-nth)))

(defn random-emojis [num include-keywords exclude-keywords]
  "Generate `num` unique emojis"
  (->> (get-emojis include-keywords exclude-keywords)
       shuffle
       (take num)
       (into [])))
