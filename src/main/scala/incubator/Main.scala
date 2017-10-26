package incubator

import
  fun.typelevel._,
  fun.list._,
  fun.interpretation._


object incubation {

  sealed trait OrEv
  case object OrEvA extends OrEv
  case object OrEvB extends OrEv

  final case class OrI[
    Ca,
    Cb,
    Ia <:,
    Ib
  ](
    interpa: Interpretation[Ca],
    interpb: Interpretation[Cb],
    ev: OrEv
  ) extends Interpretation[Or[Ca, Cb]] {

    type In[a] = And[interpa.In[a], interpb.In[a]]

    type Out

    def apply[a: In](a: a): Out = ev match {
      case OrEvA => ???
      case OrEvB => ???
    }

  }

  implicit def orIa[Ca: Or.resultOf[Ca, Cb]#t, Cb](
    implicit
    interpa: Interpretation[Ca],
    interpb: Interpretation[Cb],
  ): OrI[Ca, Cb] =
    OrI(interpa, interpb, OrEvA)

  implicit case object Oa; type Oa = Oa.type
  implicit case object Ob; type Ob = Ob.type
  trait Clue[t]
  implicit object Ca extends Clue[Oa]
  implicit object Cb extends Clue[Ob]
  object Clue {
    implicit def t2[a: Clue, b: Clue]: Clue[(a, b)] = new Clue[(a, b)] { }
  }
  implicit case object Ia extends Interpretation[Oa] {
    type In[a] = Clue[a]
    type Out = Int
    def apply[a: In](a: a): Out = 12
  }
  implicit case object Ib extends Interpretation[Ob] {
    type In[a] = Clue[a]
    type Out = String
    def apply[a: In](a: a): Out = "fuck off harry"
  }

}

object Main {
  import incubation._

  def main(args: Array[String]): Unit = println {
    Vector(
      Known[And[Clue[Oa], Clue[Ob]]],
      Known[Interpretation[Or[Oa, Ob]]],
      Known[Interpretation[Or[Oa, Ob]]].apply( Oa )
    )
  }

}
