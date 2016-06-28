package online.workbench.managers;

import online.workbench.data.WorkbenchDB;

import java.util.HashMap;
import java.util.Map;

public class TokenManager
{
	private HashMap<Integer, Token> tokens;
	private WorkbenchDB database;

	public TokenManager(WorkbenchDB database)
	{
		tokens = new HashMap<>();
		this.database = database;
	}

	public void invalidate(String token)
	{
		for (Map.Entry<Integer, Token> item : tokens.entrySet())
		{
			if (item.getValue().isValid(token))
			{
				database.invalidateToken(token.toUpperCase());
				tokens.remove(item.getKey());
				return;
			}
		}
	}

	public boolean check(int id, String clientToken)
	{
		if (!tokens.containsKey(id))
		{
			Token token = this.database.loadToken(id);

			if (token != null && token.isIssued())
			{
				this.tokens.put(id, token);
			}
			else
			{
				return true;
			}
		}

		return tokens.get(id).isValid(clientToken);
	}

	public String issue(int id)
	{
		String tokenString = this.database.generateToken(id);
		Token token = new Token(tokenString, System.currentTimeMillis());
		tokens.put(id, token);
		return tokenString;
	}

	public static class Token
	{
		private String token;
		private long time;

		public Token (String a, long b)
		{
			token = a;
			time = b;
		}

		public boolean isIssued()
		{
			if (token.isEmpty())
			{
				return false;
			}
			return true;
		}

		public boolean isValid(String token)
		{
			if ((time + 604800000) >= System.currentTimeMillis() && this.isIssued())
			{
				if (this.token.equalsIgnoreCase(token))
				{
					return true;
				}
			}
			return false;
		}
	}
}
