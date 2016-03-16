package online.workbench.security;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Token
{
	private static SecureRandom random = new SecureRandom();

	public static String gen()
	{
		String token = new BigInteger(640, random).toString(32);

		while (token.length() != 128)
		{
			token = new BigInteger(640, random).toString(32);
		}

		return token.toUpperCase();
	}
}
