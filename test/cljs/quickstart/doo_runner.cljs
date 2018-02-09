(ns quickstart.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [quickstart.core-test]))

(doo-tests 'quickstart.core-test)

