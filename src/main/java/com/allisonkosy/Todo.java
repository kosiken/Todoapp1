package com.allisonkosy;
import java.util.Date;

public class Todo {
    private String title;
    private Integer id;
    private String description;
    private Boolean completed = false;
    private final Integer userId;
    private final Date createDate = new Date();

    public Todo(Integer userId) {
        this.userId = userId;
    }

    public Todo(User user) {
        this.userId = user.getId();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    private String getDateString() {
        Integer day = createDate.getDate();
        Integer month = createDate.getMonth() + 1;
        Integer year = createDate.getYear() + 1900;

        return day + "/" + month + "/" + year;
    }


    public String describe() {
        return  '\n' + title + "\n\t" +
                description + '\n' +
               (completed  ? "Completed " : "Not Completed")+

                "\ncreateDate=" + getDateString() +
                "\n-------------------------------------------------------";

    }

    public String describeShort() {
        return  title + ' ' + (completed  ? "Completed " : "Not Completed");




    }


    @Override
    public String toString() {
        return "Todo{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                ", userId=" + userId +
                ", createDate=" + createDate +
                '}';
    }
}
