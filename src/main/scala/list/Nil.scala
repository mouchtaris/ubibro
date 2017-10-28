package list

sealed trait Nil extends (Nil :: Nil)

case object Nil extends Nil {

  val head: Nil = this

  val tail: Nil = this

}

