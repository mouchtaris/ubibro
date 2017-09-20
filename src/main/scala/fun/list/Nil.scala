package fun.list

/**
  * Nil is the end of the list.
  *
  * The `head` of Nil is Nothing (will fail), and the `tail` is also Nil.
  */
trait Nil extends (Nothing :: Nil) {

  /**
    * @throws nil.error.HeadAccess always
    */
  final def head: Nothing =
    throw nil.error.HeadAccess()

  /**
    * @return this instance (Nil)
    */
  final val tail: Nil =
    Nil
}

/**
  * The single instance of [[Nil]].
  */
object Nil extends Nil