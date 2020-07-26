package step2

import com.taisukeoe.GatherHardcodedValues

class GatherLiteralSuite extends munit.FunSuite {
  import scala.meta._
  lazy val source = """
object Hello {
  def hello(name: String) = println(name)

  def capitalize(name: String) = name.capitalize

  hello(capitalize("odersky"))
  hello(new Name("martin").value)
}

class Name(val value: String) extends AnyVal

object Values {
  // This should be allowed.
  val DEFAULT = "anonymous"
}""".parse[Source].get

  test("gather literals in method and constructor arguments") {
    val ghv = new GatherHardcodedValues()
    val list = ghv.gatherLiterals(source)

    assertEquals(list.size, 2)

    val (name, tree) :: (name2, tree2) :: Nil = list

    assertEquals(name.syntax, "CAPITALIZE_0")
    assertEquals(tree.syntax, "\"odersky\"")

    assertEquals(name2.syntax, "NAME_0")
    assertEquals(tree2.syntax, "\"martin\"")
  }
}
