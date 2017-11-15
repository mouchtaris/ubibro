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

  trait Named[t]
    extends AnyRef
    with Tagverse
    with ImplicitEvidence
  {
    final type BaseType = t
    final type Instance = tagged
  }

  trait int extends Named[Int]

  object x extends int; type x = x.type
  object y extends int; type y = y.type

  type vec2 = (x :: y :: Nil)

  object Bollock
  type Bollock = Bollock.type
  trait NamedConstructionReason
  object NamedConstructionReason {
    implicit def namedConstruction[n <: Named[_], r <: List](implicit n: n)
      : n.BaseType ⇒ n.Instance = n(_: n.BaseType)
  }

  trait NamedConstruct[rec <: List, arg, out] {
    final type Arg = arg
    final type Out = out
    def apply(argument: Arg): Out
  }
  object NamedConstruct {
    implicit def nconstruct[n <: Named[_], r <: List](implicit n: n)
      : NamedConstruct[n :: r, n.BaseType, n.Instance] =
        n(_: n.BaseType)
  }
  case class create[rec <: List]() {
    case class bind[args <: List](args: args) {
      def eval(
        implicit
        ctr: NamedConstruct[rec, _, _]
      ) = {
        ()
      }
    }
  }
  def pt[t: TypeTag] = cprintln(typeinfo[t])
  def run(): Unit = {
    cprintln ( create[vec2]().bind(y(18) :: x(32) :: Nil).eval )
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

