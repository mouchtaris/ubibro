package incubate3
package list

trait ListPackage {

  final type List = _ :: _

  sealed trait ::[+h, +t <: List] {

    def head: h

    def tail: t

    final override def toString: String = appendTo(new StringBuilder).toString

    def appendTo(sb: StringBuilder): sb.type

  }

  final case class Cons[h, t <: List](
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

  final case object Nil extends Nil {

    val head = this

    def tail = this

    def appendTo(sb: StringBuilder): sb.type = {
      sb append "Nil"
      sb
    }

  }

}
