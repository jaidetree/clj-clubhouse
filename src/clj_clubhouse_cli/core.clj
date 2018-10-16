(ns clj-clubhouse-cli.core
  (:require [config.core :refer [env]])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (clojure.pprint/pprint (:clubhouse-api-token env)))

