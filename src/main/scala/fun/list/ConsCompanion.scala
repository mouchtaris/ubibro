package fun.list

/**
  * Provide constructors and deconstructors of lists.
  */
trait ConsCompanion {

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
  @inline def apply[a, b <: List](head: a, tail: b): Cons[a, b] =
    new impl(head, tail)

  /**
    * Typical deconstruct for lists:
    * {{{
    *   def tailTail[a, b, t <: List](list: a :: b :: tail): t =
    *     list match {
    *       case _ :: _ :: tail ⇒ tail
    *     }
    * }}}
    * @param list a list
    * @tparam h list head type
    * @tparam t list tail type
    * @return always a pair of `(head, tail)`
    */
  def unapply[h, t <: List](list: h :: t): Option[(h, t)] =
    Some {
      (list.head, list.tail)
    }

}
