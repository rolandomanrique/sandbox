package sandbox


import org.scalatest.{FlatSpec, MustMatchers}

class CommonSubstringTest extends FlatSpec with MustMatchers {

  behavior of "commonSubstring"
  it should "return empty string if list is empty" in {
    strings.commonSubstrings(Set.empty) must be("")
  }

  it should "return empty string if no common substring exists" in {
    strings.commonSubstrings(Set("asd", "dsa", "ads")) must be("")
  }

  it should "return entire word when ther is only one word in set" in {
    strings.commonSubstrings(Set("cat")) must be("cat")
  }

  it should "return longest substring when smallest word is substring of all other words" in {
    strings.commonSubstrings(Set("cat", "category", "caterpillar")) must be("cat")
  }

  it should "return longest substring when available" in {
    strings.commonSubstrings(Set("catalysis", "category", "caterpillar")) must be("cat")
  }

}