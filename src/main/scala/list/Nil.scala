package list

sealed trait Nil extends (Nil :: Nil)

case object Nil extends Nil {

  @inline val head: Nil = this

  @inline val tail: Nil = this

}

