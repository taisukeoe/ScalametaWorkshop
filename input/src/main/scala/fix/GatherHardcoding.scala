/*
rules = GatherHardcoding
GatherHardcoding.pathString = "output/target/scala-2.12/src_managed/main/scala/Obj.scala"
GatherHardcoding.packageName = "com.example"
GatherHardcoding.objectName = "Obj"
*/
package fix

object GatherHardcoding {
  object Hello {
    def hello(name: String) = println(name)

    def capitalize(name: String) = name.capitalize

    hello(capitalize("odersky"))
    hello("")
    hello(null)
    hello(new Name("martin").value)
  }

  class Name(val value: String) extends AnyVal

  object Constants {
    // This should be allowed.
    val DEFAULT = "anonymous"
  }
}
