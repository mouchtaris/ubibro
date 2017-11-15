package list

import
  common.StdImports._

trait ListPackage {

  final type List = _ :: _

  trait ::[+h, +t <: List] {

    val head: h

    def tail: t

    final override def toString: String = appendTo(new StringBuilder).toString

    def appendTo(sb: StringBuilder): sb.type

  }

  case class Cons[h, t <: List](
    head: h,
    tail: t
  ) extends (h :: t)
  {
    def appendTo(sb: StringBuilder): sb.type = {
      sb append head append " :: "
      tail appendTo sb
    }
  }

  sealed trait Nil extends (Nil :: Nil)

  final val Nil: Nil = new Nil {

    val head = this

    def tail = this

    def appendTo(sb: StringBuilder): sb.type = {
      sb append "Nil"
      sb
    }

  }

}
