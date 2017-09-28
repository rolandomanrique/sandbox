package sandbox

import scala.annotation.tailrec
import scala.collection.mutable

package object graph {

  case class Path[A](nodes: List[Node[A]], cost: Int)

  object Path {
    def shortest[A]: Ordering[Path[A]] = Ordering.by[Path[A], Int](p => -p.cost)
    def longest[A]: Ordering[Path[A]] = Ordering.by[Path[A], Int](p => p.cost)
  }

  case class Node[A](value: A) {
    override def toString: String = value.toString
  }

  case class Edge[A](from: Node[A], to: Node[A], cost: Int)

  sealed trait Graph[A] {
    val next: Node[A] => Set[Edge[A]]
  }

  case class DirectionalGraph[A](edges: Set[Edge[A]]) extends Graph[A] {
    private val out: Map[Node[A], Set[Edge[A]]] = edges.groupBy(_.from)
    override val next: Node[A] => Set[Edge[A]] = out.getOrElse(_, Set.empty)
  }

  case class BidirectionalGraph[A](edges: Set[Edge[A]]) extends Graph[A] {
    private val out: Map[Node[A], Set[Edge[A]]] = edges.groupBy(_.from)
    private val in: Map[Node[A], Set[Edge[A]]] = edges.groupBy(_.from)
    override val next: Node[A] => Set[Edge[A]] = n => out.getOrElse(n, Set.empty) ++ in.getOrElse(n, Set.empty)
  }

  case class DynamicGraph[A](f: A => Set[A]) extends Graph[A] {
    override val next: Node[A] => Set[Edge[A]] = n => f(n.value).map(r => Edge(n,Node(r),0))
  }

  case class DynamicCostGraph[A](override val next: Node[A] => Set[Edge[A]]) extends Graph[A]

  private sealed trait Q[A] {
    def isEmpty: Boolean
    def dequeue(): Path[A]
    def enqueue(a: Path[A]*): Unit
  }

  def shortestPath[A](graph: Graph[A])(from: Node[A], to: Node[A]): Option[Path[A]] = {
    val q: Q[A] = new mutable.PriorityQueue[Path[A]]()(Path.shortest[A]) with Q[A]
    path[A](graph, q)(from, _ == to)
  }

  def longestPath[A](graph: Graph[A])(from: Node[A], to: Node[A]): Option[Path[A]] = {
    val q: Q[A] = new mutable.PriorityQueue[Path[A]]()(Path.longest[A]) with Q[A]
    path[A](graph, q)(from, _ == to)
  }

  def bfs[A](graph: Graph[A])(from: Node[A], to: Node[A]): Option[Path[A]] = {
    val q: Q[A] = new mutable.Queue[Path[A]]() with Q[A]
    path[A](graph, q)(from, _ == to)
  }

  def dfs[A](graph: Graph[A])(from: Node[A], to: Node[A]): Option[Path[A]] = {
    ddfs[A](graph)(from, _ == to)
  }

  def ddfs[A](graph: Graph[A])(from: Node[A], goalF: Node[A] => Boolean): Option[Path[A]] = {
    val q: Q[A] = new mutable.ArrayStack[Path[A]]() with Q[A] {
      override def dequeue(): Path[A] = pop()
      override def enqueue(a: Path[A]*): Unit = a.foreach(push)
    }
    path[A](graph, q)(from, goalF)
  }

  def path[A](graph: Graph[A], q: Q[A])(from: Node[A], goalF: Node[A] => Boolean): Option[Path[A]] = {
    @tailrec
    def go(visited: Set[Node[A]]): Option[Path[A]] = {
      if (q.isEmpty) {
        None
      } else {
        val curPath = q.dequeue()
        val lastNode = curPath.nodes.head
//println(s"D => $curPath")
        if (goalF(lastNode)) {
          Option(curPath)
        } else {
          graph.next(lastNode)
            .filterNot(e => visited.contains(e.to))
            .foreach(e => {
//println(s"     E => ${Path(curPath.nodes ++ List(e.to), curPath.cost + e.cost)}")
              q.enqueue(Path(e.to :: curPath.nodes, curPath.cost + e.cost))
            })
          go(visited ++ Set(lastNode))
        }
      }
    }

    q.enqueue(Path(List(from), 0))
    go(Set(from)).map(r => r.copy(nodes = r.nodes.reverse))
  }

}