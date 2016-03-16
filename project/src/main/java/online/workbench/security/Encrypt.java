package online.workbench.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * PBKDF2 WITH HMACSHA256
 */
public class Encrypt
{

	final private static char[] hex = "0123456789ABCDEF".toCharArray();

	public static String hash(String username, String password, String email, int id)
	{
		try
		{
			byte[] result = getKey((password + id).getBytes(), mesh(username.toLowerCase(), email.toLowerCase()).getBytes(), 4096, 32);
			return (toHex(result));
		}
		catch (NoSuchAlgorithmException | InvalidKeyException e)
		{
			e.printStackTrace();
			return "";
		}
	}

	private static byte[] getKey( byte[] password, byte[] salt, int iterationCount, int dkLen) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException
	{
		SecretKeySpec keyspec = new SecretKeySpec( password, "HmacSHA256" );
		Mac prf = Mac.getInstance( "HmacSHA256" );
		prf.init( keyspec );
		int hLen = prf.getMacLength();
		int l = Math.max( dkLen, hLen);
		int r = dkLen - (l-1)*hLen;
		byte T[] = new byte[l * hLen];
		int ti_offset = 0;
		for (int i = 1; i <= l; i++)
		{
			F( T, ti_offset, prf, salt, iterationCount, i );
			ti_offset += hLen;
		}

		if (r < hLen)
		{
			byte DK[] = new byte[dkLen];
			System.arraycopy(T, 0, DK, 0, dkLen);
			return DK;
		}
		return T;
	}


	private static void F( byte[] dest, int offset, Mac prf, byte[] S, int c, int blockIndex)
	{
		final int hLen = prf.getMacLength();
		byte U_r[] = new byte[ hLen ];
		byte U_i[] = new byte[S.length + 4];
		System.arraycopy( S, 0, U_i, 0, S.length );
		INT( U_i, S.length, blockIndex );
		for( int i = 0; i < c; i++ )
		{
			U_i = prf.doFinal( U_i );
			xor( U_r, U_i );
		}

		System.arraycopy( U_r, 0, dest, offset, hLen );
	}

	private static void xor( byte[] dest, byte[] src )
	{
		for( int i = 0; i < dest.length; i++ )
		{
			dest[i] ^= src[i];
		}
	}

	private static void INT( byte[] dest, int offset, int i )
	{
		dest[offset + 0] = (byte) (i / (256 * 256 * 256));
		dest[offset + 1] = (byte) (i / (256 * 256));
		dest[offset + 2] = (byte) (i / (256));
		dest[offset + 3] = (byte) (i);
	}

	private static String mesh(final String a, final String b)
	{
		if (a == null || a.length() == 0)
		{
			return b;
		}
		else if (b == null || b.length() == 0)
		{
			return a;
		}
		else
		{
			final int aLength = a.length();
			final int bLength = b.length();
			final StringBuilder merged = new StringBuilder(aLength + bLength);

			for (int i = 0, j = 0; i < aLength && j < bLength; i++, j++)
			{
				merged.append(a.charAt(i)).append(b.charAt(j));
			}

			if (aLength != bLength)
			{
				if (aLength > bLength)
				{
					merged.append(a.substring(bLength));
				}
				else
				{
					merged.append(b.substring(aLength));
				}
			}
			return merged.toString();
		}
	}

	private static String toHex(byte[] bytes)
	{
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ )
		{
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hex[v >>> 4];
			hexChars[j * 2 + 1] = hex[v & 0x0F];
		}
		return new String(hexChars);
	}
}