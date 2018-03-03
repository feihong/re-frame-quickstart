(ns quickstart.fonts
  (:require [clojure.string :as string]
            [clojure.java.shell :as shell]))


(defn chinese-fonts []
  (->> (shell/sh "fc-list" ":lang=zh")
       :out
       string/split-lines
       (map #(re-matches #".*\: (.*?),.*" %))
       (remove nil?)
       (map second)
       (remove #(string/starts-with? % "."))
       set
       sort))
