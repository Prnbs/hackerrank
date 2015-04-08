import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

class City{
	int cell;
	LinkedList<Edge> adjacent;
	boolean hasSentinel;
	boolean processed;
	boolean discovered;
	City parent;
	
	City(int _cell){
		cell = _cell;
		adjacent = new LinkedList<Edge>();
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
	
	Edge(City cityLeft, City cityRight, int _weight)
	{
		edgeEnds = new City[2];
		edgeEnds[0] = cityLeft;
		edgeEnds[1] = cityRight;
		weight = _weight;
	}
}

public class Matrix {
	City[] zion;
	Stack<City> dfsStack;
	int totalCost = 0;
	Queue<City> bfsQ;
	ArrayList<City> sentinalCities;
	int sentinels;
	
	public Matrix()
	{
		dfsStack = new Stack<City>();
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
		if(theEdge.edgeEnds[0].cell == start.cell) return theEdge.edgeEnds[1];
		else return theEdge.edgeEnds[0];
	}
	
	void Destroy()
	{
		while(CountConnected() != sentinels)
		{
			for(City city : zion)
			{
				if(!city.hasSentinel)
					continue;
				
				dfsStack.clear();
				
				for(Edge edge : city.adjacent)
				{
//					if(adjCity == null || adjCity.cell == city.cell) continue;
					
					dfsStack.clear();
					City adjCity = GetTheOtherCity(edge, city);
					adjCity.processed = true;
					adjCity.parent = city;
					dfsStack.push(adjCity);
					//we need to stop when we back track back to this city
					city.parent = null;
					ResetProcessed();
					Edge foundSentinal = null;
					foundSentinal = DFS(city);
					if(foundSentinal == null) continue;
					
//					System.out.println(foundSentinal.cell + " ---------- " + city.cell + " XX");
					DestroyChain(foundSentinal);
					dfsStack.clear();
					dfsStack.push(adjCity);
				}
				city.processed = true;
				if(CountConnected() == sentinels) break;
			}
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
//		System.out.println("connected "+connected);
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
		City lowCost = null;
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
		
//		System.out.println("localCost = " + localCost);
		totalCost += localCost;
		//cut off ties to the parent
		lowCost.adjacent[lowCost.parent.cell] = null;
		
		//cut off ties from parent to this cell
		//Not really needed
		lowCost.parent.adjacent[lowCost.cell] = null;
//		CountConnected();
//		System.out.println("-----------------------------------");
	}
	
	//ignoreCity is the parent which we need to ignore for back edges
	Edge DFS(City ignoreCity)
	{
		sentinalCities.clear();
		City secondCity = null;
		while(!dfsStack.isEmpty())
		{
			secondCity = dfsStack.peek();
			City currCity = dfsStack.pop();
			//if this city has a sentinel don't bother looking at it's adjacents
			if(!currCity.processed)
			{
				currCity.processed = true;
				if(currCity.hasSentinel)
				{
					PrintDFSInfo(ignoreCity, secondCity, currCity);
	//				DestroyChain(currCity);
					return currCity;
				}
			}
			
			for(Edge nextEdge : currCity.adjacent)
			{
				City nextAdj = GetTheOtherCity(nextEdge, currCity);
				if(nextAdj == null) continue;
				//ignore back edge from where we started the ith iteration of Destroy()
				//also ignore back edges to the parent
				if(nextAdj.cell == ignoreCity.cell || nextAdj.cell == currCity.cell) continue;
				if(!nextAdj.processed)
				{
					nextAdj.processed = true;
					nextAdj.parent = currCity;
					dfsStack.push(nextAdj);
					if(nextAdj.hasSentinel)
					{
						PrintDFSInfo(ignoreCity, secondCity, nextAdj);
//						DestroyChain(nextAdj);
						return nextEdge;
					}
				}
			}
		}
		PrintDFSInfo(ignoreCity, secondCity, null);
		return null;
	}
}