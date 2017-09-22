package fun
package list_predicate

import list._
import typelevel.predicate.{Known, and}
import and.And

import scala.annotation.implicitNotFound

/**
  * Evidence which signifies that instances of type `predicate[_]` are
  * implicitly known (available) for all types in the given `list`.
  * @tparam list a list
  * @tparam predicate a predicate type constructor
  */
@implicitNotFound("No evidence that ${predicate} holds for all in ${list}")
trait ForAll[list <: List, predicate[_]]

/**
  * Provides constructors and implicit providers for evidence of type
  * [[ForAll]].
  */
object ForAll {

  /**
    * The sole instance of [[ForAll]], since it's a type-level evidence type.
    */
  private[this] object instance extends ForAll[Nil, Tuple1]

  /**
    * Construct an instance of [[ForAll]] with the given types.
    * @tparam list the list type
    * @tparam predicate the predicate type constructor
    * @return a new evidence instance
    */
  @inline def apply[list <: List, predicate[_]](): ForAll[list, predicate] =
    instance.asInstanceOf[ForAll[list, predicate]]

  /**
    * There is implicit evidence that a predicate is true for all types
    * if there is implicit evidence for the conjunction of the predicate
    * for all types in the list.
    *
    * {{{
    *   def forAll = list.map(predicate).fold(_ and _)
    * }}}
    * @tparam list list type under question
    * @tparam predicate predicate which should hold for all list types
    * @tparam predicateList list of types to which predicate is applied
    * @tparam trueForAll conjunction of all predicate types
    * @return evidence for [[ForAll]]
    */
  @inline implicit def listForAll[
    list <: List,
    predicate[_],
    predicateList <: List: ListMap[predicate, list]#Result,
    trueForAll: ListFold[And, predicateList]#Result: Known
  ]: ForAll[list, predicate] =
    apply()

}