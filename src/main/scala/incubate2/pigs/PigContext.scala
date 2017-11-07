package incubate2
package pigs

trait PigTypes {
  this: Any
  =>

  type pig[t] = pigs.pig[t]

}

trait PigConstructors {
  this: Any
  =>

  final implicit def fromString[t](v: String): pig[t] =
    new pig[t](v)
}

trait PigAccessors {
  this: Any
  =>

  final def pig[t: pig]: pig[t] = implicitly
}

trait PigUnknown {
  this: Any
    with PigConstructors
  =>

  @`inline` final implicit def XXX[xxx]: pig[xxx] =
    s" XXX -- PIG UNKNOWN: -- XXX"
}

trait StandardPigs {
  this: Any
    with PigConstructors
  =>

  @`inline` final implicit val unit: pig[Unit] = "Unit"
  @`inline` final implicit val int: pig[Int] = "Int"
  @`inline` final implicit val str: pig[String] = "String"
  @`inline` final implicit val dbl: pig[Double] = "Double"
  @`inline` final implicit val flt: pig[Float] = "Float"
}

trait PigContext extends AnyRef
  with PigTypes
  with PigConstructors
  with PigAccessors
  with PigUnknown
  with StandardPigs
