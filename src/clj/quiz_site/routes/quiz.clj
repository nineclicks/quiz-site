(ns quiz-site.routes.quiz
  (:use [ring.util.response])
  (:require [clojure.tools.logging :as log]
            [clojure.walk :refer [keywordize-keys]]
            [quiz-site.routes.route-helpers :refer [wrap-status]]
            [org.tobereplaced.mapply :refer [mapply]]
            [compojure.core :refer :all]))

(defn get-quiz
  [& {:keys[quiz-id] :as args}]
  (log/debug "get-quiz")
  (log/debug "args:   " args)
  {:error "Implementation for this method not found."})

(def quiz-routes
  (context "/quiz" []
           (GET  "/:quiz-id" {params :params} (wrap-status (mapply get-quiz params)))))
