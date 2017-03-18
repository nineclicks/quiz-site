(ns quiz-site.routes.quiz
  (:use [ring.util.response])
  (:require [clojure.tools.logging :as log]
            [clojure.walk :refer [keywordize-keys]]
            [quiz-site.routes.route-helpers :refer [wrap-status]]
            [org.tobereplaced.mapply :refer [mapply]]
            [matchbox.core :as m]
            [compojure.core :refer :all]))

(def root (m/connect "https://quiz-site-fdc3a.firebaseio.com"))

(m/auth-anon root)

(defn get-quiz
  [& {:keys[quiz-id] :as args}]
  (log/debug "get-quiz")
  (log/debug "args:   " args)
  {:error "Implementation for this method not found."})

(defn get-quiz-results
  [& {:keys[quiz-id] :as args}]
  (log/debug "get-quiz")
  (log/debug "args:   " args)
  {:error "Implementation for this method not found."})

(defn create-quiz
  [& {:keys[quiz-id] :as args}]
  (log/debug "create-quiz")
  (log/debug "args:   " args)
  {:error "Implementation for this method not found."})

(defn submit-answers
  [& {:keys[quiz-id] :as args}]
  (log/debug "submit-answers")
  (log/debug "args:   " args)
  {:error "Implementation for this method not found."})

(def quiz-routes
  (defroutes document-routes
    (context "/quiz" []
             (POST "/" {params :params} (wrap-status (mapply create-quiz params)))
             (context "/:quiz-id" [quiz-id]
                      (GET  "/" {params :params} (wrap-status (mapply get-quiz       params)))
                      (POST "/" {params :params} (wrap-status (mapply submit-answers params)))))
    (context "/results/:results-id" [results-id]
             (GET "/" {params :params} (wrap-status (mapply get-quiz-results params))))))
