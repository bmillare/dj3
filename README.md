# dj3
enable distribution of Clojure apps via a single executable file

# motivation

dj3 has two main goals. First, imagine that you'd like to run a Clojure application on a user's \*nix computer as a script that the user can move around freely and should still run correctly. Running a Clojure application as a single executable file is currently not possible but creating the illusion of one is possible. By putting a special comment at the top of a `clj` file that can also be interpreted by bash, you can make a `clj` file executable. dj3 is organized to support this illusion.

Second, to enable the use of complex applications that depends on multiple libraries of Clojure code, a method to organize and load Clojure code is needed. Current approaches are inherently coarse grained (jar dependencies are specified per project) and pull more dependencies than needed for a particular namespace. (Note, that the core Clojure developers claim that clojure.spec is a work that can help solve this problem.) dj3 supports embedding jar dependency information as metadata within the `ns` form. The full recursively derived dependency tree can be determined and resolved at runtime prior to `require`ing the namespace.

# design

dj3 takes the main lessons learned from the first two iterations and simplifies it. dj3 plans to install itself within `$HOME/.dj` upon execution of any `clj` script that includes the special header. This header will self-install a dj3 distribution if it is not already installed. Once dj3 is installed, it will start Clojure by calling Java with a base set of jars and Clojure source code. Finally, it will run the originally executed `clj` file as Clojure code and supply any command line provided arguments.

This approach generalizes to many use cases and enables initilization of complex standalone Clojure projects. One common technique would be to include an initialization script within a typical project directory that adds its source directories to the classpath, execute checks at run time, and start any project initialization code.
