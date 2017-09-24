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

  case class Graph[A](edges: Set[Edge[A]]) {
    lazy val nodes: List[Node[A]] = edges.foldLeft(List.empty[Node[A]]){ case (l,e) => e.to :: e.from :: l }
    lazy val outgoing: Map[Node[A], Set[Edge[A]]] = edges.groupBy(_.from)
    lazy val incoming: Map[Node[A], Set[Edge[A]]] = edges.groupBy(_.to)
  }

  private sealed trait Q[A] {
    def isEmpty: Boolean
    def dequeue(): Path[A]
    def enqueue(a: Path[A]*): Unit
  }

  def shortestPath[A](graph: Graph[A])(from: Node[A], to: Node[A]): Option[Path[A]] = {
    val q: Q[A] = new mutable.PriorityQueue[Path[A]]()(Path.shortest[A]) with Q[A]
    path[A](graph, q)(from, to)
  }

  def longestPath[A](graph: Graph[A])(from: Node[A], to: Node[A]): Option[Path[A]] = {
    val q: Q[A] = new mutable.PriorityQueue[Path[A]]()(Path.longest[A]) with Q[A]
    path[A](graph, q)(from, to)
  }

  def bfs[A](graph: Graph[A])(from: Node[A], to: Node[A]): Option[Path[A]] = {
    val q: Q[A] = new mutable.Queue[Path[A]]() with Q[A]
    path[A](graph, q)(from, to)
  }

  def dfs[A](graph: Graph[A])(from: Node[A], to: Node[A]): Option[Path[A]] = {
    val q: Q[A] = new mutable.ArrayStack[Path[A]]() with Q[A] {
      override def dequeue(): Path[A] = pop()
      override def enqueue(a: Path[A]*): Unit = a.foreach(push)
    }
    path[A](graph, q)(from, to)
  }

  def path[A](graph: Graph[A], q: Q[A])(from: Node[A], to: Node[A]): Option[Path[A]] = {
    @tailrec
    def go(visited: Set[Node[A]]): Option[Path[A]] = {
      if (q.isEmpty) {
        None
      } else {
        val curPath = q.dequeue()
        val lastNode = curPath.nodes.head
//println(s"D => $curPath")
        if (lastNode == to) {
          Option(curPath)
        } else {
          graph.outgoing.getOrElse(lastNode, Set.empty)
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