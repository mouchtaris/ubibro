package incubator

import
  java.net.URI,
  fun.typelevel._,
  fun.list._,
  fun.interpretation._

object sample {

  final case class IsTypeI[Ta, Tb](
    is: IsType[Ta, Tb],
  ) extends Interpretation[IsType[Ta, Tb]] {

    type In[a] = a =:= Ta

    type Out = Tb

    @inline def apply[a: In](a: a): Out =
      is.evidence(a)

  }

  implicit def isTypeI[a, b](
    implicit
    is: IsType[a, b]
  ): Main.I.fullT[IsType[a, b], IsTypeI[a, b]#In, b] =
    IsTypeI(is)


}
