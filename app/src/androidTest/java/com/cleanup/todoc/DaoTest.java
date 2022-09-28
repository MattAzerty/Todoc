package com.cleanup.todoc;

import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

@RunWith(AndroidJUnit4.class)

public class DaoTest {

    // FOR DATABASE
    private TodocDatabase database;

    // DATA SET FOR TEST
    private static Project PROJECT_DEMO1 = new Project(1L,"ProjectDemo1",0xFFEADAD1);
    private static Project PROJECT_DEMO2 = new Project(2L,"ProjectDemo2",0xFFB4CDBA);
    private static Project PROJECT_DEMO3 = new Project(3L,"ProjectDemo3",0xFFA3CED2);

    private static Task TASK_DEMO_1 =
            new Task(1L,"Nettoyer les vitres",new Date().getTime());

    private static Task TASK_DEMO_2 =
            new Task(2L,"Vider le lave vaisselle", new Date().getTime());

    private static Task TASK_DEMO_3 =
            new Task(3L,"Passer l'aspirateur", new Date().getTime());


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {

        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                        TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }


    @Test
    public void insertAndGetProject() throws InterruptedException {
        // BEFORE : Adding 3 demo projects
        this.database.projectDao().createProject(PROJECT_DEMO1);
        this.database.projectDao().createProject(PROJECT_DEMO2);
        this.database.projectDao().createProject(PROJECT_DEMO3);

        // TEST
        Project project2 = LiveDataTestUtil.getValue(this.database.projectDao().getProject(2L));
        assertTrue(project2.getName().equals("ProjectDemo2") && project2.getId() == 2L);
    }


    @Test
    public void getTasksWhenNoTaskInserted() throws InterruptedException {
        // TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void insertAndGetTasks() throws InterruptedException {
        // BEFORE : Adding demo projects
        this.database.projectDao().createProject(PROJECT_DEMO1);
        this.database.projectDao().createProject(PROJECT_DEMO2);
        this.database.projectDao().createProject(PROJECT_DEMO3);
        // & demo tasks
        this.database.taskDao().createTask(TASK_DEMO_1);
        this.database.taskDao().createTask(TASK_DEMO_2);
        this.database.taskDao().createTask(TASK_DEMO_3);

        // TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertTrue(tasks.size() == 3);

    }


    @Test
    public void insertAndDeleteTask() throws InterruptedException {
        // BEFORE : Adding demo user & demo item. Next, get the item added & delete it.
        this.database.projectDao().createProject(PROJECT_DEMO1);
        this.database.taskDao().createTask(TASK_DEMO_1);
        List<Task> tasksINI = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        this.database.taskDao().deleteTask(tasksINI.get(0).getId());
        //TEST
        List<Task> tasksFINAL = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertTrue(tasksFINAL.isEmpty());
    }



}//END
