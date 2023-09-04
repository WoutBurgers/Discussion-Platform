package nl.tudelft.oopp.app.views;

import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.entities.Moderator;
import nl.tudelft.oopp.app.entities.Student;
import nl.tudelft.oopp.app.entities.User;


public class LoginView extends Application {

    /**Class to control the login screen UI.
     * @param selectedTextFields - hash map containing the textfields that
     *                           have something in them (not the default text)
     * @param stage - the stage of the UI
     */
    private HashMap<TextField, String> selectedTextFields;
    private Stage stage;
    private SessionView sessionView;

    /**Main starting point for the class (ran from the ClientMainApp class).
     *
     * @param args - the arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**Method used by JavaFX to start the application.
     *
     * @param primaryStage - the stage initialized by JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        this.selectedTextFields = new HashMap<>();

        Group root = new Group();
        Scene mainScene = new Scene(root, 800, 600);
        mainScene.getStylesheets().add(getClass()
                .getResource("/stylesheets/loginStylesheet.css").toExternalForm());

        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Login");
        primaryStage.setResizable(false);

        showJoinRoomIcons(root);
        showCreateRoomIcons(root);
        showDetails(root);

        primaryStage.show();
    }

    /**Tell a textField what it is supposed to do when it is focused on or unfocused.
     * If the textField is focused on and it only contains the grey-ish text,
     * it will delete that text and change the color to black.
     * If the textField is unfocused from, it will check
     * if it is empty and if so change the inner text to the default
     * text and the default font.
     *
     * @param textField - the textField to attach the handler to
     */
    private void handleTextFieldFocus(TextField textField) {
        //Based on:
        //shorturl.at/bsxK7
        //I've decided to shorten the url in order to respect the check style limit of
        // 100 characters per line
        textField.focusedProperty().addListener((ov, oldV, newV) -> {
            //If the textField was focused on
            if (newV && !selectedTextFields.containsKey(textField)) {
                selectedTextFields.put(textField, textField.getText());
                textField.clear();
                textField.setStyle("-fx-text-inner-color: black;");
            } else {  //If the textField was unfocused
                if (textField.getText().length() == 0) {
                    textField.setText(selectedTextFields.get(textField));
                    textField.setStyle("-fx-text-inner-color: grey;");
                    selectedTextFields.remove(textField);
                }
            }
        });
    }

