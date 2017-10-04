package fun
package interpretation

import
  list.{
    List,
    Nil
  }

sealed class Rest[rest <: List]

trait RestLow extends Any {
  @inline implicit def restAny[rest <: List]: Rest[rest] = new Rest[rest]
}

trait RestHigh extends Any with RestLow {
  @inline implicit def restNil: Rest[Nil] = new Rest[Nil]
}

object Rest extends RestHigh
