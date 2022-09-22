package com.cleanup.todoc.ui.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.databinding.ItemTaskBinding;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;


//Adapter which handles the list of tasks to display in the dedicated RecyclerView.

public class TasksAdapter extends RecyclerView.Adapter<TaskViewHolder> {


    // CALLBACK
    public interface Listener {
        void onClickDeleteButton(Task task);
        void onItemClick(Task task);
    }

    private final Listener callback;
    // FOR DATA
    private List<Task> tasks;

    // CONSTRUCTOR
    public TasksAdapter(Listener callback) {
        this.tasks = new ArrayList<>();
        this.callback = callback;
    }

    @Override

    @NonNull
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        return new TaskViewHolder(ItemTaskBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(TaskViewHolder viewHolder, int position) {
        viewHolder.updateWithTask(this.tasks.get(position), this.callback);
    }

    @Override
    public int getItemCount() {
        return this.tasks.size();
    }

    public void updateData(List<Task> tasks){
        this.tasks = tasks;
        this.notifyDataSetChanged();
    }

}//END
