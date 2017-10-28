import scala.annotation.implicitNotFound

trait Known {

  type Known[a] =
    a

  @inline def known[a](implicit a: a): a.type =
    a

}

trait interpretation {

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

trait disjunction {
  this: Known ⇒

  abstract class ||[a, b] {
    type Out <: a | b
    val evidence: Out
  }

  object || {

    implicit def orA[a: Known, b]: a || b = new ||[a, b] {
      type Out = a
      val evidence: a = known
    }

    implicit def orB[a, b: Known]: a || b = new ||[a, b] {
      type Out = b
      val evidence: b = known
    }

  }

}

trait conjunction {
  this: Known ⇒

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

}

trait stdinterp {
  this: Any
    with Known
    with interpretation
    with disjunction
  ⇒
  import stdinterp._

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

trait orinterp {
  this: Any
    with Known
    with interpretation
    with stdinterp
    with disjunction
    with conjunction
  ⇒

  implicit case object True
  type True = True.type

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

trait istype {
  this: Any
    with interpretation
    with Known
  ⇒

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

trait incubate {
  this: Any
    with Known
    with interpretation
    with stdinterp
    with disjunction
    with conjunction
  ⇒
}

object incu {

  final implicit class Tappin[a](val self: a) extends AnyVal {
    def tap[b](f: a ⇒ b): b = f(self)

    def stap(f: a ⇒ Unit): a = {
      f(self);
      self
    }
  }

}

trait probe

object Main extends AnyRef
  with Known
  with interpretation
  with stdinterp
  with disjunction
  with conjunction
  with incubate
  with probe
  with orinterp
  with istype
{
  import incu._
  import stdinterp._

  trait ist[B] { type t[a] = IsType[a, B] }
  implicit def ist31[a, b, c, d](implicit is: ist[a]#t[b]): (b, c, d) <<: a =
    IsType(t ⇒ is(t._1))
  implicit def ist32[a, b, c, d](implicit is: ist[a]#t[c]): (b, c, d) <<: a =
    IsType(t ⇒ is(t._2))
  implicit def ist33[a, b, c, d](implicit is: ist[a]#t[d]): (b, c, d) <<: a =
    IsType(t ⇒ is(t._3))


  final case class get[in](in: in) {
    def f[t](implicit ev: in <<: t): t = ev(in)
  }
  trait Typ[t]
  import java.net.{ URI ⇒ Uri }
  def uri(s: String): Uri = Uri create s
  final implicit case object TInt     extends Typ { type Out = Int }
  final implicit case object TString  extends Typ { type Out = String }
  final implicit case object TUri     extends Typ { type Out = Uri }

  def main(args: Array[String]): Unit = {
    println("Hello world!")
    println(msg)
    val t = (18, "Fuck off", uri("http://www.fuckoff.com"))
    t stap println
    println { get(t).f[Int](ist31[Int, Int, String, Uri](IsType.isType[Int, Int])) }
  }

  def msg = "I was compiled by dotty :)"

}
