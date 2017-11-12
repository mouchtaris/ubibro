package cross
package reflect

import
  common.StdImports._

trait Api {

  type TypeTag[T]
  type Type
  type Symbol

  def typeOf[T: TypeTag]: Type

  trait TypeApi extends Any {
    def self: Type
    def dealias: Type
    def baseClasses: uList[Symbol]
    def baseType(s: Symbol): Type
    def typeArgs: uList[Type]
    def isSingleType: Boolean
  }

}

object Api extends ApiImpl {

  final implicit class TypeApiDeco(val self: Type)
    extends AnyVal
    with TypeApiImpl

}