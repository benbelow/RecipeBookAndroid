package com.example.ben.recipebook.services;

public class TimeFormatter {

    public static String format(int timeInMinutes){
        int hours = timeInMinutes / 60;
        int minutes = timeInMinutes % 60;

        String displayedHours = hours > 0 ? hours + "hr " : "";
        String displayedMinutes = minutes + "min";

        return displayedHours + displayedMinutes;
    }

}
