(ns quickstart.app
  (:require [quickstart.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))
