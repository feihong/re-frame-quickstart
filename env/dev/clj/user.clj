(ns user
  (:require [mount.core :as mount]
            [quickstart.figwheel :refer [start-fw stop-fw cljs]]
            [quickstart.core :refer [start-app]]))

(defn start []
  (mount/start-without #'quickstart.core/repl-server))

(defn stop []
  (mount/stop-except #'quickstart.core/repl-server))

(defn restart []
  (stop)
  (start))

(defn start-all []
  (start)
  (start-fw))
