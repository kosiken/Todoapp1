package com.allisonkosy;

import org.apache.commons.collections4.Bag;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit test for simple App.
 */

@DisplayName("Todoapp Test class")
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    @DisplayName("Test create user")
    public void shouldCreateUser()
    {
       Database db = new Database();

       User user = db.registerUser("Allisonkosy@gmail.com", "000000");
       assertNotNull(user);
       assertEquals("allisonkosy@gmail.com", user.getEmail());
       
        
    }

    @Test
    @DisplayName("Test should not create user")
    public void shouldNotCreateUser()
    {
        Database db = new Database();

        User user = db.registerUser("Allisonkosy@gmail.com", "000000");
        user = db.registerUser("Allisonkosy@gmail.com", "000000");
        assertNull(user);


    }



    @Test
    @DisplayName("Test login user")
    public void shouldLoginUser()
    {
        Database db = new Database();

        User user = db.registerUser("Allisonkosy@gmail.com", "000000");
        User user1 = db.loginUser("Allisonkosy@gmail.com", "000000");
        assertNotNull(user1);

        assertEquals(user.getEmail(), user1.getEmail());
        assertEquals(user, user);


    }


    @Test
    @DisplayName("Test update user details")
    public void shouldUpdateUserDetails()
    {
        Database db = new Database();

        User user = db.registerUser("Allisonkosy@gmail.com", "000000");
        User user1 = db.updateDetails("Allisonkosy@gmail.com", "000000", "0000");
        assertNotNull(user1);


        assertEquals(user.getEmail(), user1.getEmail());
        assertEquals("0000", user.getPassword());


    }

    @Test
    @DisplayName("Should add Todo")
    public void shouldAddTodo() {
        Database db = new Database();

        User user = db.registerUser("Allisonkosy@gmail.com", "000000");

        Todo todo =   db.createTodo(user, "Call Joy", "Call Joy by 12am");
        assertNotNull(todo);
        assertEquals("Call Joy", todo.getTitle());

    }

    @Test
    @DisplayName("Should get all Todos")
    public void shouldGetAllTodos() {
        Database db = new Database();

        User user = db.registerUser("Allisonkosy@gmail.com", "000000");

        App.addBulkTodos(db, user);
        Bag<Todo> todos = db.getTodos();

        assertEquals(4, todos.size());


    }

    @Test
    @DisplayName("Should get all active Todos")
    public void shouldGetAllActiveTodos() {
        Database db = new Database();

        User user = db.registerUser("Allisonkosy@gmail.com", "000000");

        App.addBulkTodos(db, user);
        Bag<Todo> todos = db.getAllActiveTodos();
        Object[] todos1 = todos.toArray();
        assertEquals(2, todos.size());
        assertEquals(false, ((Todo)todos1[0]).getCompleted());
        assertEquals(false, ((Todo)todos1[1]).getCompleted());


    }


    @Test
    @DisplayName("Should get all completed Todos")
    public void shouldGetAllCompletedTodos() {
        Database db = new Database();

        User user = db.registerUser("Allisonkosy@gmail.com", "000000");

        App.addBulkTodos(db, user);
        Bag<Todo> todos = db.getAllCompletedTodos();
        Object[] todos1 = todos.toArray();
        assertEquals(2, todos.size());
        assertEquals(true, ((Todo)todos1[0]).getCompleted());
        assertEquals(true, ((Todo)todos1[1]).getCompleted());


    }

    @Test
    @DisplayName("Should delete completed Todos")
    public void shouldDeleteTodo() {
        Database db = new Database();

        User user = db.registerUser("Allisonkosy@gmail.com", "000000");

        App.addBulkTodos(db, user);
        Todo todo = db.deleteATodo(1);
        assertNotNull(todo);
        assertEquals(3, db.getTodoCount());


    }
    @Test
    @DisplayName("Should update todo title")
    public void shouldUpdateTodoTitle(){
        Database db = new Database();

        User user = db.registerUser("Allisonkosy@gmail.com", "000000");

        App.addBulkTodos(db, user);

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("title", "Lion");
        Todo todo = db.updateTodo(1,hashMap);
        Todo todo1 = db.getTodoById(1);

        assertNotNull(todo);
        assertEquals("Lion", todo1.getTitle());

    }


    @Test
    @DisplayName("Should update todo description")
    public void shouldUpdateTodoDescription(){
        Database db = new Database();

        User user = db.registerUser("Allisonkosy@gmail.com", "000000");

        App.addBulkTodos(db, user);

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("description", "Lion");
        Todo todo = db.updateTodo(1,hashMap);
        Todo todo1 = db.getTodoById(1);

        assertNotNull(todo);
        assertEquals("Lion", todo1.getDescription());

    }

    @Test
    @DisplayName("Should update todo completed")
    public void shouldUpdateTodoCompleted(){
        Database db = new Database();

        User user = db.registerUser("Allisonkosy@gmail.com", "000000");

        App.addBulkTodos(db, user);

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("completed", "true");
        Todo todo = db.updateTodo(1,hashMap);
        Todo todo1 = db.getTodoById(1);

        assertNotNull(todo);
        assertEquals(true, todo1.getCompleted());

    }

    @Test
    @DisplayName("Should find todo by title")
    public void shouldFindTodoByTitle(){
        Database db = new Database();

        User user = db.registerUser("Allisonkosy@gmail.com", "000000");

        App.addBulkTodos(db, user);

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("title", "Call Joy");
        Todo todo = db.searchTodo(hashMap);

        assertNotNull(todo);

    }


    // Buy eggs to make
    @Test
    @DisplayName("Should find todo by description")
    public void shouldFindTodoByDescription(){
        Database db = new Database();

        User user = db.registerUser("Allisonkosy@gmail.com", "000000");

        App.addBulkTodos(db, user);

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("description", "Buy eggs to make");
        Todo todo = db.searchTodo(hashMap);

        assertNotNull(todo);
        assertEquals("Buy Eggs", todo.getTitle());

    }


    @Test
    @DisplayName("Should find todo by date")
    public void shouldFindTodoByDate(){
        Database db = new Database();

        User user = db.registerUser("Allisonkosy@gmail.com", "000000");

        App.addBulkTodos(db, user);

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("date", "11/10/2021");
        Todo todo = db.searchTodo(hashMap);

        assertNotNull(todo);
        System.out.println(todo.describe());


    }


}
