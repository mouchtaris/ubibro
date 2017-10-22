package incubator

trait Interpretation[T] {

  type In[rest <: List] <: List

  type Out

  def apply[rest <: List: Rest](in: In[rest]): Out

}

object Interpretation {

  type aux[T, in[_ <: List] <: List, out] =
    Interpretation[T] {
      type In[rest <: List] = in[rest]
      type Out = out
    }

  type withInOut[in[_ <: List] <: List, out] = {
    type t[T] = aux[T, in, out]
  }
}

