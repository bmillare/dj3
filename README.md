# issues
due to old version pomegranate dependency, currently does not work with jdk9, will need to try updating pomegranate in the standalone jar to see if it fixes the issue

# dj3
A reference template for distributing Clojure apps as a single executable `clj` file. You simply add a special header to the top of any `clj` file and it will suddenly be executable. (After `chmod +x the file.clj`). While this project is personally used extensively, the code is shared mainly as a proof of concept and reference design.

# motivation in detail

dj3 has two main goals: 1, run any `clj` file as a script. 2, improve dependency information granularity

Imagine that you'd like to run a Clojure application on a user's \*nix computer as a script that the user can move around freely and should still run correctly. Running a Clojure application as a single isolated executable file is currently not possible since both a Java runtime and Clojure jar is needed at the start, however, creating the illusion of an isolated executable is possible. By putting a special comment at the top of a `clj` file that can also be interpreted by bash, you can make a `clj` file executable. The idea originated from https://en.wikibooks.org/wiki/Clojure_Programming/Tutorials_and_Tips#Shebang_Scripting_in_Clojure

To enable the use of complex applications that depends on multiple libraries of Clojure code, a method to organize and load Clojure code is needed. Current approaches are inherently coarse grained (jar dependencies are specified per project) and pull more dependencies than needed for a particular namespace. (Note, that the core Clojure developers claim that clojure.spec is a work that can help solve this problem.) dj3 supports embedding jar dependency information as metadata within the `ns` form. The full recursively derived dependency tree can be determined and resolved at runtime prior to `require`ing the namespace. See src/dj/codeloading/namespace.clj

# design

dj3 takes the main lessons learned from the first two dj iterations and presents a streamlined approach. dj3 installs itself within `$HOME/.dj` upon execution of any `clj` script that includes the special header. This header will self-install a dj3 distribution if not already installed. Once installed, dj3 will start Clojure by calling Java with a base set of jars and Clojure source code. Finally, dj3 will run the originally executed `clj` file as Clojure code and supply any provided command line arguments.

This approach generalizes to many use cases and enables initilization of complex standalone Clojure projects. One common technique would be to include an initialization script within a typical project directory that adds its source directories to the classpath, execute checks at run time, and start any project initialization code.

## dependencies

dj3 can work its scripting magic if Java, git (or alternatively unzip), and curl is installed.

# usage

see `examples/helloworld.clj`. Add the special header at the top of a `clj` file that you want to execute, `chmod +x foo.clj` and then `./foo.clj`. The `clj` file should be executed from Clojure.

see `examples/startcidernrepl.clj` on instructions for starting a cider nrepl server just by executing that `clj` file. You'll likely be surprised how little code is involved.

# limitations

Currently this only works on \*nix computers like OS X and Linux, not Windows. I'm not aware of a header that would be simultaneously compatible with both bash and batch scripts. Versions of Windows that have Windows Subsystem Linux should work because they can interpret bash.

The Clojure version is hard coded into the run script. A user/developer can easily change version of the Clojure jars located in `~/.dj/jars`.

Arguments passed to java are hard coded.
