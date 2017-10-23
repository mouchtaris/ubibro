package incubator

trait Concat[a <: List, b <: List] {

  type Out <: List

  def apply(list: Out): b

}

object Concat {

  final case class NilCat[b <: List](
  ) extends Concat[Nil, b] {

    type Out = b

    def apply(b: b): b = b

  }

  implicit def nilcat[b <: List]: NilCat[b] = NilCat()


  final case class ListCat[
    h,
    t <: List,
    b <: List
  ](
    tbcat: Concat[t, b]
  ) extends Concat[h :: t, b] {

    type Out = h :: tbcat.Out

    def apply(list: Out): b =
      tbcat(list.tail)

  }

  implicit def listcat[
    h,
    t <: List,
    b <: List
  ](
    implicit
    tbcat: Concat[t, b]
  ): ListCat[h, t, b] =
    ListCat(tbcat)

}
