package typeops

trait TMapTypeAliases extends Any {

  final type fullT[f[_], list <: List, out <: List] = tmap[f, list] {
    type Out = out
  }

}
