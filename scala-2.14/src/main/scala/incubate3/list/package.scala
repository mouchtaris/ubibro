package incubate3

package object list extends ListPackage {

  final implicit class ListOpsDeco[self <: List](
    val self: self
  ) extends AnyVal
  {
    def ::[h](h: h): h :: self = Cons(h, self)
  }

}
