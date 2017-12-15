(ns camunda-clojure.repository)

(defn- create-deployment [engine deployment-name]
  (-> engine
      (.getRepositoryService)
      (.createDeployment)
      (.name deployment-name)))

(defn deploy! [engine deployment-name resources]
  (let [deployment (create-deployment engine deployment-name)]
    (doseq [resource resources]
      (.addClasspathResource deployment resource))
    (.deploy deployment)))
