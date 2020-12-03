(ns frame.page)

(defonce pages (atom {}))

(defn registration [key page]
  (swap! pages assoc key page))
