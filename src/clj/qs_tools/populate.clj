(ns qs-tools.populate
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
        (println "Downloaded file from" dict-url)
        (io/copy stream file)
        stream))))

(defn get-lines []
  (->> (get-stream)
       (java.util.zip.GZIPInputStream.)
       io/reader
       line-seq
       (remove #(string/starts-with? % "#"))))

(defn parse-line [line]
  ; We have to use . here instead of \p{L} because certain Chinese characters do
  ; not match \p{L}
  (let [result (re-matches #"^.+ (.+) \[(.+)\] /(.+)/$" line)]
    (assert (not= result nil) (str "Line " line " does not have the expected format"))
    (rest result)))     ; throw away first group

(defn get-items []
  (->> (get-lines)
       (map parse-line)
       (map-indexed (fn [idx itm] (conj itm idx))))) ; insert index at front

(defn word-count [db]
  (-> (jdbc/query db ["select count(*) from words"]) first :count))

(defn insert-word-batch! [db words]
  (println "First item in batch:" (-> words first pr-str))
  (-> (jdbc/insert-multi! db :words [:id :word :pinyin :gloss] words)
      (#(println "Inserted " (count %) " words"))))

(defn insert-words! [db]
  (println "Found" (word-count db) "rows in words table")
  (jdbc/execute! db ["truncate table words"])
  (println "Cleared words table")

  ; Insert in batches of 1000
  (doseq [batch (->> (get-items) (partition 1000 1000 nil))]
    (insert-word-batch! db batch))

  (println "\nNow there are" (word-count db) "rows in words table"))
