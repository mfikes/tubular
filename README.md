# Tubular

A Clojure Socket REPL client.

[![Clojars Project](https://img.shields.io/clojars/v/tubular.svg)](https://clojars.org/tubular)

## Usage

### Server

First, have a remote Socket REPL server listening for connections. For example, using Clojure you could start a Socket REPL via:

```
clj -J-Dclojure.server.repl="{:port 5555 :accept clojure.core.server/repl}"
```

To set up up a ClojureScript socket REPL, if you have Clojure 1.10 as a dep, you can do

```
clj -J-Dclojure.server.repl="{:port 5555 :accept cljs.server.node/repl}"
```

To start a socket REPL with Lumo, you can

```
lumo -n 5555
```

and to start one with Planck, you can

```
plk -n 5555
```


### Client Setup

First set up Tubular as a dependency. For example with `deps.edn`:

```
{:deps {tubular {:mvn/version "1.3.0"}}}
```

Then run `tubular.core` as `-main`:

```
clj -m tubular.core -p 5555
```

#### Cursive

First ensure Tubular is set up as a dependency. Then when creating a Cursive REPL, don't pick either of the nREPL options. Instead choose: “Use clojure.main in normal JVM process”. In the Parameters field, specify 

```
-m tubular.core 5555
```

#### Plain Clojure REPL

You can start the Tubular client in a REPL by using `tubular.core/connect`:

```clojure
(require 'tubular.core)
(tubular.core/connect 5555)
```

### Client Use

Using any of the above methods, you will be connected. Now, any input you type into the REPL will be sent over a TCP socket to the remote REPL, and any evaluation results or printed output will be sent back via the TCP socket and printed locally.

When you are done and want to disconnect and return to your local plain Clojure REPL, type

```
:repl/quit
```

## License

Copyright © 2016–2018 Mike Fikes and Contributors

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
