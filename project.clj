(defproject camunda-clojure "0.1.0-SNAPSHOT"
  :description "This is a prototype to find out how it feels to develop Camunda process applications with Clojure"
  :url "https://github.com/tobiasbehr/camunda-clojure"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.camunda.bpm/camunda-engine "7.8.0"]
                 [com.h2database/h2 "1.4.196"]]
  :main ^:skip-aot camunda-clojure.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
