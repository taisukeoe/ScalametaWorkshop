package step1

import com.taisukeoe.GatherHardcodedValues

class GatherLiteralSuite extends munit.FunSuite {
  import scala.meta._
  lazy val source = """
object Hello {
  def hello(name: String) = println(name)

  def capitalize(name: String) = name.capitalize

  hello(capitalize("odersky"))
}

object Values {
  // This should be allowed.
  val DEFAULT = "anonymous"
}""".parse[Source].get

  test("gather literals in method arguments") {
    val ghv = new GatherHardcodedValues()
    val list = ghv.gatherLiterals(source)

    assertEquals(list.size, 1)

    val (name, tree) = list.head
    assertEquals(name.syntax, "CAPITALIZE_0")
    assertEquals(tree.syntax, "\"odersky\"")
  }
}
