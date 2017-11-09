package incubate2
package types

sealed trait Conforms[b] extends Any {

  type t[a] = a <:< b

}
