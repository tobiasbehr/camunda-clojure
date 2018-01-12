(ns camunda-clojure.repository)

(defn resource-definition->clj [resource]
 {
  :deployment-id (.getDeploymentId resource)
  :resource-name (.getResourceName resource)
  :diagram-resource-name (.getDiagramResourceName resource)
  :version (.getVersion resource)
  :category (.getCategory resource)
  :history-ttl (.getHistoryTimeToLive resource)
  :key (.getKey resource)
  :tenant-id (.getTenantId resource)
  :id (.getId resource)
  :name (.getName resource)
  })

(defn process-definition->clj [process-definition]
  (merge (resource-definition->clj process-definition) 
         {
          :description (.getDescription process-definition)
          :version-tag (.getVersionTag process-definition)
          :has-start-form-key (.hasStartFormKey process-definition)
          :is-suspended (.isSuspended process-definition)
          }))

(defn deployment->clj [deployment]
  {
   :id (.getId deployment)
   :name (.getName deployment)
   :deployment-time (.getDeploymentTime deployment)
   :source (.getSource deployment)
   :tenant-id (.getTenantId deployment)
   :deployed-process-definitions (map process-definition->clj (.getDeployedProcessDefinitions deployment))
   :deployed-decision-definitions (.getDeployedDecisionDefinitions deployment)
   })

(defn- create-deployment [engine deployment-name]
  (-> engine
      (.getRepositoryService)
      (.createDeployment)
      (.name deployment-name)))

(defn deploy! [engine deployment-name resources]
  (let [deployment (create-deployment engine deployment-name)]
    (doseq [resource resources]
      (.addClasspathResource deployment resource))
    (deployment->clj (.deploy deployment))))

(defn get-process-definitions [engine]
  (let [process-definitions (-> engine
             (.getRepositoryService)
             (.createProcessDefinitionQuery)
             (.list))]
    (map process-definition->clj process-definitions)))
