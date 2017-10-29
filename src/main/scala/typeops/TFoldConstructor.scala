package typeops

trait TFoldConstructor extends Any {
  this: Any
    with TFoldTypeAliases
  ⇒

  private[this] object instance extends tfold[Tuple2, Nil]

  @inline final def apply[f[_, _], list <: List, out <: f[_, _]](): fullT[f, list, out] =
    instance.asInstanceOf[fullT[f, list, out]]

}
