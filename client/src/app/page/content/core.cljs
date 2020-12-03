(ns app.page.content.core
  (:require
   [re-frame.core          :as rf]
   [app.page.content.model :as model]
   [frame.page             :as page]))

(defn component
  []
  (let [data* (rf/subscribe [::model/data])]
    (fn []
      (let [data @data*]
        [:div
         [:p.text-3xl.text-center.font-bold.pb-5 "Оглавление"]
         [:ul.text-xl
          (map-indexed
           (fn [index chapter] ^{:key index}
             [:li.pb-2
              [:a.cursor-pointer.hover:text-blue-700 chapter (:title chapter)]
              [:ul.list-disc.text-xl.pl-10
               (map-indexed
                (fn [index part]
                  [:li [:a.hover:text-red-700.cursor-pointer.hover:text-blue-700 part (:title part)]])
                (:parts chapter))]])


           (-> data :content))]]))))

(page/registration model/index component)
