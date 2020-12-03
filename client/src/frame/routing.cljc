(ns frame.routing
  (:require [re-frame.core :as rf]))

(defn *location-hash
  []
  #?(:cljs (.. js/window -location -hash)
     :clj  "#/"))

(defn- parse-search
  [search]
  (reduce
   (fn [acc [_ k _ v]]
     (assoc acc (keyword k) v))
   {} (re-seq #"([^=&]+)(=([^&]*))?" search)))

(defn- params-node
  [node]
  (->> node
       (filter (comp symbol? first))
       first))

(defn match
  ([routes location-hash]
   (let [[path search] (->> location-hash (re-seq #"[^?]+"))
         paths         (some->> path     (re-seq #"[^/]+") next)
         format-search (some->> search   parse-search)]
     (match routes paths {:search format-search :location-hash location-hash})))

  ([current-node [current-path & other-paths] params]
   (if-let [next-node (get current-node current-path)]
     (recur next-node other-paths params)
     (if-not current-path
       {:id (:- current-node) :params params}
       (let [[params-key node] (params-node current-node)]
         (recur node other-paths (assoc params (keyword params-key) current-path)))))))

(rf/reg-event-fx
 ::location-changed
 (fn [{db :db} [_ {:keys [params]}]]
   (let [{nid :id nparams :params :as new} (match (:routes db) (:location params))
         {oid :id oparams :params :as old} (:routing db)]
     {:db (assoc db :routing new)
      :dispatch-n (cond
                    (not oid)
                    [[nid {:params (assoc new :phase :init)}]]

                    (not= oid nid)
                    [[oid {:params (assoc old :phase :deinit)}]
                     [nid {:params (assoc new :phase :init)}]]

                    (not= oparams nparams)
                    [[nid {:params (assoc new :phase :params)}]])})))

(rf/reg-fx
 ::start
 (letfn [(event [params]
           (rf/dispatch [::location-changed {:params params}]))]
   (fn [{:keys [params]}]
     #?(:cljs (aset js/window "onhashchange" #(event (assoc params :location (*location-hash)))))
     (event (assoc params :location (*location-hash))))))

(rf/reg-event-db
 ::init
 (fn [db [_ {:keys [params]}]]
   (assoc db :routes (:routes params))))

(rf/reg-sub
 ::current-route
 (fn [db _]
   (:routing db)))

(rf/reg-event-fx
 ::redirect
 (fn [_ [_ {:keys [params]}]]
   {:dispatch [::location-changed {:params params}]}))
