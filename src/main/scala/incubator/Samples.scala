package incubator

trait Samples {

  case class ia()

  case class ib()

  trait ca

  trait cb

  implicit val capig: Pig[ca] = Pig("ca")

  implicit val cbpig: Pig[cb] = Pig("cb")

  implicit object caI extends Interpretation[ca] { 
    type In[r <: List] = ia :: r
    type Out = String
    def apply[r <: List: Rest](in: In[r]): Out = 
      in.head.toString
  }

  implicit object cbI extends Interpretation[cb] {
    type In[r <: List] = ib :: r
    type Out = String
    def apply[r <: List: Rest](in: In[r]): Out =
      in.head.toString
  }

}
