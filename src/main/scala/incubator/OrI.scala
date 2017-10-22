package incubator

final case class OrI[
  a,
  b,
  ain[_ <: List] <: List, aout,
  bin[_ <: List] <: List, bout
](
  interpa: Interpretation.aux[a, ain, aout],
  interpb: Interpretation.aux[b, bin, bout]
)
  extends AnyRef
  with Interpretation[Or[a, b]] 
{

  type In[rest <: List] = ain[bin[rest]]

  type Out = aout

  def apply[r <: List: Rest](in: In[r]): Out = {
    val aout: aout = interpa(in)
    aout
  }

}

object OrI {

  type aux[
    a,
    b, 
    ain[_ <: List] <: List, aout,
    bin[_ <: List] <: List, bout
  ] =
    OrI[
      a,
      b,
      ain, aout,
      bin, bout
    ]

}
