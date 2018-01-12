(ns camunda-clojure.runtime
  (:require [camunda-clojure.common :as common]))

(defn process-instance->clj [pi]
  {:id (.getId pi)
   :instance-id (.getProcessInstanceId pi)
   :business-key (.getBusinessKey pi)
   :definition-id (.getProcessDefinitionId pi)
   :is-suspended (.isSuspended pi)
   :is-ended (.isEnded pi)})

(def query-params {
                   :process-instance-id #(.processInstanceId %1 %2)
                   :active #(if %2 (.active %1))
                   :business-key #(.processInstanceBusinessKey %1 %2)
                   :activity-id-in #(.activityIdIn %1 (into-array %2))
                   :deployment-id #(.deploymentId %1 %2)
                   })

(defn- build-instance-query [runtime-service criteria]
  (common/populate-builder (.createProcessInstanceQuery runtime-service) query-params criteria))

(defn start-process! 
  ([engine key] (start-process! engine key nil))
  ([engine key business-key] (start-process! engine key business-key {}))
  ([engine key business-key vars]
   (let [process-instance (-> (.getRuntimeService engine)
                              (.startProcessInstanceByKey key (str business-key) vars))]
     (process-instance->clj process-instance))))

(defn find-process-instances 
  ([engine] (find-process-instances engine {}))
  ([engine criteria]
   (let [result (-> engine
                    (.getRuntimeService)
                    (build-instance-query criteria)
                    (.list))]
     (map process-instance->clj result))))

(defn get-variables [engine execution]
  (-> engine
      (.getRuntimeService)
      (.getVariables (:id execution))))

(defn set-variable! [engine execution key value]
  (-> engine
      (.getRuntimeService)
      (.setVariable (:id execution) key value)))

(defn set-variables! [engine execution variables]
  (-> engine
      (.getRuntimeService)
      (.setVariables (:id execution) variables)))

(defn message-event-received! 
  ([engine execution message-name]
   (message-event-received! engine execution message-name {}))
  ([engine execution message-name variables]
   (-> engine
       (.getRuntimeService)
       (.messageEventReceived message-name (:id execution) variables))))

;; TODO
; CorrelateMessage
; runtimeService.messageEventReceived
; runtimeService.createExecutionQuery
