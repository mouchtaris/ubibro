package fun.list.nil.error

/**
  * Error indicating that Nil's head is being accessed.
  */
final case class HeadAccess() extends RuntimeException("Accessing Head of Nil")
