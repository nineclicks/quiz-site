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

(defn do-get-results*
  [result-id]
  (json/read-str (:body (client/get (str fburl "/results/" result-id ".json")))
                 :key-fn keyword))

(defn do-put-results*
  [result-id results]
  (client/post (str fburl "/results/" result-id "/subs.json") {:form-params results
                                                               :content-type :json}))

(defn push-quiz
  [quiz host]
  (let [quiz-id (generate-code)
        result-id (generate-code)]
    (m/reset-in! root [:quizzes (keyword quiz-id)] (merge quiz {:result-id result-id}))
    (m/reset-in! root [:results (keyword result-id)] {:quiz-id quiz-id})
    {:quiz-url (str host "/quiz/" quiz-id)
     :result-url (str host "/result/" result-id)}))

(defn get-quiz
  [& {:keys[quiz-id] :as args}]
  (log/debug "get-quiz")
  (log/debug "args:   " args)
  (let [quiz (do-get-quiz* quiz-id)
        qs   (:questions quiz)
        qs2  (reduce-kv
               (fn [m k v]
                 (assoc m k (dissoc v :c)))
               {}
               qs)]
    (assoc (dissoc quiz :questions :result-id) :questions qs2)))

(defn get-quiz-results
  [& {:keys[result-id] :as args}]
  (log/debug "get-quiz")
  (log/debug "args:   " args)
  (let [results (do-get-results* result-id)
        subs (vals (:subs results))
        quiz-id (:quiz-id results)
        quiz (do-get-quiz* quiz-id)
        nQ (:nQuestions quiz)
        video (:video quiz)
        name (:name quiz)]
    {:nQuestions nQ
     :video video
     :name name
     :results (map (fn [x] (assoc x :score (quot (* 100 (:correct x)) nQ)))subs)}
  ))

(defn create-quiz
  [& {:keys[params headers] :as args}]
  (log/debug "create-quiz")
  (push-quiz params (:host (keywordize-keys headers))))

(defn submit-answers
  [& {:keys[params] :as args}]
  (log/debug "submit-answers")
  (log/debug "args:   " args)
  (let [quiz (do-get-quiz* (:quiz-id params))
        qs   (:questions quiz)
        qkeys (keys qs)
        results (keywordize-keys params)
        answers (:answers results)
        cmpfn (fn [x] (if (= (:c (x qs)) (x answers)) true false))
        ncorrect (count (filter cmpfn qkeys))
        sid (:sid results)
        rid (:result-id quiz)
        submission {:sid sid
                    :correct ncorrect
                    :time (new java.util.Date)}
        ]
    (do-put-results* rid submission)
    {:correct ncorrect
     :outof (:nQuestions quiz)
     :sid sid}))

(def quiz-routes
  (defroutes document-routes
    (context "/quiz" []
             (POST "/" {:keys [headers params] :as request} (wrap-status (mapply create-quiz request)))
             (context "/:quiz-id" [quiz-id]
                      (GET  "/" {params :params} (wrap-status (mapply get-quiz       params)))
                      (POST "/" {:keys [headers params] :as request} (wrap-status (mapply submit-answers request)))))
    (context "/results/:result-id" [result-id]
             (GET "/" {params :params} (wrap-status (mapply get-quiz-results params))))))
