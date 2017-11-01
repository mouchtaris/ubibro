object types {

  trait Result[limit] { 
    type Out <: limit
  }

  type resultOf[T <: Result[limit], limit] =
    [out <: limit] => T {
      type Out = out
    }

  type fullT[T <: Result[limit], limit, out <: limit] = resultOf[T, limit][out]

}
