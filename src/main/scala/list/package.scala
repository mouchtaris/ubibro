package object list {

  type List = _ :: _

  final implicit class ListDeco[l <: List](val self: l) extends AnyVal with ListOps[l]


}
