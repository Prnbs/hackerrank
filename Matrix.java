
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
	boolean coloured;
	
	Edge(City cityLeft, City cityRight, int _weight)
	{
		edgeEnds = new City[2];
		edgeEnds[0] = cityLeft;
		edgeEnds[1] = cityRight;
		weight = _weight;
		parent = null;
		ends = cityLeft.cell + "---" + cityRight.cell;
		coloured = false;
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
	//	long start = System.nanoTime();
		war.Destroy();
	//	long end = System.nanoTime();
	//	long totalTime = (end - start)/(1000*1000);

		System.out.println(war.totalCost);
	//	System.out.println(totalTime);
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
				city.processed = true;
				dfsStack.clear();
				//already cut off
				if(edge.edgeEnds[0] == null) continue;
				//we need to stop when we back track back to this edge
				edge.parent = null;
				dfsStack.push(edge);
				HashMap<String, Edge> foundSentinal = null;
				foundSentinal = DFS(city);
				ColourEdges(foundSentinal.values());
				break;
			}
			break;
		}
		FindAndDestroy();
	}
	
	Edge GetNextColouredEdge(Edge cameFrom, City city)
	{
		for(Edge edge : city.adjacent)
		{
			if(edge.coloured && edge != cameFrom) return edge;
		}
		return null;
	}
	
	void FindAndDestroy()
	{
		for(City city : zion)
		{
			if(!city.hasSentinel)
				continue;
			Edge lowEdge  = null;
			for(Edge nextEdge : city.adjacent)
			{
				if(!nextEdge.coloured) continue;
				if(nextEdge.edgeEnds[0] == null) continue;
				lowEdge = RecursiveDFS(nextEdge, city, nextEdge);
				if(lowEdge == null) continue;
				DestroyRoad(lowEdge);
			}
		}
	}
	
	void DestroyRoad(Edge lowEdge)
	{
		if(lowEdge != null && lowEdge.edgeEnds[0] != null)
		{
			lowEdge.edgeEnds[0] = null;
			lowEdge.edgeEnds[1] = null;
			numCuts++;
			totalCost += lowEdge.weight;
		}
	}
	
	
	Edge RecursiveDFS(Edge cameFromEdge, City cameFromCity, Edge lowestEdgeTillHere)
	{
		City cityHere = GetTheOtherCity(cameFromEdge, cameFromCity);
		if(cityHere == null) return null;
		if(cityHere.hasSentinel) 
		{
			return lowestEdgeTillHere;
		}
		for( Edge edge : cityHere.adjacent)
		{
			if(!edge.coloured) continue;
			if(edge == cameFromEdge) continue;
			if(edge.edgeEnds[0] == null) continue; //already cut this edge off
			Edge lowestEdge = null;
			lowestEdge = (lowestEdgeTillHere.weight > edge.weight)? edge : lowestEdgeTillHere;
			Edge foundEdge =  RecursiveDFS(edge, cityHere, lowestEdge);
			if(foundEdge == null) continue;
			DestroyRoad(foundEdge);
		}
		return null;
	}
	
	void ColourEdges(Collection<Edge> sentinalLeaves)
	{
		for(Edge edge : sentinalLeaves)
		{
			while(edge != null)
			{
				if(edge.coloured) break; 
				edge.coloured = true;
				edge = edge.parent;
			}
		}
	}
	
	City CityWithSentinal(Edge edge)
	{
		if(edge.edgeEnds[0].hasSentinel) return edge.edgeEnds[0];
		if(edge.edgeEnds[1].hasSentinel) return edge.edgeEnds[1];
		return null;
	}
	
	//ignoreCity is the parent which we need to ignore for back edges
	HashMap<String, Edge> DFS(City ignoreCity)
	{
		HashMap<String, Edge> sentinelLeaves = new HashMap<String, Edge>();
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
					sentinelLeaves.put(currEdge.ends, currEdge);
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
						sentinelLeaves.put(nextEdge.ends, nextEdge);
					}
				}
			}
		}
		return sentinelLeaves;
	}
}