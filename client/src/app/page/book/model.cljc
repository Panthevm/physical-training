(ns app.page.book.model
  (:require
   [re-frame.core :as rf]
   [frame.routing :as routing]))

(def page ::page)

(def parse-integer
  #?(:clj  (fn [^String s] (Integer/parseInt s))
     :cljs (fn [^String s] (js/parseInt s))))


(rf/reg-event-fx
 page
 (fn [{db :db} [_ {:keys [params]}]]
   {}))

(rf/reg-sub
 ::data
 :<- [::routing/current-route]
 (fn [current-route _]
   (let [page-number (if-let [page (-> current-route :params :page)]
                       (parse-integer page)
                       0)]
     {:page page-number

      :previous-page
      (when-not (= 0 page-number)
        (str "#/book/" (dec page-number)))

      :next-page
      (when-not (= 100 page-number)
        (str "#/book/" (inc page-number)))})))
