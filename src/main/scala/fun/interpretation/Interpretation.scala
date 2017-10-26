package fun
package interpretation

trait Interpretation[t] {

  type In[_]

  type Out

  def apply[a: In](a: a): Out

}

