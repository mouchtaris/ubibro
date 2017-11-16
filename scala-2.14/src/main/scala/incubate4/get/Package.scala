package incubate4.get

import
  common.StdImports._,
  list._

trait Package {

  trait Get[list <: List, T] {
    def apply(list: list): T
  }

  object Get {
    implicit def getFromHead[h, t <: List, T](implicit ev: h <:< T)
    : Get[h :: t, T] =
      _.head

    implicit def getFromTail[h, t <: List, T](implicit get: Get[t, T])
    : Get[h :: t, T] =
      list ⇒ get(list.tail)
  }

}
