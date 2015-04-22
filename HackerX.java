import java.util.ArrayList;
import java.util.Scanner;

class Missile
{
	int timeLeft;
	int frequency;
	int depthAtThisLevel;
	ArrayList<Missile> adjacent;
	//int[] adjMatrix;
	boolean destroyed;
	Missile parent;
	int ID;
	
	Missile(int _timeLeft, int _freq, int N)
	{
		timeLeft = _timeLeft;
		frequency = _freq;
		adjacent = new ArrayList<Missile>();
		destroyed = false;
		parent = null;
		depthAtThisLevel = 0;
		
		//delete
		//adjMatrix = new int[N];
	}
}

public class HackerX {

	Missile[] missiles;
	int maxDepth;
	Missile deepestMissile;
	int ID = 0;
	
	HackerX()
	{
		maxDepth = 0;
		deepestMissile = null;
	}
	
	public static void main(String[] args) {
		HackerX war = new HackerX();
		Scanner scanner = new Scanner(System.in);
		int numMissiles = scanner.nextInt();
		
		war.missiles = new Missile[numMissiles];
		int t = 0;
		int f = 0;
		for(int i = 0; i < numMissiles; i++)
		{
			t = scanner.nextInt();
			f = scanner.nextInt();
			war.missiles[i] = new Missile(t, f, numMissiles);
			war.missiles[i].ID = i+1;
		}
		war.CreateGraph();
		
		war.DFSController();
		scanner.close();
	}
	
//	void PrintAdjMatrix()
//	{
//		String line = "  ";
//		for(int i = 0; i < missiles.length; i++)
//			line += missiles[i].ID + "  ";
//		System.out.println(line);
//		line = "";
//		for(int i = 0; i < missiles.length; i++)
//		{
//			line += missiles[i].ID + "  ";
//			for(int j = 0; j < missiles.length; j++)
//			{
//				line += missiles[i].adjMatrix[j] + "  ";
//			}
//			System.out.println(line);
//			line = "";
//		}
//		
//	}
	
	void DFSController()
	{
		int counthackerX = 0;
		for(Missile missile : missiles)
		{
			if(!missile.destroyed)
			{
				counthackerX++;
				missile.parent = null;
				RecursiveDFS(missile, null, 0);
				MarkDestroyed(deepestMissile);
			//	PrintPaths(deepestMissile);
				ResetDepthAtLevel();
				maxDepth = 0;
				deepestMissile = null;
			}
		}
		System.out.println(counthackerX);
	}
	
	void PrintPaths(Missile deepest)
	{
		while(deepest != null)
		{
			if(deepest.parent != null)
				System.out.println(deepest.ID +"---->" + deepest.parent.ID);
			else
				System.out.println(deepest.ID +"----> Root" );
			deepest = deepest.parent;
		}
	}
	
	void MarkDestroyed(Missile last)
	{
		while(last != null)
		{
			last.destroyed = true;
			last = last.parent;
		}
	}
	
	void ResetDepthAtLevel()
	{
		for(Missile missile : missiles)
		{
			if(!missile.destroyed)
			{
				missile.depthAtThisLevel = 0;
				missile.parent = null;
			}
		}
	}
	
	void CreateGraph()
	{
		for(int i = 0; i < missiles.length; i++)
		{
			for(int j = i + 1; j < missiles.length; j++)
			{
				if(Math.abs((missiles[i].frequency - missiles[j].frequency)) <= 
						Math.abs(missiles[j].timeLeft - missiles[i].timeLeft))
				{
					//there is an edge from i to j with cost = cost of switching frequency
					missiles[i].adjacent.add(missiles[j]);
					//do we really need cost?
					
					//delete this
//					missiles[i].adjMatrix[j] = missiles[j].ID;
				}
//				else
//					//delete this
//					missiles[i].adjMatrix[j] = 0;
			}
		}
	}
	
	void RecursiveDFS(Missile destroyThis, Missile parent, int costHere)
	{
		costHere++;
		//global maxDepth seen at this DFS is lower than what this Node's has
		if(maxDepth < destroyThis.depthAtThisLevel 
				&& !destroyThis.destroyed)
		{
			maxDepth = destroyThis.depthAtThisLevel;
			//this is therefore the deepest missile we have yet seen
			deepestMissile = destroyThis;
		}
		//end of the line
		if(destroyThis.adjacent.size() == 0 
				&& !destroyThis.destroyed) 
		{
			if(destroyThis.depthAtThisLevel <= costHere)
			{
				destroyThis.parent = parent;
				destroyThis.depthAtThisLevel = costHere;
			}
		}
		for( Missile missile : destroyThis.adjacent)
		{
			if(!missile.destroyed && 
					missile.depthAtThisLevel <= costHere)
			{
				missile.depthAtThisLevel = costHere;
				missile.parent = destroyThis;
				RecursiveDFS(missile, destroyThis, costHere);
			}
		}
	}
}
