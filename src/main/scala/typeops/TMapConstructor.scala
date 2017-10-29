package typeops

trait TMapConstructor extends Any {
  this: Any
    with TMapTypeAliases
  ⇒

  private[this] final case object instance extends tmap[Tuple1, Nil]

  @inline final def apply[f[_], list <: List, out <: List](): fullT[f, list, out] =
    instance.asInstanceOf[fullT[f, list, out]]

}
