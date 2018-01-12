(ns camunda-clojure.common)

(defn populate-builder [builder param-repository params]
  (doseq [[key value] params]
    ((get param-repository key) builder value))
  builder)
