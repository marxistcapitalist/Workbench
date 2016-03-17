package online.workbench.websocket;

import online.workbench.model.struct.User;
import org.eclipse.jetty.websocket.api.Session;

import java.net.InetSocketAddress;
import java.util.HashMap;

public class SessionMap extends HashMap<Session, User>
{
	@Override
	public boolean containsKey(Object object)
	{
		InetSocketAddress a = ((Session) object).getRemoteAddress();

		for (Session session : this.keySet())
		{
			if (session.getRemoteAddress().getHostString().equalsIgnoreCase(a.getHostString()))
			{
				return true;
			}
		}
		return false;
	}

}
