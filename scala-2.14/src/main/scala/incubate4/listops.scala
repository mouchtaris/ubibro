package incubate4

import
  common.StdImports._,
  list._

object listops
  extends AnyRef
  with get.Package
{

  trait Map[list <: List, f, out <: List] {
    final type Out = out
    def apply(list: list): Out
  }
  object Map {
    implicit def mapNil[f]: Map[Nil, f, Nil] =
      nil ⇒ nil

    implicit def mapList[
      h,
      t <: List,
      f
    ](
      implicit dummyImplicit: DummyImplicit,
      f: Fun[f, h, _],
      tmap: Map[t, f, _ <: List]
    ): Map[h :: t, f, f.Out :: tmap.Out] =
    new Map[h :: t, f, f.Out :: tmap.Out] {
      def apply(list: h :: t): Out = {
        val fh: f.Out = f(list.head)
        val ft: tmap.Out = tmap(list.tail)
        fh :: ft
      }
    }
  }

  trait Fun[f, at, out] {
    final type F = f
    final type At = at
    final type Out = out
    def apply(a: At): Out
  }

}
