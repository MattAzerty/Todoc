package com.cleanup.todoc.ui.list;

import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.databinding.ItemTaskBinding;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    private final ItemTaskBinding binding;


    public TaskViewHolder(ItemTaskBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

    }


    public void updateWithTask(Task task, TasksAdapter.Listener callback) {
        binding.getRoot().setOnClickListener(view -> callback.onItemClick(task));
        binding.imgDelete.setOnClickListener(view -> callback.onClickDeleteButton(task));
        binding.lblTaskName.setText(task.getName());
        binding.imgProject.setSupportImageTintList(ColorStateList.valueOf(task.getProject().getColor()));
        binding.lblProjectName.setText(task.getProject().getName());


        /*if (task.getSelected()) {TODO cross task name
            binding.activityTodoListItemText.setPaintFlags(binding.activityTodoListItemText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            binding.activityTodoListItemText.setPaintFlags(binding.activityTodoListItemText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }*/
    }
}