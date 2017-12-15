(ns camunda-clojure.delegates)

(gen-class
 :name camunda_clojure.delegates.MyDelegate
 :prefix delegate-
 :implements [org.camunda.bpm.engine.delegate.JavaDelegate])

(defn delegate-execute [this execution]
  (let [variable (.getVariable execution "var1")]
    (println "This is the delegate for var1=" variable)))

(gen-class
 :name camunda_clojure.delegates.MySecondDelegate
 :prefix delegate2-
 :implements [org.camunda.bpm.engine.delegate.JavaDelegate])

(defn delegate2-execute [this execution]
  (let [variable (.getVariable execution "var1")]
    (println "This is the second delegate for var1=" variable)))
