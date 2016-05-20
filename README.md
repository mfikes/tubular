# Tubular

A Clojure Socket REPL client.

[![Clojars Project](http://clojars.org/tubular/latest-version.svg)](http://clojars.org/tubular)

## Usage

First, have a remote Socket REPL server listening for connections.

For example, you could start one with Clojure via:

```
java -Dclojure.server.repl="{:port 5555 :accept clojure.core.server/repl}" -cp clojure-1.8.0.jar clojure.main
```

Then to create a client, start a plain Clojure REPL with Tubular on the classpath.

> Note: Tubular can be used within Cursive. When setting up the Cursive REPL, don't pick either of the nREPL options. Instead choose: “Use clojure.main in normal JVM process”

Then

```clojure
(require 'tubular.core)
(tubular.core/connect 5555)
```

Alternatively, you can supply a hostname when connecting

```clojure
(tubular.core/connect "localhost" 5555)
```

You will then be connected. Now, any input you type into the REPL will be sent over a TCP socket to the remote REPL, and any evaluation results or printed output will be sent back via the TCP socket and printed locally.

When you are done and want to disconnect and return to your local plain Clojure REPL, type

```
:repl/quit
```

## License

Copyright © 2016 Mike Fikes

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
