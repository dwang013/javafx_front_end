import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.*;

public class popControl1
{
	@FXML
	private Button ok;
	
	@FXML
	private Label txt;
	
	@FXML
	private GridPane popPane;
	
	private Stage stage;
	
	protected void setStage(Stage stage)
	{
		this.stage=stage;
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
		stage.show();
	}
	
	public void initialize()
	{
		ok.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent e)
			{
				stage.close();
				e.consume();
			}
		});
	}
}