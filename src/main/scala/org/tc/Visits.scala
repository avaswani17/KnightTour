package org.tc

import scala.util.Try

trait Visits {
  import Visits._


  /**
    * Finds complete path of Pawn's Tour
    *
    * @param startPoint     starting position of pawn in the board
    * @param boardDimension board dimension as rows and columns
    * @param legalMoves     all legal moves that can be performed by the pawn
    * @return successful pawn's tour or exception wrapped as failure
    */
  def findPath(
                startPoint: Point,
                boardDimension: (X, Y),
                legalMoves: Set[(X, Y)]
              ): Try[Path]


}

object Visits {
  type X = Int
  type Y = Int
  type Path = List[Point]

  /** Utility for presenting tour path */
  def pathAsArray(path: Path, dimension: (X, Y)): Array[Array[Int]] = {
    val array = Array.ofDim[Int](dimension._1, dimension._2)  //Tuple
    path.zipWithIndex.foreach {
      case (tile, index) =>
        array(tile.y)(tile.x) = index + 1
    }
    array
  }
}

