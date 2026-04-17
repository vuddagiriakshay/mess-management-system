package mess;

import javafx.application.Application;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.*;

public class Main extends Application {

    private ObservableList<Student> students = FXCollections.observableArrayList();
    private Map<String, Student> studentMap = new HashMap<>();

    private int idCounter = 1;
    private String currentUser = "";

    private List<String> allowedUsers = Arrays.asList(
            "akshay","sampath","subash","raju","ravi","kishan",
            "abhisek","head","klassen","nitish kumar reddy",
            "aniket verma","bisnoi"
    );

    @Override
    public void start(Stage stage) {

        // ---------------- LOGIN ----------------
        Label title = new Label("Mess Management System");
        title.setStyle("-fx-font-size:18px; -fx-font-weight:bold;");

        Label roleLabel = new Label("Student Login");

        Button studentBtn = new Button("👤 Student Login");
        Button adminBtnLogin = new Button("👑 Admin Login");

        TextField username = new TextField();
        username.setPromptText("Username");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        Button loginBtn = new Button("Login");

        final String[] mode = {"student"};

        studentBtn.setOnAction(e -> {
            mode[0] = "student";
            roleLabel.setText("Student Login");
        });

        adminBtnLogin.setOnAction(e -> {
            mode[0] = "admin";
            roleLabel.setText("Admin Login");
        });

        VBox loginCard = new VBox(12, title, roleLabel, studentBtn, adminBtnLogin,
                username, password, loginBtn);

        loginCard.setAlignment(Pos.CENTER);
        loginCard.setMaxWidth(320);
        loginCard.setPadding(new Insets(20));
        loginCard.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        StackPane loginRoot = new StackPane(loginCard);
        loginRoot.setStyle("-fx-background-color: #ecf0f1;");

        Scene loginScene = new Scene(loginRoot, 900, 550);

        // ---------------- TOP BAR ----------------
        Label userLabel = new Label();
        Button logoutBtn = new Button("Logout");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, userLabel, spacer, logoutBtn);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #dfe6e9;");

        // ---------------- SIDEBAR ----------------
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2c3e50;");

        Button homeBtn = new Button("🏠 Home");
        Button manageBtn = new Button("📋 Manage");
        Button adminBtn = new Button("⚙ Admin");

        homeBtn.setMaxWidth(Double.MAX_VALUE);
        manageBtn.setMaxWidth(Double.MAX_VALUE);
        adminBtn.setMaxWidth(Double.MAX_VALUE);

        // ---------------- MENU ----------------
        VBox menuBox = new VBox(10);
        menuBox.setPadding(new Insets(20));

        LinkedHashMap<String, String[]> menu = new LinkedHashMap<>();

        menu.put("Monday", new String[]{"Idli","Rice","Chapati"});
        menu.put("Tuesday", new String[]{"Dosa","Biryani","Curry"});
        menu.put("Wednesday", new String[]{"Poori","Meals","Rice"});
        menu.put("Thursday", new String[]{"Upma","Fried Rice","Dal"});
        menu.put("Friday", new String[]{"Pongal","Lemon Rice","Curry"});
        menu.put("Saturday", new String[]{"Dosa","Pulao","Chapati"});
        menu.put("Sunday", new String[]{"Poori","Meals","Chapati"});

        String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        int today = LocalDate.now().getDayOfWeek().getValue() - 1;

        for (int i = 0; i < days.length; i++) {

            String[] meals = menu.get(days[i]);

            Label card = new Label(
                    "📅 " + days[i] + "\n" +
                    "🍳 Breakfast: " + meals[0] + "\n" +
                    "🍛 Lunch: " + meals[1] + "\n" +
                    "🍽 Dinner: " + meals[2]
            );

            card.setPadding(new Insets(10));

            if (i == today) {
                card.setStyle("-fx-background-color: #ffeaa7; -fx-background-radius: 10;");
            } else {
                card.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
            }

            menuBox.getChildren().add(card);
        }

        ScrollPane homePage = new ScrollPane(menuBox);

        // ---------------- TABLE ----------------
        TableView<Student> table = new TableView<>();

