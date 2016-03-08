package online.workbench.jetty.handlers.api.accounts;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import online.workbench.jetty.handlers.api.objects.Protocol;

import java.io.IOException;
class Login implements HttpHandler
{
	public void handle(HttpExchange exchange) throws IOException
	{
		String requestMethod = exchange.getRequestMethod();

		if (requestMethod.equalsIgnoreCase("POST"))
		{
			Gson gson = new Gson();
			gson.fromJson(exchange.getRequestBody().toString(), Protocol.HTTP_CLIENT_LOGIN.getClass());
		}
	}


}
