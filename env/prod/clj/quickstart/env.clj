(ns quickstart.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[quickstart started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[quickstart has shut down successfully]=-"))
   :middleware identity})
