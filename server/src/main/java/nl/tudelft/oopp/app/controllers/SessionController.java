package nl.tudelft.oopp.app.controllers;

import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.tudelft.oopp.app.entities.Moderator;
import nl.tudelft.oopp.app.entities.Question;
import nl.tudelft.oopp.app.entities.Session;
import nl.tudelft.oopp.app.entities.Student;
import nl.tudelft.oopp.app.entities.Update;
import nl.tudelft.oopp.app.entities.User;
import nl.tudelft.oopp.app.repositories.QuestionRepository;
import nl.tudelft.oopp.app.repositories.SessionRepository;
import nl.tudelft.oopp.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

@Controller
@RequestMapping("/room/{code}")
public class SessionController {

    /**Controller for when the user joined a room. Accessible at /room/{code}.
     * @param sessionRepository - the repository of sessions as stored in the H2 database
     */
    private HashMap<Integer, DeferredResult<List<Update>>> polledUpdates = new HashMap<>();
    private HashMap<Integer, List<Update>> unseenUpdates = new HashMap<>();

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    /**Method called when the user requests questions at /room/{code}/requestQuestions.
     *
     * @param code - the code for the room (it is later checked if this is the mod code
     *             or student code)
     * @param userId - the unique id of the student (to be used to check
     *               if the user actually is in the session)
     * @return - a list of questions formatted to JSON, sent to the client in the response body
     */
    @GetMapping("/requestQuestions")
    @ResponseBody
    public List<Question> requestQuestions(@PathVariable("code") String code,
                                           @RequestParam("userId") String userId) {
        Session studentSession = sessionRepository.findByStudentCode(code);
        Session modSession = sessionRepository.findByModCode(code);

        if (studentSession != null) {
            return studentSession.getQuestions();
        } else if (modSession != null) {
            return modSession.getQuestions();
        }
        return null;
    }


    /**To this endpoint, LongPollingThread makes requests. As a response it receives a list of
     * updates as soon as such are available.
     *
     * @param code - the room code
     * @param userId - the id of the user making the request
     * @return - deferred list of updates (deferred allows us to set the actual response
     *              when updates are available, ie: this doesn't have to happen in this method)
     */
    @GetMapping("/getUpdates")
    @ResponseBody
    public DeferredResult<List<Update>> getUpdates(@PathVariable("code") String code,
                                                     @RequestParam("userId") String userId) {
        long timeOut = 60000L;
        int id = Integer.parseInt(userId);
        DeferredResult<List<Update>> deferredResult =
                new DeferredResult<>(timeOut, new ArrayList<>());

        polledUpdates.put(id, deferredResult);

        if (unseenUpdates.get(id) != null
                && unseenUpdates.get(id).size() != 0) {
            polledUpdates.get(id).setResult(unseenUpdates.get(id));
        }

        deferredResult.onCompletion(() -> {
            polledUpdates.remove(id);
            unseenUpdates.remove(id);
        });

        return deferredResult;
    }


    /**Endpoint corresponding to adding new question.
     *
     * @param code - the room code
     * @param userId - the id of the user making the request
     * @param body - the body of the request
     */
    @PostMapping("/addQuestion")
    @ResponseBody
    public void addQuestion(@PathVariable("code") String code,
                              @RequestParam("userId") String userId,
                              @RequestBody String body) {
        Session session = findSessionByCode(code);
        if (!session.isActive()) {
            return;
        }

        User user = userRepository.findById(Integer.parseInt(userId));

        Question question = new Question(user, body, new Timestamp(System.currentTimeMillis()));

        Question newQuestion = questionRepository.save(question);
        session.addQuestion(newQuestion);
        sessionRepository.save(session);

        List<Question> questionList = new ArrayList<>();
        questionList.add(newQuestion);

        Update newUpdate = new Update("addQuestion", questionList);

        for (Student student : session.getStudents()) {
            this.handleNewUpdate(newUpdate, student.getId());
        }
        for (Moderator moderator : session.getModerators()) {
            this.handleNewUpdate(newUpdate, moderator.getId());
        }
    }

    /**Endpoint corresponding to closing the room.
     *
     * @param code - the room code
     */
    @GetMapping("/closeRoom")
    @ResponseBody
    public void closeRoom(@PathVariable("code") String code) {
        Session session = findSessionByCode(code);
        if (!session.isActive()) {
            return;
        }

        Update newUpdate = new Update("closeRoom");

        for (Student student : session.getStudents()) {
            this.handleNewUpdate(newUpdate, student.getId());
        }
        for (Moderator moderator : session.getModerators()) {
            this.handleNewUpdate(newUpdate, moderator.getId());
        }

        session.setActive(false);
        sessionRepository.save(session);
    }

