(ns quickstart.db
  (:require [quickstart.util :as util]))


(def default-db
  {:page :home
   :hanzi (util/new-hanzi-item)
   :hanzi-history []})
