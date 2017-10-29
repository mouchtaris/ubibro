package object trefine {

  sealed trait Gen extends Any { type t[T] <: T }

  sealed trait Out[out] extends Any with Gen {
    final type t[T] = T { type Out = out }
  }

  sealed trait In[in] extends Any with Gen {
    final type t[T] = T { type In = in }
  }

  sealed trait In1[in[_]] extends Any with Gen {
    final type t[T] = T { type In[a] = in[a] }
  }

  sealed trait Compose[A[T] <: T, B[T] <: T] extends Any with Gen {
    final type t[T] = A[B[T]]
  }

}
