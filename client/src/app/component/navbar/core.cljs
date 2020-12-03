(ns app.component.navbar.core
  (:require
   [re-frame.core              :as rf]
   [app.component.navbar.model :as model]))

(defn component
  []
  (let [data @(rf/subscribe [::model/data])]
    [:nav.bg-gray-800
     [:div.max-w-7xl.mx-auto.px-4.sm:px-6.lg:px-8
      [:div.flex.items-center.justify-between.h-16
       [:div.flex.items-center
        [:div.flex-shrink-0
         [:img.h-8.w-8 (-> data :logo)]]
        [:div.ml-10.flex.items-baseline.space-x-4
         (map-indexed
          (fn [index link] ^{:key index}
            [:a.px-3.py-2.rounded-md.text-sm.font-medium.text-white link
             (:title link)])
          (-> data :links))]]]]]))
