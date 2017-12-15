(ns camunda-clojure.core
  (:require [camunda-clojure.runtime :as runtime]
            [camunda-clojure.tasks :as tasks]
            [camunda-clojure.repository :as repository]
            [camunda-clojure.engine :as engine]
            [camunda-clojure.delegates :as delegates])
  (:import (org.camunda.bpm.engine ProcessEngine
                                   ProcessEngineConfiguration))
  (:gen-class))

(compile (symbol "camunda-clojure.delegates"))

; Camunda ProcessEngine gets created on application startup
(def engine (engine/create-engine {:jdbc-url "jdbc:h2:mem:my-own-db;DB_CLOSE_DELAY=1000"
                                   :db-schema-update "create-drop"
                                   :job-executor-activate true}))

; Convenience function. Call this to deploy the three process definitions available under /resources
(defn deploy []
  (repository/deploy! engine "MyDeployment" ["Process_1.bpmn" "Process_2.bpmn" "Process_3.bpmn"]))

; this is example code to see basic scenarios with 'lein run'
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Deploying process definitions ...")
  (deploy)

  (println "Starting process instance of Process_1")
  (runtime/start-process! engine "Process_1" {"var1" "Hallo" "var2" "Welt"})

  (println "Starting process instance of Process_2")
  (runtime/start-process! engine "Process_2")

  (println "Running process instances:\n\t" (runtime/find-process-instances engine))

  (let [task (first (tasks/find-tasks engine))]
    (println "Available task:\n\t" task)
    (println "Claiming the task ...")
    (tasks/claim! engine task "John Doe")

    (println "Completing the task" (:id task) " ..." )
    (tasks/complete! engine task))

  (println "Running process instances:\n\t" (runtime/find-process-instances engine))
  (println "Available tasks:\n\t" (runtime/find-process-instances engine))
  (System/exit 0))

