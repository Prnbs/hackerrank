class Cell{
	int cost;
	int parent;
}

public class StringMatching {
	final int MATCH = 0;
	final int INSERT = 1;
	final int DELETE = 2;
	int MAXSIZE = 30;
	Cell[][] m;
	
	public StringMatching(int size) {
		MAXSIZE = size;
		m = new Cell[MAXSIZE][MAXSIZE];
		for(int i = 0; i < MAXSIZE; i++)
		{
			for(int j = 0; j < MAXSIZE; j++)
			{
				m[i][j] = new Cell();
			}
		}
	}
	
	int string_compare(String s, String t)
	{
		int i, j , k;
		int[] opt = new int[3];
		
		for(i = 0; i < MAXSIZE; i++)
		{
			row_init(i);
			column_init(i);
		}
		
		for(i = 1; i < s.length(); i++)
		{
			System.out.println("in S "+ s.charAt(i));
			for(j = 1; j < t.length(); j++)
			{
				opt[MATCH]  = m[i-1][j-1].cost + match(s.charAt(i), t.charAt(j));
				opt[INSERT] = m[i][j-1].cost + indel(t.charAt(j));
				opt[DELETE] = m[i-1][j-1].cost + indel(s.charAt(i));
				
				m[i][j].cost   = opt[MATCH];
				m[i][j].parent = MATCH;
				
				System.out.println("In T " + t.charAt(j));
				System.out.println("Operation Match " + opt[MATCH]);
				System.out.println("Operation Insert " + opt[INSERT]);
				System.out.println("Operation Delete " + opt[DELETE]);
				
				for(k=INSERT ; k < DELETE; k++)
				{
					if(opt[k] < m[i][j].cost)
					{
						m[i][j].cost   = opt[k];
						m[i][j].parent = k;
					}
				}
			}
		}
		int[] index = goal_cell(s, t);
		return m[index[0]][index[1]].cost;
	}
	
	int[] goal_cell(String s, String t)
	{
		int[] result = new int[2];
		result[0] = s.length() - 1;
		result[1] = t.length() - 1;
		return result;
	}
	
	int match(char c, char d)
	{
		if(c == d) return 0;
		else return 1;
	}
	
	int indel(char c)
	{
		return 1;
	}
	
	void row_init(int i)
	{
		m[0][i].cost = i;
		if(i > 0)
			m[0][i].parent = INSERT;
		else
			m[0][i].parent = -1;
	}
	
	void column_init(int i)
	{
		m[i][0].cost = i;
		if(i > 0)
			m[i][0].parent = DELETE;
		else
			m[i][0].parent = -1;
	}
	
	void print_cost(int elem){
		for(int i = 0; i < MAXSIZE; i++)
		{
			for(int j = 0; j < MAXSIZE; j++)
			{
				if(m[i][j].cost < 10)
				{
					if(elem == 0)
						System.out.print(" "+ m[i][j].cost + " ");
					else
						System.out.print(" "+ m[i][j].parent + " ");
				}
				else
				{
					if(elem == 0)
						System.out.print(m[i][j].cost + " ");
					else
						System.out.print(m[i][j].parent + " ");
						
				}
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		 String t = " you";
		 String s = " thou";
		 int size = Math.max(t.length(), s.length());
		 StringMatching strMatch = new StringMatching(size);
		
		 strMatch.string_compare(s, t);
		 strMatch.print_cost(0);
		 strMatch.print_cost(1);
	}

}