    /**Endpoint corresponding to leaving a room.
     *It updates the student count
     * @param code - the room code
     */
    @PostMapping("/leaveRoom")
    @ResponseBody
    public void leaveRoom(@PathVariable("code") String code,
                          @RequestBody String body) {
        String[] stArray = body.split("&");
        int studentId = Integer.valueOf(stArray[0]);
        int userVotedTooFast = Integer.valueOf(stArray[1]);
        int userVotedTooSlow = Integer.valueOf(stArray[2]);
        Session session = findSessionByCode(code);
        session.removeStudentById(studentId);
        session.setTooFastVotes(session.getTooFastVotes() - userVotedTooFast);
        session.setTooSlowVotes(session.getTooSlowVotes() - userVotedTooSlow);
        sessionRepository.save(session);
        Update newUpdate = new Update("leaveRoom", session.getTooFastVotes(),
                session.getTooSlowVotes(), session.getNumberOfStudents());

        for (Student student : session.getStudents()) {
            this.handleNewUpdate(newUpdate, student.getId());
        }
        for (Moderator moderator : session.getModerators()) {
            this.handleNewUpdate(newUpdate, moderator.getId());
        }


    }

    /**Endpoint corresponding to deleting the questions.
     *
     * @param code - the code of the room
     * @param body - the body of the request. It should contain the ids of
     *             the questions to delete
     */
    @PostMapping("/deleteQuestions")
    @ResponseBody
    public void deleteQuestions(@PathVariable("code") String code,
                                @RequestBody String body) {
        Session session = this.findSessionByCode(code);

        String[] ids = body.split("&");
        List<Question> questionsToDelete = new ArrayList<>();

        for (String id : ids) {
            Question question = questionRepository.findById(Integer.parseInt(id));
            questionsToDelete.add(question);
        }

        Update newUpdate = new Update("deleteQuestions", questionsToDelete);

        for (Student student : session.getStudents()) {
            this.handleNewUpdate(newUpdate, student.getId());
        }
        for (Moderator moderator : session.getModerators()) {
            this.handleNewUpdate(newUpdate, moderator.getId());
        }

        for (Question question : questionsToDelete) {
            if (question != null) {
                questionRepository.delete(question);
            }
        }
    }


    /**
     * Server endpoint that updates the variables used to display the feedback
     * whenever a change is made to them.
     */
    @PostMapping("/updateFeedbackVariables")
    @ResponseBody
    public void updateFeedbackVariables(@PathVariable("code") String code,
                                  @RequestBody String body) {
        Session session = this.findSessionByCode(code);
        String[] stArray = body.split("&");
        int tooFastVotes = Integer.valueOf(stArray[0]);
        int tooSlowVotes = Integer.valueOf(stArray[1]);
        Update newUpdate = new Update("updateFeedbackVariables", tooFastVotes,
                tooSlowVotes, session.getNumberOfStudents());

        for (Student student : session.getStudents()) {
            this.handleNewUpdate(newUpdate, student.getId());
        }
        for (Moderator moderator : session.getModerators()) {
            this.handleNewUpdate(newUpdate, moderator.getId());
        }
        session.setTooFastVotes(newUpdate.getUpdatedTooFastVotes());
        session.setTooSlowVotes(newUpdate.getUpdatedTooSlowVotes());
        sessionRepository.save(session);
    }


    /**
     * Server endpoint that updates the variables used to display the feedback
     * when a user first joins a session.
     */
    @GetMapping("/initializeFeedbackVariables")
    @ResponseBody
    public String initializeFeedbackVariables(@PathVariable("code") String code) {
        StringBuilder toReturn = new StringBuilder();
        Session session = this.findSessionByCode(code);
        toReturn.append(session.getTooFastVotes()).append("&")
                .append(session.getTooSlowVotes()).append("&")
                .append(session.getNumberOfStudents());
        return toReturn.toString();
    }


    /**Endpoint to ban the supplied list of users.
     *
     * @param code - the code of the session from which the users should be banned
     * @param body - the body of the request, containing the ids of the users to ban
     */
    @PostMapping("/banUsers")
    @ResponseBody
    public void banUsers(@PathVariable("code") String code,
                         @RequestBody String body) {
        String[] ids = body.split("&");

        Update newUpdate = new Update("banUser");

        Session session = this.findSessionByCode(code);

        for (String id : ids) {
            this.handleNewUpdate(newUpdate, Integer.parseInt(id));

            User bannedUser = userRepository.findById(Integer.parseInt(id));
            session.addBannedUser(bannedUser);
        }
        sessionRepository.save(session);
    }


