package fun.typelevel.predicate.or

/**
  * Provide all implicit resolutions of `Or` evidence, with the
  * appropriate ordering (through inheritance).
  */
trait OrEntailments extends OrEntailmentThroughA
