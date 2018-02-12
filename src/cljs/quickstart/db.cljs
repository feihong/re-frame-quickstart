(ns quickstart.db
  (:require [quickstart.util :as util]))


(def default-db
  {:page :home
   :hanzi {:current (util/new-hanzi-item)
           :history []}
   :emoji {:count 20
           :include-keywords ""
           :exclude-keywords "flag"
           :emojis []}})
