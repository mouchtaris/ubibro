import types._
import scala.language.strictEquality
import core._

object typeops {
  pkg =>

  // l => p :: List
  type BeginsWith[p, l <: List] = l <:< p :: List

  trait tconcat[a <: List, b <: List] extends Result[List] {
    val a: BeginsWith[a, Out]
    val b: Out => b
  }
  object tconcat {
    final case class create[a <: List, b <: List, out <: List](
      a: BeginsWith[a, out],
      b: out => b
    ) extends tconcat[a, b] {
      type Out = out
    }

    //implicit def hcat[h, b <: List]: create[h :: List, b, h :: b :: List] =
    //  create(
    //    a = implicitly[BeginsWith[h, h :: b :: List]],
    //    b = out => out.tail
    //  )
    //implicit def nilcat[b <: List]: fullT[tconcat[Nil, b], List, b] =
    //  create(a = _ => Nil, b = identity[b])

    //implicit def headtailcat[h, t <: List, b <: List](
    //  implicit
    //  tbcat: tconcat[t, b]
    //): fullT[tconcat[h :: t, b], List, h :: tbcat.Out] =
    //  create(
    //    a = ??? : BeginsWith[h :: t, h :: tbcat.Out],
    //    b = ??? : ((h :: tbcat.Out) => b)
    //  )

  }



}
