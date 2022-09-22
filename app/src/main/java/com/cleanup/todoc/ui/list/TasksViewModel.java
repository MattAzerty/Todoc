package com.cleanup.todoc.ui.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TasksViewModel extends ViewModel {

    // REPOSITORIES TODO ViewModel factory?
    private final TaskDataRepository taskDataSource;
    private final ProjectDataRepository projectDataSource;
    private final Executor executor;

    // DATA
    //@Nullable
    //private LiveData<User> currentUser;
    public TasksViewModel(TaskDataRepository taskDataSource, ProjectDataRepository projectDataSource, Executor executor) {
        this.taskDataSource = taskDataSource;
        this.projectDataSource = projectDataSource;
        this.executor = executor;
    }

    /*public void init(long userId) {
        if (this.currentUser != null) {
            return;
        }
        currentUser = userDataSource.getUser(userId);
    }*/


    // FOR Project

    public LiveData<List<Project>> getProjects() {
        return projectDataSource.getProjects();
    }

    // FOR TASKS

    public LiveData<List<Task>> getTasks() {
        return taskDataSource.getTasks();
    }

    public void createTask(long projectId, @NonNull String name, long creationTimestamp) {
        executor.execute(() -> {
            taskDataSource.createTask(new Task(projectId, name,creationTimestamp));
        });

    }

    public void deleteItem(long itemId) {
        executor.execute(() -> taskDataSource.deleteTask(itemId));
    }

    public void updateTask(Task task) {
        executor.execute(() -> taskDataSource.updateTask(task));
    }
}
