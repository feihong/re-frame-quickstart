(ns quickstart.routes.services
  (:require [mount.core :as mount]
            [ring.util.http-response :refer [ok]]
            [compojure.api.sweet :refer [context GET api]]
            [schema.core :as s]
            [quickstart.db.core :as db]
            [quickstart.config :refer [env]]
            [quickstart.fonts :as fonts]))


(def Word
  {:id Integer
   :word String
   :gloss String
   :pinyin String})

(mount/defstate service-routes
  :start
  (api
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
        :summary "Return a random word"
        (ok (db/random-word)))

      (GET "/fonts" []
        :return [String]
        :summary "Return names of Chinese fonts on system"
        (ok (fonts/chinese-fonts))))))
