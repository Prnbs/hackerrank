package Hackerrank.hackerrank

import java.util.Scanner
import scala.collection.immutable.ListMap
import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks._

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
  var shortestPath = Vector[Path]()

  def insertNewPath(path: Path): Unit = {
    listOfPaths = listOfPaths :+ path
    listOfPaths = listOfPaths.sortWith(_.cost <= _.cost)
  }

  def insertShortestPath(path: Path): Unit = {
    shortestPath = listOfPaths :+ path
  }

  def tryGetCostFromExitingPaths(edge: (Int, Int)): Int = {
    var optimalCost = Int.MinValue
    breakable {
      for (path <- listOfPaths) {
        val cost = path.path.getOrElse(edge, Int.MinValue)
        if (cost == Int.MinValue){
          optimalCost = path.cost
          break()
        } //the broken edge isn't part of the path
      }
    }
    optimalCost
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
      path += ((end, graph(end).parent.name) -> true)
      path += ((graph(end).parent.name, end) -> true)
      end = graph(end).parent.name
    }
    //now add the start itself
    path += ((end, start) -> true)
    path += ((start, end) -> true)
    new Path(path, cost)
  }

  /**
   * Only called to store the edges which are in the optimal path
   * @param stop
   * @param start
   * @param cost
   * @return
   */
  def getPathForShortestRoute(stop: Int, start: Int, cost: Int): Vector[(Int, Int)] = {
    var path = Vector[(Int, Int)]()
    var end = stop
    while(graph(end).parent.name != start){
      if(end < graph(end).parent.name)
       path = path :+ (end, graph(end).parent.name)
      else
        path = path :+ (graph(end).parent.name, end)
      end = graph(end).parent.name
    }
    //now add the start itself
    if(end < start)
      path = path :+ (end, start)
    else
      path = path :+ (start, end)
   path
  }

  def runShortestPath(start: Int, stop: Int): ArrayBuffer[Int] = {
//    println("Running djikstra")
    val graphSize = graph.size
    val distances = ArrayBuffer[Int]()

    //initialize distance list
    for (i <- 0 to graphSize-1) {
      distances  += Int.MaxValue
    }

    distances(start) = 0
    var currNode = start
    var foundend = false
    var visitedNodes = Vector[Node]()
//    println(s"Running while loop on $currNode")
    breakable {
      while (!graph(currNode).visited) {
        graph(currNode).visited = true
        // assign distances to all the adjacents of the
        val adjSize = graph(currNode).adjacent.size
        breakable {
          for (j <- 0 to adjSize - 1) {
            val edge = graph(currNode).adjacent(j)
            if (!edge.broken) {
              val nextNode = getTheOtherNode(edge, currNode)
              //        println("Edge weight " + edge.weight)
              if (distances(nextNode.name) > distances(currNode) + edge.weight) {
                distances(nextNode.name) = distances(currNode) + edge.weight
                nextNode.parent = graph(currNode)
                visitedNodes = visitedNodes :+ nextNode
              }
            }
          }
        }

        //find the next node based on which not visited node has the shortest distance
        var dist = Int.MaxValue
        for (node <- visitedNodes) {
          //        val node = graph(i)
          if (!node.visited && dist > distances(node.name)) {
            dist = distances(node.name)
            currNode = node.name
          }
        }
        if (currNode == stop) {
          foundend = true
          break()
        }
        //      println(s"Next node to process $currNode")
      }
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
    println("Finished creating graph")
    //get start and end
    val start = scanner.nextInt()
    val end = scanner.nextInt()

    val t0 = System.currentTimeMillis()
    //run shortest path before query
    val dis = djikstra.runShortestPath(start, end)
    val t1 = System.currentTimeMillis()
    println("Finished 1st djikstra in " + (t1 - t0))
    //add the shortest path to allPaths
    djikstra.allPaths.insertNewPath(djikstra.getPathForCurrentSetting(end, start,  dis(end)))
    println("Added to all paths")

    //add the shortest path to the shortestPaths map
    val shortestPath = djikstra.getPathForShortestRoute(end, start, dis(end))
    println("Added to shortest path")
    println("Number of edges in shortes path = " + shortestPath.size)
    val queries = scanner.nextInt()

    for(path <- shortestPath){
      val brokenEdge = djikstra.edgeMaps((path._1, path._2))
      brokenEdge.broken = true
//      println("Cutting " + path._1 +" , " + path._2)
      val distances = djikstra.runShortestPath(start, end)
      //connect the broken edge back
      brokenEdge.broken = false

//      println("Adding this to allPaths map")
      if (distances(end) != Int.MaxValue)
        djikstra.allPaths.insertNewPath(djikstra.getPathForCurrentSetting(end, start, distances(end)))

      djikstra.resetVisited()
    }
    println("Finished looking at optimal path")

    var djikstraCalled = 0
    for(i <- 0 to queries-1){
      val brokenStart = scanner.nextInt()
      val brokenEnd = scanner.nextInt()
      var existingCost = Int.MinValue
      djikstra.resetVisited()
      existingCost = djikstra.allPaths.tryGetCostFromExitingPaths((brokenEnd, brokenStart))

      if(existingCost == Int.MinValue) {
        djikstraCalled = djikstraCalled + 1
        //break the edge
        val brokenEdge = djikstra.edgeMaps((brokenStart, brokenEnd))
        brokenEdge.broken = true

        println("Had to call djikstra ---")
        val distances = djikstra.runShortestPath(start, end)
        //connect the broken edge back
        brokenEdge.broken = false


        //get the Path if it exists
        if (distances(end) != Int.MaxValue)
          djikstra.allPaths.insertNewPath(djikstra.getPathForCurrentSetting(end, start, distances(end)))
        println(distances(end))
      }
      else
        println(existingCost)
    }
    println(s"Extra djikstra calls $djikstraCalled")
  }
}