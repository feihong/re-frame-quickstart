(ns quickstart.routes.services
  (:require [ring.util.http-response :refer [ok]]
            [compojure.api.sweet :refer [context GET defapi]]
            [schema.core :as s]
            [quickstart.db.core :as db]
            [quickstart.config :refer [env]]
            [mount.core :as mount]))

(def Word
  {:id Integer
   :word String
   :gloss String
   :pinyin String})

; Seems like this shouldn't go here
(mount/start #'quickstart.config/env)

(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:basePath (:app-context env)
                    :info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}}

  (context "/api" []
    :tags ["api"]

    (GET "/random-word" []
      :return Word
      ; :query-params [{len :- Integer 1}]
      :summary "Return a random word"
      (ok (db/random-word)))))
