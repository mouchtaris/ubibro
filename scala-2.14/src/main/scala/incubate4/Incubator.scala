package incubate4

import
  common.StdImports._,
  Console.{ println ⇒ cprintln },
  list._

object Incubator
  extends AnyRef
  with java.lang.Runnable
{

  trait &&[a, b]
  object && {
    private[this] object instance extends (Nothing && Nothing)
    def apply[a, b]() = instance.asInstanceOf[a && b]
    implicit def `a && b`[a: Implicit, b: Implicit]: a && b = apply()
  }

  trait ||[a, b]
  trait `||Constructor` {
    private[this] object instance extends (Nothing || Nothing)
    final def apply[a, b]() = instance.asInstanceOf[a || b]
  }
  trait `||DeductionsLow` extends Any {
    this: `||Constructor` ⇒
    final implicit def `|| b`[a, b: Implicit]: a || b = apply()
  }
  trait `||DeductionsHigh` extends Any with `||DeductionsLow` {
    this: `||Constructor` ⇒
    final implicit def `a ||`[a: Implicit, b]: a || b = apply()
  }
  object ||
    extends AnyRef
    with `||Constructor`
    with `||DeductionsHigh`

  trait ImplicitEvidence {
    final implicit def implicitEvidence: this.type = this
  }

  final implicit class RecordFields[fields <: List](
    val fields: fields
  ) extends AnyVal {
    type Out = fields
  }
  object RecordFields {
    implicit def nilFields: RecordFields[Nil] = Nil
    implicit def listFields[
      h <: Named[_],
      t <: List
    ](
      implicit dummyImplicit: DummyImplicit,
      h: h,
      t: RecordFields[t]
    ): RecordFields[h :: t] = h :: t.fields
  }

  final implicit class AreRecordValues[fields <: List, list <: List](
    val self: Unit
  ) extends AnyVal
  object AreRecordValues {
    implicit def nilAreRecordValues: AreRecordValues[Nil, Nil] = ()
    implicit def listAreRecordValues[
      fh <: Type,
      ft <: List,
      h,
      t <: List
    ](
      implicit
      fh: fh,
      ev: R
    ): AreRecordValues[fh :: ft, h :: t] = ()
  }

  trait Type extends AnyRef { type V }
  abstract class Named[typ <: Type](
    implicit
    val Type: typ
  )
    extends AnyRef
    with ImplicitEvidence
    with tag.Tagverse
  {
    final type Type = typ
    final type T = Type.V
  }
  abstract class Record[fields <: List](
    implicit
    rfields: RecordFields[fields],
  )
    extends AnyRef
    with ImplicitEvidence
  {
    final type Fields = fields
    val fields = rfields.fields

    final type AreRecordValues[list <: List] = Incubator.AreRecordValues[fields, list]
    def apply[list <: List: AreRecordValues](list: list) = ???
  }
  //trait Extend {
  //  type Record <: Incubator.Record
  //  type Fields <: List
  //}

  implicit case object int
    extends AnyRef
    with Type { type V = Int }
  final type int = int.type

  object x extends Named[int]
  type x = x.type
  object y extends Named[int]
  type y = y.type
  object z extends Named[int]
  type z = z.type
  object vec2 extends Record[x :: y :: Nil]
  type vec2 = vec2.type

  val x1 = x(12)
  val y1 = y(18)
  val z1 = z(32)
//  val vec = vec2(x1 :: y1 :: Nil)

  import TypeInfo._
  val rts = known[RecordTypes[vec2.Fields]]
  cprintln(typeinfo[rts.Out])
  implicitly[rts.Out =:= vec2.rtypes.Out]
  cprintln(TypeInfo.typeinfo[vec2.rtypes.Out])
  //implicit case object vec3 extends Extend {
  //  type Record = vec2
  //  type List = z :: Nil
  //}

  def run(): Unit = cprintln(1)

}


object junk {
}
