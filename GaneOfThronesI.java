import java.util.Scanner;


public class GaneOfThronesI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		scanner.close();
		int N = 0;
		char c = '-';
		int asciiVal = 0;
		for (int i = 0; i < input.length(); i++)
		{
			c = input.charAt(i);
			asciiVal = 'a' - c;
			N = N ^ (1 << asciiVal);
		}
		int sum = 0;
		while(N != 0)
		{
			sum += (N >> 1) & 1;
			N = N >> 1;
		}
		if(sum <= 1)
			System.out.println("YES");
		else
			System.out.println("NO");
	}

}
