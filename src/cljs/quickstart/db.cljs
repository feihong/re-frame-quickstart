(ns quickstart.db
  (:require [quickstart.util :refer [random-hanzi]]))


(def default-db
  {:page :home
   :hanzi (random-hanzi)
   :hanzi-history []})
