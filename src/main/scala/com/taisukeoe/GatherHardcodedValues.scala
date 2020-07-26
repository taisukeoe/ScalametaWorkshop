package com.taisukeoe

import scala.meta._

class GatherHardcodedValues(pkgName: Option[Term.Ref] = None, objName: Option[Term.Name] = None) {

  final def gatherLiterals(tree: Tree): List[(Term.Name, Lit)] = ???

  def createObjectSource(args: List[(Term.Name, Lit)]): Stat = ???

  lazy val objectName: Term.Name = objName.getOrElse(Term.Name("Constants"))
}

object AllowedLiterals {
  def isOK(term: Term): Boolean = ???
}

object AllowedTree {
  def unapply(tree: Tree): Boolean = ???
}
