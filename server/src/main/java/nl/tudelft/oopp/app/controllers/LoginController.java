package nl.tudelft.oopp.app.controllers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.TooManyListenersException;

import javax.servlet.http.HttpServletRequest;

import nl.tudelft.oopp.app.entities.Moderator;
import nl.tudelft.oopp.app.entities.Session;
import nl.tudelft.oopp.app.entities.Student;
import nl.tudelft.oopp.app.entities.User;
import nl.tudelft.oopp.app.repositories.SessionRepository;
import nl.tudelft.oopp.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class LoginController {

    /**Controller used when the client is in the login screen.
     *
     * @param sessionRepository - the repository of sessions used by the H2 database
     * @param userRepository - the repository of users used by the H2 database
     */

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    /**Method to check if a room with the given code exists.
     *
     * @param code - the room code to check for
     * @return - "user" if the code corresponds to a normal user mode,
     *              "mod" if the code corresponds to a mod
     *              and an empty String if the room does not exist
     */
    private String checkIfRoomExists(String code) {
        Session studentSession = sessionRepository.findByStudentCode(code);
        Session modSession = sessionRepository.findByModCode(code);

        if (studentSession != null) {
            return "student";
        } else if (modSession != null) {
            return "mod";
        }
        return "";
    }

    /**Method to generate a random user id, there are 2^32 combinations,
     * hence the chance of collision is very low.
     *
     * @return - the randomly generated user id
     */
    private int generateRandomUserId() {
        Random random = new Random();
        long id = random.nextLong();
        return random.nextInt(Integer.MAX_VALUE);

    }

    /**Method to join the room at /joinRoom.
     * If the room exists, "join" it.
     *
     * @param code - the room code
     * @param username - the username of the person trying to join
     * @return - empty string if the room doesn't exist, "role&userId" otherwise
     */
    @GetMapping("/joinRoom")
    @ResponseBody
    public String joinRoom(@RequestParam("code") String code,
                           @RequestParam("username") String username,
                           HttpServletRequest request) {
        String ip = request.getRemoteAddr();

        String role = checkIfRoomExists(code);

        int uniqueId = generateRandomUserId();

        if (role.equals("student")) {
            Session session = sessionRepository.findByStudentCode(code);
            if (session.getDate().compareTo(new Date()) > 0
                    || !session.isActive()
                    || ipIsBanned(session, ip)) {
                return "";
            }
            Student newStudent = new Student(username, uniqueId);
            newStudent.setIP(ip);
            session.addStudent(newStudent);

            userRepository.save(newStudent);
            return role + "&" + uniqueId;
        } else if (role.equals("mod")) {
            Session session = sessionRepository.findByModCode(code);
            Moderator newMod = new Moderator(username, uniqueId);
            newMod.setIP(ip);
            session.addModerator(newMod);

            userRepository.save(newMod);
            return role + "&" + uniqueId;
        }

        return "";
    }

    /**Method to get the student code of a session
     * If the room exists, get the student code it.
     * @param code - the moderator room code
     * @return - empty string if the room doesn't exist, "studentCode" otherwise
     */
    @GetMapping("/getStudentCode")
    @ResponseBody
    public String getStudentCode(@RequestParam("code") String code, HttpServletRequest request) {
        Session session = sessionRepository.findByModCode(code);
        if (session != null) {
            return session.getStudentCode();
        } else {
            return "";
        }
    }

    /**Method called when a user tries to create a room.
     *
     * @param body - the body of the POST request, this contains the room name and timestamp
     * @return - the codes for the room in the format: studentCode&modCode
     */
    @PostMapping("/createRoom")
    @ResponseBody
    public String createRoom(@RequestBody String body) {
        String roomName = body.split("&")[0];
        String stringTimestamp = body.split("&")[1];

        Timestamp timestamp = Timestamp.valueOf(stringTimestamp);
        Session newSession = new Session(roomName, timestamp);
        sessionRepository.save(newSession);
        return newSession.getStudentCode() + "&" + newSession.getModCode();
    }

    /**Check if the given IP is banned in the session.
     * 
     * @param session - the session to check the banned IPs in
     * @param ip - the IP that is to be checked if it is not banned
     * @return - true if ip is banned, false otherwise
     */
    public boolean ipIsBanned(Session session, String ip) {
        for (User user : session.getBannedUsers()) {
            if (user.getIP() != null && user.getIP().equals(ip)) {
                return true;
            }
        }
        return false;
    }
}
