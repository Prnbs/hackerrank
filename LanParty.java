import java.util.Arrays;
import java.util.Scanner;
class Player
{
	int gameNum;
	int connectedTo;
	
	Player(int _gameNum)
	{
		gameNum = _gameNum;
	}
}

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
	Player[] allPlayers;
	int[] totalPlayersForGame;
	int[] currPlayersForGame;
	
	
	public static void main(String[] args) {
		LanParty play = new LanParty();
		Scanner scanner = new Scanner(System.in);
		int numPlayers = scanner.nextInt();
		int numGames = scanner.nextInt();
		int numWires = scanner.nextInt();
		
		play.allPlayers = new Player[numPlayers];
		play.totalPlayersForGame = new int[numGames+1];
		play.currPlayersForGame  = new int[numGames+1];
		Arrays.fill(play.currPlayersForGame, 0);
		
		UnionSet uSet = new UnionSet(numPlayers);
		uSet.Set_Init();
		for(int i = 0; i < numPlayers; i++)
		{
			int playerNum = scanner.nextInt();
			play.allPlayers[i] = new Player(playerNum);
			play.totalPlayersForGame[playerNum]++;
		}
		for(int i = 1; i <= numWires; i++)
		{
			play.Union_Sets(uSet, scanner.nextInt(), scanner.nextInt());
		}
		scanner.close();
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
