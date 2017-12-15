(ns camunda-clojure.engine
  (:import (org.camunda.bpm.engine ProcessEngine
                                   ProcessEngineConfiguration)))

(def schema-update {"true" ProcessEngineConfiguration/DB_SCHEMA_UPDATE_TRUE
                    "create-drop" ProcessEngineConfiguration/DB_SCHEMA_UPDATE_CREATE_DROP} )

(def config-params {
                    :db-schema-update #(.setDatabaseSchemaUpdate %1 (cond %2 "true" (false? %2) "false" :else %2)) ; TODO
                    :jdbc-url #(.setJdbcUrl %1 %2)
                    :jdbc-username #(.setJdbcUsername %1 %2)
                    :jdbc-password #(.setJdbcPassword %1 %2)
                    :job-executor-activate #(.setJobExecutorActivate %1 %2)
                    })

(defn- add-config [config key value]
  ((get config-params key) config value))

(defn- create-configuration [params]
  (let [config (ProcessEngineConfiguration/createStandaloneProcessEngineConfiguration)]
    (doseq [[key value] params]
      (add-config config key value))
    config))

(defn create-engine [params]
  (-> (create-configuration params)
      (.buildProcessEngine)))

(defn create-in-memory-engine []
  (-> (ProcessEngineConfiguration/createStandaloneInMemProcessEngineConfiguration)
      (.setJobExecutorActivate true)
      (.buildProcessEngine)))
