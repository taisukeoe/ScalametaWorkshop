# Scalameta Workshop

This workshop aims to experience improving your codebase using Scalameta and Scalafix.

There is also [a guide written in Japanese](https://github.com/taisukeoe/ScalametaWorkshop/wiki/Scalameta%E3%83%AF%E3%83%BC%E3%82%AF%E3%82%B7%E3%83%A7%E3%83%83%E3%83%97). 

# How it's going on:

Write your code into `src/main/scala` directory in every step.

Running test codes by `testOnly stepN.*` .

## STEP0: Playing around AST.

Go through [Scalameta Tree Guide Docs](https://scalameta.org/docs/trees/guide.html) and play around with `sbt console`.

## STEP1: Check literals passed via method arguments.

Implement a `gatherLiterals` method to return a tuple of (s"${name_of_method}_${index_of_args}, literal) .

```sbtshell
sbt:workshop > testOnly step1.*
```

### HINT:

Check how method invocation AST is represented via `sbt conosleQuick`.

```sbtshell
import scala.meta._
"""hello("taro")""".parse[Stat].get.structure
```


# STEP2: Check literals passed via constructor arguments.

Add a case clause to `gatherLiterals` method to return a tuple of (s"${name_of_clazz}_${index_of_args}, literal) .

```sbtshell
sbt:workshop > testOnly step2.*
```

### HINT:

Check how constructor invocation AST is represented via `sbt conosleQuick`.

```sbtshell
import scala.meta._
"""new MyClass("taro")""".parse[Stat].get.structure
```

# STEP3: Avoid some false-positives

Current implementation may mark `""`, `()` or `null`, which are used as "empty values" rather than hardcoding.  So let's ignore them.

You can add other false-positive case clauses to be excluded. 

```sbtshell
sbt:workshop > testOnly step3.*
```

### HINT:

Check `Lit` object Scalameta source [here](https://github.com/scalameta/scalameta/blob/b09e2aeda87ec0a8434567b88638ca08e054aef4/scalameta/trees/shared/src/main/scala/scala/meta/Trees.scala#L48). 

# STEP4: Generate Constants object source.

```sbtshell
sbt:workshop > testOnly step4.*
```

### HINT:
Check how object definition AST is represented via `sbt conosleQuick`.

```sbtshell
import scala.meta._
"""package com.example { object Constants {val a = 1} }""".parse[Stat].get.structure
```

# STEP5: migrate STEP 1-4 into Scalafix rule

```sbtshell
sbt:workshop > testOnly step5.* 
```

Since GatherHardcoding rule will generate a source file under output/target, you can confirm if generated source is OK by:

```sbtshell
sbt:wokrshop > output/compile
```

# STEP6: Run your own Scalafix rule against your Scala project!

```sbtshell
sbt:workshop > publishLocal
```

And then, add Scalafix into your Scala project as follows.

```sbt
// project/plugins.sbt

addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.19")
```

And then, running your Scalafix rule by the following command. 

```sbtshell
sbt:YourProject > scalafix dependency:GatherHardcoding@com.taisukeoe:gather-hardcoding:0.1-SNAPSHOT --check
```

See the result and generated Constants source at the base directory, and think through further improvements.

# EXTRA STEPS: Improve your rule

There are many opportunities to improve. For example:

- Consider capturing literals in infix notation.
- Consider choosing better names for Constant fields. (Current naming is deadly simple and collides frequently :( )
- Consider using [GatherHardcodingConfig](src/main/scala/fix/GatherHardcodingConfig.scala) via `.scalafix.conf` to apply your Scala project.
- Consider changing your rule to SemanticRule, and using richer tree pattern matching by [SymbolMatcher](https://scalacenter.github.io/scalafix/docs/developers/symbol-matcher.html))
