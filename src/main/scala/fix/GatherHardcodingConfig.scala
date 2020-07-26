package fix

import java.io.File
import java.nio.file.{Files, Path, Paths}

import metaconfig._
import metaconfig.generic.Surface

import scala.meta._

case class GatherHardcodingConfig(
    pathString: Option[String] = None,
    packageName: Option[String] = None,
    objectName: Option[String] = None
) {
  def path: Option[Path] = pathString.map(p => new File(p).toPath)
  def pkg: Option[Term.Ref] = packageName.flatMap { _.parse[Term].toOption.collect { case tr: Term.Ref => tr } }
  def obj: Option[Term.Name] = objectName.flatMap { _.parse[Term].toOption.collect { case tn: Term.Name => tn } }
}

object GatherHardcodingConfig {
  lazy val default = GatherHardcodingConfig()
  implicit val reader: ConfDecoder[GatherHardcodingConfig] =
    generic.deriveDecoder[GatherHardcodingConfig](default)
  implicit val surface: Surface[GatherHardcodingConfig] =
    generic.deriveSurface[GatherHardcodingConfig]
}
