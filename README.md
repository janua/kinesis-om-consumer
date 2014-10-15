## Kinesis Om Consumer Template

This is a template that combines Scala and Clojurescript with OM to consume an AWS Kinesis stream.
It assumes that the messages pushed onto Kinesis are in `JSON` format.

In Scala it is using Play 2.3.x and the Amazon Kinesis Client Library, with very basic checkpointing.

In Clojurescript, the updates are put inside a ReactJS class/component using OM which is fed live via EventSource/Server-sent events.

### Running

To run, please install both `sbt` and `leiningen`.

#### ClojureScript / OM / React
First, `cd` into the `jsapp` directory, and run `lein cljs auto` or `lein cljs once` to build the app relative to your current working directory. This will compile the clojurescript into the `play` `public` folder.

#### Play / Kinesis
`cd` to the root of the project and run `sbt`, then type `run` when everything is resolved.

Now hit `http://localhost:9000/`

##### sbt-clojure
There is a plugin for `sbt` called `sbt-clojure` (See geal/sbt-clojure). I have decided not to use this and opted for using leiningen directly.
