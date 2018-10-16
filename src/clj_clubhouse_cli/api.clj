(ns clj-clubhouse-cli.api
  (:require [clj-http.client :as client]
            [config.core :refer [env]]))

(def url "https://api.clubhouse.io/api/v2/%s")
(def default-params {:token (:clubhouse-api-token env)})

(defn get!
  [resource get-params]
  (:body (client/get (format url resource)
                     {:query-params (merge default-params get-params)
                      :as :json})))

(defn get-workflows!
  []
  (get! "workflows" {}))

(defn get-projects!
  []
  (get! "projects" {}))

(defn get-stories!
  [project-id]
  (get! (format "projects/%s/stories" project-id) {}))

(defn read-config!
  []
  (read-string (slurp "projects.secret.edn")))

(defn in-workflow?
  [id story]
  (= (:workflow_state_id story) id))
