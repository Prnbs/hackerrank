package Hackerrank.hackerrank

import java.util.Scanner

import scala.collection.mutable.ArrayBuffer

/**
 * Created by psinha4 on 8/25/2015.
 */
class Node(cell: Int){
  //set parent as itself initially
  var parent: Node = this
  var visited: Boolean = false
  var adjacent: ArrayBuffer[Edge] = ArrayBuffer[Edge]()
  val name = cell



}

class Edge(left: Node, right: Node, weight:  Int){
  val value = left.name + " to " + right.name + " with " + weight
}

class Djikstra {
  var graph = ArrayBuffer[Node]()

  def runShortestPath(start: Int):List[Int] = {
    var distances = Nil

    distances
  }
}

object Djikstra{
  def main(args: Array[String]) {
    val scanner = new Scanner(System.in)
    val testCases = scanner.nextInt()
    val djikstra = new Djikstra()

    for(i <- 1 to testCases){
      val N = scanner.nextInt()
      for(k <- 0 to N-1){
        djikstra.graph.append(new Node(k))
      }
      val M = scanner.nextInt()
      for(j <- 0 to M-1){
        val left = scanner.nextInt() - 1
        val right = scanner.nextInt() - 1
        val weight = scanner.nextInt()
        val edge = new Edge(djikstra.graph(left), djikstra.graph(right), weight)
        djikstra.graph(left).adjacent.append(edge)
        djikstra.graph(right).adjacent.append(edge)
      }
      val Start = scanner.nextInt()
      val distances = djikstra.runShortestPath(Start)
    }
  }
}