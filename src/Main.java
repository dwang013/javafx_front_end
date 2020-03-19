import javafx.scene.*;
import javafx.stage.*;
import java.io.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
public class Main extends Application
{
	private PanelController controller;
	public void start(Stage stage) throws Exception
	{
		InputStream xmlstream = getClass().getResourceAsStream("Panel.fxml");
		
		FXMLLoader loader = new FXMLLoader();
		
		Parent parent = loader.load(xmlstream);
		
		Scene scene = new Scene(parent);
		
		scene.getStylesheets().add("tab.css");
		
		stage.setScene(scene);
		
		stage.setTitle("Public Algo Bench");
		
		stage.getIcons().add(new Image("general_skill_13.png"));
		
		controller = loader.getController();
		
		controller.setStage(stage);
		
		xmlstream = getClass().getResourceAsStream("popup0.fxml");
		
		loader = new FXMLLoader();
		
		parent = loader.load(xmlstream);
		
		scene = new Scene(parent);
		
		Stage pop = new Stage();//(StageStyle.UTILITY);
		
		//pop.initModality(Modality.APPLICATION_MODAL);
		
		pop.initOwner(stage);
		
		pop.initModality(Modality.WINDOW_MODAL);
		
		//pop.setAlwaysOnTop(true);
		
		pop.setScene(scene);
		
		//pop.setTitle("");
		
		pop.getIcons().add(new Image("general_skill_16.png"));
		
		popControl0 POPcontroller = loader.getController();
		
		POPcontroller.setPanelControl(controller);
		
		POPcontroller.setStage(pop);
		
		pop.setResizable(false);
		
		
		
		xmlstream = getClass().getResourceAsStream("popup1.fxml");
		
		loader = new FXMLLoader();
		
		parent = loader.load(xmlstream);
		
		scene = new Scene(parent);
		
		pop = new Stage();
		
		pop.initOwner(stage);
		
		pop.initModality(Modality.WINDOW_MODAL);
		
		pop.setScene(scene);
		
		pop.getIcons().add(new Image("general_skill_16.png"));
		
		pop.setTitle("Exception");
		
		popControl1 popcontroller = loader.getController();
		
		popcontroller.setStage(pop);
		
		pop.setResizable(false);
		
		controller.setPop(POPcontroller, popcontroller);
		
		stage.show();
	}
	
	public void stop()
	{
		//controller.pw.println("exit");
	}
	
	public static void main(String args[])
	{
		launch(args);
	}
}