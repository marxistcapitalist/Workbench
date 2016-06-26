package online.workbench;

import com.google.gson.Gson;
import lombok.Getter;
import online.workbench.managers.BenchManager;

import online.workbench.managers.TokenManager;
import online.workbench.managers.UserManager;
import online.workbench.data.WorkbenchDB;
import online.workbench.model.struct.*;
import online.workbench.utils.TimeConverter;
import static online.workbench.base.Protocol.*;

import java.io.Console;
import java.util.ArrayList;
import java.util.Map;

import static spark.Spark.*;

public class WorkbenchAPI2
{
	private WorkbenchDB database;
	private @Getter UserManager userManager;
	private @Getter BenchManager benchManager;
	private @Getter TokenManager tokenManager;
	private Gson gson;

	public final String API = "/api/";
	private static final char[] disallowed = {'\\', '\'', '\"', ';', '&', '@', '#', '/', '$', ':', '%', '^', '*', '<', '>', '{', '}', '[', ']', '(', ')'};

	public WorkbenchAPI2(WorkbenchDB database)
	{
		this.userManager = new UserManager(database);
		this.benchManager = new BenchManager(database, userManager);
		this.tokenManager = new TokenManager(database);
		this.database = database;
		this.gson = new Gson();
	}

	public void initialize()
	{
		////////// GENERAL ////////////
		ping();
		ping2();

		////////// ACCOUNTS ////////////
		login();
		register();
		authenticate();
		logout();
		useredit();
		user();

		///////// BENCH - CORE ////////////
		bench();
		create();
		delete();
		edit();

		///////// BENCH - USERS ///////////////
		adduser();
		moduser();
		removeuser();

		////////// BENCHNODE - CORE ////////////
		//copyBenchNode();
		createBenchNode();
		deleteBenchNode();
		editBenchNode();
		moveBenchNode();
		renameBenchNode();
		resizeBenchNode();
		//spawnBenchNode();

		/////////// BENCHNODE - MISC ///////////
		ignoreBenchNode();
		watchBenchNode();

		////////// USERNODE /////////////
		createUserNode();
		deleteUserNode();
		editUserNode();
		renameUserNode();
	}



	///////////// GENERAL ////////////////

	private void ping()
	{
		post(API + "ping", (req, res) -> "{}");
	}

	private void ping2()
	{
		get("/ping", (req, res) -> "pong!");
	}

	//////////// ACCOUNTS ///////////////

	private void login()
	{

	}

	private void register()
	{

	}

	public void authenticate()
	{

	}

	public void logout()
	{

	}

	public void user()
	{

	}

	public void useredit()
	{

	}

	/////////////// BENCH - CORE //////////////////

	public void bench()
	{

	}

	public void create()
	{

	}

	public void delete()
	{

	}

	public void edit()
	{

	}

	public void adduser()
	{

	}

	public void moduser()
	{

	}

	public void removeuser()
	{

	}

	//////////////// BENCHNODE - CORE ////////////////

	public void copyBenchNode()
	{

	}

	public void createBenchNode()
	{

	}

	public void deleteBenchNode()
	{

	}

	public void editBenchNode()
	{

	}

	public void moveBenchNode()
	{

	}

	public void renameBenchNode()
	{

	}

	public void resizeBenchNode()
	{

	}

	public void spawnBenchNode()
	{

	}

	//////////////// BENCHNODE - MISC ////////////////

	public void ignoreBenchNode()
	{

	}

	public void watchBenchNode()
	{

	}

	//////////////// USERNODE ////////////////

	public void createUserNode()
	{

	}

	public void deleteUserNode()
	{

	}

	public void editUserNode()
	{

	}

	public void renameUserNode()
	{

	}
}
