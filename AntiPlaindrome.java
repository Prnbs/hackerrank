import java.util.Scanner;

public class AntiPlaindrome {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int numTestCases = scanner.nextInt();
		int strLen = 0;
		int alphabetLen = 0;
		long result = 0;
		for(int i = 0; i < numTestCases ; i ++)
		{
			strLen = scanner.nextInt();
			alphabetLen = scanner.nextInt();
			result = (long) (Math.pow(alphabetLen, strLen) - Math.pow(alphabetLen, Math.ceil(strLen/2)));
			System.out.println(result);
		}
		scanner.close();
	}
}
