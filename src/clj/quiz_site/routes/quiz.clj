(ns quiz-site.routes.quiz
  (:use [ring.util.response])
  (:require [clojure.tools.logging :as log]
            [clojure.walk :refer [keywordize-keys]]
            [quiz-site.routes.route-helpers :refer [wrap-status]]
            [org.tobereplaced.mapply :refer [mapply]]
            [matchbox.core :as m]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [compojure.core :refer :all]))

(def fburl "https://quiz-site-fdc3a.firebaseio.com")
(def root (m/connect "https://quiz-site-fdc3a.firebaseio.com"))

(m/auth-anon root)

(defn generate-code
  []
  (reduce
    str
    (take
      16
      (repeatedly
        #(rand-nth
           (seq
             (char-array "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")))))))

(defn do-get-quiz*
  [quiz-id]
  (json/read-str (:body (client/get (str fburl "/quizzes/" quiz-id ".json")))
                 :key-fn keyword))

(defn push-quiz
  [quiz host]
  (let [quiz-id (generate-code)
        result-id (generate-code)]
    (m/reset-in! root [:quizzes (keyword quiz-id)] (merge quiz {:result-id result-id}))
    (m/reset-in! root [:results (keyword result-id)] {:quiz-id quiz-id})
    {:quiz-url (str host "/quiz/" quiz-id)
     :result-url (str host "/quiz/" result-id)}))

(defn get-quiz
  [& {:keys[quiz-id] :as args}]
  (log/debug "get-quiz")
  (log/debug "args:   " args)
  (let [quiz (do-get-quiz* quiz-id)]
    quiz))

(defn get-quiz-results
  [& {:keys[quiz-id] :as args}]
  (log/debug "get-quiz")
  (log/debug "args:   " args)
  {:error "Implementation for this method not found."})

(defn create-quiz
  [& {:keys[params headers] :as args}]
  (log/debug "create-quiz")
  (log/debug "params " params)
  (push-quiz params (:host headers))
  )

(defn submit-answers
  [& {:keys[quiz-id] :as args}]
  (log/debug "submit-answers")
  (log/debug "args:   " args)
  {:error "Implementation for this method not found."})

(def quiz-routes
  (defroutes document-routes
    (context "/quiz" []
             (POST "/" {:keys [headers params] :as request} (wrap-status (mapply create-quiz request)))
             (context "/:quiz-id" [quiz-id]
                      (GET  "/" {params :params} (wrap-status (mapply get-quiz       params)))
                      (POST "/" {params :params} (wrap-status (mapply submit-answers params)))))
    (context "/results/:results-id" [results-id]
             (GET "/" {params :params} (wrap-status (mapply get-quiz-results params))))))
