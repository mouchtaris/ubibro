package fun
package typelevel

/**
  * Evidence that `a` can be viewed as `b`
  * @tparam a type to be viewed as `b`
  * @tparam b type that `a` can be viewed as
  */
trait View[a, b] extends Any {

  /**
    * Minimal information on how to transform `a` to `b`
    * @param a a type `a`
    * @return a type `b`
    */
  def apply(a: a): b

}

/**
  * Provide constructors, type aliases and implicit resolutions.
  */
object View {

  /**
    * A convenience type alias for declaring implicit parameters.
    * {{{
    *   def couldBeInt[a: View.as[Int]#t](a: a): Int = ???
    * }}}
    * @tparam b type `b`
    */
  type as[b] = {
    type t[a] = View[a, b]
  }

  /**
    * {{{
    *   a =:= b   ⇒   View[a, b]
    * }}}
    */
  @inline implicit def typeEqualityView[a, b](implicit ev: a =:= b): View[a, b] =
    ev.apply _

  final implicit class Ops[a](val self: a) extends AnyVal {
    @inline def viewAs[b](implicit view: View[a, b]): b =
      view(self)
  }

}
