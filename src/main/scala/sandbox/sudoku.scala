package sandbox

import scala.collection.immutable.HashSet

package object sudoku {

  sealed trait Digit{
    def toInt: Int
    def toChar: Char = toString.charAt(0)
    override def toString: String = "."
  }
  sealed trait NonEmptyDigit extends Digit {
    override def toString: String = toInt.toString
  }
  case object  E extends Digit { override val toInt: Int = 0 }
  case object D1 extends NonEmptyDigit { override val toInt: Int = 1 }
  case object D2 extends NonEmptyDigit { override val toInt: Int = 2 }
  case object D3 extends NonEmptyDigit { override val toInt: Int = 3 }
  case object D4 extends NonEmptyDigit { override val toInt: Int = 4 }
  case object D5 extends NonEmptyDigit { override val toInt: Int = 5 }
  case object D6 extends NonEmptyDigit { override val toInt: Int = 6 }
  case object D7 extends NonEmptyDigit { override val toInt: Int = 7 }
  case object D8 extends NonEmptyDigit { override val toInt: Int = 8 }
  case object D9 extends NonEmptyDigit { override val toInt: Int = 9 }
  object Digit {
    val allS = HashSet(D1,D2,D3,D4,D5,D6,D7,D8,D9)
    val allA: Array[NonEmptyDigit] = Array(D1,D2,D3,D4,D5,D6,D7,D8,D9)
    def apply(c: Char): Digit = allA.find(_.toChar == c).getOrElse(E)
    def apply(i: Int): Digit = allA.find(_.toInt == i).getOrElse(E)
    def apply(s: String): Digit = allA.find(_.toString == s).getOrElse(E)

    // throws IndexOutBound exception, use with care
    def get(i: Int): NonEmptyDigit = allA(i)
  }

  sealed trait GroupType
  case object Row extends GroupType
  case object Column extends GroupType
  case object Quad extends GroupType
  type Group = Array[Digit]
  type Board = Array[Group]

  def row(b: Board, i: NonEmptyDigit): Array[Digit] = b(i.toInt - 1)
  def col(b: Board, i: NonEmptyDigit): Array[Digit] = b.map(_(i.toInt - 1))
  def quad(b: Board, i: NonEmptyDigit): Array[Digit] = {
    val (ox, oy) = i match {
      case D1 => (0,0)
      case D2 => (0,3)
      case D3 => (0,6)
      case D4 => (3,0)
      case D5 => (3,3)
      case D6 => (3,6)
      case D7 => (6,0)
      case D8 => (6,3)
      case D9 => (6,6)
    }

    val res = new Array[Digit](9)
    for (x <- ox to ox+2) {
      for (y <- oy to oy+2) {
        res((x-ox)+(3*(y-oy))) = b(x)(y)
      }
    }
    res
  }

  def quadIndex(x: NonEmptyDigit, y: NonEmptyDigit): NonEmptyDigit = (x.toInt,y.toInt) match {
    case (i,j) if i <= 3 && j <= 3 => D1
    case (i,j) if i <= 3 && j <= 6 => D2
    case (i,j) if i <= 3 && j <= 9 => D3
    case (i,j) if i <= 6 && j <= 3 => D4
    case (i,j) if i <= 6 && j <= 6 => D5
    case (i,j) if i <= 6 && j <= 9 => D6
    case (i,j) if i <= 9 && j <= 3 => D7
    case (i,j) if i <= 9 && j <= 6 => D8
    case _ => D9
  }

  def missing(a: Group): Set[Digit] = Digit.allA.diff(a).toSet

  def complete(a: Group): Boolean = a.toSet == Digit.allS

  def solved(b: Board): Boolean = {
    Digit.allA.forall(x => complete(row(b,x)) && complete(col(b,x)) && complete(quad(b,x)))
  }

  def findEmpty(b: Board): Option[(NonEmptyDigit, NonEmptyDigit)] = {
    for (i <- 0 to 8) {
      for (j <- 0 to 8) {
        if (b(i)(j) == E) return Option((Digit.get(i), Digit.get(j)))
      }
    }
    None
  }

  def clone(b: Board): Board = {
    val nb = Array.ofDim[Digit](9, 9)
    for (i <- 0 to 8) {
      for (j <- 0 to 8) {
        nb(i)(j) = b(i)(j)
      }
    }
    nb
  }

  def next(b: Board): Set[Board] = {
    findEmpty(b).map { case (x,y) =>
      val viable = missing(row(b, x)) intersect missing(col(b, y)) intersect missing(quad(b, quadIndex(x,y)))
      viable.map { d =>
        val nb = clone(b)
        nb(x.toInt-1)(y.toInt-1) = d
        nb
      }
    } getOrElse Set.empty
  }

  def solve(b: Board): Option[Board] = {
    val g = new graph.DynamicGraph[Board](next)
    graph.ddfs(g)(graph.Node(b), n => solved(n.value)).map(_.nodes.last.value)
  }

  def print(b: Board): String = {
    "\n" +
      b.map(_.sliding(3, 3).map(_.mkString(" ")).mkString(" | "))
        .sliding(3, 3)
        .map(_.mkString("\n"))
        .mkString(s"\n${"-".padTo(21, "-").mkString("")}\n") +
      "\n\n"
  }

}