(ns quickstart.db
  (:require [quickstart.util :as util]
            [quickstart.emoji :as emoji]))


(def default-voices
  {:voices []
   :current ""
   :phrase "Hello World 你好世界"})

(def default-db
  {:page :home
   :hanzi {:current nil
           :history []
           :show-meta false
           :error nil}
   :emoji {:count 20
           :category ""
           :include-text ""
           :include-keywords []
           :exclude-text ""
           :exclude-keywords []
           :emojis (emoji/random-emojis 20 {})}
   :voices default-voices
   :fonts {:fonts []
           :text "我们安排一个活动吧"}})
