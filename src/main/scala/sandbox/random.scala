package sandbox

import scala.collection.mutable.ListBuffer

object Primes extends App {

  def take(n: Int): List[Int] = {
    if (n <= 0) return List.empty
    val primes = new ListBuffer[Int]()
    primes += 2
    var i = 3
    while (primes.size <= n) {
      if (primes.takeWhile(_ <= Math.sqrt(i)).forall(p => i % p != 0)) primes += i
      i += 2
    }

    primes.toList
  }


  println(take(100))

}



object DotTree extends App {


  def print(n: Int): Unit = {
    (2 until n/2 by 2).foreach { i =>
      println(List.fill(i % (n/2))('o').padTo(n+i/2, ' ').reverse.mkString(""))
    }
    (n/2 until n-1 by 2).foreach { i =>
      println(List.fill(n - i)('o').padTo(n+n/2-i/2-1, ' ').reverse.mkString(""))
    }
  }

  print(30)

}




object ParenthesisParser extends App {

  sealed trait ParserItem { val open: Char ; val close: Char }
  case object Parenthesis extends ParserItem {
    override val open: Char = '('
    override val close: Char = ')'
  }
  case object Bracket extends ParserItem {
    override val open: Char = '['
    override val close: Char = ']'
  }
  case object Braces extends ParserItem {
    override val open: Char = '{'
    override val close: Char = '}'
  }

  val all = Set(Parenthesis, Braces, Bracket)

  def opens(c: Char): Option[ParserItem] = {
    all.find(_.open == c)
  }

  def closes(c: Char): Option[ParserItem] = {
    all.find(_.close == c)
  }


  def balanced(string: String): Boolean = {
    val q = scala.collection.mutable.Stack[ParserItem]()
    string.foreach { c =>
      opens(c).foreach(i => q.push(i))
      closes(c).foreach(i => if (q.isEmpty || q.pop != i) return false)
    }
    q.isEmpty
  }

  assert(balanced("([{}{}])"))
  assert(balanced("([{asqweqe}{adsdsfs}])"))
  assert(!balanced("([{asdaf}{dfghfgh}]"))
  assert(!balanced("([{}{}]))"))
  assert(!balanced("([{]{}})"))

  println("TESTS PASSED!")

}


object BST extends App {

  case class Node(value: Int, left: Node, right: Node) {
    override val toString: String = value.toString
  }

  def checkBST(root: Node): Boolean = {

    def go(node: Node, min: Int, max: Int): Boolean = {
      if (node == null) {
        true
      } else if (min > node.value || max < node.value) {
        false
      } else {
        go(node.left, min, node.value) && go(node.right, node.value, max)
      }
    }

    go(root, Int.MinValue, Int.MaxValue)

  }


  def get(root: Node, k: Int): Node = {

    var visited = 0

    def go(node: Node): Node = {
      val res = if (node.left != null) go(node.left) else null
      if (visited < k) {
        visited += 1
        if (visited == k) {
          node
        } else if (node.right == null) {
          null
        } else {
          go(node.right)
        }
      } else {
        res
      }

    }


    go(root)
  }

  val root = Node(5,
    Node(3, Node(1, null, Node(2,null,null)), Node(4,null,null)),
    Node(7, Node(6,null,null), Node(8,null,null))
  )

  (0 to 9).foreach(i => println(s"get(root, $i) ==> ${get(root, i)}"))

}


object Contacts extends App {
  import scala.collection.mutable
  case class Node(value: Char) {
    val next: mutable.HashMap[Char, Node] = mutable.HashMap.empty
    var count: Int = 0
    override def toString: String = if (value == Char.MinValue) "#" else value.toString
  }

  def add(node: Node, word: List[Char]): Unit = {
    val letter = word.headOption.getOrElse(Char.MinValue)
    val n = node.next.getOrElse(letter, {
      val x = Node(letter)
      node.next.put(letter, x)
      x
    })

    n.count += 1

    if (word.nonEmpty) add(n, word.tail)
  }

  def find(node: Node, word: List[Char]): Int = {
    def go(node:Node, w: List[Char]): Node = {
      if (w.nonEmpty) {
        node.next.get(w.head).map(go(_, w.tail)).orNull
      } else {
        node
      }
    }

    val o = go(node, word)
    if (o != null) o.count else 0
  }

  def print(node: Node, offset: Int = 0): Unit = {
    def pad(c: String, n: Int) = c.reverse.padTo(n," ").reverse.mkString("")

    Console.out.print(s"$node\n")
    node.next.foreach { case (_, x) =>
      Console.out.print(pad("|-", offset + 2))
      print(x, offset + 2)
    }
  }

  val root = Node(Char.MinValue)
  add(root, "caracas".toList)
  add(root, "caraotas".toList)
  add(root, "carro".toList)
  add(root, "delta".toList)
  add(root, "deltamacuro".toList)
  add(root, "delcarajo".toList)
  add(root, "tango".toList)
  print(root)

  println(root.count)
  println(find(root, "car".toList))
  println(find(root, "delta".toList))

}

object sort {

  def quick(L: Array[Int]): Array[Int] = {
    if (L.length > 1) {
      val pivot = L.last
      val (l,r) = L.dropRight(1).partition(_ <= pivot)
      Array.concat(quick(l), Array(pivot), quick(r))
    } else {
      L
    }
  }

}


object strings {


  def commonSubstrings(l: Set[String]): String = {
    if (l.nonEmpty) {
      val arrays = l.map(_.toCharArray)
      val max = l.map(_.length).min // max size of common substring is the size of the smallest string in set
      for (i <- 1 to max) {
        val isCom = arrays.forall(a => a(i - 1) == arrays.head(i - 1))
        if (!isCom) {
          return l.head.substring(0, i - 1)
        } else if (i == max) {
          return l.head.substring(0, i)
        }
      }
    }

    ""
  }

}



object Aprox extends App {
  def approx(factors: Set[Int], target: Int): Int = {
    val closest: Array[Int] = Array.fill(target+1)(0)
    val min = factors.min
    factors.foreach(f => closest(f) = f)
    var latest = min
    for (i <- min to target) {
      if (closest(i) == 0) {
        // can't get here from any previous number so we keep the latest value
        closest(i) = latest
      } else {
        // update all numbers ahead by adding factors to current value
        for (f <- factors) {
          val x = closest(i) + f
          if (x <= target && closest(x) < x) {
            // if we have not exceed target and x is closer to X than the current value we update x
            closest(x) = x
          }
        }
      }

      latest = closest(i)
    }

    closest(target)

  }


  assert(approx(Set(5, 11, 9), 23) == 23)  // 9+9+5
  assert(approx(Set(5, 11, 9), 17) == 16)  // 11+5
  assert(approx(Set(5, 11, 9), 15) == 15)  // 5+5+5
  assert(approx(Set(7, 12, 19), 44) == 43) // 19+12+12
  assert(approx(Set(7, 12, 19), 45) == 45) // 19+19+7
  assert(approx(Set(7, 12, 19), 46) == 45) // 19+19+7

}

