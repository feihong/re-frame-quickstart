-- :name get-word :? :1
-- :doc Get word with given id
SELECT * FROM words WHERE id = :id


-- :name get-words :? :*
-- :doc Get words
SELECT * FROM words
