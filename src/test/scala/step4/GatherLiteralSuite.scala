package step4

import com.taisukeoe.GatherHardcodedValues

class GatherLiteralSuite extends munit.FunSuite {
  import scala.meta._
  lazy val source = """
object Hello {
  def hello(name: String) = println(name)

  def capitalize(name: String) = name.capitalize

  hello(capitalize("odersky"))
  hello("")
  hello(null)
  hello(new Name("martin").value)
}

class Name(val value: String) extends AnyVal

object Values {
  // This should be allowed.
  val DEFAULT = "anonymous"
}""".parse[Source].get

  test("generate object code") {
    val ghv =
      new GatherHardcodedValues(Some(Term.Select(Term.Name("com"), Term.Name("example"))), Some(Term.Name("Obj")))
    val list = ghv.gatherLiterals(source)

    val obj = ghv.createObjectSource(list)

    val expected = q"""
    package com.example {
      object Obj {
        val CAPITALIZE_0 = "odersky"
        val NAME_0 = "martin"
      }
    }"""

    assertEquals(obj.syntax, expected.syntax)
  }
}
