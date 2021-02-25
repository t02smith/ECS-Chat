package uk.ac.soton.comp1206.Utility;

import java.util.Comparator;

import uk.ac.soton.comp1206.User;

public class UserComparator implements Comparator<User> {
    private Field field;

    public enum Field {
        LASTACTIVE, SCORE
    }

    public UserComparator(Field field) {
        this.field = field;
    }

    /**
     * Compares users by a requested field
     */
    public int compare(User userA, User userB) {
        switch (this.field) {
            case LASTACTIVE:
                if (userA.getLastActive() >= userB.getLastActive()) return -1;
                else return 1;
            case SCORE:
                if (userA.getTileScore() >= userB.getTileScore()) return -1;
                else return 1;
        }

        return 0;
    }
}
