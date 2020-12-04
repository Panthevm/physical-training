(ns app.component.navbar.model
  (:require
   [re-frame.core  :as rf]
   [frame.routing  :as routing]
   [clojure.string :as string]))

(defn format-links
  [links current-route]
  (let [location-hash (-> current-route :params :location-hash)]
    (if (string/blank? location-hash)
      (assoc-in links [1 :class] "bg-gray-900")
      (->> links
           (map (fn [link]
                  (cond-> link
                    (string/starts-with? location-hash (:href link))
                    (assoc :class "bg-gray-900"))))))))

(def links
  [{:title "Оглавление" :href "#/content"}
   {:title "Учебник"    :href "#/book"}
   {:title "Тест"       :href "https://panthevm.github.io/physical-training-test/"}])

(rf/reg-sub
 ::data
 :<- [::routing/current-route]
 (fn [current-route _]
   {:links (format-links links current-route)
    :logo  {:src "https://tailwindui.com/img/logos/workflow-mark-indigo-500.svg"}}))


