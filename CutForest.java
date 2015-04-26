import java.util.ArrayList;
import java.util.Scanner;

/*class Node{
	int cell;
	ArrayList<Node> adjacent;
	boolean cut;
	
	Node(int _cell){
		cell = _cell;
		adjacent = new ArrayList<Node>();
		cut = false;
	}
}*/


public class CutForest {
	Node[] allNodes;
	
	public static void main(String[] args) {
		CutForest cuts = new CutForest();
		
		Scanner scanner = new Scanner(System.in);
		int N = scanner.nextInt();
		int M = scanner.nextInt();
		cuts.allNodes = new Node[N];
		int parentNode = 0;
		int childNode = 0;
		
		for(int i = 0; i < N; i++)
		{
			cuts.allNodes[i] = new Node(i+1);
		}

		for(int i = 0; i < M; i++){
			childNode = scanner.nextInt();
			parentNode = scanner.nextInt();
			cuts.allNodes[parentNode-1].adjacent.add(cuts.allNodes[childNode-1]);
		}
		scanner.close();
		
		/*for(int i = 0; i < N; i++){
			System.out.println(cuts.allNodes[i].cell);
			for(Node adj: cuts.allNodes[i].adjacent){
				System.out.print(adj.cell + " ");
			}
			System.out.println("");
		}*/
		
		/*int children = cuts.CountChildren(cuts.allNodes[0]);
		System.out.println(children);*/
		int possibleCuts = cuts.CountCuts(cuts.allNodes[0]);
		
		
		System.out.println(possibleCuts);

	}
	
	//dfs
	public int CountCuts(Node root)
	{
		int cuts = 0;
		for(Node child : root.adjacent)
		{
			int children = CountChildren(child);
			if(children!= 0 && (children+1)%2 == 0)
				cuts++;
			cuts += CountCuts(child);
		}
		return cuts;
	}
	
	//bfs
	public int CountChildren(Node root)
	{
		int count = 0;
		for(Node adj : root.adjacent)
		{
			count++;
			count += CountChildren(adj);
		}
		return count;
	}

}
