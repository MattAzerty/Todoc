package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;

import java.util.List;

public class ProjectDataRepository {

    private final ProjectDao projectDao;

    public ProjectDataRepository(ProjectDao projectDao) { this.projectDao = projectDao; }

    // --- GET PROJECT ---

    public LiveData<Project> getProject(long projectId) {

        return this.projectDao.getProject(projectId);
    }

    // --- GET PROJECTS ---
    public LiveData<List<Project>> getProjects(){

        return this.projectDao.getProjects();
    }

}
