package fun.typelevel

/**
  * Calculate the Least Upper Bound (Most Recent Supertype)
  * of two types.
  *
  * This is always calculated by the scala compiler during
  * a method call. Therefore, this marker trait is used as an
  * implicit parameter.
  *
  * @tparam a type a
  * @tparam b type b
  */
trait Lub[a, b] {

  /**
    * Type `a`
    */
  final type A = a

  /**
    * Type `b`
    */
  final type B = b

  /**
    * The LUB type of A and B
    */
  type Out

  /**
    * Implicit evidence that `a` conforms to `Out`
    */
  implicit val aIsLub: a ⇒ Out

  /**
    * Implicit evidence that `b` conforms to `Out`
    */
  implicit val bIsLub: b ⇒ Out

}

/**
  * Provides implicit and explicit access to Lub instances.
  *
  * Implicit acquisition is effectively LUB calculation.
  */
object Lub {

  /**
    * An implementation of [[Lub]].
    * @param aIsLub evidence that `a` conforms to `out`
    * @param bIsLub evidence that `b` conforms to `out`
    * @tparam a type a
    * @tparam b type b
    * @tparam out the LUB type of `a` and `b`
    */
  private[this] class impl[a, b, out](
    val aIsLub: a ⇒ out,
    val bIsLub: b ⇒ out
  ) extends Lub[a, b] {
    type Out = out
  }

  /**
    * Convenience alias, that can be used in implicit parameter declarations.
    *
    * Ex.
    * {{{
    *   def lubIsU[a, b, u: Lub.of[a, b]#t](u: u): u.type = u
    * }}}
    * @tparam a type a
    * @tparam b type b
    */
  type of[a, b] = {
    type t[u] = Aux[a, b, u]
  }

  /**
    * An "auxiliary" type, restricting result type "Out" to `lub`.
    *
    * @tparam a type A
    * @tparam b type B
    * @tparam lub type Out
    */
  type Aux[a, b, lub] = Lub[a, b] {
    type Out = lub
  }

  /**
    * Get an instance of Lub.Aux[a, b, lub]
    *
    * *NOTE*: this is not a LUB calculation. It's merely an object constructor.
    *
    * @tparam a type A
    * @tparam b type B
    * @tparam lub type A's and B's LUB -- explicitly set
    * @return a LUB instance
    */
  @inline def apply[a <: lub, b <: lub, lub](): Aux[a, b, lub] =
    new impl[a, b, lub](implicitly, implicitly)

  /**
    * Implicitly get the instance for `Lub[a, b]`, effectively resolving
    * the LUB for A and B.
    *
    * @param lub implicitly resolved
    * @tparam a type A
    * @tparam b type B
    * @return an instance of LUB
    */
  @inline def apply[a, b](implicit lub: Lub[a, b]): lub.type =
    lub

  /**
    * Get a LUB instance implicitly.
    *
    * Requiring such an instance implicitly will effectively
    * calculate the LUB of A and B, in the implicit parameter
    * acquisition call.
    *
    * @tparam a type A
    * @tparam b type B
    * @tparam lub type A's and B's LUB
    * @return a LUB instance
    */
  @inline implicit def implicitLub[a <: lub, b <: lub, lub]: Aux[a, b, lub] =
    apply()

}
