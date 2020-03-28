package draganddrop.studdybuddy.model;

/**
 * pending.
 */
enum SemesterNumber {
    SEMESTER_ONE,
    SEMESTER_TWO
}

/**
 * pending.
 */
public class Semester {
    private int year;
    private SemesterNumber semesterNumber;

    public Semester(int year, SemesterNumber semesterNumber) {
        this.year = year;
        this.semesterNumber = semesterNumber;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public SemesterNumber getSemesterNumber() {
        return semesterNumber;
    }

    public void setSemesterNumber(SemesterNumber semesterNumber) {
        this.semesterNumber = semesterNumber;
    }
}
