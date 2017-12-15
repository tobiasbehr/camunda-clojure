(ns camunda-clojure.runtime)

(defn process-instance->clj [pi]
  {:id (.getId pi)
   :instance-id (.getProcessInstanceId pi)
   :business-key (.getBusinessKey pi)
   :definition-id (.getProcessDefinitionId pi)
   :is-suspended (.isSuspended pi)
   :is-ended (.isEnded pi)})

(defn start-process! 
  ([engine key] (start-process! engine key {}))
  ([engine key vars]
   (let [process-instance (-> (.getRuntimeService engine)
                              (.startProcessInstanceByKey key vars))]
     (process-instance->clj process-instance))))

(defn find-process-instances 
  ([engine] (find-process-instances engine {}))
  ([engine criteria]
   (map process-instance->clj (-> engine
                                  (.getRuntimeService)
                                  (.createProcessInstanceQuery)
                                  (.list)))))

(defn get-variables [engine execution]
  (-> engine
      (.getRuntimeService)
      (.getVariables (:id execution))))
