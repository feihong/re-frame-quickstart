(ns quickstart.word
  (:require [quickstart.util :as util]))

(defn random-word []
  {:word (util/random-hanzi)
   :gloss "gloss"
   :pinyin "pin1yin1"})
