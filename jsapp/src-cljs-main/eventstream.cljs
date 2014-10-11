(ns kinesis-om-template.events)

(def eventSource (js/EventSource. "/log"))

(defn attachToEventStream [callback]
  (set! (.-onmessage eventSource) callback))