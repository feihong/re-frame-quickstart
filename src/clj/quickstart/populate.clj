(ns quickstart.populate
  (:require [clojure.java.io :as io]
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

(defn get-items []
  (->> (get-stream)
       (java.util.zip.GZIPInputStream.)
       io/reader
       line-seq
       (map #(re-matches #"^\p{L}+ (\p{L}+) \[(.*)\] /(.*)/$" %))
       (map rest)))

; (jdbc/insert-multi! db/*db* :words [:id :word :pinyin :gloss]
;   [
;    [1 "hey" "hey" "hey"]
;    [2 "boo" "boo" "boo"]])
