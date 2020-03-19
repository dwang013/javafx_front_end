import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.*;
import javafx.scene.image.ImageView;
public class popControl0
{
	@FXML
	private Button ok;
	
	@FXML
	private Label txt;
	
	@FXML
	private GridPane popPane;
	
	@FXML
	private ImageView warning;
	
	@FXML
	private ImageView error;
	
	private Stage stage;
	
	private boolean critical = false;
	
	private PanelController panelControl;
	
	protected void setPanelControl(PanelController controller)
	{
		panelControl = controller;
	}
	
	protected void setStage(Stage stage)
	{
		this.stage=stage;
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			public void handle(WindowEvent e)
			{
				if(critical)
					System.exit(0);
				//e.consume();
			}
		});
	}
	
	protected GridPane popPane()
	{
		return popPane;
	}
	
	protected Label txt()
	{
		return txt;
	}
	
	protected void show()
	{
		if(critical)
		{
			stage.setTitle("Error");
			error.setVisible(true);
		}
		else
		{
			stage.setTitle("Exception");
			warning.setVisible(true);
		}
		
		stage.show();
	}
	
	protected void setExit(boolean b)
	{
		critical = b;
	}
	
	public void initialize()
	{
		ok.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent e)
			{
				stage.close();
				error.setVisible(false);
				warning.setVisible(false);
				e.consume();
				if(critical)
					System.exit(0);
				panelControl.newErrThread();
				panelControl.errThread.start();
			}
		});
	}
}