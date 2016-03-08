package online.workbench.server;

public interface WSS
{
    /**
     * Gives the websocket a reference to the {@link API} instance
     *
     * @param api API instance
     */
    void setLinkages(API api);
}
