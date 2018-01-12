(ns camunda-clojure.tasks
  (:require [camunda-clojure.common :as common]))

(defn- task->clj [task]
  {:id (.getId task)
   :task-definition-key (.getTaskDefinitionKey task)
   :assignee (.getAssignee task)
   :name (.getName task)
   :due-date (.getDueDate task)
   :priority (.getPriority task)
   :owner (.getOwner task)
   :process-instance-id (.getProcessInstanceId task)})

(defn- execute-query [taskQuery]
  (.list taskQuery))

(def query-params {
                   :id #(.taskId %1 %2)
                   :assigned #(if %2 (.taskAssigned %1) (.taskUnassigned %1))
                   :name #(.taskName %1 %2)
                   :owner #(.taskOwner %1 %2)
                   :assignee #(.taskAssignee %1 %2)
                   :due-date #(.dueDate %1 %2)
                   :due-after #(.dueAfter %1 %2)
                   :due-before #(.dueBefore %1 %2)})

(defn- build-task-query [task-service criteria]
  (common/populate-builder (.createTaskQuery task-service) query-params criteria))

(defn find-tasks 
  ([engine] (find-tasks engine {}))
  ([engine criteria]
   (let [result (-> (.getTaskService engine)
                    (build-task-query criteria)
                    (execute-query))]
     (map task->clj result))))

(defn claim! [engine task user]
  (-> engine
      (.getTaskService)
      (.claim (:id task) user)))

(defn complete! 
  ([engine task]
   (complete! engine task {}))
  ([engine task variables]
   (-> engine
       (.getTaskService)
       (.complete (:id task) variables))))
