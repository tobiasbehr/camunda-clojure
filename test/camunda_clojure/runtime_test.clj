(ns camunda-clojure.runtime-test
  (:require [clojure.test :refer :all]
            [camunda-clojure.runtime :refer :all]
            [cljito.core :as mockito])
  (:import (org.camunda.bpm.engine ProcessEngine
                                   RuntimeService)))

(def engine (mockito/mock ProcessEngine))
(def runtime-service (mockito/mock RuntimeService))

(mockito/when-> engine (.getRuntimeService) (.thenReturn runtime-service))

(deftest get-variables-test
  (testing "get-variables"
    (let [variables {"var1" "value1"}]
      (mockito/when-> runtime-service 
                      (.getVariables (mockito/any-String)) 
                      (.thenReturn variables))
      (is (= (get-variables engine {:id "4711"}) variables)))))

(deftest set-variables-test
  (let [execution {:id "4711"}
        variables {"var1" "value1" "var2" "value2"}]
    (testing "set-variable!"
      (set-variable! engine execution "key" "value")
      (mockito/verify-> runtime-service (.setVariable "4711" "key" "value")))
    (testing "set-variables!"  
      (set-variables! engine execution variables)
      (mockito/verify-> runtime-service (.setVariables "4711" variables)))))
