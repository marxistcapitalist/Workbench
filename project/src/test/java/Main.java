import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

public class Main
{
	public static void main(String[] args)
	{
		ArrayList<Integer> thing = new ArrayList<>();

		SecureRandom random = new SecureRandom();

		for (int i = 0; i < 2000000000; i++)
		{
			String string = new BigInteger(640, random).toString(32);

			int length = string.length();

			if (length != 128 && !thing.contains(length))
			{
				thing.add(length);
				System.out.println(length);
			}


		}

	}
}
