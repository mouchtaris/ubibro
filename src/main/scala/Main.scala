import scala.annotation.implicitNotFound
import java.net.{URI ⇒ Uri}

object Known {

  type Known[a] =
    a

  @inline def known[a](implicit a: a): a.type =
    a

}

object interpretation {

  trait Interpretation[T] {
    type In[_]
    type Out
    implicit def evidence: T
    def apply[a: In](a: a): Out
  }

  object Interpretation {

    type fullT[t, in[_], out] = Interpretation[t] {
      type In[a] = in[a]
      type Out = out
    }

  }

}

object disjunction {
  import Known._

  trait ||[a, b] {
    type Out <: a | b
    val evidence: Out
  }

  object || {

    case class ||[a, b, out <: a | b](
      evidence: out
    ) extends disjunction.||[a, b] {
      type Out = out
    }

    implicit def orA[a, b](implicit a: a): ||[a, b, a.type] = ||(a)

    implicit def orB[a, b](implicit b: b): ||[a, b, b.type] = ||(b)

  }

}

object conjunction {
  import Known._

  abstract case class &&[a, b]()(
    implicit
    val a: a,
    val b: b
  )

  object && {

    implicit def and[a: Known, b: Known]: a && b =
      new &&[a, b]() { }

  }

  trait AndDeductions {

    @inline final implicit def aFromABConjunction[a, b](implicit and: a && b): a = and.a

    @inline final implicit def bFromABConjunction[a, b](implicit and: a && b): b = and.b

  }

}

object t {

  sealed trait Gen { type t[T] <: T }

  sealed trait Out[out] extends Gen {
    final type t[T] = T { type Out = out }
  }

  sealed trait In[in] extends Gen {
    final type t[T] = T { type In = in }
  }

  sealed trait In1[in[_]] extends Gen {
    final type t[T] = T { type In[a] = in[a] }
  }

  sealed trait Compose[A[T] <: T, B[T] <: T] extends Gen {
    final type t[T] = A[B[T]]
  }

}

object stdinterp {

  final implicit class Name[t](val name: String) extends AnyVal {
    @inline override def toString: String = name
  }

  object Name {
    final implicit val Int: Name[Int] = "Int"

    final implicit val String: Name[String] = "String"
  }

  import
    Known._,
    interpretation._,
    disjunction._,
    stdinterp._

  final case class StdInterpretation[T: Name]() extends Interpretation[T] {
    type In[a] = a <:< T

    type Out = T

    @inline implicit lazy val evidence: T = ???

    @inline def apply[a: In](a: a): Out = a

    override def toString: String = s"StdInterpretation[${known[Name[T]].name}]"
  }

  implicit def stdInterpretation[T: Name](
    implicit
    ev: (T <:< Int) || (T <:< String)
  ): StdInterpretation[T] =
    StdInterpretation()

}

object orinterp {
  import
    Known._,
    interpretation._,
    stdinterp._,
    disjunction._,
    conjunction._

  final case class OrInterpretation[
    ca, cb,
    ia <: Interpretation[ca],
    ib <: Interpretation[cb],
    out
  ](
    interpa: ia,
    interpb: ib
  )(
    decide: (⇒ interpa.Out) ⇒ (⇒ interpb.Out) ⇒ out
  )(
    implicit
    @inline val evidence: ca || cb
  ) extends Interpretation[ca || cb] {
    type In[a] = interpa.In[a] && interpb.In[a]
    type Out = out
    def apply[a](a: a)(implicit ev: In[a]): Out =
      decide { interpa(a)(ev.a) } { interpb(a)(ev.b) }
  }

  implicit def orInterpretationA[ca, cb](
    implicit
    interpa: Interpretation[ca],
    interpb: Interpretation[cb],
    ev: ca
  ): OrInterpretation[ca, cb, interpa.type, interpb.type, interpa.Out] =
    OrInterpretation(interpa, interpb) { a ⇒ _ ⇒ a }

  implicit def orInterpretationB[ca, cb](
    implicit
    interpa: Interpretation[ca],
    interpb: Interpretation[cb],
    ev: cb
  ): OrInterpretation[ca, cb, interpa.type, interpb.type, interpb.Out] =
    OrInterpretation(interpa, interpb) { _ ⇒ b ⇒ b }

}

object istype {
  import
    interpretation._,
    Known._

  @implicitNotFound("Cannot prove ${A} <<: ${B}")
  final case class IsType[A, B](
    a2b: A ⇒ B
  ) extends Interpretation[IsType[A, B]] {

    @inline val evidence: IsType[A, B] = this

    type In[a] = a <:< A

    type Out = B

    @inline def apply[a](a: a)(implicit a2a: a <:< A): B = a2b(a2a(a))

  }

  type <<:[a, b] = a `IsType` b

  object IsType {

