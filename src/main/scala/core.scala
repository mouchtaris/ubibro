trait core {
  def known[t](implicit t: t): t.type = t
  type Read[T, R] = implicit T => R

  sealed trait True
  implicit case object True extends True
  trait ::[h, t <: _ :: _] {
    val head: h
    def tail: t
  }
  type List = _ :: _
  trait Nil extends (Nil :: Nil)
  case object Nil extends Nil {
    val head: Nil = this
    val tail: Nil = this
  }

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

  trait Evaluator {
    type Out
    type Operation
    val eval: Read[Operation, Out]
  }
  trait EvaluatorCombinator[Eval1, Eval2] extends Evaluator

  trait Interpretation[E] {
    final type Expression = E
    type In <: Arguments
    type Eval <: Evaluator
    def plug[a: In](a: a): Eval
  }

  trait InterpretationComposition[E[_, _], E1, E2](
    val interpa: Interpretation[E1],
    val interpb: Interpretation[E2]
  ) extends Interpretation[E[E1, E2]] {
    type In[in] <: ArgumentCombinator[interpa.In[in], interpb.In[in], in]
    type Eval <: EvaluatorCombinator[interpa.Eval, interpb.Eval]
  }
}
