package incubator


trait OrIPigs {

  implicit def orIPig[
    a: Pig,
    b: Pig
  ]: Pig[OrI.aux[a, b, In1, Any, In1, Any]] =
    Pig {
      s"or.I[${pig[a]}, ${pig[b]}]"
    }
}
