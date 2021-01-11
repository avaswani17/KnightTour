package org.tc

import scala.util.{Failure, Try}


/**
  * Demonstrates Pawns Tour on a N*N sized board
  *
  * It uses Warnsdorff's technique which does the following:
  * 1. Start from any point on the board and mark it as visited.
  * 2. To decide next point on board, look at all possible unmarked points based on moving rules.
  * 3. Rank each possibility by the number of next moves from that point.
  * 4. Move to any point with the lowest rank.
  * 5. Add chosen point to pawn's tour path (i.e now marked) and repeat the process from last chosen point.
  *
  */
class Logic extends Visits {

  import Visits._

  override def findPath(startPoint: Point, boardDimension: (X, Y), legalMoves: Set[(X, Y)]): Try[Path] = {
    if (boardDimension._1 < 1 || boardDimension._2 < 1) {
      Failure(
        new Exception("InvalidBoardDimension")
      )
    } else if (!startPoint.isInBounds(boardDimension)) {
      Failure(
        new Exception("InvalidStartPoint")
      )
    } else
      Try {
        findPathRec(startPoint,Set(startPoint),List(startPoint),boardDimension,legalMoves)
        throw new Exception("PathNotFound")
      } recover {
        case found: Found => found.tour
      }
  }

  /** Find all valid UnVisited points */
  private def allValidUnvisitedPoints( point: Point,visited: Set[Point],
                                       boardDimension: (X, Y),
                                       legalMoves: Set[(X, Y)]): List[Point] = {
    def filterNotVisited(p: Point): List[Point] =
      p.validMoves(legalMoves, boardDimension).filter(!visited.contains(_)).toList
    // sort unvisited points by number of possible unvisited moves that can be taken from them
    filterNotVisited(point).sortBy(filterNotVisited(_).length)
  }

  /** Find path for pawn's tour recursively using Warnsdorff's technique.
    *
    * This might not be the best approach as far as throwing exception for a valid scenario
    * but is optimal as it breaks as soon as a valid tour path is found
    *
    * */
  private def findPathRec( point: Point, visited: Set[Point], path: List[Point],
                           boardDimension: (X, Y), legalMoves: Set[(X, Y)]): Unit =
    if (visited.size == boardDimension._1 * boardDimension._2)
    // break when a complete tour path is discovered
      throw Found(path)
    else
      allValidUnvisitedPoints(point, visited, boardDimension, legalMoves)
        .foreach { move => findPathRec(move,visited + move,move :: path,boardDimension,legalMoves) }
}

