(ns quiz-site.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [quiz-site.core-test]
   [quiz-site.common-test]))

(enable-console-print!)

(doo-tests 'quiz-site.core-test
           'quiz-site.common-test)
