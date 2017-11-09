package incubate2

case object Known {

  type Known[T] = T

  def known[t <: AnyRef](implicit t: t): t.type = t

}
