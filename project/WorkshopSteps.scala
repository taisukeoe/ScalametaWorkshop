import sbt.Keys._
import sbt._

object WorkshopSteps {

  def commons(origin: Project): Seq[Setting[_]] =
    Seq(
      libraryDependencies ++= Seq(
        "org.scalameta" %% "scalameta" % "4.3.20",
        "org.scalameta" %% "munit" % "0.7.9" % Test
      ),
      unmanagedSourceDirectories.in(Compile) += scalaSource.in(origin).in(Compile).value,
      testFrameworks += new TestFramework("munit.Framework")
    )
}
