(ns camunda-clojure.tasks)

(defn- task->clj [task]
  {:id (.getId task)
   :assignee (.getAssignee task)
   :name (.getName task)
   :due-date (.getDueDate task)
   :priority (.getPriority task)
   :owner (.getOwner task)})

(defn- execute-query [taskQuery]
  (.list taskQuery))

(def query-params {
                   :task-id #(.taskId %1 %2)
                   :assigned #(if %2 (.isAssigned %1) (.isUnassigned %1))
                   :task-name #(.taskName %1 %2)
                   :task-owner #(.taskOwner %1 %2)
                   :task-assignee #(.taskAssignee %1 %2)})

(defn- add-criteria [task-query key value]
  ((get query-params key) task-query value))

(defn- build-task-query [task-service criteria]
  (let [query (.createTaskQuery task-service)]
    (doseq [[key val] criteria]
      (add-criteria query key val))
    query))

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

(defn complete! [engine task]
  (-> engine
      (.getTaskService)
      (.complete (:id task))))
