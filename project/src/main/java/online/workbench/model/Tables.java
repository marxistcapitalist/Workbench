package online.workbench.model;

public class Tables
{
	public static final String Accounts =
			"CREATE TABLE IF NOT EXISTS Accounts " +
			"( " +
			"userId int UNSIGNED NOT NULL AUTO_INCREMENT, " +
			"password char(64) NOT NULL, " +
			"username varchar(32) NOT NULL, " +
			"email varchar(254) NOT NULL, " +
			"confirmed tinyint(1) DEFAULT 1, " +
			"PRIMARY KEY (userId) " +
			") DEFAULT CHARACTER SET=utf-8";

	public static final String BenchNodeContent =
			"CREATE TABLE IF NOT EXISTS BenchNodeContent " +
			"( " +
			"bNodeId bigint UNSIGNED NOT NULL, " +
			"lastEditor int UNSIGNED, " +
			"lastEdit bigint UNSIGNED, " +
			"content text, " +
			"PRIMARY KEY (bNodeId) " +
			") DEFAULT CHARACTER SET=utf-8";

	public static final String BenchNodes =
			"CREATE TABLE IF NOT EXISTS BenchNodes " +
			"( " +
			"bNodeId bigint UNSIGNED NOT NULL AUTO_INCREMENT, " +
			"userId int UNSIGNED, " +
			"benchId int UNSIGNED NOT NULL, " +
			"x smallint NOT NULL, " +
			"y smallint NOT NULL, " +
			"width smallint UNSIGNED NOT NULL, " +
			"height smallint UNSIGNED NOT NULL, " +
			"archived tinyint(1) DEFAULT 0, " +
			"PRIMARY KEY (bNodeId) " +
			") DEFAULT CHARACTER SET=utf-8";

	public static final String Benches =
			"CREATE TABLE IF NOT EXISTS Benches " +
			"( " +
			"benchId int UNSIGNED NOT NULL AUTO_INCREMENT, " +
			"owner int UNSIGNED NOT NULL, " +
			"created int UNSIGNED, " +
			"background varchar(255),  " +
			"archived tinyint(1) DEFAULT 0, " +
			"PRIMARY KEY (benchId) " +
			") DEFAULT CHARACTER SET=utf-8";

	public static final String Folders =
			"CREATE TABLE IF NOT EXISTS Folders " +
			"( " +
			"folderId bigint UNSIGNED NOT NULL AUTO_INCREMENT, " +
			"userId int UNSIGNED NOT NULL, " +
			"name char(255), " +
			"archived tinyint(1) DEFAULT 0, " +
			"PRIMARY KEY (folderId) " +
			") DEFAULT CHARACTER SET=utf-8";

	public static final String Members =
			"CREATE TABLE IF NOT EXISTS Members " +
			"( " +
			"id bigint UNSIGNED NOT NULL AUTO_INCREMENT, " +
			"benchId int UNSIGNED NOT NULL, " +
			"userId int UNSIGNED NOT NULL, " +
			"level tinyint DEFAULT 0, " +
			"PRIMARY KEY (id) " +
			") DEFAULT CHARACTER SET=utf-8";

	public static final String Sessions =
			"CREATE TABLE IF NOT EXISTS Sessions " +
			"( " +
			"userId int UNSIGNED NOT NULL, " +
			"token char(128), " +
			"time int UNSIGNED, " +
			"PRIMARY KEY (userId) " +
			") DEFAULT CHARACTER SET=utf-8";

	public static final String UserNodeContent =
			"CREATE TABLE IF NOT EXISTS UserNodeContent " +
			"( " +
			"uNodeId bigint UNSIGNED NOT NULL, " +
			"lastEditor int UNSIGNED, " +
			"lastEdit int UNSIGNED, " +
			"content text, " +
			"PRIMARY KEY (uNodeId) " +
			") DEFAULT CHARACTER SET=utf-8";

	public static final String UserNodes =
			"CREATE TABLE IF NOT EXISTS UserNodes " +
			"( " +
			"uNodeId bigint UNSIGNED NOT NULL AUTO_INCREMENT, " +
			"folder bigint UNSIGNED, " +
			"userId int UNSIGNED, " +
			"title char(255), " +
			"archived tinyint(1) DEFAULT 0, " +
			"PRIMARY KEY (uNodeId), " +
			"FOREIGN KEY (folder) REFERENCES Folders(folderId) " +
			") DEFAULT CHARACTER SET=utf-8";
}
