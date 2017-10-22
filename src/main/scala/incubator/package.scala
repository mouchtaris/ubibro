import fun.list.{
  List => fList,
  Nil => fNil,
}
import example.pig.{
  Pig => exPig
}
import fun.typelevel.{
  Known => tlKnown
}

package object incubator {
  type List = fList
  type Nil = fNil
  val Nil: fNil = fNil
  type ::[a, b <: List] = fun.list.::[a, b]

  type withInOut[
    F[_],
    in[_ <: List] <: List,
    out
  ] = {
    type t[T] = F[T] {

      type In[withInOut_rest <: List] =
        in[withInOut_rest]

      type Out = out

    }
  }

  type In1[_ <: List] = Nil

  type Pig[t] = exPig[t]
  val Pig: exPig.type = exPig
  def pig[t: Pig]: Pig[t] = exPig.pig

  type Known[t] = tlKnown[t]
  val Known: tlKnown.type = tlKnown

}
