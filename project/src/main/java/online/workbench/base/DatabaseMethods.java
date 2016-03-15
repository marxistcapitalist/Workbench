package online.workbench.base;

import online.workbench.api.TokenManager;
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
	 * <p>
	 * Should rehash and store the password
	 *
	 * @param id Whose email is being updated
	 * @param email The new email
	 */
	void updateUserEmailAsync(int id, String email);

	/**
	 * Changes username field in the database for a user
	 * Does not do any data validation
	 * Does not update information locally
	 * <p>
	 * Should rehash and store the password
	 *
	 * @param id Whose username is being changed
	 * @param name The new username
	 */
	void updateUserNameAsync(int id, String name);

	/**
	 * Updates the user avatar in the database
	 * Does not update information locally
	 * <p>
	 * In the future this will not be needed as the profile images will be served
	 * statically and will just need to be refreshed client side without
	 * any database involvement
	 *
	 * @param id Whose avatar is being updated
	 * @param avatar For now a hexcode for the avatar color
	 */
	void updateUserAvatarAsync(int id, String avatar);

	/**
	 * Updates the user's password in the database
	 * Does not update information locally
	 * DOES NOT HASH INTERNALLY
	 *
	 * @param id Whose password is being changed
	 * @param passwordHash The new password hash
	 */
	void updateUserPasswordAsync(int id, String passwordHash);

	/**
     * Empties the token field in the database
     * to enact a global logout of sorts
     *
     * @param token token to invalidate
     */
    void invalidateToken(String token);

	/**
	 * Empties the token field in the database
	 * to enact a global logout of sorts
	 *
	 * @param id user id for which to invalidate token
	 */
	void invalidateToken(int id);

	/**
     * Determines whether or not a username has already been used
     *
     * @param username Supplied username to check
     * @return true if the username is available
     */
    boolean checkUsernameAvailability(String username);

	/**
	 * Determines whether or not an email has already been used
	 *
	 * @param email Supplied email to check
	 * @return true if the email is available
	 */
	boolean checkEmailAvailability(String email);

	/**
	 * Loads a token object from the database for cache
	 *
	 * @param id User id for which token is being retrieved
	 * @return token object with empty token if not issued
	 */
    TokenManager.Token loadToken(int id);

	/**
     * Issues a new token to an account
     *
     * @param id account id the token is being issued to
     * @return the new token
     */
    String generateToken(int id);

    /**
     * Checks if user login is valid
	 * DOES NOT HASH INTERNALLY
     *
     * @param id User's account ID
     * @param passwordHash User's password hash
     * @return true if valid, false otherwise
     */
    boolean validateUserLogin(int id, String passwordHash);

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
	 * DOES NOT HASH INTERNALLY
     *
     * @param username supplied unique username
     * @param email supplied email
     * @return the created user's user ID
     */
    int createNewUser(String username, String email);

	/**
	 * Sets a user's initial password once a user ID is obtained
	 * is blocking
	 *
	 * @param id User's new ID
	 * @param passwordHash hashed password
	 */
	void setPassword(int id, String passwordHash);

	/**
	 * Creates a new bench in the database
	 *
	 * @param user Creator of the bench
	 * @param title Title of the bench
	 * @param w Initial bench width
	 * @param h Initial bench height
	 * @return Constructed bench, now with valid ID
	 */
    Bench createBench(User user, String title, int w, int h);

	/**
	 * Archives a bench object by setting archive to true
	 *
	 * @param id ID of bench to archive
	 */
	void archiveBench(int id);

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
	 * Submits the retitling of a node to the database asynchronously
	 *
	 * @param type Type of node
	 * @param id Node id in the database
	 * @param title New node title
	 */
	void submitNodeRenameAsync(NodeType type, int id, String title);

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

	/**
	 * Retrieves the email paired with a username in the database
	 *
	 * @param username the username to search for
	 * @return the found email if any, null otherwise
	 */
	String grabEmail(String username);

	/**
	 * Retrieves the username paired with an email in the database
	 *
	 * @param email the email to search for
	 * @return the found username if any, null otherwise
	 */
	String grabUser(String email);

	/**
	 * Retrieves the user id associated with an email OR username in the database
	 * @param loginKey Email or Username provided
	 * @return found id, 0 otherwise
	 */
	int grabId(String loginKey);

	/**
	 * Submits the change of background color or image to database
	 *
	 * @param bench The bench that is being changed
	 * @param background The new image path or hex color
	 */
	void submitBenchBackgroundEdit(Bench bench, String background);

	/**
	 * Submits a new bench title to the database
	 *
	 * @param bench The bench that is being changed
	 * @param title The new title of the bench
	 */
	void submitBenchTitleEdit(Bench bench, String title);

	/**
	 * Submits a resize of a workbench to the database
	 *
	 * @param bench The bench that is being resized
	 * @param w The new bench width
	 * @param h The new bench height
	 */
	void submitBenchResize(Bench bench, int w, int h);

	/**
	 * Adds user to list of bench's members and adds bench to
	 * User's benches that they are a member of
	 *
	 * @param bench The bench the user is being added to
	 * @param user The user who is being added to the bench
	 * @param role The starting role at which to add the user
	 */
	void addUserToBench(Bench bench, int user, PermissionLevel role);

	/**
	 * Removes the specified user from the bench's user list and
	 * removes the bench from the user's list of joined benches
	 *
	 * @param bench The bench from which the user is being removed
	 * @param user The user who is being removed from the bench
	 */
	void removeUserFromBench(Bench bench, int user);

	/**
	 * Modifies a user role in a bench
	 * Modifies the user role in the bench list of the user object
	 *
	 * @param bench The bench whose user is being modified
	 * @param user The user whose permission level is being modified
	 * @param role the new permission level
	 */
	void modifyUserInBench(Bench bench, int user, PermissionLevel role);
}
