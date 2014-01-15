(ns clojure.cli.colors-test
  (:require [clojure.test :refer :all]
            [clojure.cli.colors :refer :all]))

(defn get-fn
  "get function from symbol in clojure.cli.colors package"
  [fname]
  (ns-resolve (the-ns 'clojure.cli.colors)
              (-> fname name symbol)))

(defn test-colors-from-map
  "test print colors from a color map"
  [colormap & more]
  (eval
   `(do ~@(map (fn [[color _]]
                 `(println ((get-fn ~color)
                            (name ~color) (str ~@more))))
               colormap))))

(deftest color-test
  (testing "Testing colors."
    (test-colors-from-map colors " foreground.")
    (test-colors-from-map highlights " background.")
    (test-colors-from-map attributes " attributes.")))
