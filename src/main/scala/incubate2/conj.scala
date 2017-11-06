package incubate2

import Known._

object Conj {
  pkg =>

  trait &&[A, B] {
    val a: A
    val b: B
    final override def toString: String = s"$a && $b"
  }

  case object && {

    @`inline` implicit def `⇒ a && b`[a: Known, b: Known]: a && b =
      new &&[a, b] {
        val a: a = implicitly[a]
        val b: b = implicitly[b]
      }

  }

}
