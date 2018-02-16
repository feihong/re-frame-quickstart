(ns quickstart.populate
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]
            [clj-http.client :as client]))


(def dict-url "https://www.mdbg.net/chinese/export/cedict/cedict_1_0_ts_utf-8_mdbg.txt.gz")

(defn get-stream []
  "Fetch the stream if the dict file already exists, otherwise download it"
  (let [file (io/file "cedict.txt.gz")]
    (if (.exists file)
      (io/input-stream file)
      (let [stream (-> (client/get dict-url {:as :stream}) :body)]
        (io/copy stream file)
        stream))))

(defn get-lines []
  (->> (get-stream)
       (java.util.zip.GZIPInputStream.)
       io/reader
       line-seq
       (remove #(string/starts-with? % "#"))))

(defn get-items []
  (->> (get-lines)
       (map #(re-matches #"^.+ (.+) \[(.+)\] /(.+)/$" %))
       (map rest)   ; throw away first group
       (map-indexed (fn [idx itm] (conj itm idx))))) ; insert index at front

(defn insert-words! [db]
  (jdbc/delete! db :words)
  (->> (get-items)
       (partition 1000 1000 nil)  ; insert in batches of 1000
       (map #(jdbc/insert-multi! db [:id :word :pinyin :gloss] %))))

#_(doseq [line (p/get-lines)]
    (if-not (re-matches #"^.+ (.+) \[(.+)\] /(.+)/$" line)
      (prn line)))

#_(doseq [item (p/get-items)]
    (prn item))
