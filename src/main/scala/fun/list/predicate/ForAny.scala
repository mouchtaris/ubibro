package fun
package list
package predicate

import
  typelevel.predicate._
import
  scala.annotation.implicitNotFound

/**
  * Evidence which signifies that instances of type `predicate[_]` are
  * implicitly known (available) for any type in the given `list`.
  * @tparam list a list
  * @tparam predicate a predicate type constructor
  */
@implicitNotFound("No evidence that ${predicate} holds for any of ${list}")
trait ForAny[list <: List, predicate[_]]

/**
  * Provides constructors and implicit providers for evidence of type
  * [[ForAny]].
  */
object ForAny {

  /**
    * The sole instance of [[ForAny]], since it's a a type-level evidence type.
    */
  private[this] object instance extends ForAny[Nil, Tuple1]

  /**
    * A convenience type alias for a conceptual `ForAny[Any, Any]`.
    */
  type any = ForAny[list, predicate] forSome {
    type list <: List
    type predicate[_]
  }

  /**
    * Construct an instance of [[ForAny]] with the given types.
    * @tparam list the list type
    * @tparam predicate the predicate type constructor
    * @return a new evidence instance
    */
  @inline def apply[list <: List, predicate[_]](): ForAny[list, predicate] =
    instance.asInstanceOf[ForAny[list, predicate]]

  /**
    * There is implicit evidence that a predicate is true for any type
    * in the list if there is implicit evidence for the disjunction of the
    * predicate for all types in the list.
    *
    * {{{
    *   def forAny = list.map(predicate).fold(_ or _)
    * }}}
    * @tparam list list type under question
    * @tparam predicate predicate which should hold for any type in the list
    * @tparam predicateList list of types to which the predicate is applied
    * @tparam trueForAny disjunction of all predicate types
    * @return evidence for [[ForAny]]
    */
  @inline implicit def listForAny[
    list <: List,
    predicate[_],
    predicateList <: List: ListMap[predicate, list]#Result,
    trueForAny: ListFold[Or, predicateList]#Result: Known
  ]: ForAny[list, predicate] =
    apply()

}
