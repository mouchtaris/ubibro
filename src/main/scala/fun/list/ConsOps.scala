package fun.list

import fun.list.{ :: ⇒ cons }

/**
  * Extension methods on a Cons instance.
  */
trait ConsOps[l <: List] extends Any {

  /**
    * Instance on which we operate.
    * @return our self
    */
  protected[this] def self: l

  /**
    * Prepend the given new head `c` to `self`.
    * @param c the new head
    * @tparam c the new head's type
    * @return `c :: self`
    */
  def ::[c](c: c): c :: l = cons(c, self)

}
