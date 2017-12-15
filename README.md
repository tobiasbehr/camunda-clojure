# Camunda Clojure

As I am currently working with Camunda BPM in a Java environment and I am also fascinated by the simplicity of 
Clojure I wanted to find out how it might feel to develop process applications with Clojure. I did some research
but didn't find anything about this so I started this prototype.

## What works
As of now I only implemented some basic wrappers around commonly used Camunda calls which were necessary to start a process,
query tasks and complete them. When retrieving data from the Camunda engine the results will be mapped to Clojure data
structures (immutable maps) so that further processing in the Clojure code is convenient.

## Structure
Currently the `camunda-clojure.core` and `camunda-clojure.delegates` namespaces mime the process application while the other namespaces provide the Camunda
wrapper which can be used by the process application. If this prototype turns out to be interesting for anybody, this could
be extracted as a library.

## How to implement delegates
One tricky part is to wire together the BPMN process models with the code artifacts. This can be done by using the fully
qualified class name of the delegate and set this as the Java Class for the `Service Task` in the Camunda Modeler. However,
Clojure doesn't generate class files out of the box. But fortunately we can use the `gen-class' function to accomplish that
(also refer to the `camunda-clojure.delegates` namespace):

```
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

```(compile (symbol "camunda-clojure.delegates"))```

## How to run it

The best is to play around in the REPL. 

```lein repl```

Then you can play around with the engine to start some processes, query existing tasks, and claim or complete them. From the 
the `core` namespace you can user the following functions: 

```(deploy)```

```(runtime/start-process! engine "Process_2")```

```(runtime/find-process-instances engine)```

```(tasks/find-tasks engine)```