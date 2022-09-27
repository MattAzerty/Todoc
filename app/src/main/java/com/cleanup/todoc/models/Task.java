package com.cleanup.todoc.models;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Comparator;

//Model for the tasks of the application

@Entity(foreignKeys = @ForeignKey(entity = Project.class,
        parentColumns = "id",
        childColumns = "projectId"))

public class Task {

//The unique identifier of the task
    @PrimaryKey(autoGenerate = true)
    private long id;

//The unique identifier of the project associated to the task
    private long projectId;
//The name of the task
    @NonNull
    private String name;
//The timestamp when the task has been created
    private long creationTimestamp;

    /**
     Instantiates a new Task.
     @param id                the unique identifier of the task to set
     @param projectId         the unique identifier of the project associated to the task to set
     @param name              the name of the task to set
     @param creationTimestamp the timestamp when the task has been created to set
     */

    public Task() { } //Constructor empty for database implementation

    public Task(long projectId, @NonNull String name, long creationTimestamp) {
        this.projectId = projectId;
        this.name = name;
        this.creationTimestamp = creationTimestamp;
    }


    // getters

    public long getId() {
        return id;
    }

    public long getProjectId() {
        return projectId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    /*/**
     * Returns the project associated to the task.
     *
     * @return the project associated to the task
     */
   @Nullable
    public Project getProject() {
        return Project.getProjectById(projectId);
    }



// setters

    public void setId(long id) {
        this.id = id;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }




    /**
     * Comparator to sort task from A to Z
     */
    public static class TaskAZComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return left.name.compareTo(right.name);
        }
    }

    /**
     * Comparator to sort task from Z to A
     */
    public static class TaskZAComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return right.name.compareTo(left.name);
        }
    }

    /**
     * Comparator to sort task from last created to first created
     */
    public static class TaskRecentComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return (int) (right.creationTimestamp - left.creationTimestamp);
        }
    }

    /**
     * Comparator to sort task from first created to last created
     */
    public static class TaskOldComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return (int) (left.creationTimestamp - right.creationTimestamp);
        }
    }



}
