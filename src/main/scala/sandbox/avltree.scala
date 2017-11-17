package sandbox

import scala.annotation.tailrec

package object avltree {

  case class Node[A](value: A, var L: Node[A], var R: Node[A], var h: Int)(implicit ordering: Ordering[A]) {
    require(isOrdered, s"balanced node must ensure L(${L.value}) < $value < R(${R.value})")

    def isOrdered: Boolean = {
      (L == null || ordering.lt(L.value, value)) &&
        (R == null || ordering.lt(value, R.value))
    }

    def balanceFactor: Int = height(L) - height(R)

  }

  object Node {
    def apply[A](v: A)(implicit ordering: Ordering[A]): Node[A] = Node(v,null,null,0)
  }

  def insert[A](root: Node[A], value: A)(implicit ordering: Ordering[A]): Node[A] = {
    val nn = Node(value)

    @tailrec
    def go(n: Node[A], acc: List[Node[A]]): List[Node[A]] = {
      val nacc = n :: acc
      if (ordering.lt(value, n.value)) {
        if (n.L == null) {
          n.L = nn
          nn :: nacc
        } else {
          go(n.L, nacc)
        }
      } else if (ordering.gt(value, n.value)) {
        if (n.R == null) {
          n.R = nn
          nn :: nacc
        } else {
          go(n.R, nacc)
        }
      } else {
        // duplicate entry leave everything as is
        nacc
      }
    }

    if (root == null) {
      nn
    } else {
      val changed = go(root, Nil)
      val (_, res) = changed.foldLeft((0, nn)) { case ((i, nr), c) =>
        c.h = Math.max(i, c.h)
        val balance = c.balanceFactor

        val r = if (balance > 1 && ordering.lt(value, c.L.value)) {
          // Left Left Case
          println(s"LEFT-LEFT RR(${c.value})")
          RR(c)
        } else if (balance < -1 && ordering.gt(value, c.R.value)) {
          // Right Right Case
          println(s"RIGHT-RIGHT LR(${c.value})")
          LR(c)
        } else if (balance > 1 && ordering.gt(value, c.L.value)) {
          // Left Right Case
          println(s"LEFT-RIGHT LR(${c.L.value}) > RR(${c.value})")
          c.L = LR(c.L)
          RR(c)
        } else if (balance < -1 && ordering.lt(value, c.R.value)) {
          // Right Left Case
          println(s"RIGHT-LEFT RR(${c.R.value}) > LR(${c.value})")
          c.R = RR(c.R)
          LR(c)
        } else {
          if (ordering.gt(c.value, nr.value)) c.L = nr
          else if (ordering.lt(c.value, nr.value)) c.R = nr
          c
        }

        (r.h + 1, r)
      }

      res
    }
  }

  def height[A](a: Node[A]): Int = {
    if (a == null) -1 else a.h
  }

  def computeHeight[A](a: Node[A]): Int = Math.max(height(a.L), height(a.R))+1

  def LR[A](z: Node[A]): Node[A] = {
    val y = z.R
    z.R = y.L
    y.L = z
    z.h = computeHeight(z)
    y.h = computeHeight(y)
    y
  }

  def RR[A](z: Node[A]): Node[A] = {
    val y = z.L
    z.L = y.R
    y.R = z
    z.h = computeHeight(z)
    y.h = computeHeight(y)
    y
  }


  /// BST ///
  def checkBST[A](node: Node[A], min: A, max: A)(implicit ordering: Ordering[A]): Boolean = {
    if (node == null) {
      true
    } else if (ordering.gt(min, node.value) || ordering.lt(max, node.value)) {
      false
    } else {
      checkBST(node.L, min, node.value) && checkBST(node.R, node.value, max)
    }
  }


}



object TreeToList extends App {

  case class Node[A](value: A) {
    var left: Node[A] = _
    var right: Node[A] = _
    override def toString: String = value.toString
  }


  def toList[A](node: Node[A]): (Node[A], Node[A]) = {
    val f = if (node.left != null) {
      val (pf,pl) = toList(node.left)
      node.left = pl
      pl.right = node
      pf
    } else node

    val l = if (node.right != null) {
      val (nf,nl) = toList(node.right)
      node.right = nf
      nf.left = node
      nl
    } else node

    (f,l)
  }


  val root = Node(5)
  root.left = Node(2)
  root.left.left = Node(1)
  root.left.right = Node(3)
  root.left.right.right = Node(4)
  root.right = Node(7)
  root.right.left = Node(6)

  var res = toList(root)._1
  while (res != null) {
    print(res.value + " ")
    res = res.right
  }

}

