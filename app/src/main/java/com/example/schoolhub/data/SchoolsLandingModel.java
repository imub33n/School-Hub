package com.example.schoolhub.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.List;

public class SchoolsLandingModel {

    private SchoolData schools;
    private Float rating;

    public SchoolsLandingModel(SchoolData schoolData, float rating) {
        this.schools=schoolData;
        this.rating=rating;
    }

    public Float getRating() { return rating; }

    public void setRating(Float rating) { this.rating = rating; }

    public SchoolData getSchools() { return schools; }

    public void setSchools(SchoolData schools) { this.schools = schools; }

    public static Comparator<SchoolsLandingModel> RatingComparator = new Comparator<SchoolsLandingModel>() {

        public int compare(SchoolsLandingModel s1, SchoolsLandingModel s2) {
            return Float.compare(s2.getRating() , s1.getRating());
//            String StudentName1 = s1.getStudentname().toUpperCase();
//            String StudentName2 = s2.getStudentname().toUpperCase();

            //ascending order
//            return StudentName1.compareTo(StudentName2);

        }
    };
}


