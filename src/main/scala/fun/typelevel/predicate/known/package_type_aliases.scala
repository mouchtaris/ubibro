package fun.typelevel.predicate
package known

/**
  * Type aliases provided by [[KnownCompanion]] to package [[predicate]].
  */
trait package_type_aliases {

  /**
    * Indicate that type "a" is known.
    *
    * That is, there is an implicit resolution of it.
    *
    * {{{
    *   implicit object Drogon
    *   Known[Drogon]
    * }}}
    *
    * `Known` can also be used instead of `implicitly`, because it
    * retains type refinements on the return type. The downside and restriction
    * that comes from this is that only [[scala.AnyRef]] types can be used with `Known`.
    *
    * @tparam a the predicate that is known
    */
  final type Known[a] = a
}
