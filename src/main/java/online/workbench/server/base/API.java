package online.workbench.server;

public interface RESTAPI
{
    protected static String BASE_PATH = "/api";
    
    /**
     * Gives the restful api a reference to the {@link Websocket} instance
     *
     * @param ws websocket instance
     */
    void setLinkages(Websocket ws);

    /**
     * Creates user endpoint with basic user data
     * 
     * @param user basic user data
     * @param api backreference to api
     */
    void createUserEndpoint(UserMeta user, RESTAPI api);
    
    /**
     * Removes the user endpoint from deployment
     * Does not propogate changes to the database
     *
     * @param userId ID of user endpoint to be removed
     */
    void deleteUserEndpoint(int userId);
    
    /**
     * Creates bench endpoint with basic bench data
     * 
     * @param bench basic bench data
     * @param api backreference to api
     */
    void createBenchEndpoint(BenchMeta bench, RESTAPI api);
    
    /**
     * Removes the bench endpoint from deployment
     * Does not propogate changes to the database
     *
     * @param benchId ID of bench endpoint to be removed
     */
    void deleteBenchEndpoint(int benchId);
}
