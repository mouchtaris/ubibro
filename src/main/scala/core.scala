import scala.language.strictEquality

object core {
  def known[t](implicit t: t): t.type = t
  type Read[T, R] = implicit T => R

  sealed trait True
  implicit case object True extends True
  sealed trait ::[+h, +t <: _ :: _] {
    val head: h
    def tail: t

    @`inline` final override lazy val toString: String = {
      if (this == Nil)
        "Nil"
      else {
        val sb = new StringBuilder
        appendTo(sb)
        sb.toString
      }
    }

    def appendTo(b: StringBuilder): Unit = {
      b ++= head.toString
      b ++= " :: "
      tail appendTo b
    }
  }
  type List = _ :: _
  sealed trait Nil extends (Nil :: Nil)
  final case object Nil extends Nil {
    val head: Nil = this
    val tail: Nil = this
    override def appendTo(b: StringBuilder): Unit =
      b ++= "Nil"
  }
  final case class Cons[h, t <: List](
    val head: h,
    val tail: t
  ) extends (h :: t)
  trait ListOps[l <: List] extends Any {
    val self: l
    inline final def ::[h](h: h): h :: l = Cons(h, self)
  }
  final implicit class ListDeco[l <: List](val self: l) extends AnyVal with ListOps[l]


  final case class &&[a, b]()(
    implicit
    a: a,
    b: b
  )
  object && {
    implicit def and[a, b](implicit a: a, b: b): a && b = &&()
  }


  trait Arguments[in] {
    final type In = in
  }
  trait NoArguments[in] extends Arguments[in]
  trait Argument[a, in] extends Arguments[in] {
    val a: Read[In, a]
  }
  trait ArgumentCombinator[
    +ain <: Arguments[in],
    +bin <: Arguments[in],
    in
  ] extends Arguments[in] {
    type a
    val a: Read[In, a]
    type b
    val b: Read[In, b]
    val ain: ain
    val bin: bin
  }

  trait Evaluator[in] extends Any {
    type Out
    type Operation
    val eval: Read[Operation, Out]
  }
  trait EvaluatorCombinator[Eval1 <: Evaluator, Eval2 <: Evaluator, in]
    extends Evaluator[in]

  trait Interpretation[E] {
    final type Expression = E
    type In <: Arguments
    type Eval <: Evaluator
    def plug[a: In](a: a): Eval[a]
  }

  trait InterpretationComposition[E[_, _], E1, E2](
    val interpa: Interpretation[E1],
    val interpb: Interpretation[E2]
  ) extends Interpretation[E[E1, E2]] {
    type In[in] <: ArgumentCombinator[interpa.In[in], interpb.In[in], in]
    type Eval[in] <: EvaluatorCombinator[interpa.Eval, interpb.Eval, in]
  }
}