    /**Endpoint corresponding to upvoting a question.
     *
     * @param code - the room code
     * @param body - the body of the request. It should contain the id of
     *             the question to upvote
     */
    @PostMapping("/upvoteQuestion")
    @ResponseBody
    public void upvoteQuestion(@PathVariable("code") String code,
                               @RequestBody String body)  {
        Session session = findSessionByCode(code);

        if (!session.isActive()) {
            return;
        }
        int questionId = Integer.parseInt(body);

        Question question = questionRepository.findById(questionId);

        question.upvoteQuestion();
        questionRepository.save(question);

        Update newUpdate = new Update("upvoteQuestion", question);

        for (Student student : session.getStudents()) {
            this.handleNewUpdate(newUpdate, student.getId());
        }
        for (Moderator moderator : session.getModerators()) {
            this.handleNewUpdate(newUpdate, moderator.getId());
        }
    }
    
    /** Method to accept an HTTP request for marking questions as answered,
     * making sure it is marked as answeres server side and sent to all
     * connected clients.
     *
     * @param code - Code of the session that the request came from
     * @param body - HTTP body containing ids of all questions to be marked as answered
     */
    
    @PostMapping("markAnswered")
    @ResponseBody
    public void markAnswered(@PathVariable("code") String code, @RequestBody String body) {
        Session session = findSessionByCode(code);
        String[] ids = body.split("&");
        List<Question> questions = new ArrayList<>();
        
        for (int i = 0; i < ids.length; i++) {
            Question current = questionRepository.findById(Integer.parseInt(ids[i]));
            if (!current.isAnswered()) {
                questions.add(current);
            }
            current.setAnswered(true);
            questionRepository.save(current);
        }
        
        Update newUpdate = new Update("markAnswered", questions);
        
        for (Student student : session.getStudents()) {
            this.handleNewUpdate(newUpdate, student.getId());
        }
        for (Moderator moderator : session.getModerators()) {
            this.handleNewUpdate(newUpdate, moderator.getId());
        }
    }

    /**Endpoint corresponding to rephrasing a question.
     *
     * @param code - the room code
     * @param questionId - the id of the question that should be rephrased
     * @param body - the body of the request (in this case the message of the question)
     */
    @PostMapping("/rephraseQuestion")
    @ResponseBody
    public void rephraseQuestion(@PathVariable("code") String code,
                            @RequestParam("questionId") String questionId,
                            @RequestBody String body) {
        Session session = findSessionByCode(code);

        Question question = questionRepository.findById(Integer.parseInt(questionId));

        question.setMessage(body);

        questionRepository.save(question);

        Update newUpdate = new Update("rephraseQuestion", question);

        for (Student student : session.getStudents()) {
            this.handleNewUpdate(newUpdate, student.getId());
        }
        for (Moderator moderator : session.getModerators()) {
            this.handleNewUpdate(newUpdate, moderator.getId());
        }
    }

    /**Endpoint corresponding to answering a question.
     *
     * @param code - the room code
     * @param questionId - the id of the question that should be rephrased
     * @param body - the body of the request (in this case the message of the question)
     */
    @PostMapping("/answerQuestion")
    @ResponseBody
    public void answerQuestion(@PathVariable("code") String code,
                                 @RequestParam("questionId") String questionId,
                                 @RequestBody String body) {
        Session session = findSessionByCode(code);

        Question question = questionRepository.findById(Integer.parseInt(questionId));

        question.setAnswer(body);

        questionRepository.save(question);

        Update newUpdate = new Update("answerQuestion", question);

        for (Student student : session.getStudents()) {
            this.handleNewUpdate(newUpdate, student.getId());
        }
        for (Moderator moderator : session.getModerators()) {
            this.handleNewUpdate(newUpdate, moderator.getId());
        }
    }

    /**Handle new update, ie: inform the long polling requests that new update is
     * available and if no request exists from the user (for example because
     * of slow internet) add the update to the list of awaited updates for the user.
     *
     * @param update - the new update to inform users about
     * @param id - the id of the user to send the request to
     */
    private void handleNewUpdate(Update update, int id) {
        List<Update> updateList = new ArrayList<>();
        updateList.add(update);

        if (polledUpdates.get(id) != null) {
            polledUpdates.get(id).setResult(updateList);
        } else {
            if (unseenUpdates.get(id) == null) {
                unseenUpdates.put(id, updateList);
            } else {
                unseenUpdates.get(id).addAll(updateList);
            }
        }
    }

    /**Find the session corresponding to the given code.
     *
     * @param code - the code of the session to search for
     * @return - the found session
     */
    private Session findSessionByCode(String code) {
        Session session = sessionRepository.findByStudentCode(code);
        if (session == null) {
            session = sessionRepository.findByModCode(code);
        }
        return session;
    }

}
