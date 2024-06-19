//
//package it.polimi.ingsw.VIEW.GuiPackage.CONTROLLER;
//
//import javafx.scene.control.Menu;
//import javafx.scene.control.MenuButton;
//import javafx.scene.control.MenuItem;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//
//import java.io.File;
//import java.io.IOException;
//
///**
// * Abstract base class for scene controllers in the graphical user interface (GUI)
// * with chat and opponent field functionalities.
// *
// */
//public class HeaderUX extends GenericSceneController {
//
//    public MenuButton menuBar;
//
//    public void initialize() {
//        addGoalMenu();
//    }
//
//    private void addGoalMenu() {
//        // Create the GoalMenu and its items
//        Menu goalMenu = new Menu("View GOAL");
//        goalMenu.setId("GoalMenu");
//
//        // Paths to your image files
//        String common_goal = "src/resources/IconUI/focus.png";
//        String personal_goal = "src/resources/IconUI/target.png";
//
//        // Create ImageViews for icons
//        ImageView personalGoalIcon = new ImageView(new Image(new File(personal_goal).toURI().toString()));
//        personalGoalIcon.setFitWidth(20);
//        personalGoalIcon.setFitHeight(20);
//
//        ImageView commonGoalIcon = new ImageView(new Image(new File(common_goal).toURI().toString()));
//        commonGoalIcon.setFitWidth(20);
//        commonGoalIcon.setFitHeight(20);
//
//
//        // Create MenuItems with text and graphics
//        MenuItem personalGoal = new MenuItem("Personal Goal", personalGoalIcon);
//        personalGoal.setId("PersonalGoal");
//        personalGoal.setOnAction(event -> {
//            try {
//                showPersonalGoal(event);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//
//        MenuItem commonGoal = new MenuItem("Common Goal", commonGoalIcon);
//        commonGoal.setId("CommonGoal");
//        commonGoal.setOnAction(event -> {
//            try {
//                showCommonGoal(event);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        // Add items to GoalMenu
//        goalMenu.getItems().addAll(personalGoal, commonGoal);
//
//        // Set the goalMenu as the graphic for the MenuButton
//        menuBar.getItems().add(goalMenu);
//    }
//}
//
//
