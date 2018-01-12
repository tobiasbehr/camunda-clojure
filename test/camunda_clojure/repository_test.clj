(ns camunda-clojure.repository-test
  (:require [clojure.test :refer :all]
            [camunda-clojure.repository :refer :all]
            [cljito.core :as mockito])
  (:import (org.camunda.bpm.engine ProcessEngine
                                   RepositoryService)
           (org.camunda.bpm.engine.repository DeploymentBuilder
                                              ProcessApplicationDeployment)))

(def engine (mockito/mock ProcessEngine))
(def repository-service (mockito/mock RepositoryService))
(def deployment-builder (mockito/mock DeploymentBuilder))
(def deployment (mockito/mock ProcessApplicationDeployment))

(mockito/when-> engine (.getRepositoryService) (.thenReturn repository-service))
(mockito/when-> repository-service (.createDeployment) (.thenReturn deployment-builder))

(mockito/when-> deployment-builder (.name (mockito/any-String)) (.thenReturn deployment-builder))
(mockito/when-> deployment-builder (.addClasspathResource (mockito/any-String)) (.thenReturn deployment-builder))
(mockito/when-> deployment-builder (.deploy) (.thenReturn deployment))

(deftest deploy-test
  (testing "Deployment"
    (deploy! engine "Tester" ["res1"])
    (mockito/verify-> deployment-builder (.addClasspathResource "res1"))
    (mockito/verify-> deployment-builder (.deploy))))
