(ns camunda-clojure.engine
  (:require [camunda-clojure.common :as common])
  (:import (org.camunda.bpm.engine ProcessEngine
                                   ProcessEngineConfiguration)))

(def config-params {
                    :db-schema-update #(.setDatabaseSchemaUpdate %1 (cond %2 "true" (false? %2) "false" :else %2))
                    :jdbc-url #(.setJdbcUrl %1 %2)
                    :jdbc-username #(.setJdbcUsername %1 %2)
                    :jdbc-password #(.setJdbcPassword %1 %2)
                    :job-executor-activate #(.setJobExecutorActivate %1 %2)
                    })

(defn- create-configuration [params]
  (common/populate-builder (ProcessEngineConfiguration/createStandaloneProcessEngineConfiguration) config-params params))

(defn create-engine [params]
  (-> (create-configuration params)
      (.buildProcessEngine)))

(defn create-in-memory-engine []
  (-> (ProcessEngineConfiguration/createStandaloneInMemProcessEngineConfiguration)
      (.setJobExecutorActivate true)
      (.buildProcessEngine)))
