package sandbox.sudoku

import org.scalatest.{FlatSpec, MustMatchers}

class SudokuTest extends FlatSpec with MustMatchers {

  val sol = Array.ofDim[Digit](9, 9)
  sol.update(0, Array( D1, D2, D3,  D4, D5, D6,  D7, D8, D9))
  sol.update(1, Array( D7, D8, D9,  D1, D2, D3,  D4, D5, D6))
  sol.update(2, Array( D4, D5, D6,  D7, D8, D9,  D1, D2, D3))

  sol.update(3, Array( D2, D3, D4,  D5, D6, D7,  D8, D9, D1))
  sol.update(4, Array( D8, D9, D1,  D2, D3, D4,  D5, D6, D7))
  sol.update(5, Array( D5, D6, D7,  D8, D9, D1,  D2, D3, D4))

  sol.update(6, Array( D3, D4, D5,  D6, D7, D8,  D9, D1, D2))
  sol.update(7, Array( D6, D7, D8,  D9, D1, D2,  D3, D4, D5))
  sol.update(8, Array( D9, D1, D2,  D3, D4, D5,  D6, D7, D8))


  behavior of "solve"
  it should "return true if sudoku is solved" in {
    solved(sol) must be(true)
  }

  behavior of "solve"
  it should "solve sudoku" in {



    val in = sol.clone()
    in(0)(0) = E
    in(0)(1) = E
    in(1)(3) = E
    in(1)(4) = E
    in(2)(6) = E
    in(2)(7) = E
    in(3)(0) = E
    in(8)(1) = E
    in(4)(3) = E
    in(5)(5) = E
    in(5)(6) = E
    in(6)(7) = E
    in(6)(8) = E
    in(7)(4) = E
    in(7)(5) = E

    println(s"START BOARD: ${sandbox.sudoku.print(in)}")

    val out: Option[Board] = solve(in)

    out must not be empty

  }

}