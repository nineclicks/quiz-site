(ns quiz-site.server
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [compojure.core :refer [context ANY GET PUT POST DELETE defroutes]]
            [compojure.route :refer [resources]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :as json]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [environ.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]]
            [quiz-site.routes.quiz]
            )
  (:gen-class))

(defroutes routes
  (GET "/" _
    {:status 200
     :headers {"Content-Type" "text/html; charset=utf-8"}
     :body (io/input-stream (io/resource "public/index.html"))})
  (GET "/quiz/:quiz-id" [quiz-id] {:status 200
                                   :headers {"Content-Type" "text/html; charset=utf-8"}
                                   :body (string/replace (slurp (io/resource "public/takequiz.html")) ":quiz-id:" quiz-id)})
  (context "/api" []
           (-> (defroutes document-routes
                 quiz-site.routes.quiz/quiz-routes)
               (json/wrap-json-response)
               (json/wrap-json-params)))
  (resources "/"))

(def http-handler
  (-> routes
      (wrap-defaults api-defaults)
      wrap-with-logger
      wrap-gzip))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 10555))]
    (run-jetty http-handler {:port port :join? false})))