        TableColumn<Student,String> nameCol = new TableColumn<>("Name");
        TableColumn<Student,Number> totalCol = new TableColumn<>("Total ₹");

        nameCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getName()));

        totalCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleIntegerProperty(d.getValue().getTotal()));

        table.getColumns().addAll(nameCol, totalCol);
        table.setItems(students);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ---------------- PRICE ----------------
        Map<String,Integer> priceMap = Map.of(
                "Breakfast",70,"Lunch",100,"Snacks",50,"Dinner",80
        );

        // ---------------- STUDENT ----------------
        ComboBox<String> foodBox = new ComboBox<>();
        foodBox.getItems().addAll("Breakfast","Lunch","Snacks","Dinner");

        Button addBtn = new Button("Add Meal");
        Button billBtn = new Button("View Bill");

        addBtn.setOnAction(e -> {
            if (foodBox.getValue() == null) return;

            int price = priceMap.get(foodBox.getValue());

            Student s = studentMap.get(currentUser);

            if (s == null) {
                s = new Student(idCounter++, currentUser, 0);
                studentMap.put(currentUser, s);
                students.add(s);
            }

            s.addAmount(price);
            table.refresh();
        });

        billBtn.setOnAction(e -> {
            Student s = studentMap.get(currentUser);
            if (s != null)
                new Alert(Alert.AlertType.INFORMATION,"Bill = ₹"+s.getTotal()).show();
        });

        VBox studentControls = new VBox(15, foodBox, addBtn, billBtn);
        studentControls.setPadding(new Insets(20));

        HBox studentPage = new HBox(20, studentControls, table);
        studentPage.setPadding(new Insets(20));
        HBox.setHgrow(table, Priority.ALWAYS);

        // ---------------- ADMIN ----------------
        TextField nameField = new TextField();
        nameField.setPromptText("Student Name");

        ComboBox<String> adminFood = new ComboBox<>();
        adminFood.getItems().addAll("Breakfast","Lunch","Snacks","Dinner");

        Button adminAdd = new Button("Add Food");

        adminAdd.setOnAction(e -> {
            String name = nameField.getText().trim().toLowerCase();
            if (name.isEmpty() || adminFood.getValue() == null) return;

            int price = priceMap.get(adminFood.getValue());

            Student s = studentMap.get(name);

            if (s == null) {
                s = new Student(idCounter++, name, 0);
                studentMap.put(name, s);
                students.add(s);
            }

            s.addAmount(price);
            table.refresh();
        });

        VBox adminControls = new VBox(15, nameField, adminFood, adminAdd);
        adminControls.setPadding(new Insets(20));

        HBox adminPage = new HBox(20, adminControls, table);
        adminPage.setPadding(new Insets(20));
        HBox.setHgrow(table, Priority.ALWAYS);

        // ---------------- ROOT ----------------
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setLeft(sidebar);
        root.setCenter(homePage);

        Scene mainScene = new Scene(root, 900, 550);

        // ---------------- NAV ----------------
        homeBtn.setOnAction(e -> root.setCenter(homePage));
        manageBtn.setOnAction(e -> root.setCenter(studentPage));
        adminBtn.setOnAction(e -> root.setCenter(adminPage));

        // ---------------- LOGIN ----------------
        loginBtn.setOnAction(e -> {

            String user = username.getText().toLowerCase().trim();
            String pass = password.getText().toLowerCase().trim();

            if (mode[0].equals("admin")) {
                if (user.equals("admin") && pass.equals("admin123")) {
                    sidebar.getChildren().setAll(homeBtn, adminBtn);
                    userLabel.setText("👑 Admin");
                    stage.setScene(mainScene);
                }
                return;
            }

            if (allowedUsers.contains(user)) {
                if (pass.equals(user.replace(" ","").substring(0,3))) {
                    currentUser = user;
                    sidebar.getChildren().setAll(homeBtn, manageBtn);
                    userLabel.setText("👤 " + user);
                    stage.setScene(mainScene);
                }
            }
        });

        logoutBtn.setOnAction(e -> stage.setScene(loginScene));

        stage.setScene(loginScene);
        stage.setTitle("Mess Management System");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}