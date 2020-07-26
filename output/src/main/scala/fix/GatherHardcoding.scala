package fix

import com.example.Obj
object GatherHardcoding {
  object Hello {
    def hello(name: String) = println(name)

    def capitalize(name: String) = name.capitalize

    hello(capitalize(Obj.CAPITALIZE_0))
    hello("")
    hello(null)
    hello(new Name(Obj.NAME_0).value)
  }

  class Name(val value: String) extends AnyVal

  object Constants {
    // This should be allowed.
    val DEFAULT = "anonymous"
  }
}
