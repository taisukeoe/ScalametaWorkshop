package com.taisukeoe

import scala.meta._

class GatherHardcodedValues(pkgName: Option[Term.Ref] = None, objName: Option[Term.Name] = None) {

  final def gatherLiterals(tree: Tree): List[(Term.Name, Lit)] =
    tree.collect {
      case AllowedTree() => Nil
      case Term.Apply(name, args) if args.exists(AllowedLiterals.isOK) =>
        args.collect {
          case l: Lit if !AllowedLiterals.set(l.syntax) =>
            Term.Name(s"${name.syntax.toUpperCase()}_${args.indexOf(l)}") -> l
        }
      case Term.New(Init(name, _, List(args))) if args.exists(AllowedLiterals.isOK) =>
        args.collect {
          case l: Lit if !AllowedLiterals.set(l.syntax) =>
            Term.Name(s"${name.syntax.toUpperCase()}_${args.indexOf(l)}") -> l
        }
    }.flatten

  def createObjectSource(args: List[(Term.Name, Lit)]): Stat = {
    val obj = Defn.Object(
      Nil,
      objName.getOrElse(Term.Name("Constants")),
      Template(
        Nil,
        Nil,
        Self(Name.Anonymous(), None),
        args.map {
          case (term, lit) =>
            q"val ${Pat.Var(term)} = $lit"
        }
      )
    )
    pkgName.map(Pkg(_, List(obj))).getOrElse(obj)
  }

  lazy val objectName: Term.Name = objName.getOrElse(Term.Name("Constants"))
}

object AllowedLiterals {
  val set: Set[String] = Set(Lit.String(""), Lit.Unit(), Lit.Null()).map(_.syntax)
  def isOK(term: Term): Boolean = !set(term.syntax) && term.isInstanceOf[Lit]
}

object AllowedTree {
  def unapply(tree: Tree): Boolean = false
}
