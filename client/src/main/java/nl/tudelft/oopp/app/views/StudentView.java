package nl.tudelft.oopp.app.views;

import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.stage.WindowEvent;
import nl.tudelft.oopp.app.communication.LongPollingThread;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.entities.Moderator;
import nl.tudelft.oopp.app.entities.Question;
import nl.tudelft.oopp.app.entities.Student;

public class StudentView extends SessionView {

    /**View that the student will see.
     *
     * @param textArea - The area where an user can enter a question
     * @param sentQuestionYet - boolean if a question has been sent
     * @param startTimer - time when the user asked a question
     * @param currentTime - the current time in millis
     * @param SLOWDOWN_PERIOD - amount of time between an user can ask a question
     */
    private TextArea textArea;
    private boolean sentQuestionYet;
    private long startTimer;
    private long currentTimer;
    //SLOWDOWN_PERIOD has to be changed back to 120000 (=2 min)
    private static long SLOWDOWN_PERIOD = 120000;

    /**Constructor for the StudentView class.
     *
     * @param roomCode - the room code
     * @param student - a student object corresponding to this window
     */
    public StudentView(String roomCode, Student student) {
        super(roomCode, student);
    }


    /**Method called from LoginView that starts the whole session view.
     * The method adds all the different parts to the view.
     *
     * @throws FileNotFoundException - exception thrown by some methods
     */
    public void start() throws FileNotFoundException {
        startTimer = System.currentTimeMillis();
        sentQuestionYet = false;


        Group mainGroup = new Group();

        createBlueRectangle(mainGroup);

        createSendButton(mainGroup);

        createLeaveButton(mainGroup);

        createDeleteButton(mainGroup);

        setTooFastButton(createTooFastButton(mainGroup));
        setTooSlowButton(createTooSlowButton(mainGroup));
        this.setFeedbackSlider(createSlider(mainGroup));

        createTextArea(mainGroup);

        showQuestions(mainGroup, 545, false);

        createFilterButtons(mainGroup);

        createUsernameText(mainGroup);

        createLogoImage(mainGroup);



        Scene mainScene = new Scene(mainGroup, 1280, 720);
        mainScene.getStylesheets().add(getClass()
                .getResource("/stylesheets/sessionStylesheet.css")
                .toExternalForm());

        this.getStage().setScene(mainScene);
        this.getStage().setTitle("Student");

        this.getStage().setResizable(false);
        this.getStage().show();
        this.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                int votedTooFast = isPressedTooFastButton() ? 1 : 0;
                int votedTooSlow = isPressedTooSlowButton() ? 1 : 0;
                ServerCommunication.leaveRoom(getRoomCode(), getUser().getId(),
                        votedTooFast, votedTooSlow);
            }
        });
    }




    /**
     * Creates a TextArea such that the student can ask a Question.
     */
    public void createTextArea(Group group) {
        this.textArea = new TextArea("Type your question here...(250 characters maximum)");

        this.textArea.setTranslateX(20);
        this.textArea.setTranslateY(600);
        this.textArea.setMinWidth(800);
        this.textArea.setMaxHeight(100);
        this.textArea.setWrapText(true);
        this.textArea.setStyle("-fx-text-inner-color: grey;");
        textArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 250
                        && !change.getControlNewText().contains("\n")
                        && !change.getControlNewText().contains("\t")
                        ? change : null
                )
        );

        addToGroup(group, textArea);

        this.handleQuestionTextAreaFocus(textArea);
    }

    /**
     * Creates a send Button such that the student can ask a Question.
     */
    public void createSendButton(Group group) {
        Button sendButton = new Button("Ask question");

        sendButton.setTranslateX(825);
        sendButton.setTranslateY(675);
        sendButton.setMinWidth(170);

        sendButton.setStyle("-fx-background-color: #00A6D6");

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentTimer = System.currentTimeMillis();
                if (sentQuestionYet == true && currentTimer - startTimer < SLOWDOWN_PERIOD) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("You can only send a question every two minutes"
                            + "\nTry again after "
                            + ((SLOWDOWN_PERIOD - currentTimer + startTimer) / 1000)
                            + " seconds");
                    alert.show();
                } else if (isQuestionAreaSelected()
                        && !textArea.getText().equals("")) {
                    Question question = new Question(getUser(), textArea.getText(),
                            new Timestamp(System.currentTimeMillis()));
                    ServerCommunication.addQuestion(getRoomCode(), question);

                    textArea.setText("Type your question here...(250 characters maximum)");
                    textArea.setStyle("-fx-text-inner-color: grey;");
                    setQuestionAreaSelected(false);
                    sentQuestionYet = true;
                    startTimer = System.currentTimeMillis();
                }
            }
        });

        addToGroup(group, sendButton);
    }

    /**
     * Creates the Logo of TU Delft on the main screen.
     */
    public void createLogoImage(Group group) {
        Image image = new Image(getClass()
                .getResource("/images/tudelft-logo.png")
                .toExternalForm());

        ImageView view = new ImageView(image);

        view.setY(270);
        view.setX(950);
        view.setFitHeight(150);
        view.setPreserveRatio(true);
        view.setRotate(90);

        addToGroup(group, view);
    }

}
