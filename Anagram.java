import java.util.Arrays;
import java.util.Scanner;


public class Anagram {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String line1 = scanner.nextLine();
		String line2 = scanner.nextLine();
		scanner.close();
		
		int asciiVal  = 0;
		int[] arrLine1 = new int[26];
		int[] arrLine2 = new int[26];
		
		Arrays.fill(arrLine1, 0);
		Arrays.fill(arrLine2, 0);
		for(int i = 0; i < line1.length() ; i++)
		{
			asciiVal = line1.charAt(i) - 'a';
			arrLine1[asciiVal]++;
		}
		
		for(int i = 0; i < line2.length() ; i++)
		{
			asciiVal = line2.charAt(i) - 'a';
			arrLine2[asciiVal]++;
		}
		
		int count = 0;
		for(int i = 0; i < 26; i ++)
		{
			count += Math.abs(arrLine1[i] - arrLine2[i]);
		}
		
		System.out.println(count);
	}

}
