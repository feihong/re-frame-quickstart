(ns quickstart.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [quickstart.word :as word]))

(def Word
  {:word String
   :gloss String
   :pinyin String})

(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}}

  (context "/api" []
    :tags ["quickstart"]

    (GET "/random-word" []
      :return Word
      :query-params [{len :- Integer 1}]
      :summary "Return a random word with the given length"
      (ok (word/random-word)))))
