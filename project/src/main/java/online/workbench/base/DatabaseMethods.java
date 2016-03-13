package online.workbench.base;

import online.workbench.model.struct.*;

import java.util.List;

public interface DatabaseMethods
{

	/**
     * Empties the token field in the database
     * to enact a global logout of sorts
     *
     * @param token token to invalidate
     */
    void invalidateToken(String token);

	/**
     * Determines whether or not a username has already been used
     *
     * @param username Supplied username to check
     * @return true if the username is available
     */
    boolean checkUsernameAvailability(String username);

	/**
     * Determines whether client token is valid and matches id
     *
     * @param id client id
     * @param token client token
     * @return true if valid, false otherwise
     */
    boolean checkToken(int id, String token);

	/**
     * Issues a new token to an account
     *
     * @param id account id the token is being issued to
     * @return the new token
     */
    String issueToken(int id);

    /**
     * Checks if user login is valid
     * @param username supplied username
     * @param passwordHash supplied SHA256 password hash
     * @return user id; '0' if invalid
     */
    int validateUserName(String username, String passwordHash);

    /**
     * Checks if user login is valid
     * This method should always produce a login failure if email is not confirmed
     * @param email supplied email
     * @param passwordHash supplied SHA256 password hash
     * @return user id; '0' if invalid
     */
    int validateUserEmail(String email, String passwordHash);

    /**
     * Loads a user and all associated data from the database
     * @param userId - authenticated user's ID
     * @return the user; a user with an ID of '0' if user does not exist
     */
    User loadUser(int userId);

    /**
     * Loads a workbench and all associated data from the database
     * @param benchId - the ID of the requested bench
     * @return the Bench; a bench with an ID of '0' if bench does not exist
     */
    Bench loadBench(int benchId);

	/**
     * Creates a new user in the database
     *
     * @param username supplied unique username
     * @param email supplied email
     * @param password supplied password, unhashed
     * @return the created user's user ID
     */
    int createUser(String username, String email, String password);

    /**
     * Creates a bench in the database
     * @param bench - Bench object with an ID of '0'
     * @return Valid Bench object
     */
    Bench createBench(Bench bench);

    /**
     * Submits content edit of node batch to database
     * @param nodes - valid Nodes of any type
     */
    void submitNodeEdit(List<Node> nodes);

    /**
     * Submits content type edit of node batch to database
     * @param nodes - valid Nodes of any type
     */
    void submitNodeContent(List<Node> nodes);

    /**
     * Submits location edit of node batch to database
     * @param nodes - valid BenchNodes
     */
    void submitNodeMove(List<BenchNode> nodes);

    /**
     * Submits width or height edit of node batch to database
     * @param nodes - valid BenchNodes
     */
    void submitNodeResize(List<BenchNode> nodes);

    /**
     * Submits archive action of node batch to database
     * @param nodes - valid Nodes to be archived
     */
    void submitNodeArchive(List<Node> nodes);

    /**
     * Adds a single UserNode to database
     * @param node - created UserNode with id of '0'
     * @return valid UserNode
     */
    UserNode submitNodeCreate(UserNode node);

    /**
     * Adds a single BenchNode to database
     * @param node - created BenchNode with id of '0'
     * @return valid BenchNode
     */
    BenchNode submitNodeCreate(BenchNode node);

    /**
     * Transfers a BenchNode to a User's cabinet
     * @param node - valid BenchNode
     * @param user - the User the node is being copied to
     * @return the created, valid UserNode
     */
    UserNode submitNodeCopy(BenchNode node, User user);

    /**
     * Transfers a UserNode to a Bench
     * @param node - valid UserNode
     * @param bench - the Bench the node is being copied to
     * @return the created, valid BenchNode
     */
    BenchNode submitNodeCopy(UserNode node, Bench bench);
}