    /**Method to specify what should happen when user clicks the join room button.
     * It should check if the code and the username have been provided and
     * then send the join information to the server. If the room
     * exists, close the UI and open the SessionView.
     *
     * @param joinRoomButton - the button responsible for joining a room
     * @param usernameTextField - the textField containing the username
     * @param roomCodeTextField - the textField containing the room code
     */
    private void joinRoomEventHandler(Button joinRoomButton, TextField usernameTextField,
                                      TextField roomCodeTextField) {
        joinRoomButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (checkIfUsernameIsValid(usernameTextField)
                        && checkIfRoomCodeIsValid(roomCodeTextField)) {
                    String data = ServerCommunication.joinRoom(roomCodeTextField.getText(),
                            usernameTextField.getText());
                    if (data != null) {
                        data += "&" + usernameTextField.getText()
                                + "&" + roomCodeTextField.getText();
                        stage.close();
                        createNewSession(data);
                        return;
                    }

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("No such active room");
                    alert.setHeaderText("No such room exists or the room is currently closed. "
                            + "\nPlease try again later or with a different code.");
                    alert.show();
                }
            }
        });
    }

    /**Check if the username consists only of alphanumeric characters and "_".
     *
     * @param usernameTextField - the textField containing the username
     * @return - true if the username is valid, false otherwise
     */
    public boolean checkIfUsernameIsValid(TextField usernameTextField) {
        if (!selectedTextFields.containsKey(usernameTextField)
                || usernameTextField.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No username specified");
            alert.setHeaderText("The username must not be empty.");
            alert.show();
            return false;
        }

        String username = usernameTextField.getText();
        if (username.matches("([a-z]|[A-Z]|[0-9]|_)+")) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect username");
            alert.setHeaderText("The username may only consist of "
                    + "alphanumeric characters and \"_\".");
            alert.show();
            return false;
        }
    }

    /**Check if the provided room code consists only of lowercase letters.
     *
     * @param roomCodeTextField - the textField containing the room code
     * @return - true if room code is valid, false otherwise
     */
    public boolean checkIfRoomCodeIsValid(TextField roomCodeTextField) {
        if (!selectedTextFields.containsKey(roomCodeTextField)
                || roomCodeTextField.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No room code specified");
            alert.setHeaderText("The room code must not be empty.");
            alert.show();
            return false;
        }
        String roomCode = roomCodeTextField.getText();
        if (roomCode.matches("([a-z])+")) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect room code");
            alert.setHeaderText("The room code may only consist of lowercase letters.");
            alert.show();
            return false;
        }
    }

    /**Creates a new session and joins it.
     *
     * @param data - the data as returned by the server + username + roomCode:
     *            (role&id&username&roomCode)
     */
    public void createNewSession(String data) {
        User user = createNewUser(data);
        String code = data.split("&")[3];

        try {
            if (user instanceof Student) {
                StudentView studentView = new StudentView(code, (Student)user);
                this.sessionView = studentView;
                studentView.start();
            } else if (user instanceof Moderator) {
                ModeratorView moderatorView = new ModeratorView(code, (Moderator)user);
                this.sessionView = moderatorView;
                moderatorView.start();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**Creates a new user based on the data returned from the server + username.
     *
     * @param data - the data as returned by the server + username + roomCode:
     *             (role&id&username&roomCode)
     * @return - new Student if the user is a student, new Moderator if the user is a moderator
     */
    private User createNewUser(String data) {
        String role = data.split("&")[0];
        int id = Integer.parseInt(data.split("&")[1]);
        String username = data.split("&")[2];

        if (role.equals("student")) {
            return new Student(username, id);
        } else {
            return new Moderator(username, id);
        }

    }

    /**Method to specify what should happen when a user tries to create a new room.
     * Currently it only checks if the room name, date, hour are not empty.
     *
     * @param createRoomButton - the button responsible for creating a room
     * @param roomNameTextField - the textField containing the room name
     * @param startDateDatePicker - the textField containing the start date of the room
     * @param startTimeTextField - the textField containing the start time of the room
     */
    private void createRoomEventHandler(Button createRoomButton, TextField roomNameTextField,
                                        DatePicker startDateDatePicker,
                                        TextField startTimeTextField) {
        createRoomButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (checkIfRoomNameIsValid(roomNameTextField)
                        && checkIfStartTimeIsValid(startTimeTextField)) {
                    String startDate = startDateDatePicker.getEditor().getText();
                    String startTime = startTimeTextField.getText();
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    if (startDate == null || startDate.equals("")
                            || !selectedTextFields.containsKey(startDateDatePicker.getEditor())) {
                        startDate = now.getDate() + "/"
                                + (now.getMonth() + 1) + "/"
                                + (now.getYear() + 1900);
                    }
                    if (startTime == null || startTime.equals("")
                            || !selectedTextFields.containsKey(startTimeTextField)) {
                        startTime = now.getHours() + ":" + now.getMinutes();
                    }

                    String roomName = roomNameTextField.getText();

                    Timestamp timestamp = convertToTimestamp(startDate, startTime);
                    String code = ServerCommunication.createRoom(roomName, timestamp);
                    if (code != null) {
                        String[] codes = code.split("&");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Room created");
                        alert.setHeaderText("You have created a room. The codes are:\n");
                        alert.setWidth(300);
                        alert.setHeight(70);
                        TextArea textArea = new TextArea("Student code: " + codes[0] + "\n"
                                + "Moderator code: " + codes[1]);
                        textArea.setEditable(false);
                        textArea.setMaxWidth(300);
                        textArea.setMaxHeight(50);
                        textArea.setMinHeight(50);
                        textArea.setFont(new Font(14));
                        textArea.setStyle("-fx-text-box-border: transparent;"
                                + "-fx-background-color: transparent;"
                                + "-fx-control-inner-background: transparent;"
                                + "-fx-text-fill: #323232;"
                                + "-fx-focus-color: transparent;"
                                + "-fx-faint-focus-color: transparent;");
                        GridPane gridPane = new GridPane();
                        gridPane.add(textArea, 0,0);
                        alert.getDialogPane().setContent(gridPane);
                        alert.show();
                    }
                }
            }
        });
    }

    /**Check if the room name contains only alphanumeric characters, spaces and "_".
     *
     * @param roomNameTextField - the textField containing the room name
     * @return - true if the room name is valid, false otherwise
     */
    public boolean checkIfRoomNameIsValid(TextField roomNameTextField) {
        if (!selectedTextFields.containsKey(roomNameTextField)
                || roomNameTextField.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No room name specified");
            alert.setHeaderText("You must specify the name of the room.");
            alert.show();
            return false;
        }
        String roomName = roomNameTextField.getText();
        if (roomName.matches("([a-z]|[A-Z]|[0-9]|_| )+")) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid room name");
            alert.setHeaderText("The name may only contain alphanumeric "
                    + "characters, spaces and \"_\"");
            alert.show();
            return false;
        }
    }
    

    /**Check if the start time is in the correct format, ie: hh:mm.
     *
     * @param startTimeTextField - the textField containing the start time
     * @return - true if the time is in the correct format, false otherwise
     */
    public boolean checkIfStartTimeIsValid(TextField startTimeTextField) {
        if (!selectedTextFields.containsKey(startTimeTextField)
                || startTimeTextField.getText().equals("")) {
            return true;
        }
        String startTime = startTimeTextField.getText();
        if (startTime.matches("(([0-1][0-9])|(2[0-3])|([0-9])):(([0-5][0-9])|([0-9]))")) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect time specified");
            alert.setHeaderText("The start time must be specified in the format hh:mm, eg: 21:37.");
            alert.show();
            return false;
        }
    }

    /**Creates a timestamp from the start date and start time specified by the user.
     *
     * @param startDate - the start date of the room
     * @param startTime - the start time of the room
     * @return - the timestamp generated from startDate and startTime
     */
    private Timestamp convertToTimestamp(String startDate, String startTime) {
        String[] splitStartDate = startDate.split("/");
        String[] splitStartTime = startTime.split(":");

        int day = Integer.parseInt(splitStartDate[0]);
        int month = Integer.parseInt(splitStartDate[1]);
        int year = Integer.parseInt(splitStartDate[2]);

        int hour = Integer.parseInt(splitStartTime[0]);
        int minute = Integer.parseInt(splitStartTime[1]);

        return new Timestamp(year - 1900, month - 1, day, hour, minute, 0, 0);
    }

    /**Method used to draw icons corresponding to joining a room
     * and attaching the correct event handlers to them.
     *
     * @param root - the group corresponding to the elements in this window
     */
    private void showJoinRoomIcons(Group root) {
        TextField usernameTextField = new TextField();
        TextField roomCodeTextField = new TextField();
        Button joinRoomButton = new Button();

        usernameTextField.setText("Username");
        roomCodeTextField.setText("Room Code");
        joinRoomButton.setText("Join Room");

        // Set a character limit to the username textfield.
        usernameTextField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 20 ? change : null)
        );


        joinRoomButton.setFont(new Font(18));
        joinRoomButton.setMinWidth(150);

        roomCodeTextField.setFont(new Font(18));
        usernameTextField.setFont(new Font(18));

        root.getChildren().add(usernameTextField);
        root.getChildren().add(roomCodeTextField);
        root.getChildren().add(joinRoomButton);

        root.applyCss();
        root.layout();

        joinRoomButton.setTranslateX(300 - joinRoomButton.getWidth() / 2);
        joinRoomButton.setTranslateY(125);

        roomCodeTextField.setTranslateX(300 - roomCodeTextField.getWidth() / 2);
        roomCodeTextField.setTranslateY(70);

        usernameTextField.setTranslateX(300 - usernameTextField.getWidth() / 2);
        usernameTextField.setTranslateY(20);

        this.handleTextFieldFocus(roomCodeTextField);
        this.handleTextFieldFocus(usernameTextField);

        this.joinRoomEventHandler(joinRoomButton, usernameTextField, roomCodeTextField);
    }

    /**Method used to draw icons corresponding to creating a room
     * and attaching the correct event handlers to them.
     *
     * @param root - the group corresponding to the elements in this window
     */
    private void showCreateRoomIcons(Group root) {
        TextField roomNameTextField = new TextField();
        DatePicker startDateDatePicker = new DatePicker();
        TextField startTimeTextField = new TextField();

        roomNameTextField.setText("Room Name");
        startDateDatePicker.getEditor().setText("Start Date");
        startTimeTextField.setText("Start Time [hh:mm]");

        // Set a character limit to the room name textfield.
        roomNameTextField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 20 ? change : null)
        );

        Button createRoomButton = new Button();
        //had to move the declaration here, due to check styles
        createRoomButton.setText("Create Room");

        createRoomButton.setFont(new Font(18));
        createRoomButton.setMinWidth(150);

        roomNameTextField.setFont(new Font(18));
        startDateDatePicker.getEditor().setFont(new Font(18));
        startDateDatePicker.getEditor().setDisable(true);
        startTimeTextField.setFont(new Font(18));


        root.getChildren().add(roomNameTextField);
        root.getChildren().add(startDateDatePicker);
        root.getChildren().add(startTimeTextField);
        root.getChildren().add(createRoomButton);

        root.applyCss();
        root.layout();

        roomNameTextField.setTranslateX(300 - roomNameTextField.getWidth() / 2);
        roomNameTextField.setTranslateY(250);

        startDateDatePicker.setTranslateX(300 - roomNameTextField.getWidth() / 2);
        startDateDatePicker.setTranslateY(300);

        startTimeTextField.setTranslateX(300 - roomNameTextField.getWidth() / 2);
        startTimeTextField.setTranslateY(350);

        createRoomButton.setTranslateX(300 - createRoomButton.getWidth() / 2);
        createRoomButton.setTranslateY(405);

        this.handleTextFieldFocus(roomNameTextField);
        this.handleTextFieldFocus(startDateDatePicker.getEditor());
        this.handleTextFieldFocus(startTimeTextField);


        this.createRoomEventHandler(createRoomButton, roomNameTextField,
                startDateDatePicker, startTimeTextField);

        startDateDatePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    /**Method used to draw icons corresponding to details.
     *
     * @param root - the group corresponding to the elements in this window
     */
    private void showDetails(Group root) {
        Rectangle rectangle = new Rectangle();
        Image tuDelftLogo = new Image(getClass().getResourceAsStream("/images/tudelft-logo.png"));
        ImageView tuDelftLogoView = new ImageView(tuDelftLogo);
        tuDelftLogoView.setRotate(90);

        rectangle.setX(600);
        rectangle.setY(0);
        rectangle.setWidth(200);
        rectangle.setHeight(600);

        double scalingFactor = 1.25;

        tuDelftLogoView.setFitWidth(tuDelftLogo.getWidth() * scalingFactor);
        tuDelftLogoView.setFitHeight(tuDelftLogo.getHeight() * scalingFactor);

        rectangle.setFill(Color.web("#00A6D6"));

        root.getChildren().add(rectangle);
        root.getChildren().add(tuDelftLogoView);

        root.applyCss();
        root.layout();

        tuDelftLogoView.setX(700 - tuDelftLogo.getWidth() * scalingFactor / 2);
        tuDelftLogoView.setY(250 - tuDelftLogo.getHeight() * scalingFactor / 2);
    }

    /**
     * Method to interrupt the long polling thread.
     */
    @Override
    public void stop() {
        if (sessionView != null) {
            sessionView.getLongPollingThread().interrupt();
        }
    }
}
