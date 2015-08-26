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

class Edge(_left: Node, _right: Node, _weight:  Int){
  val left = _left
  val right = _right
  val weight = _weight
}


class Djikstra {
  var graph = ArrayBuffer[Node]()

  def getTheOtherNode(edge: Edge, currNode: Int): Node = {
    if(edge.left.name == currNode)
      edge.right
    else
      edge.left
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
        val nextNode = getTheOtherNode(edge, currNode)
//        println("Edge weight " + edge.weight)
        if(distances(nextNode.name) > distances(currNode) + edge.weight){
          distances(nextNode.name) = distances(currNode) + edge.weight
          nextNode.parent = graph(currNode)
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

object Solution{
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
        djikstra.graph(left).adjacent += edge
        djikstra.graph(right).adjacent += edge
      }
      val Start = scanner.nextInt() - 1
      val distances = djikstra.runShortestPath(Start)
      for(i <- 0 to distances.size - 1){
        if(distances(i) == Int.MaxValue)
          print(-1 + " ")
        else if(distances(i) != 0)
          print(distances(i) + " ")
      }
    }
  }
}