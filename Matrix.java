
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

class City
{
	int cell;
	ArrayList<Edge> adjacent;
	boolean hasSentinel;
	boolean processed;
	
	City(int _cell)
	{
		cell = _cell;
		adjacent = new ArrayList<Edge>();
		hasSentinel = false;
		processed = false;
	}
}

class Edge
{
	int weight;
	City[] edgeEnds;
	Edge parent;
	String ends;
	
	Edge(City cityLeft, City cityRight, int _weight)
	{
		edgeEnds = new City[2];
		edgeEnds[0] = cityLeft;
		edgeEnds[1] = cityRight;
		weight = _weight;
		parent = null;
		ends = cityLeft.cell + "---" + cityRight.cell;
	}
}

public class Matrix {
	City[] zion;
	Stack<Edge> dfsStack;
	int totalCost = 0;
	Queue<City> bfsQ;
	ArrayList<City> sentinalCities;
	int sentinels;
	int numCuts = 1;
	
	public Matrix()
	{
		dfsStack = new Stack<Edge>();
		bfsQ = new LinkedList<City>();
		sentinalCities = new ArrayList<City>();
	}
		
	public static void main(String[] args) {
		Matrix war = new Matrix();
		Scanner scanner = new Scanner(System.in);
		
		int cities = scanner.nextInt();
		war.sentinels = scanner.nextInt();
		int city1 = 0;
		int city2 = 0;
		int cost = 0;
		war.zion = new City[cities];
		
		for(int i = 0; i < cities; i++)
		{
			war.zion[i] = new City(i);
		}
		for(int i = 0; i < cities-1; i++)
		{
			city1 = scanner.nextInt();
			city2 = scanner.nextInt();
			cost  = scanner.nextInt();
			
			Edge edge = new Edge(war.zion[city1], war.zion[city2], cost);
			war.zion[city1].adjacent.add(edge);
			war.zion[city2].adjacent.add(edge);
		}
		int sentinalIn = 0;
		for(int i = 0; i < war.sentinels; i++)
		{
			sentinalIn = scanner.nextInt();
			war.zion[sentinalIn].hasSentinel = true;
		}
		scanner.close();
		war.Destroy();

		System.out.println(war.totalCost);
	}
	
	void ResetProcessed()
	{
		for(City city : zion)
			city.processed = false;
	}
	
	City GetTheOtherCity(Edge theEdge, City start)
	{
		if(theEdge.edgeEnds[0] == null) return null;
		if(theEdge.edgeEnds[0].cell == start.cell) return theEdge.edgeEnds[1];
		else return theEdge.edgeEnds[0];
	}
	
	City GetUnProcessedCity(Edge theEdge)
	{
		if(theEdge.edgeEnds[0].processed) return theEdge.edgeEnds[1];
		else if (theEdge.edgeEnds[1].processed) return theEdge.edgeEnds[0];
		return null;
	}
	
	void Destroy()
	{
		for(City city : zion)
		{
			if(!city.hasSentinel)
				continue;
			for(Edge edge : city.adjacent)
			{
				ResetProcessed();
				city.processed = true;
				dfsStack.clear();
				//already cut off
				if(edge.edgeEnds[0] == null) continue;
				//we need to stop when we back track back to this edge
				edge.parent = null;
				dfsStack.push(edge);
				ArrayList<Edge> foundSentinal = null;
				foundSentinal = DFS(city);
				DestroyChain(foundSentinal);
			}
			city.processed = true;
		}
	}
	
	City CityWithSentinal(Edge edge)
	{
		if(edge.edgeEnds[0].hasSentinel) return edge.edgeEnds[0];
		if(edge.edgeEnds[1].hasSentinel) return edge.edgeEnds[1];
		return null;
	}
	
	void DestroyChain(ArrayList<Edge> sentinelLeaves)
	{
		Edge lowCost = null;
		
//		System.out.println("-----------------------------------");
		boolean ignoreChain = false;
		for(Edge foundSentinal : sentinelLeaves)
		{
			ignoreChain = false;
			int localCost = 1000001;
			while(foundSentinal != null)
			{
				if(foundSentinal.edgeEnds[0] == null)
				{
					//this edge has already been cut and needs to be ignored
					ignoreChain = true;
					break;
				}
				if(localCost > foundSentinal.weight)
				{
					localCost = foundSentinal.weight;
					lowCost = foundSentinal;
				}
				foundSentinal = foundSentinal.parent;
			}
			//if we haven't cut this chain then now is the time to cut it
			if(!ignoreChain)
			{
//				System.out.println("localCost = " + localCost);
				totalCost += localCost;
				//cut off ties to the parent
//				System.out.println("Cutting " + lowCost.ends);
				lowCost.edgeEnds[0] = null;
				//cut off ties from parent to this cell
				lowCost.edgeEnds[1] = null;
//				System.out.println("-----------------------------------");
			}
		}
		

	}
	
	//ignoreCity is the parent which we need to ignore for back edges
	ArrayList<Edge> DFS(City ignoreCity)
	{
		ArrayList<Edge> sentinelLeaves = new ArrayList<Edge>();
		while(!dfsStack.isEmpty())
		{
			Edge currEdge  = dfsStack.pop();
			//if this city has a sentinel don't bother looking at it's adjacents
			City currCity = GetUnProcessedCity(currEdge);
			if (currCity == null) continue;
			if(!currCity.processed)
			{
				currCity.processed = true;
				if(currCity.hasSentinel)
				{
					dfsStack.push(currEdge);
					sentinelLeaves.add(currEdge);
				}
			}
			
			for(Edge nextEdge : currCity.adjacent)
			{
				if(nextEdge == currEdge) continue;
				if(nextEdge.edgeEnds[0] == null) continue;
				City nextAdj = GetUnProcessedCity(nextEdge);
				if(nextAdj == null) continue;
				//ignore back edge from where we started the ith iteration of Destroy()
				//also ignore back edges to the parent
				if(nextAdj.cell == ignoreCity.cell || nextAdj.cell == currCity.cell) continue;
				if(!nextAdj.processed)
				{
					nextEdge.parent = currEdge;
					dfsStack.push(nextEdge);
					if(nextAdj.hasSentinel)
					{
						sentinelLeaves.add(nextEdge);
					}
				}
			}
		}
		return sentinelLeaves;
	}
}