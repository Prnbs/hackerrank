import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class UnionSet
{
	int[] parent;
	int[] size;
	int n;
	
	UnionSet(int numPlayers)
	{
		parent = new int[numPlayers+1];
		size = new int[numPlayers+1];
		n = numPlayers;
	}
	
	void Set_Init()
	{
		for(int i = 0; i <= n; i++)
		{
			parent[i] = i;
			size[i] = 1;
		}
	}
}

public class LanParty {
	int[] totalPlayersForGame;
	int[] currPlayersForGame;
	int[] thePlayers;
	int[] gameLiveAt;
	ArrayList<Integer> gamesAndTheirPlayers[];
	
	
	public static void main(String[] args) {
		LanParty play   = new LanParty();
		Scanner scanner = new Scanner(System.in);
		int numPlayers  = scanner.nextInt();
		int numGames    = scanner.nextInt();
		int numWires    = scanner.nextInt();
		
	
		play.gamesAndTheirPlayers = new ArrayList[numGames+1];
		play.totalPlayersForGame  = new int[numGames+1];
		play.currPlayersForGame   = new int[numGames+1];
		play.thePlayers 		  = new int[numPlayers+1];
		play.gameLiveAt 		  = new int[numGames+1];
		
		Arrays.fill(play.currPlayersForGame, 0);
		Arrays.fill(play.gameLiveAt, -1);
		
		UnionSet uSet = new UnionSet(numPlayers);
		uSet.Set_Init();
		for(int i = 1; i <= numPlayers; i++)
		{
			int gameNum = scanner.nextInt();
			play.totalPlayersForGame[gameNum]++;
			play.thePlayers[i] = gameNum;
			//for eg. if Player 1 and 4 want to play game 1 and 2 and 3 want to play game 2
			// Game  -   Player list
			//  1    - 1,4
			//  2    - 2,3
			if(play.gamesAndTheirPlayers[gameNum] == null)
				play.gamesAndTheirPlayers[gameNum] = new ArrayList<Integer>();
			
			play.gamesAndTheirPlayers[gameNum].add(i);
		}
		
		
		for(int i = 1; i <= numWires; i++)
		{
			int node1 = scanner.nextInt();
			int node2 = scanner.nextInt();
			play.Union_Sets(uSet, node1, node2);
			
			int connectedAt = -1;
			//now iterate over all the games and see if any got enabled
			for(int j = 1; j < numGames; j++)
			{
				boolean connected = true;
				//get first player of game j
				int startPlayer = play.gamesAndTheirPlayers[j].get(0);
				for(int k  : play.gamesAndTheirPlayers[j])
				{
					if(k == startPlayer) continue; //ignore self
					if(!play.Is_Connected(uSet, startPlayer, k))
					{
						connected = false;
						break;
					}
					else
					{
						connectedAt = i;
					}
				}
				if(connected)
				{
					play.gameLiveAt[play.thePlayers[node1]] = connectedAt;
				}
			}
		}
		
		scanner.close();
		for(int i = 1; i <= numGames; i++)
		{
			if(play.totalPlayersForGame[i] == 1)
				System.out.println(0);
			else
				System.out.println(play.gameLiveAt[i]);
		}
	}
	
	int Find(UnionSet s, int item)
	{
		if(s.parent[item] == item)
			return item;
		else
			return Find(s, s.parent[item]);
	}
	
	boolean Is_Connected(UnionSet s, int node1, int node2)
	{
		return (Find(s, node1) == Find(s, node2));
	}
	
	void Union_Sets(UnionSet s, int node1, int node2)
	{
		int root1 = Find(s, node1);
		int root2 = Find(s, node2);
		if(root1 == root2) return;
		if(s.size[root1] >= s.size[root2])
		{
			s.size[root1] += s.size[root2];
			s.parent[root2] = root1;
		}
		else
		{
			s.size[root2] += s.size[root1];
			s.parent[root1] = root2; 
		}
		
		
	}
}
