(ns quickstart.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [quickstart.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[quickstart started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[quickstart has shut down successfully]=-"))
   :middleware wrap-dev})
