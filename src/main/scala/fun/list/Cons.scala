package fun.list

/**
  * A consecutive list junction.
  *
  * @tparam Head type of list Head
  * @tparam Tail type of list Tail
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
