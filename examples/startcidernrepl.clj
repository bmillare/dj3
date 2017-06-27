":"; if [ ! -e "$HOME/.rundj" ]; then curl 'https://raw.githubusercontent.com/bmillare/dj3/master/bin/run' > $HOME/.rundj ; fi; exec sh ~/.rundj $0 $@
(load "dj/init/cidernrepl")
