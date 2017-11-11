package cross
package reflect

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
  }

  trait SingleTypeMatch {
    def unapply(tpe: Type): Option[(Type, Symbol)]
  }
  val SingleType: SingleTypeMatch

}

object Api extends ApiImpl {

  final implicit class TypeApiDeco(val self: Type)
    extends AnyVal
    with TypeApiImpl

}