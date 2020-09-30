package application;
	
import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	public static BorderPane root;
	public static Scene scene;
	public static String filePath;
	
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			root = (BorderPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
			scene = new Scene(root,1000,1000);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//filePath = System.getProperty("user.home").replace("\\", "/") + "/Pictures/YuvyWordSearch";
		filePath = System.getProperty("user.dir").replace("\\", "/") + "/pics";
		System.out.println(filePath);
		boolean success = new File(filePath).mkdirs();
		if(!success) {
			System.out.println("Folder already made or error");
		}
		 
		
		launch(args);
	}
}
