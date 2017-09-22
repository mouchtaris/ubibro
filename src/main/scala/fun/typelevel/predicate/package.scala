package fun.typelevel

/**
  * The [[predicate]] package provides helpful types, which makes
  * simple propositional reasoning possible in implicit parameter resolution.
  *
  * Fast-forward:
  * {{{
  *   def onlyIfAll[a, b](implicit both: And[a, b]): All[a, b] = ??? // TODO test
  *
  *   def justOneWillDo[a, b](implicit either: Or[a, b]): IDontKnowWhich[a, b] = ??? // TODO test
  *
  *   def itIsKnown[a <: AnyRef: Known]: a = Known[a]
  * }}}
  *
  * The predicate package become interesting through its extensions, provided
  * by the [[list_predicate]] package, which depends on lists.
  */
package object predicate extends AnyRef
  with known.package_type_aliases {

  object Known extends known.KnownCompanion

}