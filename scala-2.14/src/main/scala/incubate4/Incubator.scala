package incubate4

import
  common.StdImports._,
  Console.{ println ⇒ cprintln },
  list._,
  TypeInfo._,
  done._,
  tag._,
  cross.reflect.Api.{ TypeTag }

object Incubator
  extends AnyRef
    with java.lang.Runnable
{
  final implicit class ForReason[reason, evidence](val evidence: evidence) extends AnyVal {
    type Evidence = evidence
    type Reason = reason
  }
  trait ImplicitEvidence {
    final implicit def implicitEvidence: this.type = this
  }
  final implicit case object True
  final type True = True.type
  final type True1[_] = True

  trait Type {
    type Instance
  }
  trait Named[t]
    extends AnyRef
    with Type
    with Tagverse
    with ImplicitEvidence
  {
    final type BaseType = t
    final type Instance = tagged
  }

  trait Record[rec <: List]
    extends AnyRef
    with RecordEvidence
  {
    final type Record = rec
  }

  trait Extension[rec <: Record[_], ext <: List]
    extends AnyRef
  {

  }

  trait RecordEvidence
    extends AnyRef
  {
    this: Record[_] ⇒
    trait Evidence[args <: List]
  }

  trait int extends Named[Int]

  object x extends int; type x = x.type
  object y extends int; type y = y.type
  object z extends int; type z = z.type
  object vec2 extends Record[x :: y :: Nil]
  type vec2 = vec2.type
  object vec3 extends Extension[vec2, z :: Nil]

  trait Contains[list <: List, T] {
    def apply(list: list): T
  }
  object Contains {
    implicit def fromHead[h, t <: List, T](implicit ev: h <:< T): Contains[h :: t, T] =
      list ⇒ ev(list.head)
    implicit def fromTail[h, t <: List, T](implicit ev: Contains[t, T]): Contains[h :: t, T] =
      list ⇒ ev(list.tail)
  }
  def get[t <: Type, args <: List](typ: t)(args: args)(
    implicit
    ev: Contains[args, typ.Instance]
  ): typ.Instance =
    ev(args)

  def pt[t: TypeTag] = cprintln(typeinfo[t])
  def run(): Unit = {
    val v = x(12) :: y(13) :: Nil
    cprintln { get(x)(v) }
    cprintln { get(y)(v) }
  }
}

object done {

  final case class &&[a, b](a: a, b: b)
  object && {
    implicit def `a && b`[a: Implicit, b: Implicit]: a && b = &&(implicitly, implicitly)
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

}

object junk {
}

object junk000
  extends AnyRef
    with java.lang.Runnable
{
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
    final type BaseType = Type.V
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

  //implicit case object vec3 extends Extend {
  //  type Record = vec2
  //  type List = z :: Nil
  //}

  def run(): Unit = cprintln(1)

}

