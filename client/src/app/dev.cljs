(ns ^:figwheel-hooks app.dev
  (:require [app.core      :as core]
            [re-frisk.core :as frisk]))

(defn ^:after-load re-render []
  (core/mount))

(defonce start-up
  (do (core/mount)
      (frisk/enable-re-frisk!)
      true))