    @inline implicit def isType[a, b](implicit ev: a =:= b): IsType[a, b] =
      IsType(ev)

  }

}

object list {

  type List = _ :: _

  trait ::[head, tail <: List] extends Any {
    val head: head
    val tail: tail
  }

  private[list] final case class Cons[head, tail <: List](
    head: head,
    tail: tail
  ) extends (head :: tail) {

    @inline override lazy val toString: String =
      s"$head :: $tail"

  }

  sealed trait Nil extends (Nil :: Nil)

  final implicit case object Nil extends Nil {
    val head: Nil = this
    val tail: Nil = this
  }

  final implicit class ListOps[l <: List](val self: l) extends AnyVal {
    def ::[a](a: a): a :: l = Cons(a, self)
  }

}

object Pig extends AnyRef
{
  import
    list._,
    istype._,
    Known._,
    interpretation._,
    disjunction._

  final implicit class pig[t](@inline override val toString: String) extends AnyVal

  trait VeryLowPriorityPig {
    implicit val any: pig[Any] = " PIG XXX "
  }

  object pig extends VeryLowPriorityPig {
    implicit val int: pig[Int] = "Int"
    implicit val string: pig[String] = "String"
    implicit val uri: pig[Uri] = "Uri"
    implicit val unit: pig[Unit] = "Unit"
    implicit val nil: pig[Nil] = "Nil"

    def apply[t: pig]: pig[t] = implicitly

    implicit def list[h: pig, t <: List: pig]: pig[h :: t] = s"${pig[h]} :: ${pig[t]}"
    implicit def istype[a: pig, b: pig]: pig[IsType[a, b]] = s"${pig[a]} <<: ${pig[b]}"
    implicit def disjunction[a: pig, b: pig]: pig[a || b] = s"${pig[a]} || ${pig[b]}"
  }

}

object typeops extends Any {
  import list._

  trait tmap[f[_], list <: List] { type Out <: List }

  object tmap {

    type fullT[f[_], list <: List, out <: List] = tmap[f, list] {
      type Out = out
    }

    private[this] final case object instance extends tmap[Tuple1, Nil]

    @inline def apply[f[_], list <: List, out <: List](): fullT[f, list, out] =
      instance.asInstanceOf[fullT[f, list, out]]

    @inline implicit def nil[f[_]]: fullT[f, Nil, Nil] =
      tmap()

    @inline implicit def list[f[_], h, t <: List](implicit t: tmap[f, t]): fullT[f, h :: t, f[h] :: t.Out] =
      tmap()

  }

  trait tfold[f[_, _], list <: List] { type Out }

  object tfold {

    type fullT[f[_, _], list <: List, out] = tfold[f, list] {
      type Out = out
    }

    private[this] object instance extends tfold[Tuple2, Nil]

    @inline def apply[f[_, _], list <: List, out <: f[_, _]](): fullT[f, list, out] =
      instance.asInstanceOf[fullT[f, list, out]]

    @inline implicit def list2[f[_, _], a, b]: fullT[f, a :: b :: Nil, f[a, b]] =
      tfold[f, a :: b :: Nil, f[a, b]]()

    @inline implicit def list[f[_, _], h, t <: List](implicit tf: tfold[f, t]): fullT[f, h :: t, f[h, tf.Out]] =
      tfold[f, h :: t, f[h, tf.Out]]()

  }
}

object incubate {

  final implicit class Tappin[a](val self: a) extends AnyVal {
    def tap[b](f: a ⇒ b): b = f(self)

    def stap(f: a ⇒ Unit): a = {
      f(self);
      self
    }

    def sp: a = this stap println
  }

  import
    istype._,
    Known._,
    interpretation._

  final case class get[in](in: in) {
    def f[t](implicit ev: in <<: t): t = ev(in)
  }

  trait Typ[t]

  def uri(s: String): Uri = Uri create s

  final implicit case object TInt extends Typ[Int]

  final implicit case object TString extends Typ[String]

  final implicit case object TUri extends Typ[Uri]
}

object probe

object Main {
  import
    istype._,
    list._,
    typeops._,
    Known._,
    Pig._,
    incubate._,
    disjunction._

  def main(args: Array[String]): Unit = {
    println("Hello world!")
    println(msg)
    type isint[t] = IsType[t, Int]
    type l0 = String :: Uri :: Int :: Unit :: Nil
    val lm = known[tmap[isint, l0]]
    val lf = known[tfold[||, lm.Out]]
    println("****************************")
    print  ("* "); pig[lm.Out].sp
    print  ("* "); pig[lf.Out].sp
    println("****************************")
    known[Int <<: Int].sp
    known[Int <<: Int || Uri <<: Int].sp
    known[String <<: Int || Int <<: Int || Uri <<: Int].sp
  }

  def msg = "I was compiled by dotty :)"

}
