package example

import
  fun._,
  list._,
  predicate._,
  typelevel._,
  typelevel.predicate._

import
  scala.reflect.runtime.universe._

object poo {

  trait thinkaboutit {

    abstract class Pika[or, a, b] {
      type Out
      val pick: (a, b) ⇒ Out
    }

    object Pika {
      type aux[or, a, b, out] = Pika[or, a, b] { type Out = out }

      def apply[or, a, b, out](p: (a, b) ⇒ out): aux[or, a, b, out] =
        new Pika[or, a, b] {
          type Out = out
          val pick: (a, b) ⇒ out = p
        }

      implicit def pickaA[
        ora, orb, orOut: Or.resultOf[ora, orb]#t: IsType.is[ora]#t,
        a, b, u: Lub.of[a, b]#t
      ]: aux[Or[ora, orb], a, b, u] =
        apply { case (a, _) ⇒ Known[Lub.Aux[a, b, u]].aIsLub(a) }

      implicit def pickB[
        ora, orb, orOut: Or.resultOf[ora, orb]#t: IsType.is[orb]#t,
        a, b, u: Lub.of[a, b]#t
      ]: aux[Or[ora, orb], a, b, u] =
        apply { case (_, b) ⇒ Known[Lub.Aux[a, b, u]].bIsLub(b) }
    }

  }

}

object Hello extends Greeting with App {
  import poo._
  object tai extends thinkaboutit
  import tai._

  case object a
  type a = a.type
  implicit case object b
  type b = b.type
  type or = Or[a, b]

  trait u
  case object ifa extends u
  case object ifb extends u

  Known[or]
  println {
    Known[Pika[or, ifa.type, ifb.type]].pick(ifa, ifb)
  }

  println(greeting)

}

trait Greeting {
  lazy val greeting: String = "hello"
}
