":"; mkdir -p ~/.dj; if [ ! -e "$HOME/.dj/run" ]; cd $HOME/.dj; curl -O 'https://raw.githubusercontent.com/bmillare/dj3/master/bin/run'; cd -; fi; exec sh ~/.dj/run $0 $@
(prn "clojure runs baybe" *command-line-args*)
(println (System/getProperty "user.dir"))
