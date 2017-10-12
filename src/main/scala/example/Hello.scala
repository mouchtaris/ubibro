package example

import fun._,
  list._,
  list.typelevel._,
  fun.typelevel._,
  interpretation._

import
  scala.reflect.ClassTag

object pig {

  final case class Pig[t](desc: String) {
    override val toString: String = desc
  }

  object Pig {
    def pig[t: Pig]: Pig[t] = implicitly

    implicit def orpig[a: Pig, b: Pig]: Pig[Or[a, b]] = Pig {
      s"Or[${pig[a]}, ${pig[b]}]"
    }

    implicit val intpig: Pig[Int] = Pig("int")
    implicit val nilpig: Pig[Nil] = Pig("nil")
    implicit val stringpig: Pig[String] = Pig("string")

    implicit def istypepig[a: Pig, b: Pig]: Pig[IsType[a, b]] = Pig {
      s"IsType[${pig[a]}, ${pig[b]}]"
    }

    implicit def allpig[t: ClassTag]: Pig[t] = Pig(implicitly[ClassTag[t]].runtimeClass.toString)
  }

}
import pig._


object ors {

  case object ca
  case object cb
  case object cc

  type ca = ca.type
  type cb = cb.type
  type cc = cc.type

  final case class ii[t]() extends Interpretation[t] {
    type In = t :: Nil
    type Out = t
    val f: In ⇒ Out = _.head
  }

  implicit val interpa = ii[ca]()
  implicit val interpb = ii[cb]()
  implicit val interpc = ii[cc]()

  type |||[a, b] = a `Or` b

  type cab = ca ||| cb
  type cbc = cb ||| cc
  type cabc = cab ||| cc
  Known[ cabc =:= (ca ||| cb ||| cc) ]
  Known[ cabc =:= Or[Or[ca, cb], cc] ]

  implicit val truth = cb
  val orab = Known[ cab ]
  val orbc = Known[ cbc ]
  val orabc = Known[ cabc ]

  def interp[t] = new {
    def apply[in, out](in: in)(implicit i: Interpretation.withInOut[in, out]#t[t]): out =
      i(in)
  }

  val interpab = Known[ Interpretation[cab] ]
  val interpbc = Known[ Interpretation[cbc] ]
  val interpabc = Known[ Interpretation[cabc] ]

}
import ors._

object poo {

  def main(args: Array[String]): Unit = {
    println { interpab(ca :: cb :: Nil) }
    println { interpbc(cb :: cc :: Nil) }
    println { interpabc(ca :: cb :: cc :: Nil) }
  }

}
