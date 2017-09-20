package fun

package object list extends AnyRef
  with package_type_aliases
  with colon_colon {

  //
  // Implicit decorations
  //

  /**
    *  Implicitly decorate a list with {@link ConsOps}
    * @param self the list instance to operate on
    * @tparam l the list's type
    */
  implicit class ConsOpsDeco[l <: List](val self: l) extends AnyVal with ConsOps[l]

}
