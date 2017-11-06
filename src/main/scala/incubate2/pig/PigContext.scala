package incubate2
package pig

trait PigDefinitions {
  final class pig[t](
    override val toString: String
  )
}

trait PigConstructors {
  this: Any
    with PigDefinitions
  =>

  @`inline` final implicit def fromString[t](v: String): pig[t] =
    new pig[t](v)
}

trait PigAccessors {
  this: Any
    with PigDefinitions
  =>

  @`inline` final def pig[t: pig]: pig[t] = implicitly
}

trait PigUnknown {
  this: Any
    with PigDefinitions
    with PigConstructors
  =>

  @`inline` final implicit def XXX[xxx]: pig[xxx] =
    s" XXX -- PIG UNKNOWN: -- XXX"
}

trait StandardPigs {
  this: Any
    with PigDefinitions
    with PigConstructors
  =>

  @`inline` final implicit val unit: pig[Unit] = "Unit"
  @`inline` final implicit val int: pig[Int] = "Int"
  @`inline` final implicit val str: pig[String] = "String"
  @`inline` final implicit val dbl: pig[Double] = "Double"
  @`inline` final implicit val flt: pig[Float] = "Float"
}

trait PigContext extends AnyRef
  with PigDefinitions
  with PigConstructors
  with PigAccessors
  with PigUnknown
  with StandardPigs
