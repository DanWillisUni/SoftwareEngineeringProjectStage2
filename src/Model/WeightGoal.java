package Model;

import java.util.Date;

public class WeightGoal {
    int id;
    User user;
    int targetWeight;
    Date set;
    Date due;
    boolean toLoose;//true if the goal is to get under targetweight

    public int getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public int getTargetWeight() {
        return targetWeight;
    }
    public Date getSet() {
        return set;
    }
    public Date getDue() {
        return due;
    }
    public boolean getToLoose() {
        return toLoose;
    }

    public void add(){

    }
    public void remove(){

    }
}

