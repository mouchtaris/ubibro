package incubate3

import
  cross.lang._,
  list._,
  Console.{ println ⇒ cprintln },
  TypeInfo._

object hell0 {

  object tag {

    trait Tagverse {
      type T

      sealed abstract trait tag extends Any
      final type t = T & tag


      def apply(t: T): t = t.asInstanceOf[t]
      def unapply(t: t): T = t
    }

  }

  object ghosts {

    class Getter[ev[_ <: U], U](val self: Unit) extends AnyVal {
      def apply[t <: U: ev](): ev[t] = implicitly
    }

    trait ResultOf2Ext[U] extends Any {
      type on[a, b] = {
        type t[_ <: U]
      }
    }

    trait Getter2[U] {
      val resultOf: ResultOf2Ext[U]
      def apply[a, b] = new Getter[resultOf.on[a, b]#t, U](())
    }

  }

  object evidence {

    final class Or[a, b, ev](val self: Unit) extends AnyVal {
      type Ev = ev
    }
    sealed trait OrDeductionB
      extends Any
    {
      implicit def fromB[a, b: Implicit]: Or[a, b, b] = ()
    }
    sealed trait OrDeductionA
      extends Any
      with OrDeductionB
    {
      implicit def fromA[a: Implicit, b]: Or[a, b, a] = ()
    }
    final type ||[a, b] = Or[a, b, _]
    sealed trait OrResultOf[a, b]
      extends Any
      with ghosts.ResultOfExt[Any]
      { final type t[out] = Or[a, b, out] }
    object Or
      extends AnyRef
      with OrDeductionA
      with ghosts.Getter2[OrResultOf, Any]
    {
      final type resultOf[a, b] = OrResultOf[a, b]
      implicit def fromUnit[a, b, ev](u: Unit): Or[a, b, ev] = new Or(u)
    }

    final implicit class &&[a, b](val self: Unit) extends AnyVal
    object && {
      implicit def fromAB[a: Implicit, b: Implicit]: a && b = ()
    }

  }

  object listops {

    final class Map[f[_], list <: List, out <: List](val self: Unit) extends AnyVal
    sealed trait MapResultOf[f[_], list <: List]
      extends Any
      with ghosts.ResultOfExt[List]
      { type t[out <: List] = Map[f, list, list] }

  }

}

object Incubator {
  import
    hell0.tag._

  def entry(args: Array[String]): Unit = {
    new Tests.evidenceTest
  }

}
