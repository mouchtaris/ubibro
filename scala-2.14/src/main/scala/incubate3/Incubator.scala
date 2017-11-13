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

  object ghosts

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
      { final type t[out] = Or[a, b, out] }
    object Or
      extends AnyRef
      with OrDeductionA
    {
      final type resultOf[a, b] = OrResultOf[a, b]
      implicit def fromUnit[a, b, ev](u: Unit): Or[a, b, ev] = new Or(u)

      final class Getter[a, b](val self: Unit) extends AnyVal {
        def apply[out: resultOf[a, b]#t](): Or[a, b, out] = ()
      }

      def apply[a, b] = new Getter[a, b](())
    }

    final implicit class &&[a, b](val self: Unit) extends AnyVal
    object && {
      implicit def fromAB[a: Implicit, b: Implicit]: a && b = ()
    }


    import listops._

    type ForAll[p[_], list <: List, out] = MapReduce[p, &&, list, out]
    type ForAny[p[_], list <: List, out] = MapReduce[p, ||, list, out]
  }

  object listops {

    final class Map[f[_], list <: List, out <: List](val self: Unit) extends AnyVal {
      type Out = out
    }
    sealed trait MapResultOf[f[_], list <: List]
      extends Any
      { type t[out <: List] = Map[f, list, out] }
    object Map {
      final type resultOf[f[_], list <: List] = MapResultOf[f, list]
      implicit def fromUnit[f[_], list <: List, out <: List](u: Unit): Map[f, list, out] = new Map(u)

      implicit def mapNil[f[_]]: Map[f, Nil, Nil] = ()
      implicit def mapList[
        f[_],
        h, t <: List,
        tmap <: List: resultOf[f, t]#t
      ]: Map[f, h :: t, f[h] :: tmap] = ()

      final class Getter[f[_], list <: List](val self: Unit) extends AnyVal {
        def apply[out <: List: resultOf[f, list]#t](): Map[f, list, out] = ()
      }
      def apply[f[_], list <: List] = new Getter[f, list](())
    }

    final class Fold[f[_, _], list <: List, out](val self: Unit) extends AnyVal {
      type Out = out
    }
    trait FoldResultOf[f[_, _], list <: List] extends Any {
      type t[out] = Fold[f, list, out]
    }
    object Fold {
      final type resultOf[f[_, _], list <: List] = FoldResultOf[f, list]
      implicit def fromUnit[f[_, _], list <: List, out](u: Unit): Fold[f, list, out] = new Fold[f, list, out](u)

      implicit def foldNil[f[_, _]]: Fold[f, Nil, f[Nil, Nil]] = ()
      implicit def foldList[
        f[_, _],
        h, t <: List,
        tfold: resultOf[f, t]#t
      ]: Fold[f, h :: t, f[h, tfold]] = ()

      final class Getter[f[_, _], list <: List](val self: Unit) extends AnyVal {
        def apply[out: resultOf[f, list]#t](): Fold[f, list, out] = ()
      }
      def apply[f[_, _], list <: List] = new Getter[f, list](())
    }

    final class MapReduce[p[_], f[_, _], list <: List, out](val self: Unit) extends AnyVal {
      type Out = out
    }
    sealed trait MapReduceResultOf[p[_], f[_, _], list <: List] {
      final type t[out] = MapReduce[p, f, list, out]
    }
    object MapReduce {
      type resultOf[p[_], f[_, _], list <: List] = MapReduceResultOf[p, f, list]
      implicit def fromUnit[p[_], f[_, _], list <: List, out](u: Unit): MapReduce[p, f, list, out] = new MapReduce(u)

      implicit def listMapReduce[
      p[_],
      f[_, _],
      list <: List,
      listMap <: List: Map.resultOf[p, list]#t,
      listFold: Fold.resultOf[f, listMap]#t
      ]: MapReduce[p, f, list, listFold] = ()
    }

  }

}

object Incubator {
  import
    hell0.tag._

  def entry(args: Array[String]): Unit = {
    new test.evidenceTest
    new test.listopsTest
  }

}
