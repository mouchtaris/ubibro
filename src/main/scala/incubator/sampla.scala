package incubator

import java.net.URI

object sample {

  //
  // Input restriction
  //
  trait Clue[t]

  //
  // Sample inputs
  //
  case object OA
  type OA = OA.type
  case object OB
  type OB = OB.type

  //
  // Restriction satisfied
  //
  implicit case object CA extends Clue[OA]
  implicit case object CB extends Clue[OB]

  //
  // Interpreters
  //
  implicit case object IA extends Interpretation[OA] {
    type In[a] = Clue[a]
    type Out = String
    def apply[a: In](a: a): Out = "fuck off harry"
  }
  implicit case object IB extends Interpretation[OB] {
    type In[a] = Clue[a]
    type Out = URI
    def apply[a: In](a: a): Out = URI create "https://www.com.com"
  }
}
