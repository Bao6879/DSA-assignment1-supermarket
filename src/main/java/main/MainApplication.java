package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.linkedList.BaoList;

import java.io.IOException;

public class MainApplication extends Application {
    public static boolean atCli=true;
    public CliController cliController;
    public ModelViewController modelViewController;
    public static Scene cli, model;
    public static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("cliView.fxml"));
        FXMLLoader secondLoader= new FXMLLoader(MainApplication.class.getResource("modelView.fxml"));

        Parent cliView=fxmlLoader.load();
        Parent modelView=secondLoader.load();

        cliController=fxmlLoader.getController();
        modelViewController=secondLoader.getController();

        cliController.setMainApp(this);
        modelViewController.setMainApp(this);

        cli = new Scene(cliView, 1080, 720);
        model = new Scene(modelView, 1177, 699);
        MainApplication.stage = stage;
        stage.setTitle("CLI VIEW");
//        stage.setFullScreen(true);
        stage.setScene(cli);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void switchScene() {
        if (atCli) {
            stage.setScene(model);
            modelViewController.baoList=cliController.baoList;
            modelViewController.populateTree();
            stage.setFullScreen(true);
        }
        else
            stage.setScene(cli);
    }
}