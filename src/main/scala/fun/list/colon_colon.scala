package fun.list

//
// This file cannot be named "::" for Microsoft Windows reasons.
//
/**
  * Package extension trait.
  */
trait colon_colon {

  /**
    * This object acts as a constructor and deconstructor of lists.
    */
  object :: {

    /**
      * A concrete implementation of Cons.
      * @param head list's head
      * @param tail list's tail
      * @tparam a type of list Head
      * @tparam b type of list Tail
      */
    private[this] final class impl[a, b <: List](val head: a, val tail: b) extends Cons[a, b]

    /**
      * Create a Cons instance with the given head and tail.
      * @param head list's head
      * @param tail list's tail
      * @tparam a list's head type
      * @tparam b list's tail type
      * @return a new `head :: tail` Cons instance
      */
    def apply[a, b <: List](head: a, tail: b): Cons[a, b] =
      new impl(head, tail)

  }

}
