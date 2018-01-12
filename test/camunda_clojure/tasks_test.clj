(ns camunda-clojure.tasks-test
  (:require [clojure.test :refer :all]
            [camunda-clojure.tasks :refer :all]
            [cljito.core :as mockito])
  (:import (org.camunda.bpm.engine ProcessEngine
                                   TaskService)
           (org.camunda.bpm.engine.task TaskQuery)))

(def engine (mockito/mock ProcessEngine))
(def task-service (mockito/mock TaskService))
(def task-query (mockito/mock TaskQuery))

(mockito/when-> engine (.getTaskService) (.thenReturn task-service))

(deftest tasks-modification-test
  (let [task {:id "4711"}
        user "john doe"]
    (testing "claim!"
      (claim! engine task user)
      (mockito/verify-> task-service (.claim "4711" user)))
    (testing "complete! without variables"
      (complete! engine task)
      (mockito/verify-> task-service (.complete "4711" {})))
    (testing "complete! with variables"
      (let [variables {"var1" "value1"}]
        (complete! engine task variables)
        (mockito/verify-> task-service (.complete "4711" variables))))))

(deftest task-query-test
  (mockito/when-> task-service (.createTaskQuery) (.thenReturn task-query))
  (testing "id"
    (find-tasks engine {:id "4711"})
    (mockito/verify-> task-query (.taskId "4711")))
  (testing "assigned"
    (find-tasks engine {:assigned true})
    (mockito/verify-> task-query (.taskAssigned)))
  (testing "unassigned"
    (find-tasks engine {:assigned false})
    (mockito/verify-> task-query (.taskUnassigned)))
  (testing "multiple criteria (owner and name)"
    (mockito/when-> task-query (.taskOwner (mockito/any-String)) (.thenReturn task-query))
    (find-tasks engine {:owner "john" :name "taskName"})
    (mockito/verify-> task-query (.taskOwner "john"))
    (mockito/verify-> task-query (.taskName "taskName"))))
