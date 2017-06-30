(ns ^{:dj.codeloading/dependencies '[[org.apache.xmlgraphics/batik-dom "1.9"]
                                     [org.apache.xmlgraphics/batik-swing "1.9"]]}
    dj.gui.svg)

(def document-factory
  (org.apache.batik.anim.dom.SAXSVGDocumentFactory. (org.apache.batik.util.XMLResourceDescriptor/getXMLParserClassName)))

(defn hello-world []
  (let [f (javax.swing.JFrame. "A Frame")
        s (org.apache.batik.swing.JSVGCanvas.)
        parser (org.apache.batik.util.XMLResourceDescriptor/getXMLParserClassName)
        df (org.apache.batik.anim.dom.SAXSVGDocumentFactory. parser)
        svg-str "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" height=\"100 \" width=\"100 \">
  <circle cx=\"50 \" cy=\"50 \" r=\"40 \" stroke=\"black \" stroke-width=\"3 \" fill=\"red \" />
  Sorry, your browser does not support inline SVG.
</svg>"
        document (.createSVGDocument df "" (java.io.ByteArrayInputStream. (.getBytes svg-str
                                                                                     java.nio.charset.StandardCharsets/UTF_8)))]
    (.setSVGDocument s document)
    (doto (.getContentPane f)
      (.add s))
    (doto f
      (.pack)
      (.setVisible true))))

(defn view! [args]
  (let [default {:title "Replace me!"
                 :width 640
                 :height 480}
        {:keys [title svg-str width height]} (merge default args)]
    (javax.swing.SwingUtilities/invokeLater
     (fn []
       (let [f (javax.swing.JFrame. title)
             s (org.apache.batik.swing.JSVGCanvas.)
             parser (org.apache.batik.util.XMLResourceDescriptor/getXMLParserClassName)
             df (org.apache.batik.anim.dom.SAXSVGDocumentFactory. parser)
             document (.createSVGDocument df "" (java.io.ByteArrayInputStream. (.getBytes svg-str
                                                                                          java.nio.charset.StandardCharsets/UTF_8)))]
         (.setSVGDocument s document)
         (doto (.getContentPane f)
           (.add s))
         (doto f
           (.setPreferredSize (java.awt.Dimension. width height))
           (.pack)
           (.setVisible true)))))))

(defn new-svg-canvas []
  (org.apache.batik.swing.JSVGCanvas.))

(defn init-frame! [^org.apache.batik.swing.JSVGCanvas s ^String svg-str]
  (javax.swing.SwingUtilities/invokeLater
   (fn []
     (let [f (javax.swing.JFrame. "A Frame")
           document (.createSVGDocument document-factory
                                        ""
                                        (java.io.ByteArrayInputStream. (.getBytes svg-str
                                                                                  java.nio.charset.StandardCharsets/UTF_8)))]
       (doto s
         (.setDocumentState org.apache.batik.swing.svg.JSVGComponent/ALWAYS_DYNAMIC)
         (.setSVGDocument document))
       (doto (.getContentPane f)
         (.add s))
       (doto f
         (.pack)
         (.setVisible true))))))

(defn swap-svg-str! [^org.apache.batik.swing.JSVGCanvas s ^String svg-str]
  (-> s
      (.getUpdateManager)
      (.getUpdateRunnableQueue)
      (.invokeLater
       (fn []
         (.setSVGDocument s
                          (.createSVGDocument document-factory
                                              ""
                                              (java.io.ByteArrayInputStream. (.getBytes svg-str
                                                                                        java.nio.charset.StandardCharsets/UTF_8))))))))
