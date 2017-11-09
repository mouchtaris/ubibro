package incubate2
package types


sealed trait IsType[b] extends Any {

  type t[a] = a =:= b

}
