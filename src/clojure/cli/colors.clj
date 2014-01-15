(ns clojure.cli.colors)

(defn- escape-code
  [i]
  (str "\033[" i "m"))

(def colors
  "foreground color map"

  (zipmap [:grey :red :green :yellow
           :blue :magenta :cyan :white]
          (map escape-code
               (range 30 38))))

(def highlights
  "background color map"

  (zipmap [:on-grey :on-red :on-green :on-yellow
           :on-blue :on-magenta :on-cyan :on-white]
          (map escape-code
            (range 40 48))))

(def attributes
  "attributes color map"

  (into {}
        (remove #(= (key %) nil)
                (zipmap [:bold, :dark, nil, :underline,
                         :blink, nil, :reverse-color, :concealed]
                        (map escape-code (range 1 9))))))

(def reset (escape-code 0))

(defmacro define-color-function
  "define a function `fname' which wraps its arguments with
        corresponding `color' codes"
  [fname color]

  (let [fname (symbol (name fname))
        args (symbol 'args)]
    `(defn ~fname [& ~args]
       (str (reduce str (map #(str ~color %) ~args)) ~reset))))

(defn define-color-functions-from-map
  "define functions from color maps."
  [colormap]

  (eval `(do ~@(map (fn [[color escape-code]]
                `(println ~color ~escape-code)
                `(define-color-function ~color ~escape-code))
                    colormap))))

(define-color-functions-from-map colors)
(define-color-functions-from-map highlights)
(define-color-functions-from-map attributes)
