(ns quickstart.emoji
  (:require [clojure.string :as string]))


(def emojis (as-> (.-shortnames js/emojione) $
                  (string/split $ "|")
                  (map (fn [v] {:shortname v
                                :text (js/emojione.shortnameToUnicode v)})
                       $)
                  (filter #(-> (:text %)
                               (string/starts-with? ":")
                               not)
                          $)))

(defn includes-keywords? [text keywords]
  "Return true if text includes one of the keywords"
  (some #(string/includes? text %) keywords))

(defn meets-criteria [shortname include-keywords exclude-keywords]
  (and
    (or
      (empty? include-keywords)
      (includes-keywords? shortname include-keywords))
    (not (includes-keywords? shortname exclude-keywords))))

(defn get-emojis [include-keywords exclude-keywords]
  (->> emojis
       (filter #(meets-criteria (:shortname %) include-keywords exclude-keywords))))

(defn random-emoji [include-keywords exclude-keywords]
  (-> (get-emojis include-keywords exclude-keywords)
      (rand-nth)))

(defn random-emojis [num include-keywords exclude-keywords]
  "Generate `num` unique emojis"
  (->> (get-emojis include-keywords exclude-keywords)
       shuffle
       (take num)
       (into [])))
