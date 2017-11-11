package console

final case class ind(n: Int, sigil: String = "  ") {
  val indent: String = Range inclusive (1, n + 1) map (_ ⇒ sigil) mkString
  def println(o: Any): Unit = { Console.println(indent + o.toString) }
  def next = copy(n = n + 1)
  def withSigil(sig: String) = copy(sigil = sig)
}


