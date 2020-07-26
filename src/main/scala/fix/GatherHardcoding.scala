package fix

import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files

import metaconfig.Configured
import scalafix.v1._

import scala.collection.mutable.ArrayBuffer
import scala.meta._

import com.taisukeoe.GatherHardcodedValues

class GatherHardcoding(config: GatherHardcodingConfig) extends SyntacticRule("GatherHardcoding") {

  def this() = this(GatherHardcodingConfig.default)

  lazy val ghv = new GatherHardcodedValues(config.pkg, config.obj)

  private val queue = new ArrayBuffer[(Term.Name, Lit)]()
  override def withConfiguration(config: Configuration): Configured[Rule] = {
    config.conf.getOrElse("GatherHardcoding")(GatherHardcodingConfig.default).map(new GatherHardcoding(_))
  }

  override def fix(implicit doc: SyntacticDocument): Patch =
    ghv
      .gatherLiterals(doc.tree)
      .map {
        case tup @ (name, lit) =>
          queue += tup
          val importPatch = config.pkg.fold(Patch.empty)(pkg =>
            Patch.addGlobalImport(Importer(pkg, Importee.Name(Name.Indeterminate(ghv.objectName.value)) :: Nil))
          )
          val replacePatch = Patch.replaceTree(lit, s"${ghv.objectName.value}.${name.value}")
          (importPatch + replacePatch).atomic
      }
      .asPatch

  override def afterComplete(): Unit = {
    val path = config.path.getOrElse {
      new File(s"${ghv.objectName.value}.scala").toPath
    }
    Files.createDirectories(path.getParent)
    val source = ghv.createObjectSource(queue.toList)
    Files.write(path, source.syntax.getBytes(Charset.forName("UTF-8")))
  }
}
