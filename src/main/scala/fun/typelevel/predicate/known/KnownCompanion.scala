package fun.typelevel.predicate
package known

/**
  * Provides implicit resolution of types while retaining type refinements
  * on the return type.
  *
  * The difference from `implicitly` is this:
  * {{{
  *
  *   trait A {
  *     type Out
  *   }
  *
  *   implicit object A extends A {
  *     type Out = String
  *   }
  *
  *   def test(): Unit = {
  *     val impl1 = implicitly[A]
  *     val impl2 = Known[A]
  *     val str1: impl1.Out = "Hello" // does not compile
  *     val str2: impl2.Out = "Hello" // compiles
  *   }
  *
  * }}}
  */
trait KnownCompanion {

  /**
    * Implicitly resolve an instance of type `t`.
    *
    * Any type refinements on the found implicit instance are retained
    * on the return type of this method.
    *
    * @param t implicitly resolved
    * @tparam t type to resolve implicitly
    * @return the implicitly resolved instance for t, retaining type refinements
    */
  @inline def apply[t <: AnyRef](implicit t: t): t.type =
    t

}
