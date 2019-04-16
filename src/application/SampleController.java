package application;
import java.awt.FileDialog;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;

public class SampleController {
	public Rectangle selectionRect = new Rectangle(0, 0, 0, 0);
	 @FXML
	 private BorderPane pane;
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	private int startingX;
	private int startingY;
	@FXML
	private void addFile() {//When add button clicked in menu
		for(int i = 0; i < wordsearch.Recognition.textArr.size(); i++) {
			Main.root.getChildren().remove(wordsearch.Recognition.textArr.get(i));
			
		}
		for(int i = 0; i < wordsearch.Start.lineArr.size(); i++) {
			Main.root.getChildren().remove(wordsearch.Start.lineArr.get(i));
		}
		String filePath = null;
		try {
			FileDialog dialog = new FileDialog((java.awt.Frame)null);
			dialog.setVisible(true);
			filePath = "file:/" + dialog.getDirectory() + dialog.getFile();
			filePath = filePath.replace("\\", "/");//Correct way to get absolute path
			filePath = Funcs.fixURL(filePath);
			pane.setStyle("-fx-background-image: url('" + filePath + "');");//Set Background
		}
		catch(Exception e) {
			System.err.println("Error Importing file: " + e);
		}
		
		
		
	}
	
	@FXML
	private void dragStart() {
		
		Main.root.getChildren().add(selectionRect);
		Point p = MouseInfo.getPointerInfo().getLocation();
		selectionRect.setOpacity(0.2);
		selectionRect.setX((p.x - pane.localToScreen(pane.getBoundsInLocal()).getMinX()));//Get initial click position. Relative to the pane, and then add the rectangle there.
		selectionRect.setY(p.y - pane.localToScreen(pane.getBoundsInLocal()).getMinY());
		this.startingX = p.x;//In order to get position WITHIN the GUI. Other gives relative to thescreen. So difference
		this.startingY = p.y;//to get the right coordinates. Subtract the beginning ones from the ending ones. Becasue last set only gives dimensions.
		//System.out.println("Started dragging: " + (this.x) + " : " + (this.y));
		
		
		
	}
	@FXML
	private void dragging() {
		selectionRect.setWidth((MouseInfo.getPointerInfo().getLocation().getX() - pane.localToScreen(pane.getBoundsInLocal()).getMinX()) - selectionRect.getX());//Must subtract or else the width and height will be huge
		selectionRect.setHeight((MouseInfo.getPointerInfo().getLocation().getY() - pane.localToScreen(pane.getBoundsInLocal()).getMinY()) - selectionRect.getY());
		
	}
	@FXML
	private void dragStop() {
		Main.root.getChildren().remove(selectionRect);//Remove the rectangle as soon as selection is complete
		Point p = MouseInfo.getPointerInfo().getLocation();
		Bounds boundsInScreen = pane.localToScreen(pane.getBoundsInLocal());
		int boundsX = (int) boundsInScreen.getMinX();
		int boundsY = (int) boundsInScreen.getMinY();
		int width = 0;
		int height = 100;
		System.out.println("X: " + (this.startingX - boundsX));
		System.out.println("Y: " + (this.startingY - boundsY));//Must do
		System.out.println("Then X: " + ((p.x - boundsX) - (this.startingX - boundsX)));//REMEBER. THIS IS WIDTH OF THE RECTANGLE. NOT THE POSITIONS
		System.out.println("Then Y: " + ((p.y - boundsY) - (this.startingY - boundsY)));

		if(p.x > pane.localToScreen(pane.getBoundsInLocal()).getMaxX()) {//Incase the user drags off the window
			System.out.println("MAXED OUT: " + pane.localToScreen(pane.getBoundsInLocal()).getMaxX());
			width =  (int) (pane.localToScene(pane.getBoundsInLocal()).getMaxX() - (this.startingX - boundsX));//(p.x - boundsX) - (this.startingX - boundsX);//Window width - beginning X
		}
		else {
			width = (p.x - boundsX) - (this.startingX - boundsX);
		}
		
		if(p.y > pane.localToScreen(pane.getBoundsInLocal()).getMaxY()) {
			height = (int) (pane.localToScene(pane.getBoundsInLocal()).getMaxY() - (this.startingY - boundsY));
		}
		else {
			height = (p.y - boundsY) - (this.startingY - boundsY);
		}
		
		
		
		saveAsPng();//Screenshot whole window
		BufferedImage bufferedImage;
		try{
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Is this selection the word search or word bank?");//Check whether selection was search or bank
			ButtonType buttonTypeOne = new ButtonType("Word search");
			ButtonType buttonTypeTwo = new ButtonType("Word bank");
			ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);
			Optional<ButtonType> result = alert.showAndWait();
			boolean isSearch = true;//Tell the cropping function what to name the file
			if(result.get() == buttonTypeTwo) {
				isSearch = false;
			}
			System.out.println(Main.filePath + "/snapshot.png");
			bufferedImage = ImageIO.read(new File(Main.filePath + "/snapshot.png"));//Crop 
			cropImage(bufferedImage, (this.startingX - boundsX), (this.startingY - boundsY),width , height,isSearch);//(p.x - boundsX) - (this.startingX - boundsX)
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	@FXML
	public void saveAsPng() {
		WritableImage image = pane.snapshot(new SnapshotParameters(), null);
		File file = new File(Main.filePath + "/snapshot.png");
		try {
	        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	    } catch (IOException e) {
	        // TODO: handle exception here
	    }
	}
	
	@FXML
	private void startRec() {
		screenWidth = (int) pane.localToScene(pane.getBoundsInLocal()).getMaxX();
		screenHeight = (int) pane.localToScene(pane.getBoundsInLocal()).getMaxY();
		pane.setStyle("-fx-background-color: transparent;");
		wordsearch.Start.startFunction(Main.filePath + "/search.png", Main.filePath + "/bank.png");
	}
	
	public static BufferedImage cropImage(BufferedImage bufferedImage, int x, int y, int width, int height, boolean isSearch){
	    BufferedImage croppedImage = bufferedImage.getSubimage(x, y, width, height);
	   
	    try {
	    	File pathFile = null;
	    	if(isSearch) {
	    		pathFile = new File(Main.filePath + "/search.png");
	    	}
	    	else {
	    		pathFile = new File(Main.filePath + "/bank.png");
	    	}
	    	ImageIO.write(croppedImage,"png", pathFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return croppedImage;
	}
	@FXML//CHANGE CURSORS
	private void paneMouseEntered() {
		Main.scene.setCursor(Cursor.CROSSHAIR);
	}
	@FXML
	private void menuMouseEntered() {
		Main.scene.setCursor(Cursor.DEFAULT);
	}
	
	
	
	

}