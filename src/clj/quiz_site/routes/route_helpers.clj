(ns quiz-site.routes.route-helpers
  (:use [ring.util.response]))

(defn is-ssl?
  [headers]
  (= "https" (get headers "x-forwarded-proto")))

(defn wrap-status
  [resp]

  ; don't override the response if headers are already set
  (if (or (not (coll? resp))
          (contains? resp :headers))
    resp

    ; maybe override the status code based on key values
    (if (contains? resp :error)
      (cond
        ; HTTP "not found"
        (.contains (:error resp) "not found")
        (status (response resp) 404)

        ; HTTP "not allowed"
        (.contains (:error resp) "not allowed")
        (status (response resp) 405)

        ; HTTP "see other"
        (.contains (:error resp) "exists")
        (status (response resp) 303)

        ; default
        :else
        (response resp))

      ; if a status code is specified, use it, otherwise just return status 200
      (status (response (dissoc resp :status))
              (or (:status resp)
                  200))) ))
