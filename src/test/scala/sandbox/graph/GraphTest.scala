package sandbox.graph

import org.scalatest.{FlatSpec, MustMatchers}

class GraphTest extends FlatSpec with MustMatchers {

  val start = 1001
  val end = 2000000
  val nodes: IndexedSeq[Node[Int]] = (start to end).map(Node[Int])
  val edges: Set[Edge[Int]] = (start to end-2).map { i =>
    Edge(nodes(i - start), nodes(i - start + 2), 1)
  }.toSet ++ Set(
    Edge(nodes(0), nodes(1), 1),                      // regular cost path in first to second node
    Edge(nodes(nodes.size-2), nodes(nodes.size-1), 2) // highest cost path in second to last node
  )

  val bigGraph = Graph(edges)
  bigGraph.outgoing

  val n1: Node[Int] = Node[Int](1)
  val n2: Node[Int] = Node[Int](2)
  val n3: Node[Int] = Node[Int](3)
  val n4: Node[Int] = Node[Int](4)
  val n5: Node[Int] = Node[Int](5)

  behavior of "shortestPath"
  it should "return None when graph has no edges" in {
    val g = Graph[Int](Set(Edge(n3, n1, 1), Edge(n3, n2, 1)))
    shortestPath(g)(n1, n2) must be(None)
  }

  it should "find 1 hop path" in {
    val g = Graph(Set(Edge(n1, n2, 1)))
    shortestPath(g)(n1, n2) must be(Some(Path(List(n1,n2), 1)))
  }

  it should "find 2 hop path" in {
    val g = Graph(Set(Edge(n1, n2, 1),Edge(n1, n3, 1),Edge(n2, n4, 2),Edge(n3, n4, 1)))
    shortestPath(g)(n1, n4) must be(Some(Path(List(n1,n3,n4), 2)))
  }

  it should "find big hop path" in {
    val s = System.currentTimeMillis
    val exp = Path(nodes.zipWithIndex.filter { case (x, i) => i % 2 == 1 || x == nodes.head }.map(_._1).toList, nodes.size/2)
    shortestPath(bigGraph)(nodes.head, nodes.last) must be(Some(exp))
  }

  behavior of "longestPath"
  it should "return None when graph has no edges" in {
    val g = Graph[Int](Set(Edge(n3, n1, 1), Edge(n3, n2, 1)))
    longestPath(g)(n1, n2) must be(None)
  }

  it should "find 1 hop path" in {
    val g = Graph(Set(Edge(n1, n2, 1)))
    longestPath(g)(n1, n2) must be(Some(Path(List(n1,n2), 1)))
  }

  it should "find 2 hop path" in {
    val g = Graph(Set(Edge(n1, n2, 1),Edge(n1, n3, 1),Edge(n2, n4, 2),Edge(n3, n4, 1)))
    longestPath(g)(n1, n4) must be(Some(Path(List(n1,n2,n4), 3)))
  }

  it should "find big hop path" in {
    val exp = Path(nodes.zipWithIndex.filter { case (x, i) => i % 2 == 0 || x == nodes.last }.map(_._1).toList, 1+nodes.size/2)
    longestPath(bigGraph)(nodes.head, nodes.last) must be(Some(exp))
  }

  behavior of "bfs"
  it should "find children with lowest depth" in {
    val g = Graph(Set(Edge(n1, n2, 1),Edge(n2, n3, 1),Edge(n3, n5, 1),Edge(n1, n4, 2),Edge(n4, n5, 2)))
    bfs(g)(n1,n5) must be(Some(Path(List(n1,n4,n5), 4)))
  }

  behavior of "dfs"
  it should "search depth first" in {
    val g = Graph(Set(Edge(n1, n2, 1),Edge(n1, n3, 1),Edge(n3, n4, 1),Edge(n4, n5, 1)))
    dfs(g)(n1,n5) must be(Some(Path(List(n1,n3,n4,n5), 3)))
  }

}