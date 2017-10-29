package typeops

trait TMapInferences extends Any {
  this: Any
    with TMapTypeAliases
    with TMapConstructor
  ⇒

  @inline final implicit def nil[f[_]]: fullT[f, Nil, Nil] =
    apply()

  @inline final implicit def list[f[_], h, t <: List](implicit t: tmap[f, t]): fullT[f, h :: t, f[h] :: t.Out] =
    apply()

}
