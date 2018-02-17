(ns quickstart.db
  (:require [quickstart.util :as util]
            [quickstart.emoji :as emoji]))


(def default-db
  {:page :home
   :hanzi {:current nil
           :history []
           :show-meta false}
   :emoji {:count 20
           :category ""
           :include-text ""
           :include-keywords []
           :exclude-text ""
           :exclude-keywords []
           :emojis (emoji/random-emojis 20 {})}})
