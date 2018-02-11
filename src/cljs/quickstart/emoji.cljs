(ns quickstart.emoji
  (:require [clojure.string :as str]))


(def shortnames (-> (.-shortnames js/emojione)
                    (str/split "|")))

(defn get-emoji [shortname]
  (-> (js/emojione.shortnameToImage shortname)
      ((fn [html]
         (let [div (js/document.createElement "div")]
           (set! (.-innerHTML div) html)
           (.-firstChild div))))
      ((fn [img]
         {:text (.-alt img)
          :shortname (.-title img)
          :url (.-src img)}))))

(defn random-emoji []
  (-> (rand-nth shortnames) get-emoji))

(defn random-emojis [count]
  (for [_ (range count)]
    (random-emoji)))
