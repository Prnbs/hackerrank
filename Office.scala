package Hackerrank.hackerrank

import java.util.Scanner
import scala.collection.immutable.ListMap
import scala.collection.mutable.ArrayBuffer

/**
 * Created by psinha4 on 8/25/2015.
 */
class Node(val name: Int){
  //set parent as itself initially
  var parent: Node = this
  var visited: Boolean = false
  var adjacent: ArrayBuffer[Edge] = ArrayBuffer[Edge]()
}

class Edge(var left: Node, var right: Node, var weight: Int){
  var broken = false
}

class Path(var path: Map[(Int, Int), Boolean], var cost: Int){}

class AllPaths{
  var listOfPaths = Vector[Path]()

  def insertNewPath(path: Path): Unit = {
    listOfPaths = listOfPaths :+ path
    listOfPaths = listOfPaths.sortWith(_.cost <= _.cost)
  }

  def tryGetCostFromExitingPaths(edge: (Int, Int)): Int = {
    for(path <- listOfPaths){
      val cost = path.path.getOrElse(edge, Int.MinValue)
      if(cost != Int.MinValue)
        cost
    }
    Int.MinValue
  }
}


class Office {
  var graph = ArrayBuffer[Node]()

  var edgeMaps = Map[(Int, Int), Edge]()

  var allPaths = new AllPaths()


  def getTheOtherNode(edge: Edge, currNode: Int): Node = {
    if(edge.left.name == currNode)
      edge.right
    else
      edge.left
  }

  def resetVisited():Unit = {
    val graphSize = graph.size
    for (i <- 0 to graphSize-1) {
      graph(i).visited = false
    }
  }

  /**
   * Returns a map of tuples which denote the two edges, the value stored in the map is irrelevant
   * the tuples are stored in forward and reverse order since the edges are bi directional
   *
   * This method should only be called if a path actually exists
   * @param stop
   * @param start
   * @return
   */
  def getPathForCurrentSetting(stop: Int, start: Int, cost: Int): Path = {
    var path = Map[(Int, Int), Boolean]()
    var end = stop
    while(graph(end).parent.name != start){
      path += ((end, start) -> true)
      path += ((start, end) -> true)
      end = graph(end).parent.name
    }
    //now add the start itself
    path += ((end, start) -> true)
    path += ((start, end) -> true)
    new Path(path, cost)
  }

  def runShortestPath(start: Int): ArrayBuffer[Int] = {
    val graphSize = graph.size
    val distances = ArrayBuffer[Int]()

    //initialize distance list
    for (i <- 0 to graphSize-1) {
      distances  += Int.MaxValue
    }

    distances(start) = 0
    var currNode = start
//    println(s"Running while loop on $currNode")
    while(!graph(currNode).visited){
      graph(currNode).visited = true
      // assign distances to all the adjacents of the
      val size = graph(currNode).adjacent.size
      for(j <- 0 to size-1){
        val edge = graph(currNode).adjacent(j)
        if(!edge.broken) {
          val nextNode = getTheOtherNode(edge, currNode)
          //        println("Edge weight " + edge.weight)
          if (distances(nextNode.name) > distances(currNode) + edge.weight) {
            distances(nextNode.name) = distances(currNode) + edge.weight
            nextNode.parent = graph(currNode)
          }
        }
      }

      //find the next node based on which not visited node has the shortest distance
      var dist = Int.MaxValue
      for (i <- 0 to graphSize-1){
        val node = graph(i)
        if(!node.visited && dist > distances(node.name)){
          dist = distances(node.name)
          currNode = node.name
        }
      }
//      println(s"Next node to process $currNode")
    }
    distances
  }
}

object Office{
  def main(args: Array[String]) {
    val scanner = new Scanner(System.in)
    val djikstra = new Office()

    // get number of nodes
    val N = scanner.nextInt()
    for(k <- 0 to N-1){
      djikstra.graph.append(new Node(k))
    }

    //get the edges
    val M = scanner.nextInt()
    for(j <- 0 to M-1){
      val left = scanner.nextInt()
      val right = scanner.nextInt()
      val weight = scanner.nextInt()
      val edge = new Edge(djikstra.graph(left), djikstra.graph(right), weight)
      djikstra.graph(left).adjacent += edge
      djikstra.graph(right).adjacent += edge
      djikstra.edgeMaps += ((left, right) -> edge)
      djikstra.edgeMaps += ((right, left) -> edge)
    }

    //get start and end
    val start = scanner.nextInt()
    val end = scanner.nextInt()

    val queries = scanner.nextInt()
    for(i <- 0 to queries-1){
      val brokenStart = scanner.nextInt()
      val brokenEnd = scanner.nextInt()
      val existingCost = djikstra.allPaths.tryGetCostFromExitingPaths((brokenEnd, brokenStart))
      if(existingCost == Int.MinValue) {
        //break the edge
        val brokenEdge = djikstra.edgeMaps((brokenStart, brokenEnd))
        brokenEdge.broken = true

        val distances = djikstra.runShortestPath(start)
        //connect the broken edge back
        brokenEdge.broken = false
        djikstra.resetVisited()

        //get the Path if it exists
        if (distances(end) != Int.MaxValue)
          djikstra.allPaths.insertNewPath(djikstra.getPathForCurrentSetting(start, end, distances(end)))
        println(distances(end))
      }
      else
        println(existingCost)
    }
  }
}