# Feihong's re-frame Quickstart

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run

To start hot reloading server (figwheel), run:

    lein figwheel

To auto-compile scss files, run:

    lein auto sassc one

## Deployment

Build a jar file:

    lein uberjar

Run the jar file:

    java -Dconf=dev-config.edn -jar target/uberjar/quickstart.jar

## References

- [Running figwheel in a Cursive Clojure REPL](https://github.com/bhauman/lein-figwheel/wiki/Running-figwheel-in-a-Cursive-Clojure-REPL)

## Notes

Created by running:

    lein new luminus quickstart +re-frame

Generated using Luminus version "2.9.12.25"
