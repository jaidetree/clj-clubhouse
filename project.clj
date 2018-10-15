(defproject clj-clubhouse-cli "0.1.0-SNAPSHOT"
  :description "ClubHouse CLI for common tasks."
  :url "https://github.com/jayzawrotny/clj-clubhouse"
  :license {:name "BSD-3-Clause"
            :url "https://tldrlegal.com/license/bsd-3-clause-license-(revised)"}
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :main ^:skip-aot clj-clubhouse-cli.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
