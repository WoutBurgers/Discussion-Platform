package nl.tudelft.oopp.app.entities;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * Class Session, represents a newly made chatroom for the current lecture.
 */
@Entity
@Table(name = "sessions")
public class Session {
    /**Class responsible for creating a Session.
     * @param id - unique id for the session
     * @param active - check if the current Session is active
     * @param Date - the date for which the Session is scheduled
     * @param studentCode - the code used by the Student to log in
     * @param modCode - the code used by the Moderators/Teachers to log in
     * @param questions - a list that contains all the questions
     * @param students - a list that contains the active Students
     * @param moderators - a list that contains the active Moderators
     * @param bannedUsers - list of the IP banned users
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private boolean active;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "studentCode")
    private String studentCode;

    @Column(name = "modCode")
    private String modCode;

    @Column(name = "tooSlowVotes")
    private int tooSlowVotes;

    @Column(name = "tooFastVotes")
    private int tooFastVotes;

    @OneToMany
    @JoinColumn(name = "questionId")
    List<Question> questions;

    @OneToMany
    @JoinColumn(name = "studentId")
    private List<Student> students;

    @OneToMany
    @JoinColumn(name = "modId")
    private List<Moderator> moderators;

    @OneToMany
    @JoinColumn(name = "bannedUsers")
    private List<User> bannedUsers;

    /**
     * Empty constructor to be used by the H2 Database.
     */
    public Session() {

    }

    /**
     * Constructs a new Session.
     */
    public Session(String name, Timestamp date) {
        this.name = name;
        this.active = true;
        this.date = date;
        this.studentCode = generateRandomCode();
        this.modCode = generateRandomCode();
        this.questions = new ArrayList<>();
        this.students = new ArrayList<>();
        this.moderators = new ArrayList<>();
        this.tooFastVotes = 0;
        this.tooSlowVotes = 0;
        this.bannedUsers = new ArrayList<>();
    }

    /**
     * Constructor to be *only* used in testing that
     * allows setting the student code and the mod code.
     */
    public Session(String name, Timestamp date, String studentCode, String modCode) {
        this.name = name;
        this.active = true;
        this.date = date;
        this.studentCode = studentCode;
        this.modCode = modCode;
        this.questions = new ArrayList<>();
        this.students = new ArrayList<>();
        this.moderators = new ArrayList<>();
        this.tooFastVotes = 0;
        this.tooSlowVotes = 0;
        this.bannedUsers = new ArrayList<>();
    }

    /**
     * Getters and setters for all attributes of the class.
     *
     */
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public String getModCode() {
        return modCode;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<Student> getStudents() {
        return this.students;
    }

    public List<Moderator> getModerators() {
        return this.moderators;
    }

    public int getNumberOfStudents() {
        return this.students.size();
    }

    public int getNumberOfModerators() {
        return this.moderators.size();
    }

    public int getTooSlowVotes() {
        return tooSlowVotes;
    }

    public int getTooFastVotes() {
        return tooFastVotes;
    }

    public List<User> getBannedUsers() {
        return bannedUsers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public void setModCode(String modCode) {
        this.modCode = modCode;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void setModerators(List<Moderator> moderators) {
        this.moderators = moderators;
    }

    public void setTooSlowVotes(int tooSlowVotes) {
        this.tooSlowVotes = tooSlowVotes;
    }

    public void setTooFastVotes(int tooFastVotes) {
        this.tooFastVotes = tooFastVotes;
    }

    public void setBannedUsers(List<User> bannedUsers) {
        this.bannedUsers = bannedUsers;
    }

    /**
     * Methods to add new object to the corresponding list.
     */
    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void addModerator(Moderator mod) {
        this.moderators.add(mod);
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public void removeQuestion(Question question) {
        this.questions.remove(question);
    }

    public void addBannedUser(User user) {
        this.bannedUsers.add(user);
    }

    /**
     * Methods that searches the student list by Id and removes the corresponding student if found.
     * @param id - id of the searched student
     */
    public boolean removeStudentById(int id) {
        for (Student student: students) {
            if (student.getId() == id) {
                return students.remove(student);
            }
        }
        return false;
    }

    /**
     * Method that generates a random String, used for the Student login.
     * source: https://www.baeldung.com/java-random-string
     * @return the newly generated String, in order to be displayed

     */
    public String generateRandomCode() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

    /**
     * Check if 2 Session Objects are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        return isActive() == session.isActive()
                && Objects.equals(getDate(), session.getDate())
                && Objects.equals(getStudentCode(), session.getStudentCode())
                && Objects.equals(getModCode(), session.getModCode())
                && Objects.equals(getStudents(), session.getStudents())
                && Objects.equals(getModerators(), session.getModerators())
                && Objects.equals(getQuestions(), session.getQuestions());
    }

    /**Method to generate a random hash code for the session.
     *
     * @return - the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(isActive(), getDate(),
                getStudentCode(), getModCode(),
                getStudents(), getModerators());
    }

    /**Override the default toString method.
     *
     * @return - a string representation of the session
     */
    @Override
    public String toString() {
        return "Session<id: " + this.id
                + ", studentCode: " + this.studentCode
                + ", modCode: " + this.modCode + ">";
    }
}
