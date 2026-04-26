package com.example.workouttrackerr;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class AppDataManager {
    private static final String PREFS_NAME = "workout_tracker_data";
    private static final String KEY_PLANS = "plans";
    private static final String KEY_EXERCISES = "exercises";
    private static final String KEY_WORKOUT_ACTIVE = "workout_active";
    private static final String KEY_BODY_PROFILE_DONE = "body_profile_done";
    private static final String KEY_BODY_PROFILE = "body_profile";
    private static final String RECORD_SEPARATOR = "\n";
    private static final String FIELD_SEPARATOR = " | ";

    private final SharedPreferences prefs;

    public AppDataManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean isWorkoutActive() {
        return prefs.getBoolean(KEY_WORKOUT_ACTIVE, false);
    }

    public boolean toggleWorkout() {
        boolean active = !isWorkoutActive();
        prefs.edit().putBoolean(KEY_WORKOUT_ACTIVE, active).apply();
        return active;
    }

    public List<String[]> getPlans() {
        String defaults = "Beginner Strength" + FIELD_SEPARATOR + "3 days per week - Full body routine"
                + RECORD_SEPARATOR + "Push Pull Legs" + FIELD_SEPARATOR + "6 days per week - Muscle building split"
                + RECORD_SEPARATOR + "Fat Loss Circuit" + FIELD_SEPARATOR + "4 days per week - Conditioning focus";
        return parseRecords(prefs.getString(KEY_PLANS, defaults));
    }

    public void addPlan(String name, String details) {
        saveRecord(KEY_PLANS, getPlans(), name, details);
    }

    public void deletePlan(int index) {
        List<String[]> plans = getPlans();
        if (index >= 0 && index < plans.size()) {
            plans.remove(index);
            prefs.edit().putString(KEY_PLANS, serializeRecords(plans)).apply();
        }
    }

    public List<String[]> getExercises() {
        String defaults = "Bench Press" + FIELD_SEPARATOR + "Chest - Strength - Barbell"
                + RECORD_SEPARATOR + "Squat" + FIELD_SEPARATOR + "Legs - Strength - Barbell"
                + RECORD_SEPARATOR + "Lat Pulldown" + FIELD_SEPARATOR + "Back - Hypertrophy - Cable";
        return parseRecords(prefs.getString(KEY_EXERCISES, defaults));
    }

    public void addExercise(String name, String details) {
        saveRecord(KEY_EXERCISES, getExercises(), name, details);
    }

    public void deleteExercise(int index) {
        List<String[]> exercises = getExercises();
        if (index >= 0 && index < exercises.size()) {
            exercises.remove(index);
            prefs.edit().putString(KEY_EXERCISES, serializeRecords(exercises)).apply();
        }
    }

    public boolean hasBodyProfile() {
        return prefs.getBoolean(KEY_BODY_PROFILE_DONE, false);
    }

    public String getBodyProfile() {
        return prefs.getString(KEY_BODY_PROFILE, "");
    }

    public void saveBodyProfile(String profile) {
        prefs.edit()
                .putString(KEY_BODY_PROFILE, profile)
                .putBoolean(KEY_BODY_PROFILE_DONE, true)
                .apply();
    }

    public void clear() {
        prefs.edit().clear().apply();
    }

    private void saveRecord(String key, List<String[]> records, String name, String details) {
        records.add(new String[]{sanitize(name), sanitize(details)});
        prefs.edit().putString(key, serializeRecords(records)).apply();
    }

    private List<String[]> parseRecords(String value) {
        List<String[]> records = new ArrayList<>();
        if (value == null || value.trim().isEmpty()) {
            return records;
        }
        String[] rows = value.split(RECORD_SEPARATOR);
        for (String row : rows) {
            String[] fields = row.split("\\Q" + FIELD_SEPARATOR + "\\E", 2);
            if (fields.length == 2) {
                records.add(new String[]{fields[0], fields[1]});
            }
        }
        return records;
    }

    private String serializeRecords(List<String[]> records) {
        StringBuilder builder = new StringBuilder();
        for (String[] record : records) {
            if (builder.length() > 0) {
                builder.append(RECORD_SEPARATOR);
            }
            builder.append(record[0]).append(FIELD_SEPARATOR).append(record[1]);
        }
        return builder.toString();
    }

    private String sanitize(String value) {
        return value == null ? "" : value.trim().replace(RECORD_SEPARATOR, " ").replace(FIELD_SEPARATOR, " - ");
    }
}
