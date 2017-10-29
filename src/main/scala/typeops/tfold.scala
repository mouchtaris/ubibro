package typeops

trait tfold[f[_, _], list <: List] {

  type Out

}

object tfold extends AnyRef
  with TFoldTypeAliases
  with TFoldConstructor
  with TFoldInferences

