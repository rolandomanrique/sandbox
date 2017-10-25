package sandbox.triecontacts

import org.scalatest.{FlatSpec, MustMatchers}

class TrieContactsTest extends FlatSpec with MustMatchers {

  behavior of "add and find"
  it must "insert word and increase counters" in {

    val root = Node(Char.MinValue)

    add(root, "caracas".toList)
    add(root, "caraotas".toList)
    add(root, "carro".toList)
    add(root, "delta".toList)
    add(root, "deltamacuro".toList)
    add(root, "delcarajo".toList)
    add(root, "tango".toList)

    print(root)

    find(root, "car".toList) must be(3)
    find(root, "cara".toList) must be(2)
    find(root, "foo".toList) must be(0)
    find(root, "del".toList) must be(3)
    find(root, "delta".toList) must be(2)
    find(root, "tango".toList) must be(1)

  }

}