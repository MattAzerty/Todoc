package com.cleanup.todoc.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.databinding.ActivityMainBinding;
import com.cleanup.todoc.injections.ViewModelFactory;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.ui.list.TasksAdapter;
import com.cleanup.todoc.ui.list.TasksViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/*
Home activity of the application which is displayed when the user opens the app.
Displays the list of tasks.
 */
public class MainActivity extends AppCompatActivity implements TasksAdapter.Listener {
    private ActivityMainBinding mActivityMainBinding;
    private TasksViewModel tasksViewModel;
    private TasksAdapter adapter;
    //get all projects available
    public Project[] allProjects;
    //Dialog to create a new task
    @Nullable
    public AlertDialog dialog = null;
    //EditText that allows user to set the name of a task
    @Nullable
    private EditText dialogEditText = null;
    //Spinner that allows the user to associate a project to a task
    @Nullable
    private Spinner dialogSpinner = null;
//The sort method to be used to display tasks
    @NonNull
    private SortMethod sortMethod = SortMethod.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());

        //Configure view (with recyclerview) & ViewModel
        configureViewModel();
        //initialize view
        initView();
        //Get current tasks from Database
        getProjects();
        getTasks();


//ADD task dialog
        mActivityMainBinding.fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog();
            }
        });
    }

    //Toolbar settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }
    //menu item for toolbar optionsMenu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter_alphabetical) {
            sortMethod = SortMethod.ALPHABETICAL;
        } else if (id == R.id.filter_alphabetical_inverted) {
            sortMethod = SortMethod.ALPHABETICAL_INVERTED;
        } else if (id == R.id.filter_oldest_first) {
            sortMethod = SortMethod.OLD_FIRST;
        } else if (id == R.id.filter_recent_first) {
            sortMethod = SortMethod.RECENT_FIRST;
        }

        //updateTasks();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickDeleteButton(Task task) {
        // Delete item after user clicked on button
        deleteTask(task);
    }

    @Override
    public void onItemClick(Task task) {
        // Update item after user clicked on it
        updateTask(task);
    }

    // -------------------
    // DATA
    // -------------------

    // Configuring ViewModel
    private void configureViewModel() {
        this.tasksViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(TasksViewModel.class);
    }

    // Get Current User
    private void getProjects() {
        LiveData<List<Project>> projectListLiveData = tasksViewModel.getProjects();
        projectListLiveData.observe(this, this::updateProjectList);
    }

    // Get all tasks
    private void getTasks() {
        this.tasksViewModel.getTasks().observe(this, this::updateTaskList);
    }

    // Create a new task
    private void createTask(long projectId, @NonNull String name, long creationTimestamp) {
        tasksViewModel.createTask(projectId, name, creationTimestamp);
    }

    // Delete an item
    private void deleteTask(Task task) {
        this.tasksViewModel.deleteItem(task.getId());
    }

    // Update an item (selected or not)
    private void updateTask(Task task) {
        //task.setSelected(!task.getSelected());TODO 2 cross task name
        this.tasksViewModel.updateTask(task);
    }

    /*
    //Updates the list of tasks in the UI
    private void updateTasks() {
        if (tasks.size() == 0) {
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
            switch (sortMethod) {
                case ALPHABETICAL:
                    Collections.sort(tasks, new Task.TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(tasks, new Task.TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    Collections.sort(tasks, new Task.TaskRecentComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(tasks, new Task.TaskOldComparator());
                    break;

            }
            adapter.updateTasks(tasks);
        }
    }*/

    // -------------------
    // UI
    // -------------------

    private void initView() {
        /*ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        configureSpinner();
        binding.todoListActivityButtonAdd.setOnClickListener(view -> {

        });

        //Create item after user clicked on button
        binding.todoListActivityButtonAdd.setOnClickListener(view -> {
            createItem();
        });*/

        //Configure RecyclerView
        configureRecyclerView();

    }

    // Configure RecyclerView

    private void configureRecyclerView() {
        this.adapter = new TasksAdapter(this);
        mActivityMainBinding.listTasks.setAdapter(this.adapter);
        mActivityMainBinding.listTasks.setLayoutManager(new LinearLayoutManager(this));

    }

    /*// Update view (username & picture)TODO change to mine

    private void updateView(User user) {
        if (user == null) return;
        binding.todoListActivityHeaderProfileText.setText(user.getUsername());
        Glide.with(this).load(user.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(binding.todoListActivityHeaderProfileImage);

    }*/

    // Update the list of Task
    private void updateTaskList(List<Task> tasks) {
        this.adapter.updateData(tasks);
    }

    // Update the list of project
    private void updateProjectList(List<Project> projects) {
        allProjects = projects.toArray(new Project[projects.size()]);
    }

    /*private void configureSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.todoListActivitySpinner.setAdapter(adapter);
    }*/

    // -----------------
    // ADD A TASK DIALOG
    // -----------------


    //Shows the Dialog for adding a Task
    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

        populateDialogSpinner();
    }


    /*
     Returns the dialog allowing the user to create a new task.
     @return the dialog allowing the user to create a new task
     */
    @NonNull
    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogEditText = null;
                dialogSpinner = null;
                dialog = null;
            }
        });

        dialog = alertBuilder.create();

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        onPositiveButtonClick(dialog);
                    }
                });
            }
        });

        return dialog;
    }


    //Sets the data of the Spinner with projects to associate to a new task TODO populateSpinner
    private void populateDialogSpinner() {
        ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }

        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialogSpinner.setAdapter(adapter);*/

    }



    /*
 Called when the user clicks on the positive button of the Create Task Dialog.
 @param dialogInterface the current displayed dialog
 */
    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();

            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {

            createTask(taskProject.getId(), taskName, new Date().getTime());

                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else{
                dialogInterface.dismiss();
            }
        }
        // If dialog is already closed
        else {
            dialogInterface.dismiss();
        }
    }


     //List of all possible sort methods for task
    private enum SortMethod {
        /**
         * Sort alphabetical by name
         */
        ALPHABETICAL,
        /**
         * Inverted sort alphabetical by name
         */
        ALPHABETICAL_INVERTED,
        /**
         * Lastly created first
         */
        RECENT_FIRST,
        /**
         * First created first
         */
        OLD_FIRST,
        /**
         * No sort
         */
        NONE
    }


}
