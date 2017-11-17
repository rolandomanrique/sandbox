package sandbox

package object triecontacts {
  import scala.collection.mutable

  case class Node(value: Char) {
    val next: mutable.HashMap[Char, Node] = mutable.HashMap.empty
    var count: Int = 0
    override def toString: String = if (value == Char.MinValue) "" else value.toString
  }

  def add(node: Node, word: List[Char]): Unit = {
    val letter = word.headOption.getOrElse(Char.MinValue)
    val n = node.next.getOrElseUpdate(letter, Node(letter))
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

  def contains(node: Node, word: List[Char]): Boolean = {
    val n = find(node, word)
    val res = n != null && n.word
    res
  }

  def wordBreak(node: Node, word: List[Char]): Boolean = {
    if (word.isEmpty) return true
    val size = word.size
    val wb: Array[Boolean] = Array.fill(size+1)(false)
    for (i <- 1 to size) {
      if (!wb(i) && contains(node, word.take(i))) {
        wb(i) = true
      }

      if (wb(i)) {
        if (i == size) return true

        for (j <- i+1 to size) {
          if (!wb(j) && contains(node, word.slice(i,j))) {
            wb(j) = true
          }

          if (j == size && wb(j)) return true
        }
      }
    }

    false
  }

  def print(node: Node, offset: Int = 0): Unit = {
    def pad(c:String, n: Int): String = c.reverse.padTo(n, " ").reverse.mkString("")

    Console.out.print(s"$node\n")
    node.next.foreach { case (_, x) =>
      Console.out.print(pad("|-", offset + 2))
      print(x, offset + 2)
    }
  }


  val root = Node(Char.MinValue, "")

  add(root, "caracas".toList)
  add(root, "caraotas".toList)
  add(root, "carro".toList)
  add(root, "delta".toList)
  add(root, "deltamacuro".toList)
  add(root, "delcarajo".toList)
  add(root, "tango".toList)

  //print(root)

  assert(count(root, "car".toList) == 3)
  assert(count(root, "cara".toList) == 2)
  assert(count(root, "foo".toList) == 0)
  assert(count(root, "del".toList) == 3)
  assert(count(root, "delta".toList) == 2)
  assert(count(root, "tango".toList) == 1)


  println(words(root, "car".toList))
  assert(words(root, "car".toList) == Set("caracas", "caraotas", "carro"))


  /// word break tests
  val dict = Node(Char.MinValue, "")
  List("mobile","samsung","sam","sung","man","mango","icecream","and","go","i","like","ice","cream")
    .foreach(w => add(dict, w.toList))

  print(dict)

  assert(wordBreak(dict, "ilikesamsung".toList))
  assert(wordBreak(dict, "iiiiiiii".toList))
  assert(wordBreak(dict, "".toList))
  assert(wordBreak(dict, "ilikelikeimangoiii".toList))
  assert(wordBreak(dict, "samsungandmango".toList))
  assert(!wordBreak(dict, "samsungandmangok".toList))


}




object Dictionary extends App {


  type Edge = (Char, Char)
  def edgesAndNodes(w1: String, w2: String): (Option[Edge], Set[Char])  = {
    val (a1,a2) = (w1.toCharArray, w2.toCharArray)
    val chars = a1.toSet ++ a2.toSet

    for (i <- 0 until Math.min(a1.length, a2.length)) {
      if (a1(i) != a2(i)) return (Option((a1(i), a2(i))), chars)
    }

    (None, chars)
  }

  def alphabet(words: List[String]): String = {
    if (words.size <= 1) return ""

    // Given words, get a set of all chars and known edges between them
    val (edges: List[Edge], nodes: Set[Char]) = words.sliding(2).foldLeft((List.empty[Edge], Set.empty[Char])) {
      case ((e, n), h :: t :: Nil) =>
        val (x,y) = edgesAndNodes(h,t)
        (x.map(_ :: e).getOrElse(e), n ++ y)
    }

    // PREPARE visited array and outgoing nodes map and stack
    val nodesIndex: Map[Char, Int] = nodes.zipWithIndex.toMap
    val visited: Array[Boolean] = Array.fill(nodes.size)(false)
    val outgoing: Map[Char, List[Char]] = edges.groupBy(_._1).mapValues(_.map(_._2))
    var stack: List[Char] = Nil

    // recurse DFS
    def go(c: Char): Unit = {
      if (visited(nodesIndex(c))) return
      visited(nodesIndex(c)) = true
      outgoing.getOrElse(c, Nil).foreach(go)
      stack = c :: stack
    }

    nodes.foreach(go)

    stack.mkString("")
  }

  assert(alphabet(List("baa", "abcd", "abca", "cab", "cad")) == "bdac")
  assert(alphabet(List("caa", "aaa", "aab")) == "cab")
  assert(alphabet(List("wrt", "wrf", "er", "ett", "rftt")) == "wertf")

}

