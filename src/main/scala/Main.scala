import scala.language.strictEquality
import typeops._
import pig._
import core._
import listarguments.{ ListArgumentHandling }
import evaluators._

object Main {

  def I[E](implicit interp: Interpretation[E]): interp.type = interp

  // Example of 0-arg, 0-retrun (Unit/SF)
  trait SayHi
  final case class SayHiEvaluator[args <: NoArguments, in](
    in: in
  )(
    implicit 
    args: args[in]
  ) extends Evaluator[in]
  {
    type Out = Unit
    type Operation = True
    val eval: Read[True, Unit] = println("FUCK OFF HARRY")
  }
  implicit case object ISayHi extends AnyRef
    with Interpretation[SayHi]
    with ListArgumentHandling
  {
    type In = NoArguments

    implicit class Eval[in](val a: in) extends Evaluator[in] {
      type Out = Unit
      type Operation = True
      val eval: Read[True, Unit] = println("Fuck you, Harry")
    }

    def plug[a: In](a: a): Eval[a] = a
  }

  // Example of 0-arg, 1-return
  trait Const[T](final val value: T)
  final case class IConst[T]() extends AnyRef
    with Interpretation[Const[T]]
    with ListArgumentHandling 
  {
    type In = NoArguments

    implicit class Eval[in](val a: in) extends Evaluator[in] {
      type Out = T
      type Operation = Const[T]
      val eval: Read[Const[T], T] = implicit const => const.value
    }
    def plug[a: In](a: a): Eval[a] = a
  }
  implicit def iConst[T]: IConst[T] = IConst()

  // Example of 1-arg, 1-return
  trait Neg[E]
  case class INeg[E](
    interp: Interpretation[E]
  ) extends AnyRef
    with Interpretation[Neg[E]]
    with ListArgumentHandling
  {
    type In = Argument[
    def plug[a: In](a: a): Eval[a] = ???
  }
  implicit def iNeg[E](
    implicit
    interp: Interpretation[E]
  ): INeg[E] =
    INeg(interp)


  def main(args: Array[String]): Unit = {
    println("Hello world!")
    println(msg)
    println { 12 :: 24 :: "Hi" :: Nil }
    type l = Int :: String :: Nil
    type p = Int :: List
    val l: l = 12 :: "Hello" :: Nil
    println { pig of known[l <:< p].apply(l) }
    known[ l :: l <:< l :: List]
    known[ BeginsWith[l, l :: l] ]
    // BeginsWith[String :: Nil, String :: Int :: Nil]
    // val cat = known[ tconcat[String :: List, Int :: List] ]
    I[SayHi] plug (12 :: 23 :: Nil) eval
    implicit val c42: Const[42] = new Const[42](42) {}
    println {
      I[Const[42]] plug ("Hello " :: "does " :: "not" :: "matress" :: Nil) eval
    }
  }

  def msg = "I was compiled by dotty :)"

}
