package com.example.trackmyworkout.LandingPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackmyworkout.R;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.WorkoutViewHolder> {
    private List<Exercise> exercises;
    private OnItemLongClickListener longClickListener;
    private OnWeightClickListener weightClickListener;

    public List<Exercise> getExercises() {
        return exercises;
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public interface OnWeightClickListener {
        void onWeightClick(Exercise exercise);
    }

    public void setOnWeightClickListener(OnWeightClickListener listener) {
        this.weightClickListener = listener;
    }

    public ExerciseAdapter(List<Exercise> workouts) {
        this.exercises = workouts;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        return new WorkoutViewHolder(view, weightClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Exercise workout = exercises.get(position);
        holder.workoutName.setText(workout.getName());
        holder.workoutWeight.setText(String.valueOf(workout.getWeight()));
        holder.workoutWeight.setTag(workout); // Set the exercise as a tag

        // Set long click listener for context menu
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) return longClickListener.onItemLongClick(position);
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView workoutName;
        TextView workoutWeight;

        public WorkoutViewHolder(View itemView, final OnWeightClickListener weightClickListener) {
            super(itemView);
            workoutName = itemView.findViewById(R.id.textViewExerciseName);
            workoutWeight = itemView.findViewById(R.id.textViewExerciseWeight);

            workoutWeight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (weightClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        weightClickListener.onWeightClick((Exercise) workoutWeight.getTag());
                    }
                }
            });
        }
    }

    public void addExercise(Exercise exercise) {
        this.exercises.add(exercise);
        notifyDataSetChanged();
    }

    public void removeExercise(int position) {
        if (position >= 0 && position < exercises.size()) {
            this.exercises.remove(position);
            notifyDataSetChanged();
        }
    }
}
