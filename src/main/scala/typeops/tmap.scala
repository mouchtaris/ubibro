package typeops

trait tmap[f[_], list <: List] {

  type Out <: List

}

object tmap extends AnyRef
  with TMapInferences
  with TMapConstructor
  with TMapTypeAliases
