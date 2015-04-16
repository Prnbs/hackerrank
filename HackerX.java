import java.util.ArrayList;
import java.util.Scanner;

class Missile
{
	int timeLeft;
	int frequency;
	int depthAtThisLevel;
	ArrayList<Missile> adjacent;
	boolean destroyed;
	Missile parent;
	
	Missile(int _timeLeft, int _freq)
	{
		timeLeft = _timeLeft;
		frequency = _freq;
		adjacent = new ArrayList<Missile>();
		destroyed = false;
		parent = null;
		depthAtThisLevel = 0;
	}
}

public class HackerX {

	Missile[] missiles;
	int maxDepth;
	Missile deepestMissile;
	
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
			war.missiles[i] = new Missile(t, f);
		}
		war.CreateGraph();
		war.DFSController();
		scanner.close();
	}
	
	void DFSController()
	{
		int counthackerX = 0;
		for(Missile missile : missiles)
		{
			if(!missile.destroyed)
			{
				counthackerX++;
				RecursiveDFS(missile, null, 0);
				MarkDestroyed(deepestMissile);
			}
		}
		System.out.println(counthackerX);
	}
	
	void MarkDestroyed(Missile last)
	{
		while(last != null)
		{
			last.destroyed = true;
			last = last.parent;
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
				}
			}
		}
	}
	
	void RecursiveDFS(Missile destroyThis, Missile parent, int costHere)
	{
		costHere++;
		if(maxDepth < destroyThis.depthAtThisLevel && !destroyThis.destroyed)
		{
			maxDepth = destroyThis.depthAtThisLevel;
			deepestMissile = destroyThis;
		}
		if(destroyThis.adjacent.size() == 0 && !destroyThis.destroyed) //end of the line
		{
			if(destroyThis.depthAtThisLevel < costHere)
				destroyThis.parent = parent;
		}
		for( Missile missile : destroyThis.adjacent)
		{
			if(missile.depthAtThisLevel < costHere && !missile.destroyed)
			{
				missile.depthAtThisLevel = costHere;
				missile.parent = destroyThis;
				RecursiveDFS(missile, destroyThis, costHere);
			}
		}
	}
}
