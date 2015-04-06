import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class Node{
	int cell;
	ArrayList<Node> adjacent;
	boolean visited;
	int weight;
	Node parent;
	
	Node(int _cell){
		cell = _cell;
		adjacent = new ArrayList<Node>();
		visited = false;
		parent = null;
		
	}
}

public class Snake {
	Node[] allNodes;
	Queue<Node> bfsQ;
	int[] result;
	
	public Snake()
	{
		bfsQ = new LinkedList<Node>();
		
	}
	
	public void CreateBasicBoard()
	{
		int N = 100;
		allNodes = new Node[N];
	
		for(int i = 0; i < N; i++)
		{
			allNodes[i] = new Node(i+1); //the cell name won't be index name
		}
		//now add 6 adjacents for cells from 1 to 93
		//94 onwards each cell has only an adjacent to get to 100
		for(int i = 0; i < N-6; i++)
		{
			for(int j = i + 1; j < i + 7; j++)
			{
				allNodes[i].adjacent.add(allNodes[j]);
			}
		}
		//cell 94 to 99 have an edge to 100 only
		for(int i = N-6; i < N-1; i++)
		{
			allNodes[i].adjacent.add(allNodes[N-1]);
		}
	}
	
	
	
	public static void main(String[] args) {
		Snake game = new Snake();
		Scanner scanner = new Scanner(System.in);
		int N = 100;
		
		int testCases = scanner.nextInt();
		game.result = new int[testCases];
		for(int k = 0; k < testCases; k++)
		{
			game.CreateBasicBoard();
			int ladders = scanner.nextInt();
			int ladderStart = 0;
			int ladderEnd = 0;
			for(int i = 0; i < ladders; i++)
			{
				ladderStart = scanner.nextInt();
				ladderEnd = scanner.nextInt();
				game.allNodes[ladderStart-1].adjacent.clear();
				game.allNodes[ladderStart-1].adjacent.add(game.allNodes[ladderEnd-1]);
			}
			int snakes = scanner.nextInt();
			int snakeStart = 0;
			int snakeEnd = 0;
			for(int i = 0; i < snakes; i++)
			{
				snakeStart = scanner.nextInt();
				snakeEnd = scanner.nextInt();
				game.allNodes[snakeStart-1].adjacent.clear();
				game.allNodes[snakeStart-1].adjacent.add(game.allNodes[snakeEnd-1]);
			}
			
			
			game.bfsQ.add(game.allNodes[0]);
			game.bfs(game.allNodes[0]);
			//System.out.println("\n");
			game.CountSteps(game.allNodes[N-1], k);
		}
		scanner.close();
		for(int result : game.result)
			System.out.println(result);
	}
	
	public void CountSteps(Node end, int testcase)
	{
		int count = 0;
		while(end.parent != null) //start has null for parent
		{
		//	System.out.println("\nReached "+ end.cell + " from "+ end.parent.cell);
			if(end.parent.adjacent.size() == 1) //either a snake, ladder or the last 5 cells
			{
				if(end.parent.cell > 93) //then we had to roll a die
					count++;
				//else it was a ladder or snake so no dice roll
			}
			else //otherwise we had to roll the dice
				count++;
			
			end = end.parent;
		}
		//System.out.println(count);
		count = (count == 0) ? -1 : count;
		result[testcase] = count;
	}
	
	public void bfs(Node root)
	{
		while(!bfsQ.isEmpty())
		{
			Node currElem = bfsQ.remove();
			if(currElem.cell == 100)
			{	
				bfsQ.clear();
				return;
			}
			for(Node nextCell : currElem.adjacent)
			{
				if(!nextCell.visited)
				{
					bfsQ.add(nextCell);
					nextCell.parent = currElem;
					nextCell.visited = true;
				}
			}
		}
		
	}
	

}
