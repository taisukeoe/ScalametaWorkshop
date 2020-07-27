lazy val V = _root_.scalafix.sbt.BuildInfo
inThisBuild(
  List(
    name := "ScalametaWorkshop",
    version := "0.1-SNAPSHOT",
    organization := "com.taisukeoe",
    scalaVersion := V.scala212,
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    semanticdbOptions += "-P:semanticdb:synthetics:on"
  )
)

lazy val workshop = project
  .in(file("."))
  .settings(
    moduleName := "gather-hardcoding",
    libraryDependencies ++= Seq(
      "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion,
      "org.scalameta" %% "munit" % "0.7.9" % Test
    ),
    testFrameworks += new TestFramework("munit.Framework")
  )
  .settings(
    //scalafix rule test settings in step5
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full,
    compile.in(Compile) :=
      compile.in(Compile).dependsOn(compile.in(input, Compile)).value,
    scalafixTestkitOutputSourceDirectories :=
      sourceDirectories.in(output, Compile).value,
    scalafixTestkitInputSourceDirectories :=
      sourceDirectories.in(input, Compile).value,
    scalafixTestkitInputClasspath :=
      fullClasspath.in(input, Compile).value
  )
  .enablePlugins(ScalafixTestkitPlugin)

lazy val input = project.settings(
  skip in publish := true
)

lazy val output = project.settings(
  skip in publish := true
)
