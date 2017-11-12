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
    sealed trait ResultOf
      extends Any
    {
      type t[out]
    }

    final implicit class Getter[resultOf[_]](val self: Unit) extends AnyVal {
      def apply[l: resultOf](): resultOf[l] = implicitly
    }

    trait Getter2[resultOf[_, _] <: ResultOf] {
      def apply[a, b]: Getter[resultOf[a, b]#t] = ()
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
        with ghosts.ResultOf
    {
      final type t[out] = Or[a, b, out]
    }
    object Or
      extends AnyRef
      with OrDeductionA
      with ghosts.Getter2[OrResultOf]
    {
      final type resultOf[a, b] = OrResultOf[a, b]
      implicit def fromUnit[a, b, ev](u: Unit): Or[a, b, ev] = new Or(u)
    }

    final implicit class &&[a, b](val self: Unit) extends AnyVal
    object && {
      implicit def fromAB[a: Implicit, b: Implicit]: a && b = ()
    }

  }

}

object Incubator {
  import
    hell0.tag._

  def entry(args: Array[String]): Unit = {
    new Tests.evidenceTest
  }

}
