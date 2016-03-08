package online.workbench.server;

public interface PUB
{
    /**
     * Gives the public server a reference to the {@link API} instance
     *
     * @param api API instance
     */
    void setLinkages(API api);
}
