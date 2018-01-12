(defproject camunda-clojure "0.1.0-SNAPSHOT"
  :description "This is a prototype library to find out how it feels to develop Camunda process applications with Clojure"
  :url "https://github.com/tobiasbehr/camunda-clojure"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :profiles {
             :test {
                    :dependencies [[org.camunda.bpm/camunda-engine "7.8.0"]
                                   [cljito "0.2.1"]
                                   [org.mockito/mockito-all "1.9.5"]]}})
