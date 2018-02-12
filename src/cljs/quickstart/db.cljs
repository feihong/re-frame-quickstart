(ns quickstart.db
  (:require [quickstart.util :as util]))


(def default-db
  {:page :home
   :hanzi {:current (util/new-hanzi-item)
           :history []}
   :emoji {:count 20
           :include-text ""
           :include-keywords ""
           :exclude-text "flag"
           :exclude-keywords ["flag"]
           :emojis []}})
