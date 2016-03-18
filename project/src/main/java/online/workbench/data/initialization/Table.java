package online.workbench.data.initialization;

public class Table
{
	public static final String Accounts =
			"CREATE TABLE IF NOT EXISTS Accounts " +
					"( " +
					"userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"password char(64), " +
					"username varchar(32) NOT NULL, " +
					"email varchar(254) NOT NULL, " +
					"avatar varchar(256), " +
					"confirmed tinyint(1) DEFAULT 1 " +
					")";

	public static final String BenchNodeContent =
			"CREATE TABLE IF NOT EXISTS BenchNodeContent " +
					"( " +
					"bNodeId bigint PRIMARY KEY NOT NULL, " +
					"lastEditor int UNSIGNED, " +
					"lastEdit bigint UNSIGNED, " +
					"content text, " +
					"type VARCHAR(32) NOT NULL " +
					")";

	public static final String BenchNodes =
			"CREATE TABLE IF NOT EXISTS BenchNodes " +
					"( " +
					"bNodeId INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"userId int UNSIGNED, " +
					"benchId int UNSIGNED NOT NULL, " +
					"x smallint NOT NULL, " +
					"y smallint NOT NULL, " +
					"width smallint UNSIGNED NOT NULL, " +
					"height smallint UNSIGNED NOT NULL, " +
					"title text, " +
					"created bigint UNSIGNED, " +
					"archived tinyint(1) DEFAULT 0 " +
					")";

	public static final String Benches =
			"CREATE TABLE IF NOT EXISTS Benches " +
					"( " +
					"benchId INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"owner int UNSIGNED NOT NULL, " +
					"created bigint UNSIGNED, " +
					"title varchar(256), " +
					"background varchar(256),  " +
					"archived tinyint(1) DEFAULT 0 " +
					")";

	//TODO: Fix for SQLite
	public static final String Folders =
			"CREATE TABLE IF NOT EXISTS Folders " +
					"( " +
					"folderId bigint PRIMARY KEY AUTOINCREMENT, " +
					"userId int UNSIGNED NOT NULL, " +
					"name char(255), " +
					"archived tinyint(1) DEFAULT 0 " +
					")";

	public static final String Members =
			"CREATE TABLE IF NOT EXISTS Members " +
					"( " +
					"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"benchId int UNSIGNED NOT NULL, " +
					"userId int UNSIGNED NOT NULL, " +
					"level tinyint DEFAULT 0 " +
					")";

	public static final String Sessions =
			"CREATE TABLE IF NOT EXISTS Sessions " +
					"( " +
					"userId INTEGER PRIMARY KEY NOT NULL, " +
					"token char(128), " +
					"time bigint UNSIGNED " +
					")";

	//TODO: Fix for SQLite
	public static final String UserNodeContent =
			"CREATE TABLE IF NOT EXISTS UserNodeContent " +
					"( " +
					"uNodeId bigint PRIMARY KEY NOT NULL, " +
					"lastEditor int UNSIGNED, " +
					"lastEdit bigint UNSIGNED, " +
					"content text " +
					")";

	//TODO: Fix for SQLite
	public static final String UserNodes =
			"CREATE TABLE IF NOT EXISTS UserNodes " +
					"( " +
					"uNodeId bigint UNSIGNED AUTOINCREMENT, " +
					"folder bigint UNSIGNED, " +
					"userId int UNSIGNED, " +
					"title char(255), " +
					"archived tinyint(1) DEFAULT 0, " +
					"PRIMARY KEY (uNodeId), " +
					"FOREIGN KEY (folder) REFERENCES Folders(folderId) " +
					")";
}
