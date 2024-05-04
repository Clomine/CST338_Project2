package com.example.trackmyworkout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackmyworkout.LandingPage.Exercise;
import com.example.trackmyworkout.R;

import java.util.ArrayList;
import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private List<Exercise> exercises;
    private List<Integer> checkedExercises; // List to store IDs of checked exercises

    public WorkoutAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
        this.checkedExercises = new ArrayList<>();
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.bind(exercise);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public List<Integer> getCheckedExercises() {
        return checkedExercises;
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private CheckBox checkBox;
        private TextView textViewIndex;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewExerciseName);
            checkBox = itemView.findViewById(R.id.checkboxExercise);
            textViewIndex = itemView.findViewById(R.id.textViewIndex);

            // Set an OnCheckedChangeListener for the checkbox
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Get the exercise ID associated with this checkbox
                    int exerciseId = exercises.get(getAdapterPosition()).getExId();
                    // If the checkbox is checked, add the exercise ID to the checkedExercises list
                    if (isChecked) {
                        checkedExercises.add(exerciseId);
                    } else {
                        // If the checkbox is unchecked, remove the exercise ID from the checkedExercises list
                        checkedExercises.remove(Integer.valueOf(exerciseId));
                    }
                }
            });
        }

        public void bind(Exercise exercise) {
            nameTextView.setText(exercise.getName());

            int exerciseId = exercise.getExId();
            int index = checkedExercises.indexOf(exerciseId);
            if (index != -1) {
                textViewIndex.setText(String.valueOf(index + 1)); // Add 1 to display 1-based index
            } else {
                textViewIndex.setText(""); // Clear the index if exercise is not checked
            }

            checkBox.setOnCheckedChangeListener(null); // Remove previous listener to prevent triggering
            checkBox.setChecked(index != -1); // Check the checkbox if exercise is in the checkedExercises list

            // Set an OnCheckedChangeListener for the checkbox
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // If the checkbox is checked, add the exercise ID to the checkedExercises list
                    if (isChecked) {
                        checkedExercises.add(exerciseId);
                    } else {
                        // If the checkbox is unchecked, remove the exercise ID from the checkedExercises list
                        checkedExercises.remove(Integer.valueOf(exerciseId));
                    }
                    // Update the index text for all exercises after a checkbox state change
                    notifyDataSetChanged(); // Notify adapter to refresh all items
                }
            });
        }
    }
}

