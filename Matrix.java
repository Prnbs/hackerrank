import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

class City{
	int cell;
	ArrayList<Edge> adjacent;
	boolean hasSentinel;
	boolean processed;
	boolean discovered;
	City parent;
	
	City(int _cell){
		cell = _cell;
		adjacent = new ArrayList<Edge>();
		hasSentinel = false;
		processed = false;
		parent = null;
		discovered = false;
	}
}

class Edge
{
	int weight;
	City[] edgeEnds;
	Edge parent;
	
	Edge(City cityLeft, City cityRight, int _weight)
	{
		edgeEnds = new City[2];
		edgeEnds[0] = cityLeft;
		edgeEnds[1] = cityRight;
		weight = _weight;
		parent = null;
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
		war.CountConnected();
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
		while(CountConnected() != sentinels)
		{
//			System.out.println(numCuts);
			for(City city : zion)
			{
				if(!city.hasSentinel)
					continue;
				
				dfsStack.clear();
				
				for(Edge edge : city.adjacent)
				{
					ResetProcessed();
					dfsStack.clear();
					//already cut off
					if(edge.edgeEnds[0] == null) continue;
					City adjCity = GetTheOtherCity(edge, city);
					adjCity.parent = city;
					dfsStack.push(edge);
					//we need to stop when we back track back to this city
					city.parent = null;
					city.processed = true;
					edge.parent = null;
					
					Edge foundSentinal = null;
					foundSentinal = DFS(city);
					if(foundSentinal == null) continue;
					
//					System.out.println(foundSentinal.cell + " ---------- " + city.cell + " XX");
					DestroyChain(foundSentinal);
					dfsStack.clear();
					dfsStack.push(edge);
				}
				city.processed = true;
				if(CountConnected() == sentinels) break;
			}
			System.out.println("Repeat");
		}
//		System.out.println("found sentinel = "+ sentinelFoundCount);
//		System.out.println("called dfs = " + dfscallcount);
//		System.out.println(totalCost);
	
	}
	
	void ResetDiscovered()
	{
		for(City city : zion)
			city.discovered = false;
	}
	
	int CountConnected()
	{
		bfsQ.add(zion[0]);
		int connected = 0;
		for(int i = 0; i < zion.length; i++)
		{
			if(!zion[i].discovered)
			{
				zion[i].discovered = true;
				connected++;
				bfs(zion[i]);
			}
		}
		ResetDiscovered();
		System.out.println("connected "+connected);
		return connected;
	}
	
	public void bfs(City root)
	{
		bfsQ.clear();
		bfsQ.add(root);
		while(!bfsQ.isEmpty())
		{
			City currElem = bfsQ.remove();
			for(Edge nextEdge : currElem.adjacent)
			{
				City nextCell = GetTheOtherCity(nextEdge, currElem);
				if(nextCell == null) continue;
				if(!nextCell.discovered)
				{
					bfsQ.add(nextCell);
					nextCell.discovered = true;
				}
			}
		}
	}
	
	void PrintDFSInfo(City start, City second, City adjacent)
	{
//		if(start != null && second != null)
//		{
//			if(adjacent != null)
//				System.out.println("DFS start with "+start.cell +" adjacent to " + second.cell + " found adjacent in " + adjacent.cell);
//			else 
//				System.out.println("DFS start with "+start.cell +" adjacent to " + second.cell + " no adjacent found");
//		}
	}
	
	void DestroyChain(Edge foundSentinal)
	{
		Edge lowCost = null;
		int localCost = 1000001;
//		System.out.println("-----------------------------------");
		
		while(foundSentinal.parent != null)
		{
			if(localCost > foundSentinal.weight)
			{
				localCost = foundSentinal.weight;
				lowCost = foundSentinal;
			}
			foundSentinal = foundSentinal.parent;
		}
		if(foundSentinal.parent == null)
		{
			if(localCost > foundSentinal.weight)
			{
				localCost = foundSentinal.weight;
				lowCost = foundSentinal;
			}
		}
		
//		System.out.println("localCost = " + localCost);
		totalCost += localCost;
		//cut off ties to the parent
//		if(lowCost.edgeEnds[0].hasSentinel)
//			System.out.println("Cutting "+lowCost.edgeEnds[0].cell);
//		else
//			System.out.println("Cutting "+lowCost.edgeEnds[1].cell);
		lowCost.edgeEnds[0] = null;
		
		//cut off ties from parent to this cell
		lowCost.edgeEnds[1] = null;
		numCuts++;
//		CountConnected();
//		System.out.println("-----------------------------------");
	}
	
	//ignoreCity is the parent which we need to ignore for back edges
	Edge DFS(City ignoreCity)
	{
		sentinalCities.clear();
		Edge secondCity = null;
		while(!dfsStack.isEmpty())
		{
			secondCity = dfsStack.peek();
			Edge currEdge  = dfsStack.pop();
			//if this city has a sentinel don't bother looking at it's adjacents
			City currCity = GetUnProcessedCity(currEdge);
			if (currCity == null) continue;
			if(!currCity.processed)
			{
				currCity.processed = true;
				if(currCity.hasSentinel)
				{
//					PrintDFSInfo(ignoreCity, secondCity, currCity);
					return currEdge;
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
					nextAdj.processed = true;
					nextAdj.parent = currCity;
					dfsStack.push(nextEdge);
					if(nextAdj.hasSentinel)
					{
//						PrintDFSInfo(ignoreCity, secondCity, nextAdj);
						return nextEdge;
					}
				}
			}
		}
//		PrintDFSInfo(ignoreCity, secondCity, null);
		return null;
	}
}