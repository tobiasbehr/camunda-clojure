# Camunda Clojure

As I am currently working with Camunda BPM in a Java environment and I am also fascinated by the simplicity of 
Clojure I wanted to find out how it might feel to develop process applications with Clojure. I did some research
but didn't find anything about this so I started this prototype.

## Current state
As of now there are some basic wrappers around commonly used Camunda calls which were necessary to start a process,
query tasks and complete them. When retrieving data from the Camunda engine the results will be mapped to Clojure data
structures (mainly immutable maps) so that further processing in the Clojure code is convenient.

## How to use it
Add the following dependency to your `project.clj`

```
[camunda-clojure "0.1.0"]
```

For more detailed usage examples see the [camunda-clojure-examples](https://github.com/tobiasbehr/camunda-clojure-examples) repository.

## How to implement delegates
One tricky part is to wire together the BPMN process models with the code artifacts. This can be done by using the fully
qualified class name of the delegate and set this as the Java Class for the `Service Task` in the Camunda Modeler. However,
Clojure doesn't generate class files out of the box. But fortunately we can use the `gen-class' function to accomplish that:

```clojure
(gen-class
 :name camunda_clojure.delegates.MyDelegate
 :prefix delegate-
 :implements [org.camunda.bpm.engine.delegate.JavaDelegate])

(defn delegate-execute [this execution]
  (let [variable (.getVariable execution "var1")]
    (println "This is the delegate for var1=" variable)))
```

To eventually have the class generated, we need to call the `compile` function for the namespace which holds the delegate 
definition:

```clojure
(compile (symbol "camunda-clojure.delegates"))
```
