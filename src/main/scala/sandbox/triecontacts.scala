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

  def print(node: Node, offset: Int = 0): Unit = {
    def pad(c:String, n: Int): String = c.reverse.padTo(n, " ").reverse.mkString("")

    Console.out.print(s"$node\n")
    node.next.foreach { case (_, x) =>
      Console.out.print(pad("|-", offset + 2))
      print(x, offset + 2)
    }
  }

}
