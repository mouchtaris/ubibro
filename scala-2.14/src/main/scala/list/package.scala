package object list
  extends scala.AnyRef
  with common.StdImports
  with ListPackage
{

  final implicit class ListOpsDeco[self <: List](
    val self: self
  ) extends AnyVal
  {
    def ::[h](h: h): h :: self = Cons(h, self)
  }

}
