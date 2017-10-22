package incubator

case class Rest[t <: List]()

object Rest {

  implicit val nil: Rest[Nil] =
    Rest()

  implicit def other[t <: List]: Rest[t] =
    Rest()

}

