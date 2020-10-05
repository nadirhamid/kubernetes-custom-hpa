(ns custom-hpa.core
  (:require [custom-hpa.helpers.env :refer [int-env]]
            [custom-hpa.web.server :as web-server]
            [custom-hpa.control-loop :as control-loop]
            [taoensso.timbre :as logger]
            [custom-hpa.metric.core :as metric])
  (:gen-class))

(defn -main
  "Initializes core components and starts the custom HPA"
  [& args]
  (let [deployment (System/getenv "DEPLOYMENT")
        deployment-namespace (System/getenv "NAMESPACE")]
    (logger/info "Custom HPA started")
    (logger/set-level! (keyword (or (System/getenv "LOG_LEVEL") "info")))
    (logger/swap-config! assoc-in [:timestamp-opts :pattern] "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    (web-server/start (or (int-env "PORT") 3000))
    (control-loop/start deployment deployment-namespace (metric/default-provider))))
