package incubate4

import
  common.StdImports._,
  Console.{ println ⇒ cprintln },
  list._

object Incubator
  extends AnyRef
  with java.lang.Runnable
{

  trait &&[a, b]
  object && {
    private[this] object instance extends (Nothing && Nothing)
    def apply[a, b]() = instance.asInstanceOf[a && b]
    implicit def `a && b`[a: Implicit, b: Implicit]: a && b = apply()
  }

  trait ||[a, b]
  trait `||Constructor` {
    private[this] object instance extends (Nothing || Nothing)
    final def apply[a, b]() = instance.asInstanceOf[a || b]
  }
  trait `||DeductionsLow` extends Any {
    this: `||Constructor` ⇒
    final implicit def `|| b`[a, b: Implicit]: a || b = apply()
  }
  trait `||DeductionsHigh` extends Any with `||DeductionsLow` {
    this: `||Constructor` ⇒
    final implicit def `a ||`[a: Implicit, b]: a || b = apply()
  }
  object ||
    extends AnyRef
    with `||Constructor`
    with `||DeductionsHigh`

  trait Poly[f[_], a] {
    type Out
    def apply(a: a): Out
  }
  sealed trait ListMap[list <: List, f[_], out <: List] extends Any {
    final type Out = out
    def apply(list: list): Out
  }
  object ListMap {
    sealed trait resultOf[list <: List, f[_]] {
      final type t[out <: List] = ListMap[list, f, out]
    }

    implicit def nilMap[f[_]]: ListMap[Nil, f, Nil] =
      new ListMap[Nil, f, Nil] {
        def apply(list: Nil): Nil = list
      }
    implicit def listMap[
      f[_],
      h, t <: List
    ](
      implicit
      tmap: ListMap[t, f, _ <: List],
      poly: Poly[f, h],
    ): ListMap[h :: t, f, poly.Out :: tmap.Out] =
      new ListMap[h :: t, f, poly.Out :: tmap.Out] {
        def apply(list: h :: t): Out =
          poly(list.head) :: tmap(list.tail)
      }
  }

  sealed trait TFun1[f[_], a, out] extends Any {
    type Out = out
  }
  object TFun1 {
    private[this] object instance extends TFun1[scala.Tuple1, Nothing, Nothing]
    def apply[f[_], a, out]() = instance.asInstanceOf[TFun1[f, a, out]]
  }
  sealed trait TListMap[list <: List, f[_], out <: List] extends Any {
    final type Out = out
  }
  object TListMap {
    private[this] object instance extends TListMap[Nil, scala.Tuple1, Nil]
    def apply[list <: List, f[_], out <: List]() = instance.asInstanceOf[TListMap[list, f, out]]
    implicit def nilMap[f[_]]: TListMap[Nil, f, Nil] = apply()
    implicit def listMap[
      f[_],
      h, t <: List
    ](
      implicit
      tmap: TListMap[t, f, _ <: List],
      fun: TFun1[f, h, _]
    ): TListMap[h :: t, f, fun.Out :: tmap.Out] = apply()
  }

  trait ImplicitEvidence {
    final implicit def implicitEvidence: this.type = this
  }

  trait Type extends Any { type V }
  case class Named[t <: Type](t: t)
    extends AnyRef
    with ImplicitEvidence
    with tag.Tagverse
    { type T = t.V }
  case class Record[fields <: List](fields: fields)
    extends AnyRef
    with tag.Tagverse
  {
    type T = List
    trait ToValue[_]
    object ToValue {
      implicit def toValueTFun1[T_, n <: Named[_] { type T = T_ }]
        : TFun1[ToValue, n, T_] = TFun1()
    }
  }
  case class Extend[r, fields <: List](record: r, fields: fields)

  case object int
    extends AnyRef
    with Type { type V = Int }

  val x = Named(int)
  val y = Named(int)
  val vec2 = Record(x :: y :: Nil)

  val z = Named(int)
  val vec3 = Extend(vec2, z :: Nil)

  val x1 = x(12)
  val y1 = y(18)
  val z1 = z(32)
  val v1 = vec2(x1 :: y1 :: z1 :: Nil)

  def run(): Unit = cprintln(1)

}


object junk {
  trait ForAll[+list <: List, p[_]]
    extends Any
  object ForAll {
    private[this] object instance extends ForAll[Nil, scala.Tuple1]
    def apply[list <: List, p[_]]() = instance.asInstanceOf[ForAll[list, p]]

    type holds[p[_]] = { type t[list <: List] = ForAll[list, p] }

    implicit def lastForAll[p[_], h: p]: ForAll[h :: Nil, p] = apply()
    implicit def listForAll[p[_], h: p, t <: List: holds[p]#t]: ForAll[h :: t, p] = apply()
  }

  trait IsRecord[t] extends Any
  object IsRecord {
    private[this] object instance extends IsRecord[Nothing]
    def apply[t]() = instance.asInstanceOf[IsRecord[t]]
  }
}
