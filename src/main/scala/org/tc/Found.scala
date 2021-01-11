package org.tc

import Visits.Path

/** Custom Exception for breaking from recursion when a tour path is found */
final case class Found(tour: Path) extends Exception