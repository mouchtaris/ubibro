package fun.list

/**
  * A consecutive list junction.
  *
  * @tparam a type of list Head
  * @tparam b type of list Tail
  */
trait Cons[+Head, +Tail <: List] {

  /**
    * @return the head element of this list
    */
  def head: Head

  /**
    * @return the tail of this list
    */
  val tail: Tail

}