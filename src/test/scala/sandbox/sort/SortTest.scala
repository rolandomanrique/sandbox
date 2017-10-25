package sandbox


import org.scalatest.{FlatSpec, MustMatchers}

class SortTest extends FlatSpec with MustMatchers {

  import sort._

  behavior of "quick sort"
  it should "not fail with empty array" in {
    quick(Array.empty) must be(Array.empty)
  }

  it should "sort a array of one element" in {
    quick(Array(4)) must be(Array(4))
  }

  it should "sort an array" in {
    quick(Array(4,2,5,6,8,1,9,7,3)) must be(Array(1,2,3,4,5,6,7,8,9))
  }

  it should "sort an array with duplicates" in {
    quick(Array(4,2,5,7,6,3,8,1,9,7,3,3)) must be(Array(1,2,3,3,3,4,5,6,7,7,8,9))
  }

}