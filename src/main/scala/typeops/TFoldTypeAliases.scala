package typeops

trait TFoldTypeAliases extends Any {

  final type fullT[f[_, _], list <: List, out] = tfold[f, list] {
    type Out = out
  }


}
