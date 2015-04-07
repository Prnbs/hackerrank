import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

class City{
	int cell;
	City[] adjacent;
	int[] weight;
	boolean hasSentinel;
	boolean processed;
	boolean discovered;
	City parent;
	
	City(int _cell, int totalCities){
		cell = _cell;
		adjacent = new City[totalCities];
		weight = new int[totalCities];
		Arrays.fill(weight, -1);
		hasSentinel = false;
		processed = false;
		parent = null;
		discovered = false;
	}
}

public class Matrix {
	City[] zion;
	Stack<City> dfsStack;
	int totalCost = 0;
	Queue<City> bfsQ;
	ArrayList<City> sentinalCities;
	
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
		int sentinels = scanner.nextInt();
		int city1 = 0;
		int city2 = 0;
		int cost = 0;
		war.zion = new City[cities];
		
		for(int i = 0; i < cities; i++)
		{
			war.zion[i] = new City(i, cities);
		}
		
		for(int i = 0; i < cities-1; i++)
		{
			city1 = scanner.nextInt();
			city2 = scanner.nextInt();
			cost = scanner.nextInt();
			war.zion[city1].adjacent[city2] = war.zion[city2];
			war.zion[city1].weight[city2] = cost;
			war.zion[city2].adjacent[city1] = war.zion[city1];
			war.zion[city2].weight[city1] = cost;
		}
		int sentinalIn = 0;
		for(int i = 0; i < sentinels; i++)
		{
			sentinalIn = scanner.nextInt();
			war.zion[sentinalIn].hasSentinel = true;
		}
		scanner.close();
		war.Destroy();
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		war.Destroy();
		System.out.println(war.totalCost);
	}
	
	void ResetProcessed()
	{
		for(City city : zion)
			city.processed = false;
	}
	
	void Destroy()
	{
		int totalAdditions = 0;
		int forLpcnt = 0;
		int dfscallcount = 0;
		int sentinelFoundCount = 0;
		CountConnected();
		for(City city : zion)
		{
			int localCost = 100001;
			if(!city.hasSentinel)
				continue;
			//TODO: Unnecessarily costly
			ResetProcessed();
			dfsStack.clear();
			forLpcnt++;
			
			for(City adjCity : city.adjacent)
			{
				if(adjCity == null || adjCity.cell == city.cell) continue;
				
				dfsStack.clear();
				adjCity.processed = true;
				adjCity.parent = city;
				dfsStack.push(adjCity);
//				System.out.println(city.cell +" has adjacent "+  adjCity.cell + " with weight "+ adjCity.weight[city.cell]);
				//we need to stop when we back track back to this city
				city.parent = null;
//				dfscallcount++;
				
				City foundSentinal = DFS(city);
				if(foundSentinal == null) continue;
				sentinelFoundCount++;
				
				System.out.println(foundSentinal.cell + " ---------- " + city.cell + " XX");
				DestroyChain(foundSentinal);
				
				dfsStack.clear();
				
			}
			city.processed = true;
		}
		System.out.println("inner for loop cnt = "+forLpcnt);
		System.out.println("found sentinel = "+ sentinelFoundCount);
		System.out.println("called dfs = " + dfscallcount);
		System.out.println(totalCost);
		System.out.println("totalAdditions "+totalAdditions);
	
	}
	
	void ResetDiscovered()
	{
		for(City city : zion)
			city.discovered = false;
	}
	
	void CountConnected()
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
		
	}
	
	public void bfs(City root)
	{
		bfsQ.clear();
		bfsQ.add(root);
		while(!bfsQ.isEmpty())
		{
			City currElem = bfsQ.remove();
			for(City nextCell : currElem.adjacent)
			{
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
		if(adjacent != null)
			System.out.println("DFS start with "+start.cell +" adjacent to " + second.cell + " found adjacent in " + adjacent.cell);
		else
			System.out.println("DFS start with "+start.cell +" adjacent to " + second.cell + " no adjacent found");
	}
	
	void DestroyChain(City foundSentinal)
	{
		City lowCost = null;
		int localCost = 1000001;
		System.out.println("-----------------------------------");
		
		while(foundSentinal.parent != null)
		{
			System.out.println(foundSentinal.cell +" -----> "+  foundSentinal.parent.cell + " with weight "
					+ foundSentinal.weight[foundSentinal.parent.cell]);
		
			if(localCost > foundSentinal.weight[foundSentinal.parent.cell])
			{
				localCost = foundSentinal.weight[foundSentinal.parent.cell];
				lowCost = foundSentinal;
			}
			foundSentinal = foundSentinal.parent;
		}
		
		System.out.println("localCost = " + localCost);
		totalCost += localCost;
		//cut off ties to the parent
		lowCost.adjacent[lowCost.parent.cell] = null;
		
		//cut off ties from parent to this cell
		//Not really needed
		lowCost.parent.adjacent[lowCost.cell] = null;
		CountConnected();
		System.out.println("-----------------------------------");
	}
	
	//ignoreCity is the parent which we need to ignore for back edges
	City DFS(City ignoreCity)
	{
		sentinalCities.clear();
		City secondCity = dfsStack.peek();
		while(!dfsStack.isEmpty())
		{
			City currCity = dfsStack.pop();
			//if this city has a sentinel don't bother looking at it's adjacents
			currCity.processed = true;
			if(currCity.hasSentinel)
			{
				PrintDFSInfo(ignoreCity, secondCity, currCity);
				return currCity;
			}
			
			for(City nextAdj : currCity.adjacent)
			{
				if(nextAdj == null) continue;
				//ignore back edge from where we started the ith iteration of Destroy()
				//also ignore back edges to the parent
				if(nextAdj.cell == ignoreCity.cell || nextAdj.cell == currCity.cell) continue;
				if(!nextAdj.processed)
				{
					nextAdj.processed = true;
					nextAdj.parent = currCity;
					dfsStack.push(nextAdj);
				}
				if(nextAdj.hasSentinel)
					{
						PrintDFSInfo(ignoreCity, secondCity, nextAdj);
						return nextAdj;
					}
			}
		}
		PrintDFSInfo(ignoreCity, secondCity, null);
		return null;
	}
}