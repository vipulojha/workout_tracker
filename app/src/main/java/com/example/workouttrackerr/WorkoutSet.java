package com.example.workouttrackerr;

public class WorkoutSet {
    private String exerciseId;
    private double weight;
    private int reps;
    private long timestamp;

    public WorkoutSet(String exerciseId, double weight, int reps, long timestamp) {
        this.exerciseId = exerciseId;
        this.weight = weight;
        this.reps = reps;
        this.timestamp = timestamp;
    }

    // Getters
    public String getExerciseId() { return exerciseId; }
    public double getWeight() { return weight; }
    public int getReps() { return reps; }
    public long getTimestamp() { return timestamp; }
}