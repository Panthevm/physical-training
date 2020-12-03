(ns app.core
  (:require [reagent.dom    :as dom]
            [re-frame.core  :as rf]
            [re-frame.subs  :as subs]

            [frame.routing :as routing]
            [frame.page    :as page]

            [app.routes :as routes]

            [app.page.book.core]
            [app.page.content.core]

            [app.component.navbar.core :as navbar]))

(rf/reg-event-fx
 ::initialize
 (fn [{db :db} _]
   {:db             (assoc db :routes routes/data)
    ::routing/start {}}))

(defn current-page
  []
  (let [route (rf/subscribe [::routing/current-route])]
    (fn []
      (let [page (->> @route :id (get @page/pages))]
        [:<>
         [navbar/component]
         [:div.max-w-7xl.mx-auto.py-6.sm:px-6.lg:px-48
          (when page [page])]]))))

(defn ^:export mount
  []
  (subs/clear-subscription-cache!)
  (rf/dispatch [::initialize])
  (dom/render [current-page] (js/document.getElementById "app")))
