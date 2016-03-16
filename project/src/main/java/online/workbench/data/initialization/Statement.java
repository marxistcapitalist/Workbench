package online.workbench.data.initialization;

public class Statement
{
	public static final String UPDATE_EMAIL = "UPDATE `Accounts` SET `email`= ? WHERE `userId` = ?;";

	public static final String UPDATE_USERNAME = "UPDATE `Accounts` SET `username`= ? WHERE `userId` = ?;";

	public static final String UPDATE_AVATAR = "UPDATE `Accounts` SET `avatar`= ? WHERE `userId` = ?;";

	public static final String UPDATE_PASSWORD = "UPDATE `Accounts` SET `password`= ? WHERE `userId` = ?;";

	public static final String INVALIDATE_TOKEN = "UPDATE `Sessions` SET `time`= ? WHERE `token` = ?;";

	public static final String CHECK_USERNAME_AVAILABILITY = "SELECT `username` FROM `Accounts` WHERE `username` = ?;";

	public static final String CHECK_EMAIL_AVAILABILITY = "SELECT `email` FROM `Accounts` WHERE `email` = ?;";

	public static final String LOAD_TOKEN = "SELECT `token`, `time` FROM `Sessions` WHERE `userId` = ?;";

	public static final String UPDATE_TOKEN = "UPDATE `Sessions` SET `token`= ? , `time`= ? WHERE `userId` = ?;";

	public static final String INSERT_TOKEN = "INSERT INTO `Sessions`(`userId`, `token`, `time`) VALUES ( ? , ? , ? );";

	public static final String VALIDATE_USER_LOGIN = "SELECT `password` FROM `Accounts` WHERE `userId` = ?;";

	public static final String LOAD_USER__INFO = "SELECT `username`, `email`, `avatar`, `confirmed` FROM `Accounts` WHERE `userId` = ?;";

	public static final String LOAD_USER__BENCH_COUNT = "SELECT `benchId` FROM `Members` WHERE `userId` = ?;";

	public static final String LOAD_USER__BENCH_DATA = "SELECT `owner`, `created`, `title`, `background`, `archived`, FROM `Benches` WHERE `benchId` = ?;";

	public static final String LOAD_USER__BENCH_DATA_OWNER_USERNAME = "SELECT `username` FROM `Accounts` WHERE `userId` = ?;";

	public static final String LOAD_BENCH__DATA = "SELECT `owner`, `created`, `title`, `background` FROM `Benches` WHERE `benchId` = ?;";

	public static final String LOAD_BENCH__GET_USER_IDS = "SELECT `userId`, `level` FROM `Members` WHERE `benchId` = ?;";

	public static final String LOAD_BENCH__GET_BENCH_NODE_DATA = "SELECT `bNodeId`, `userId`, `created`, `title`, `x`, `y`, `width`, `height`, `archived` FROM `BenchNodes` WHERE `benchId` = ?;";

	public static final String LOAD_BENCH__GET_BENCH_NODE_CONTENT = "SELECT `bNodeId`, `lastEdit`, `type`, `content` FROM `BenchNodeContent` WHERE `benchId` = ?;";

	public static final String COUNT_BENCH_MEMBERS = "SELECT `id` FROM `Members` WHERE `benchId` = ?;";

	public static final String CREATE_USER__INITIAL_DATA = "INSERT INTO `Accounts`(`username`, `email`) VALUES ( ? , ? );";

	public static final String CREATE_USER__GET_ID = "SELECT `userId` FROM `Accounts` WHERE `username` = ?;";

	public static final String CREATE_USER__INSERT_PASSWORD = "UPDATE `Accounts` SET `password`= ? WHERE `userId` = ?;";

	public static final String CREATE_BENCH__INSERT_DATA = "INSERT INTO `Benches` (`owner`, `created`, `title`, `background`) VALUES ( ? , ? , ? , ? );";

	public static final String CREATE_BENCH__GRAB_BENCH_ID = "SELECT `benchId` FROM `Benches` WHERE `owner` = ? AND `created` = ? AND `title` = ?;";

	public static final String CREATE_BENCH__ADD_OWNER_AS_MEMBER = "INSERT INTO `Members` (`benchId`, `userId`, `level`) VALUES ( ? , ? , ? );";

	public static final String ARCHIVE_BENCH = "UPDATE `Benches` SET `archived`= ? WHERE `benchId` = ?;";

	public static final String BENCH_NODE_CONTENT_EDIT = "UPDATE `BenchNodeContent` SET `content`= ? WHERE `bNodeId` = ?;";

	public static final String BENCH_NODE_CONTENT_TYPE_EDIT = "UPDATE `BenchNodeContent` SET `type`= ? WHERE `bNodeId` = ?;";

	public static final String BENCH_NODE_MOVE = "UPDATE `BenchNodes` SET `x`= ? , `y`= ? WHERE `bNodeId` = ?;";

	public static final String BENCH_NODE_RESIZE = "UPDATE `BenchNodes` SET `w`= ? , `h`= ? WHERE `bNodeId` = ?;";

	public static final String BENCH_NODE_RENAME =  "UPDATE `BenchNodes` SET `title`= ? WHERE `bNodeId` = ?;";

	public static final String BENCH_NODE_ARCHIVE = "UPDATE `BenchNodes` SET `archived= ? WHERE `bNodeId` = ?;";

	public static final String BENCH_NODE_CREATE__MAIN = "INSERT INTO `BenchNodes` (`userId`, `benchId`, `x`, `y`, `width`, `height`, `title`) VALUES (?, ?, ?, ?, ?, ?, ?);";

	public static final String BENCH_NODE_CREATE__GRAB_BENCH_NODE_ID = "SELECT `bNodeId` FROM `BenchNodes` WHERE `userId` = ? AND `benchId` = ? AND `title` = ?;";

	public static final String BENCH_NODE_CREATE__SET_CONTENT_ENTRY = "INSERT INTO `BenchNodeContent` (`bNodeId`, `content`, `type`) VALUES (?, ?, ?);";

	public static final String GRAB_EMAIL = "SELECT `email` FROM `Accounts` WHERE `username` = ?;";

	public static final String GRAB_USER = "SELECT `username` FROM `Accounts` WHERE `email` = ?;";

	public static final String GRAB_ID = "SELECT `userId` FROM `Accounts` WHERE `username` = ? OR `email` = ?;";

	public static final String BENCH_EDIT_BACKGROUND = "UPDATE `Benches` SET `background`= ? WHERE `benchId` = ?;";

	public static final String BENCH_EDIT_TITLE = "UPDATE `Benches` SET `title`= ? WHERE `benchId` = ?;";

	public static final String BENCH_EDIT_RESIZE___DO_NOT_USE_WILL_BREAK_EVERYTHING = "UPDATE `Benches` SET `width`= ?, `height`= ? WHERE `benchId` = ?;";

	public static final String BENCH_USER_ADD = "INSERT INTO `Members` (`benchId`, `userId`, `level`) VALUES ( ? , ? , ? );";

	public static final String BENCH_USER_MODIFY = "UPDATE `Members` SET `level` = ? WHERE `userId` = ? AND `benchId` = ?;";

	public static final String BENCH_USER_REMOVE = "DELETE FROM `Members` WHERE `userId` = ? AND `benchId` = ?;";
}
