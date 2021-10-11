package com.allisonkosy;

import com.google.common.base.Preconditions;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Database {

    private  Bag<Todo> todos = new HashBag<>();
    private final OrderedMap<String, User> users = new LinkedMap<>();
    private Integer idCounter = 0;
    private Integer idTodo = 0;



    public User registerUser(String email, String password) {

        User user = null;
        // escape email case issues
        Preconditions.checkArgument(password.length() > 5,"Password length must be greater than 5 characters" );
        String realMail = email.toLowerCase(Locale.ROOT);

        if(password.length() < 3) {
            App.logger.error("password length too short");
        }

        else if(!users.containsKey(realMail)) {

            user = new User();

            user.setEmail(realMail);
            user.setPassword(password);
            user.setId(++idCounter);
            users.put(realMail, user);
            // App.logger.info(realMail + " added successfully");
            App.logger.info(user);



        }

        else {
            App.logger.error("email " + email + " already taken");
        }

        return user;

    }

    public User loginUser(String email, String password) {
        // escape email case issues

        String realMail = email.toLowerCase(Locale.ROOT);

        User user = null;
        
        if(users.containsKey(realMail)) {
            user = users.get(realMail);
            if(!user.getPassword().equals(password)) {
                App.logger.error("password does not match");
                user = null;
            }

            
        }
        else {
            App.logger.error("No such user with email " + email + " exists");
        }

        return user;

    }

    public User updateDetails(String email, String password, String newPassword) {


        User user = loginUser(email, password);
        Preconditions.checkArgument(newPassword.length() > 5, "Password length must be greater than 5 characters");

        if(user == null) {

            App.logger.error("Failed to update password");

        }

        else {
            user.setPassword(newPassword);
            App.logger.info("Changed " + email + " password successfully");
        }

        return user;

    }

    public Todo createTodo(User user, String title, String description) {

        Todo todo;

        Preconditions.checkNotNull(user, "user cannot be null");
        Preconditions.checkNotNull(title, "title cannot be null");
        Preconditions.checkNotNull(description, "description cannot be null");
        todo = new Todo(user);
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setId(++idTodo);
        App.logger.info("Added " + todo);


        todos.add(todo);


        return todo;
    }

    private Bag<Todo> shallowTodoCopy() {
        Bag<Todo> shallowCopy = new HashBag<>();
        shallowCopy.addAll(todos);
        return shallowCopy;
    }


    public Bag<Todo>  getAllActiveTodos() {
        Bag<Todo> shallowCopy = shallowTodoCopy();
        CollectionUtils.filter(shallowCopy, new Predicate<Todo>() {
            @Override
            public boolean evaluate(Todo todo) {
                return !todo.getCompleted();
            }
        });

        return shallowCopy;
    }


    public Bag<Todo>  getAllCompletedTodos() {
        Bag<Todo> shallowCopy = shallowTodoCopy();
        CollectionUtils.filter(shallowCopy, new Predicate<Todo>() {
            @Override
            public boolean evaluate(Todo todo) {
              return todo.getCompleted();
            }
        });

        return shallowCopy;
    }


    public Todo getTodoByName(String name) {
        Todo todo = null;
        for (Todo t :
                todos) {
            String title = t.getTitle().toLowerCase(Locale.ROOT);
            if(name.equals(title)) {
                todo = t;
                break;
            }
        }

        return todo;
    }
    private Todo getTodoByDetails(String details) {

        Todo todo = null;
        for (Todo t :
                todos) {
            if(details.length() > t.getDescription().length()) {
                continue;
            }

            String detailsTodo = t.getDescription().toLowerCase(Locale.ROOT).substring(0, details.length());

//            detailsTodo.s
            if(details.equals(detailsTodo)) {
                todo = t;
                break;
            }
        }

        return todo;
    }
    public Todo getTodoById(Integer id) {
        Todo todo = null;
        for (Todo t :
                todos) {
            if(id == t.getId()) {
                todo = t;
                break;
            }
        }

        return todo;
    }

    private Todo getTodoByDate(String dateString) throws ArrayIndexOutOfBoundsException, NumberFormatException {
        Todo todo = null;
        String[] dateSplit = dateString.split("\\/");
        Integer d = Integer.parseInt(dateSplit[0]);
        Integer m = Integer.parseInt(dateSplit[1]);
        Integer y = Integer.parseInt(dateSplit[2]);


            for (Todo t :
                    todos) {
                Date date = t.getCreateDate();
                Integer day = date.getDate();
                Integer month = date.getMonth() + 1;
                Integer year = date.getYear() + 1900;


                if(day.equals(d) && (month.equals(m) ) && (year.equals(y)) ) {
                    todo = t;
                    break;
                }
            }



        return todo;
    }

    public Todo deleteATodo(final Integer id) {
       Todo t = getTodoById(id);

       if(t == null) {
           App.logger.error("Cannot find specified todo");
       }
        else {
           CollectionUtils.filterInverse(todos, new Predicate<Todo>() {
               @Override
               public boolean evaluate(Todo todo) {
                   return todo.getId() == id;
               }
           });

           App.logger.info("Deleted " + t);
       }

        return t;
    }

    public Bag<Todo> addBulkTodos(User user, String[] titles, String[] descriptions, Boolean[] states ){
        int len = titles.length;
        Preconditions.checkArgument(len > 0, "Length cannot be zero");
        Preconditions.checkArgument((len == descriptions.length )&&(len== states.length),
                "Lengths must be equal" );

        Bag<Todo> todos1 = new HashBag<>();
        for (int i = 0; i < len; i++) {
            Todo todo = createTodo(user, titles[i], descriptions[i]);
            todo.setCompleted(states[i]);
            todos1.add(todo);
        }



        return todos1;

    }



    public Todo searchTodo(HashMap<String, String> criteria) {
        Todo todo = null;
        Preconditions.checkNotNull(criteria, "Criteria cannot be null");
        Preconditions.checkArgument(criteria.size() > 0, "Must give at least one crieteria");
        if(criteria.containsKey("title")) {
            todo = getTodoByName(criteria.get("title").toLowerCase(Locale.ROOT));
        }
        else if(criteria.containsKey("description")) {
            todo = getTodoByDetails(criteria.get("description").toLowerCase(Locale.ROOT));
        }

        else if(criteria.containsKey("date")) {
            todo = getTodoByDate(criteria.get("date"));
        }

       if(todo != null)  App.logger.info("Found "  + todo);
       else System.out.println("Could not find any todo with given crieteria");
        return todo;
    }

    public Todo updateTodo(Integer id, HashMap<String, String> criteria) {
        Todo todo = getTodoById(id);

        if(todo != null) {
            String title = criteria.get("title");
            String description = criteria.get("description");
            String completed = criteria.get("completed");

            if(title != null) {
                todo.setTitle(title);
            }
            if(description != null) {
                todo.setDescription(description);
            }
            if(completed != null) {
                todo.setCompleted(Boolean.parseBoolean(completed));
            }


        }

        return todo;
    }



    public Integer getTodoCount() {
        return todos.size();
    }

    public Bag<Todo> getTodos() {
        return todos;
    }
}
