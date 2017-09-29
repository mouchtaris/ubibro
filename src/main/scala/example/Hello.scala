package example

import fun._,
  list._,
  fun.typelevel._,
  list.typelevel._,
  interpreter._

import
  scala.reflect.ClassTag,
  scala.reflect.runtime.universe._

object Hello extends Greeting with App {
  poo.main(Array.empty)
  println(greeting)
}

trait Greeting {
  lazy val greeting: String = "hello"
}

object poo {

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

  abstract class I[t] {
    final type Evidence = t
    type In
    type Out
    def apply(in: In)(implicit evidence: Evidence): Out
  }

  abstract class IImpl[t, in, out] extends I[t] {
    final type In = in
    final type Out = out
  }

  object I {
    type Aux[t, in, out] = I[t] {
      type In = in
      type Out = out
    }

    def apply[t, in, out](f: in ⇒ t ⇒ out): Aux[t, in, out] =
      new IImpl[t, in, out] {
        def apply(in: In)(implicit evidence: Evidence): Out =
          f(in)(evidence)
      }
  }

  implicit def IsTypeI[a, b]: I.Aux[IsType[a, b], a, b] =
    new IImpl[IsType[a, b], a, b] {
      def apply(a: a)(implicit isType: Evidence): b =
        isType.evidence(a)
    }

  implicit def OrIb[a, b: Or.resultOf[a, b]#t: Known](
    implicit
    inta: I[a],
    intb: I[b]
  ): I.Aux[Or[a, b], (inta.In, intb.In), intb.Out] =
    I { case (a, b) ⇒ or ⇒ intb(b) }

  type list = java.net.URI :: Boolean :: String :: Int :: Nil
  //val list: list = 12 :: Nil

  val lm = Known[ ListMap[IsType.is[Int]#t, list] ]
  val lf = Known[ ListFold[Or, lm.Out] ]
  val fa = Known[ ForAny[list, IsType.is[Int]#t] ]
  val con = Known[ Contains[list, Int] ]

  type isint[t] = IsType[t, Int]
  type isint2[a, b] = isint[a] `Or` isint[b]
  type isint3[a, b, c] = isint[a] `Or` isint2[b, c]
  val ori = Known[ I[Or[IsType[String, Int], IsType[Int, Int]]] ]
  val wat = ori.apply(("Hello", 12))
  val superor = Known[ I[isint3[String, Unit, Int]] ]
  println {
    superor( ("fuck off", ((), 12)) ) + 1
  }
//  (
//    Contains.listContains(
//      fa
//    )
//  )

  println( implicitly[ Pig[lf.Out] ])

  def main(args: Array[String]): Unit = {
    println {
    }
  }
}
