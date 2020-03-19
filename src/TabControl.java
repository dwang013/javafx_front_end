import javafx.scene.control.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.fxml.FXML;
import java.util.*;
import java.io.File;
public class TabControl
{
	@FXML
	private Button Browse1;
	
	@FXML
	TextField txt1;
	
	@FXML
	private Button Browse2;
	
	@FXML
	TextField txt2;
	
	private PanelController mainPanel;
	
	protected void setMainPanel(PanelController p)
	{
		mainPanel = p;
		
		Browse1.setOnAction(new XMLhandler()
		{
			public void handle(ActionEvent e)
			{
				String dirs = "";
				try
				{
					List<File> files = choose.showOpenMultipleDialog(mainPanel.stage);
					if(files != null)
					{
						for(File file : files)
							dirs +=  file.getPath() + ';';
						
						txt1.setText(dirs);
						choose.setInitialDirectory(files.get(0).getParentFile());
					}
				}
				catch(IllegalArgumentException ex)
				{
					mainPanel.popin.txt().setText(ex.getMessage());
					mainPanel.popin.show();
					choose.setInitialDirectory(null);
				}
				e.consume();
			}
		});
	
		Browse2.setOnAction(new EventHandler<ActionEvent>()
		{
			FileChooser choose = new FileChooser();
			public void handle(ActionEvent e)
			{
				String dataFiles = "";
				try
				{
					List<File> files = choose.showOpenMultipleDialog(mainPanel.stage);
					if(files != null)
					{
						for(File file : files)
							dataFiles +=  file.getPath() + ';';
						
						txt2.setText(dataFiles);
						choose.setInitialDirectory(files.get(0).getParentFile());
					}
				}
				catch(IllegalArgumentException ex)
				{
					mainPanel.popin.txt().setText(ex.getMessage());
					mainPanel.popin.show();
					choose.setInitialDirectory(null);
				}
				e.consume();
			}
		});
	}
	
	abstract private class XMLhandler implements EventHandler<ActionEvent>
	{
		FileChooser choose = new FileChooser();
	
		public XMLhandler()
		{
			choose.getExtensionFilters().add(new ExtensionFilter("XML Files", "*.xml"));
		}
	
		abstract public void handle(ActionEvent e);
	}
	
	/*public void initialized()
	{
		
	}*/
}