":"; if [ ! -e "$HOME/.rundj" ]; then curl 'https://raw.githubusercontent.com/bmillare/dj3/master/bin/run' > $HOME/.rundj ; fi; exec sh ~/.rundj $0 $@
(prn "clojure runs baybe" *command-line-args*)
(println (System/getProperty "user.dir"))
