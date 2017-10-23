package incubator

object HereIsTheThing {

  /**
   * Assume we are modeling functions that instead of requiring a specific
   * input argument type, they require that any input given has an implicit
   * "view" as a typeclass/effect.
   */
  // Effect-function
  trait FFunc {
    type In[t]
    type Out
    def apply[t: In](in: t): Out
  }

  /**
   * Now suppose that we model some data structures, which we need to map to
   * runtime behaviour through implicitly known "type traits", one of which specifies
   * the [[FFunc]] used to model their behaviour.
   */
  trait Command 
  trait CommandBehaviour[command <: Command] {
    type ffunc <: FFunc
  }

}

object wat {

  /**
   * A "type class", "implicit evidence" type, etc...
   *
   * @tparam t just for looks, and facilitate
   *  the implicit resolution scenario
   */
  trait fo[t] {
    /**
     * An abstract type member THAT IS A TYPE CONSTRUCTOR
     */
    type f[_]
  }

  //
  // Types that will be used for `fo`'s abstract type `f[_]`
  //
  trait F1[t]
  trait F2[t]
  //
  // Couple of case for type class `fo`
  //
  trait loo
  implicit object loo extends loo with fo[loo] {
    type f[t] = F1[t]
  }
  //
  trait poo
  implicit object poo extends poo with fo[poo] {
    type f[t] = F2[t]
  }

  // Double checking, this compiles
  val w0 = implicitly[ fo[loo] ]
  val w1 = implicitly[ fo[poo] ]

  /**
   * *** PROBLEM HERE ***
   *
   * A method call, in which the abstract TYPE CONSTRUCTOR type member
   * needs to be inferred by the compiler.
   *
   * This fails to be implicitly resolved, because the compiler
   * fails to instantiate the type parameters, (probably) because
   * it is unable to infer abstract type `f`. See further below
   * for the failed invocation.
   *
   */
  def fu0[t, in[_]](t: t)(
    implicit
    fo: fo[t] { type f[a] = in[a] }
  ): String = s"Hi $t: $fo"

  trait Trait[f[_]]

  // These will work find, since we explicitly set type param `in`
  val w2 = fu0[loo, F1](loo: loo)
  val w3 = fu0[poo, F2](poo: poo)

  // *** PROBLEM HERE ***
  // The following fails to compile
  val w4 = fu0(loo: loo) // type ascription for test simplification
  val w5 = fu0(poo: poo) // type ascription for test simplification

  //
  // Error message:
  //
  // (notice the "type f has one type parameter, but type in has one"
  //  part of the error)
  //
  // [info] .../incubator/Main.scala:64:15: poo is not a valid implicit value for incubator.wat.fo[incubator.wat.poo]{type f[a] = in[a]} because:
  // [info] type parameters weren't correctly instantiated outside of the implicit tree: inferred kinds of the type arguments (incubator.wat.poo.f[t]) do not conform to the expected kinds of the type parameters (type in).
  // [info] incubator.wat.poo.f[t]'s type parameters do not match type in's expected parameters:
  // [info] type f has one type parameter, but type in has one
  // [info]   val w5 = fu0(poo: poo) // type ascription for test simplification
  // [info]               ^
  // [info] .../incubator/Main.scala:64:15: incubator.this.wat.poo is not a valid implicit value for incubator.wat.fo[incubator.wat.poo]{type f[a] = in[a]} because:
  // [info] type parameters weren't correctly instantiated outside of the implicit tree: inferred kinds of the type arguments (incubator.wat.poo.f[t]) do not conform to the expected kinds of the type parameters (type in).
  // [info] incubator.wat.poo.f[t]'s type parameters do not match type in's expected parameters:
  // [info] type f has one type parameter, but type in has one
  // [info]   val w5 = fu0(poo: poo) // type ascription for test simplification
  // [info]               ^
  // [error] .../incubator/Main.scala:64:15: could not find implicit value for parameter fo: incubator.wat.fo[incubator.wat.poo]{type f[a] = in[a]}
  // [error]   val w5 = fu0(poo: poo) // type ascription for test simplification
  // [error]               ^
  // [error] two errors found
  // [error] (compile:compileIncremental) Compilation failed
  // [error] Total time: 1 s, completed Oct 22, 2017 4:48:35 PM
  //

}

object Main extends Samples {

  def main(args: Array[String]): Unit = {
    { Known[Pig[ca]]; pig[ca]; }.asInstanceOf[Unit]
    Known[ Interpretation[ca] ]
    val interp = Known[ Interpretation[ Or[ca, cb]] ]

    println {
      interp(ia() :: ib() :: "hello" :: 12 :: Nil)
    }
  }

}
