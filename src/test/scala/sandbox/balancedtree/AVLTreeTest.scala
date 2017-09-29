package sandbox.avltree

// C++ implementation http://www.geeksforgeeks.org/avl-tree-set-1-insertion/
import org.scalatest.{FlatSpec, MustMatchers}

class AVLTreeTest extends FlatSpec with MustMatchers {


  behavior of "insert"
  it should "balance tree on insert" in {

    val in =
      Node(3,
        Node(2),
        Node(4, null, Node(5), 1),
        2
      )

    val exp =
      Node(3,
        Node(2),
        Node(5, Node(4), Node(6), 1),
        2
      )

    insert(in, 6) must be(exp)

  }

  it should "handle Right Right re-balancing case" in {
    val n1 = insert(null, 5)
    n1 must be(Node(5))

    val n2 = insert(n1, 6)
    n2 must be (Node(5, null, Node(6), 1))

    val n3 = insert(n2, 2)
    n3 must be(Node(5, Node(2), Node(6), 1))

    val n4 = insert(n3, 3)
    n4 must be(Node(5, Node(2, null, Node(3), 1), Node(6), 2))

    val n5 = insert(n4, 4)
    n5 must be(Node(5, Node(3, Node(2), Node(4), 1), Node(6), 2))
  }

  it should "handle Left Left re-balancing case" in {
    val n1 = insert(null, 2)
    n1 must be(Node(2))

    val n2 = insert(n1, 1)
    n2 must be (Node(2, Node(1), null, 1))

    val n3 = insert(n2, 5)
    n3 must be(Node(2, Node(1), Node(5), 1))

    val n4 = insert(n3, 4)
    n4 must be(Node(2, Node(1), Node(5, Node(4), null, 1), 2))

    val n5 = insert(n4, 3)
    n5 must be(Node(2, Node(1), Node(4, Node(3), Node(5), 1), 2))
  }

  it should "handle Left Right re-balancing case" in {
    val n1 = insert(null, 5)
    n1 must be(Node(5))

    val n2 = insert(n1, 6)
    n2 must be (Node(5, null, Node(6), 1))

    val n3 = insert(n2, 4)
    n3 must be(Node(5, Node(4), Node(6), 1))

    val n4 = insert(n3, 2)
    n4 must be(Node(5, Node(4, Node(2), null, 1), Node(6), 2))

    val n5 = insert(n4, 3)
    n5 must be(Node(5, Node(3, Node(2), Node(4), 1), Node(6), 2))
  }

  it should "handle Right Left re-balancing case" in {
    val n1 = insert(null, 5)
    n1 must be(Node(5))

    val n2 = insert(n1, 6)
    n2 must be (Node(5, null, Node(6), 1))

    val n3 = insert(n2, 2)
    n3 must be(Node(5, Node(2), Node(6), 1))

    val n4 = insert(n3, 3)
    n4 must be(Node(5, Node(2, null, Node(3), 1), Node(6), 2))

    val n5 = insert(n4, 1)
    n5 must be(Node(5, Node(2, Node(1), Node(3), 1), Node(6), 2))
  }

  behavior of "RR"
  it should "rotate right" in {
    val in = Node(6,
      Node(4,
        Node(2, Node(1), Node(3), 1),
        Node(5),
        2
      ),
      Node(7),
      3
    )

    val out = Node(4,
      Node(2, Node(1), Node(3), 1),
      Node(6, Node(5), Node(7), 1),
      2
    )

    RR(in) must be(out)
  }

  behavior of "LR"
  it should "rotate left" in {
    val in = Node(2,
      Node(1),
      Node(4,
        Node(3),
        Node(5, Node(6), Node(7), 1),
        2
      ),
      3
    )

    val out = Node(4,
      Node(2, Node(1), Node(3), 1),
      Node(6, Node(5), Node(7), 1),
      2
    )

    LR(in) must be(out)

  }

}