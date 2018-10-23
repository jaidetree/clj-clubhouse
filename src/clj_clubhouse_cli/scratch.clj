(-main)

(get! "workflows" {})

(get-projects)

(defn pluck
  [keys coll]
  (map #(select-keys % keys) coll))

(->> (get-projects!)
     (pluck '(:name :id)))
;; => ({:name "#$ Incoming Ideas $#", :id 38}
;;     {:name "#< Internal Reports >#", :id 121}
;;     {:name "#X Reported Bugs X#", :id 37}
;;     {:name "Analytics / Tracking", :id 691}
;;     {:name "Build Farm", :id 27063}
;;     {:name "Client Requests", :id 26293}
;;     {:name "Event Planner Tools", :id 361}
;;     {:name "ExpressBook", :id 412}
;;     {:name "External Data Feeds", :id 5}
;;     {:name "Hubspot Tracking", :id 23544}
;;     {:name "Internal Requests", :id 754}
;;     {:name "Marketplace", :id 116}
;;     {:name "Marketplace", :id 23560}
;;     {:name "Messaging", :id 2250}
;;     {:name "RAQ Improvements", :id 117}
;;     {:name "Selenium", :id 23557}
;;     {:name "Technical Debt", :id 91}
;;     {:name "Testlink", :id 23558}
;;     {:name "Venue Listing Page", :id 1894}
;;     {:name "Venue Manager Tools", :id 23561}
;;     {:name "Venue Manager Tools", :id 497}
;;     {:name "Venue Registration", :id 118})
(get-projects!)

(->> (get-workflows!)
     first
     :states
     (pluck '(:name :id)))
;; => ({:name "Idea Box", :id 500000014}
;;     {:name "Declined/Resolved", :id 500011115}
;;     {:name "Comment Needed", :id 500011122}
;;     {:name "Validated Backlog", :id 500000011}
;;     {:name "Prioritized Backlog", :id 500011116}
;;     {:name "Design Backlog", :id 500012473}
;;     {:name "Design Needed", :id 500008444}
;;     {:name "Proposal Editor", :id 500015123}
;;     {:name "Next Sprint", :id 500008986}
;;     {:name "This Sprint ", :id 500008928}
;;     {:name "In Development", :id 500000015}
;;     {:name "Failed QA (Proposal Editor)", :id 500016728}
;;     {:name "Design Ready for Handoff", :id 500008445}
;;     {:name "Code Complete", :id 500000017}
;;     {:name "Waiting for Fixes", :id 500010332}
;;     {:name "Ready For QA", :id 500000016}
;;     {:name "Passed QA", :id 500003868}
;;     {:name "Ready for Release", :id 500000018}
;;     {:name "Done / In Production", :id 500000013})

(defn get-stories
  [project-id]
  (get! (format "projects/%s/stories" project-id) {}))

(->> (get-stories 116)
     (filter #(= (:workflow_state_id %) 500008928))
     (pluck '(:name :id)))

(defn in-workflow?
  [id story]
  (= (:workflow_state_id story) id))

(defn read-config!
  []
  (read-string (slurp "projects.secret.edn")))

(read-config!)

(defn tap
  ([f]
   (fn [x] (tap f x)))
  ([f x]
   (f x) x))

(defn get-active-epics!
  []
  (->> (get! "epics" {})
       (filter (every-pred #(= (:archived %) false)
                           #(= (:completed %) false)
                           #(= (:started %) true)))))

(defn get-stories-for-project!
  [view config]
  (clojure.pprint/pprint view)
  (let [epics (->> (get-active-epics!) (map :id) (set))]
    (clojure.pprint/pprint epics)
    (->> view
         (:projects)
         (map #(get-in config [:projects %]))
         (tap println)
         (mapcat get-stories!)
         (filter (every-pred #(in-workflow? (:state-id view) %)
                             #(= (:archived %) false)
                             #(contains? epics (:epic_id %))))
         (pluck [:id :name]))))

(defn test-stories
  []
  (let [config (read-config!)]
    (->> (:views config)
         (drop 1)
         (map #(get-stories-for-project! % config)))))


(test-stories)
