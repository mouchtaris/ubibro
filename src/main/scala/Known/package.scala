package object Known {

  type Known[a] =
    a

  @inline def known[a](implicit a: a): a.type =
    a

}
