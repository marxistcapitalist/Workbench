package online.workbench.base;

import online.workbench.model.struct.*;

import java.sql.Connection;
import java.util.List;

public interface DatabaseMethods
{
	/**
	 * Changes email field in the database for a user
	 * Does not update information locally
	 * Does not do any data validation
	 * <p>
	 * For now, the emails will auto confirm as emailing
	 * hasn't been implemented in the software
	 *
	 * @param user User whose email is being updated
	 * @param email The new email
	 */
	void updateUserEmailAsync(User user, String email);

	/**
	 * Changes username field in the database for a user
	 * Does not do any data validation
	 * Does not update information locally
	 *
	 * @param user User whose username is being changed
	 * @param name The new username
	 */
	void updateUserNameAsync(User user, String name);

	/**
	 * Updates the user avatar in the database
	 * Does not update information locally
	 * <p>
	 * In the future this will not be needed as the profile images will be served
	 * statically and will just need to be refreshed client side without
	 * any database involvement
	 * <p>
	 *
	 * @param user User whose avatar is being updated
	 * @param avatar For now a hexcode for the avatar color
	 */
	void updateUserAvatarAsync(User user, String avatar);

	/**
	 * Updates the user's password in the database
	 * Does not update information locally
	 * DOES HASH INTERNALLY
	 *
	 * @param user User whose password is being changed
	 * @param password The new password
	 */
	void updateUserPasswordAsync(User user, String password);

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
	 * DOES HASH INTERNALLY
     *
     * @param username supplied username
     * @param password User's password
     * @return user id; '0' if invalid
     */
    int validateUserName(String username, String password);

    /**
     * Checks if user login is valid
     * This method should always produce a login failure if email is not confirmed
	 * DOES HASH INTERNALLY
     *
     * @param email supplied email
     * @param password User's password
     * @return user id; '0' if invalid
     */
    int validateUserEmail(String email, String password);

    /**
     * Loads a user and all associated data from the database
     *
     * @param userId - authenticated user's ID
     * @return the user; a user with an ID of '0' if user does not exist
     */
    User loadUser(int userId);

    /**
     * Loads a workbench and all associated data from the database
     *
     * @param benchId - the ID of the requested bench
     * @return the Bench; a bench with an ID of '0' if bench does not exist
     */
    Bench loadBench(int benchId);

	/**
	 * Returns the number of members in a bench if that bench exists
	 *
	 * @param benchId ID of the bench
	 * @return Number of users or zero if nonexistant
	 */
	int countBenchMembers(int benchId);

	/**
     * Creates a new user in the database
	 * DOES HASH INTERNALLY
     *
     * @param username supplied unique username
     * @param email supplied email
     * @param password supplied password, unhashed
     * @return the created user's user ID
     */
    int createUser(String username, String email, String password);

    /**
     * Creates a bench in the database
     *
     * @param bench - Bench object with an ID of '0'
     * @return Valid Bench object
     */
    Bench createBench(Bench bench);

	/**
     * Submits edit of node content to database asynchronously
     *
     * @param type Type of node
     * @param id Node id in the database
     * @param content New content
     */
    void submitNodeContentEditAsync(NodeType type, int id, String content);


	/**
	 * Submits edit of node content type to database asynchronously
     *
     * @param type Type of node
     * @param id Node id in the database
     * @param cType New content type
     */
    void submitNodeContentTypeEditAsync(NodeType type, int id, ContentType cType);

	/**
     * Submits new final node location to database asynchronously
     *
     * @param type Type of node
     * @param id Node id in the database
     * @param x New x location
     * @param y new y location
     */
    void submitNodeMoveAsync(NodeType type, int id, int x, int y);

	/**
     * Submits new final width and height of node to database asynchronously
     *
     * @param type Type of node
     * @param id Node id in the database
     * @param w New width of node
     * @param h New height of node
     */
    void submitNodeResizeAsync(NodeType type, int id, int w, int h);

	/**
     * Submits archival of node to database asynchronously
     *
     * @param type Type of Node
     * @param id Node id in the database
     */
    void submitNodeArchiveAsync(NodeType type, int id);

    /**
     * Adds a single UserNode to database
     *
     * @param node - created UserNode with id of '0'
     * @return valid UserNode
     */
    UserNode submitNodeCreate(UserNode node);

    /**
     * Adds a single UserNode to database asynchronously
     *
     * @param node - created UserNode with id of '0'
     */
    void submitNodeCreateAsync(UserNode node);

    /**
     * Adds a single BenchNode to database asynchronously
     *
     * @param node - created BenchNode with id of '0'
     */
    void submitNodeCreateAsync(BenchNode node);

    /**
     * Adds a single BenchNode to database
     *
     * @param node - created BenchNode with id of '0'
     * @return valid BenchNode
     */
    BenchNode submitNodeCreate(BenchNode node);

    /**
     * Transfers a BenchNode to a User's cabinet
     *
     * @param node - valid BenchNode
     * @param user - the User the node is being copied to
     * @return the created, valid UserNode
     */
    UserNode submitNodeCopyAsync(BenchNode node, User user);

    /**
     * Transfers a UserNode to a Bench
     *
     * @param node - valid UserNode
     * @param bench - the Bench the node is being copied to
     * @return the created, valid BenchNode
     */
    BenchNode submitNodeCopy(UserNode node, Bench bench);
}
