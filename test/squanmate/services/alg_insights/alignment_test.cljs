(ns squanmate.services.alg-insights.alignment-test
  (:require [squanmate.services.alg-insights.alignment :as sut]
            [cljs.test :as t :refer [is]]
            [squanmate.alg.execution :as execution]
            [squanmate.alg.puzzle :as p]
            [squanmate.utils.either-utils :as eu]
            [cats.core :as m]
            [squanmate.alg.parser :as parser])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(defn- execute [alg-string]
  (->> alg-string
       (execution/transformations p/square-square)
       eu/list-of-eithers->either-list
       m/extract))

;; test dsl
(def leaving-aligned (sut/->LeavingCubeshape true))
(def leaving-misaligned (sut/->LeavingCubeshape false))

(def entered-aligned (sut/->EnteredCubeshape true))
(def entered-misaligned (sut/->EnteredCubeshape false))

(deftest alignments-when-entering-or-leaving-cubeshape-test []
  (is (= {0 leaving-aligned}
         (sut/alignments-when-entering-or-leaving-cubeshape (execute "/")))
      "only leave cubeshape")

  (is (= {0 leaving-aligned
          2 entered-aligned}
         (sut/alignments-when-entering-or-leaving-cubeshape (execute "/0/")))
      "leave and enter back right away")

  (is (= {1 leaving-misaligned}
         (sut/alignments-when-entering-or-leaving-cubeshape (execute "1,-1/")))
      "leave misaligned")

  (is (= {1 leaving-misaligned
          3 entered-misaligned}
         (sut/alignments-when-entering-or-leaving-cubeshape (execute "1,-1/0/")))
      "leave misaligned and enter back misaligned")

  (is (= {1 leaving-misaligned
          3 entered-aligned}
         (sut/alignments-when-entering-or-leaving-cubeshape (execute "1,-1/6,6/")))
      "leave misaligned and enter back aligned")

  (is (= {0 leaving-aligned
          2 entered-misaligned}
         (sut/alignments-when-entering-or-leaving-cubeshape (execute "/6,6/")))
      "leave aligned and enter back misaligned")

  (is (= {}
         (sut/alignments-when-entering-or-leaving-cubeshape (execute "1/3/")))
      "don't leave cubeshape at all")

  (is (= {5 leaving-aligned}
         (sut/alignments-when-entering-or-leaving-cubeshape (execute "1/3/-1/")))
      "some rotations in cubeshape and then leave"))
