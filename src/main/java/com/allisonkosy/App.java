package com.allisonkosy;

import org.apache.commons.collections4.Bag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static final Logger logger = LogManager.getLogger(App.class);
    public static User currentUser = null;
    public static Todo currentTodo = null;
    public static final String[] users = "Frank,John,Shaun".split(",");
    public static final boolean isDev = true;

    public static void main( String[] args )
    {

        Database server = new Database();

        Scanner scanner = new Scanner(System.in);
        addDummyUsers(server);
        printIntroduction();
        int ans = 1;
        while (ans == 1) {
            try {
                if(currentUser != null) {
                    if(currentTodo != null) ans = hasCurrentTodo(server, scanner);
                    else ans = authenticated(server, scanner);
                }
                else ans = unauthenticated(server, scanner);
            }
            catch (Exception e) {
                App.logger.error(e.getMessage());
            }



        }





    }

    public static void printIntroduction() {
        System.out.println("Welcome to TodoApp \n ---------------------- \n");
    }
    public static int hasCurrentTodo(Database server, Scanner in) {
//        System.out.println("Current todo -> " + currentTodo.getTitle());

        System.out.print("Input 1 to delete current todo\nInput 2 to update current todo\n" +
                "Input 3 to view current todo (" + currentTodo.getTitle()+ ")\n"+

                "Input 4 to go back to previous menu\n" +

                "Input 5 to logout\n" +



                "Input >=6 to exit\nchoice: ");

        int choice = 0;
        int ans = 1;
        try {
            choice = in.nextInt();
            System.out.println(choice);
            if(choice == 1) {
                deleteTodo(server);
            }
            else if(choice == 2) {
                updateTodo(server, in);
            }

            else if(choice == 3) {
                System.out.println(currentTodo.describe());
            }
            else if(choice == 4) {
                currentTodo = null;
            }

            else if(choice == 4) {
                currentTodo = null;
                currentUser = null;
            }

            else  {
                ans = 0;
            }

        }
        catch (InputMismatchException exception) {
            System.out.println("Incorrect input");
            in.nextLine();
        }

        return ans;
    }

    public static int authenticated(Database server, Scanner in) {
        System.out.println("Input 1 to add todo\nInput 2 to get all the TODO\n" +

                "Input 3 to get all active TODO\n" +

                "Input 4 to get all the completed TODO\n" +

                "Input 5 to select a TODO\n" +
                "Input 6 to update password\n" +
                "Input 7 to logout\n" +

                "Input >=8 to exit\nchoice: ");
        int choice = 0;
        int ans = 1;

        try {
            choice = in.nextInt();
            System.out.println(choice);
            if(choice == 1) {
                addTodo(server, in);
            }
            else if(choice == 2) {
                Bag<Todo> todos = server.getTodos();
                printTodoGrid(todos);
            }
            else if(choice == 3) {
                Bag<Todo> todos = server.getAllActiveTodos();
                printTodoGrid(todos);
            }

            else if(choice == 4) {
                Bag<Todo> todos = server.getAllCompletedTodos();
                printTodoGrid(todos);
            }
            else if(choice == 5) {
                searchTodo(server, in);
            }

            else if(choice == 6) {
                updateDetails(server, in);
            }
            else if(choice == 7) {
                currentUser = null;
            }
            else  {
                ans = 0;
            }

        }
        catch (InputMismatchException exception) {
            System.out.println("Incorrect input");
            in.nextLine();
        }


        return ans;


    }


    public static String getMultipleQuestion(String[] questions, Scanner in) {
        int index  = 0;
        int len = questions.length;
        StringBuilder builder = new StringBuilder();

        while (index <  len) {
            System.out.print(questions[index]);
            String ans = in.nextLine();
            if(ans.length() == 0) continue;
            builder.append(ans)
                    .append(',');

            index++;

        }
        return builder.toString();


    }
    public static int unauthenticated(Database server, Scanner in) {
//        in.nextLine();
        System.out.println("Input 1 to register new user\nInput 2 to log in as existing user\n"
                + "Input >=3 to exit");
        System.out.print("choice: ");

        int choice;
        int ans = 1;

        try {
            choice = in.nextInt();
            System.out.println(choice);
            if(choice == 1) {
                register(server, in);
            }
            else if(choice == 2) {
                login(server, in);
            }
            else  {
                ans = 0;
            }

        }
        catch (InputMismatchException exception) {
            System.out.println("Incorrect input");
            in.nextLine();
        }


        return ans;
    }
    public static void register(Database server, Scanner in) {
        in.nextLine();

        String[] questions = {
                "Input an email: ",
                "Input a password (must be >= 6 characters): "
        };
        int index = 0;
        String email = "";
        String password = "";

        String[] s  = getMultipleQuestion(questions, in).split(",");
        email = s[0];
        password = s[1];
        currentUser = server.registerUser(email, password);
        if (currentUser == null) {
            System.out.println("must use unique and defined name" );
            System.out.println("must use defined password and greater than 5 characters");
        }
        else {
            System.out.println("Logged in as " + currentUser.getEmail());

        }
    }
    public static void login(Database server, Scanner in) {

        printAllUsers();
        System.out.println("Input one of the following emails and password '000000' to log in or use yours");
        User user = null;
        String[] questions = {
                "What is your email: ",
                "What is your password: "
        };
         String email = "";
        String password = "";
        in.nextLine();

        String[] s  = getMultipleQuestion(questions, in).split(",");
        email = s[0];
        password = s[1];

        user = server.loginUser(email, password);
        if( user != null ) {

            currentUser = user;
            System.out.println("Logged in as " + currentUser.getEmail());

        }
        else {
            System.out.println("Invalid username or password");
        }

    }
    public static void updateDetails(Database server, Scanner in) {
        String[] questions = {
                "Input previous password: ",
                "Input new password: ",
        };

        String password = "";
        String passwordNew = "";
        in.nextLine();
        String[] s  = getMultipleQuestion(questions, in).split(",");
        password = s[0];
        passwordNew = s[1];

        server.updateDetails(currentUser.getEmail(), password, passwordNew);
    }
    public static void addTodo(Database server, Scanner in) {
        String[] questions = {
                "What is todo's title: ",
                "What is todo's description: "
        };
        String title = "";
        String description = "";
        in.nextLine();
        String[] s  = getMultipleQuestion(questions, in).split(",");
        title = s[0];
        description = s[1];

        Todo todo = server.createTodo(currentUser, title, description);

        if(todo != null) System.out.println("Created " + todo.getTitle());
        else System.out.println("Failed to create todo " + title);

    }
    public static void searchTodo(Database server, Scanner in) {
        String[] questions = {
                "Input the todo's title or details or date in such a manner\n \"title={title}\" or \"description={description}\" or \"date={DD/MM/YYYY}\" e.g date=11/10/2021: ",
             
        };
        
        String ans = "";
        
        in.nextLine();
        String[] s  = getMultipleQuestion(questions, in).split(",");
        Todo todo = null;
        ans = s[0];
        int id = -1;
        try {

            HashMap<String, String> hashMap = buildHashMapFromKeyValueString(ans);
            todo = server.searchTodo(hashMap);
            
        }
        catch (Exception e) {
            logger.error(e.getMessage());

            System.out.println("An error occurred while trying to parse input");


            
        }

        currentTodo = todo;
    }
    public static void updateTodo(Database server, Scanner in) {
        String[] questions = {
                "Input the todo's values you want to update in such a manner\n \"title={updateTitle},description={updateDescription},completed={updateCompleted}\"\nomit values that you do not need e.g title=hello world,description=new description,completed=false \nprompt$: ",

        };


        in.nextLine();
        String ans = getMultipleQuestion(questions, in);
        Todo todo = null;


        try {

            HashMap<String, String> hashMap = buildHashMapFromKeyValueString(ans);
            todo = server.updateTodo(currentTodo.getId(), hashMap);

        }
        catch (Exception e) {
            logger.error(e.getMessage());

            System.out.println("An error occurred while trying to parse input");



        }

        if(todo!=null) {
            currentTodo = todo;
            System.out.println(currentTodo.describe());
        }

    }

    public static void deleteTodo(Database server) {
        server.deleteATodo(currentTodo.getId());
        currentTodo = null;
    }
    public static void addBulkTodos(Database db, User user) {
        String[] titles = "Call Joy,Buy Eggs,Make Call,Be Calm".split(",");
        String[] descriptions = {"Call Joy by 12:00am",
                "Buy eggs to make Kenechukwu's birthday cake",
                "Make a call to building manager",
        "Be calm, be calm, I know you feel like you are breaking down"};

        Boolean[] states = {false, true, false, true};
        db.addBulkTodos(user, titles, descriptions, states);
    }

    public static void addDummyUsers(Database server) {
        int i = 0;
        for (String user :
                users) {
           User user1 = server.registerUser(user + "@krc.com", "000000");
            if(isDev && i == 0) {
                addBulkTodos(server, user1);
            }
            i++;

        }
    }

    public static void printAllUsers() {
        for (String user :
                users) {
            System.out.println(user + "@krc.com " + "000000");

        }
    }


    public static void printTodoGrid(Bag<Todo> todos) {
        StringBuilder builder = new StringBuilder();
        int i =0;
        for (Todo todo: todos) {
            if(i % 2 == 1) {
                builder.append("        " )
                        .append((todo.getId()) + ". "+ todo.describeShort()+ '\n');
            }
            else {
                builder.append((todo.getId()) + ". "+ todo.describeShort());
            }
            i++;
        }

        System.out.println(builder);
    }

    public static HashMap<String, String> buildHashMapFromKeyValueString(String keyValueString) {
        HashMap<String, String> hashMap = new HashMap<>();
        String[] keyValuePairs = keyValueString.split(",");
        for (String keyValuePair:
             keyValuePairs) {
            String[] kv = keyValuePair.split("=");
            hashMap.put(kv[0].trim(), kv[1].trim());
        }
        return hashMap;
    }
}
