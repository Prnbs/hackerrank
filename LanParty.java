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
	
	UnionSet(int numwires)
	{
		parent = new int[numwires];
		size = new int[numwires+1];
		n = numwires;
	}
	
	void Set_Init()
	{
		for(int i = 0; i < n; i++)
		{
			parent[i] = i;
			size[i] = 1;
		}
	}
}

public class LanParty {
	Player[] allPlayers;
	int[] totalPlayersForGame;
	
	
	public static void main(String[] args) {
		LanParty play = new LanParty();
		Scanner scanner = new Scanner(System.in);
		int numPlayers = scanner.nextInt();
		int numGames = scanner.nextInt();
		int numWires = scanner.nextInt();
		
		play.allPlayers = new Player[numPlayers];
		play.totalPlayersForGame = new int[numGames+1];
		
		for(int i = 0; i < numPlayers; i++)
		{
			play.allPlayers[i] = new Player(scanner.nextInt());
			play.totalPlayersForGame[scanner.nextInt()]++;
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
		
	}

}
