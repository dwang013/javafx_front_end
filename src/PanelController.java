import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.concurrent.Task;
import javafx.application.Platform;
import javafx.beans.value.*;
import javafx.beans.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
//import java.net.URL;
//import java.util.ResourceBundle;
import java.util.*;
import java.io.*;
import javafx.util.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.*;
import javafx.scene.shape.Circle;
public class PanelController
{
	private String calculate(double num)
	{
		int count = 0;
		while(num/1024 >= 1 && count <= 3)
		{
			count++;
			num /= 1024;
		}
		
		long number = (long)(num + 0.5);
		
		switch(count)
		{
			case 0 : return number + " B";
			
			case 1 : return number + " KB";
			
			case 2 : return number + " MB";
			
			case 3 : return number + " GB";
			
			default : return number + " TB";
		}
	}
	private class sortByValue implements Comparator<XYChart.Data>
	{
		
		public int compare(XYChart.Data a, XYChart.Data b)
		{
			long result = Long.valueOf(a.getXValue().toString()) - Long.valueOf(b.getXValue().toString());
			
			if(result < 0)
			{
				return -1;
			}
			else if(result > 0)
			{
				return 1;
			}
			else
				return 0;
		}
	}
	@FXML
	private GridPane resultPane;
	
	@FXML
	private GridPane configPane;
	
	@FXML
	private LineChart<Number,Number> lineChart1;
	
	@FXML
	private NumberAxis y1;
	
	@FXML
	private NumberAxis x1;
	
	@FXML
	private LineChart<Number,Number> lineChart2;
	
	@FXML
	private NumberAxis y2;
	
	@FXML
	private NumberAxis x2;
	
	@FXML
	private TableView tableview1;
	
	@FXML
	private TableView tableview2;
	
	@FXML
	private Button okButton;
	
	@FXML
	private Button submit;
	
	@FXML
	private GridPane progressPane;
	
	/*@FXML
	private ProgressIndicator progress;*/
	
	@FXML
	private ProgressBar bar;
	
	@FXML
	private TextArea console;
	
	@FXML
	private Button cancel;
	
	@FXML
	private TabPane tabPane;
	
	@FXML
	private Label title;
	
	Stage stage;
	
	private sortByValue compare = new sortByValue();
	
	private popControl0 pop;
	
	popControl1 popin;
	
	private ArrayList<ArrayList<XYChart.Data>> data = new ArrayList<ArrayList<XYChart.Data>>();
	
	private ArrayList<ArrayList<XYChart.Data>> ops = new ArrayList<ArrayList<XYChart.Data>>();
	
	private ArrayList<TabControl> tabData = new ArrayList<TabControl>();
	
	private Thread thread;
	
	private BufferedReader serr;
	
	PrintWriter pw;
	
	Thread errThread;
	
	private Random r = new Random();
	
	protected void newErrThread()
	{
		errThread = new Thread(() ->
		{
			try
			{
				String error = serr.readLine();
					
				Platform.runLater(() ->
				{
					pop.txt().setText(error);
					if(error.contains("Error"))
						pop.setExit(true);
					pop.show();
				});
					
				while(serr.ready())
					serr.readLine();
				thread.interrupt();
			}
			catch(NoSuchElementException ex)
			{
				
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		});
	}
	
	protected void setStage(Stage stage)
	{
		this.stage = stage;
	}
	
	protected void setPop(popControl0 p, popControl1 ap)
	{
		pop = p;
		
		popin = ap;
	}
	
	public void initialize() throws Exception
	{
		//y.setAnimated(false);
		
		//lineChart1.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
		
		/*Process p;
		InputStream in, err; OutputStream out;
		BufferedReader s;*/

		/*p = Runtime.getRuntime().exec("Java -cp \""+ System.getProperty("java.class.path") +"\" Control");
		
		in = p.getInputStream();
		
		out = p.getOutputStream();
		
		err = p.getErrorStream();
		
		pw = new PrintWriter(out, true);
		
		s = new BufferedReader(new InputStreamReader(in));
		
		serr = new BufferedReader(new InputStreamReader(err));
		
		newErrThread();
		
		errThread.start();*/
		
		/*Runtime.getRuntime().addShutdownHook(new Thread(() ->
		{
			
		}));*/
		
		final Button addButton = new Button("+");
		addButton.setMaxWidth(25);
		addButton.setMinWidth(25);
		addButton.setMinHeight(25);
		addButton.setMaxHeight(25);
		
		Tab tab = new Tab();
		tab.setDisable(true);
		tab.setClosable(false);
		tab.setGraphic(addButton);
		tab.getStyleClass().add("tab1");
		tabPane.getTabs().add(tab);
		
		addButton.setOnAction((event)->
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("TabPanel.fxml"));

			Tab tab1 = new Tab("\t\t");
			
			try
			{
				tab1.setContent(loader.load());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			TabControl con = loader.getController();
			
			con.setMainPanel(this);
			
			tabData.add(con);
			
			tab1.setOnClosed((evt)->
			{
				tabData.remove(con);
				if(tabPane.getTabs().size() == 1)
				{
					title.setVisible(false);
					submit.setVisible(false);
				}
			});
			
			tabPane.getTabs().add(tabPane.getTabs().size()-1, tab1);
			tabPane.getSelectionModel().select(tab1);
			title.setVisible(true);
			submit.setVisible(true);
		});
		
		addButton.fire();
		
		tabPane.getSelectionModel().select(0);
		
		cancel.setOnAction(e -> 
		{
			cancel.setDisable(true);
			cancel.setText("cancelling...");
			thread.interrupt();
		});
	
		submit.setOnAction(new EventHandler<ActionEvent>() 
		{
			public void handle(ActionEvent e)
			{
				int seriesNum = r.nextInt(10) + 1, dataNum = r.nextInt(10) + 1;
				long max = seriesNum*dataNum;
				ArrayList<String> taskName1 = new ArrayList<String>(), taskName2 = new ArrayList<String>();
				/*if(dir.size() > 0)
				{*/
					Task<Void> task = new Task<Void>()
					{
						private String consoleTxt = "";
						public Void call()
						{
							long curr = 0;
							try
							{	
								for(int i = 0; i < seriesNum; i++)
								{
									taskName1.add("item "+i);
									data.add(new ArrayList<XYChart.Data>());
									for(int j = 0; j < dataNum; j++)
									{
										if(Thread.currentThread().isInterrupted())
											throw new InterruptedException();
										consoleTxt += "stage " + curr + "...";
										Platform.runLater(() -> console.setText("\r" + consoleTxt + "0%"));
										
										Thread.sleep(50);
										
										Platform.runLater(() -> console.setText("\r" + consoleTxt + "25%"));
										
										Thread.sleep(50);
										
										Platform.runLater(()->console.setText("\r" + consoleTxt + "50%"));
										
										Thread.sleep(50);
										
										Platform.runLater(()->console.setText("\r" + consoleTxt + "75%"));
										
										Thread.sleep(50);
										
										Platform.runLater(()->console.setText("\r" + consoleTxt + "100%"));
										
										Thread.sleep(50);
										
										int first = r.nextInt(10000), second = first * (r.nextInt(1000) + 100);
										
										data.get(i).add(new XYChart.Data( first, second ));
										
										consoleTxt += "100%\n";
										
										updateProgress(++curr, max);
									}
									data.get(i).sort(compare);
								}
								
								Platform.runLater(new Runnable() 
								{
									public void run()
									{
										calData(taskName1, taskName2); //javafx ui elements must be interacted with fx ui threads only
									}
								});
							}
							catch(InterruptedException ex)
							{
								Platform.runLater(()->cancelTask());
							}
							catch(Exception ex)
							{
								ex.printStackTrace();
								Platform.runLater(()->cancelTask());
							}
							
							return null;
						}
						
						private void cancelTask()
						{	
							data.clear();
							ops.clear();
							/*txt1.setText("");
							txt2.setText("");*/
							console.setText("");
							if(cancel.isDisabled())
							{
								cancel.setDisable(false);
								cancel.setText("Cancel");
							}
							progressPane.setVisible(false);
							configPane.setVisible(true);
						}
					};
					
					bar.progressProperty().bind(task.progressProperty());
					thread = new Thread(task); thread.start();
					
					configPane.setVisible(false);
					
					progressPane.setVisible(true);
				/*}
				else
				{
					popin.txt().setText("please fill in at least one xml.");
					popin.show();
				}*/
				e.consume();
			}
		});
		
		okButton.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent e)
			{
				tableview1.getItems().clear();
				tableview2.getItems().clear();
				lineChart1.getData().clear();
				lineChart2.getData().clear();
				tableview1.getColumns().clear();
				tableview2.getColumns().clear();
				configPane.setVisible(true);
				resultPane.setVisible(false);
				/*txt1.setText("");
				txt2.setText("");*/
				console.setText("");
				data.clear();
				ops.clear();
				e.consume();
			}
		});
		lineChart1.setTitle("Time taken");
        y1.setLabel("miliseconds");       
        x1.setLabel("File Size(byte)");
		lineChart2.setTitle("Calculated");
		y2.setLabel("Number of operation");
		x2.setLabel("File Size(byte)");
	}
	
	private void calData(ArrayList<String> taskName1, ArrayList<String> taskName2)
	{
		for(int i = 0; i < data.size(); i ++)
		{
			XYChart.Series series = new XYChart.Series();
			for(int j = 0; j < data.get(i).size(); j++)
			{
				Label label = new Label('(' + calculate(Long.parseLong(data.get(i).get(j).getXValue().toString())) + " ," + data.get(i).get(j).getYValue().toString() + ')');
				label.setVisible(false);
				Pane pane = new Pane(label);
				pane.setShape(new Circle(5.0));
				pane.setScaleShape(false);
				pane.setOnMouseEntered((event)->label.setVisible(true));
				pane.setOnMouseExited((event)->label.setVisible(false));
				data.get(i).get(j).setNode(pane);
				
				series.getData().add(data.get(i).get(j));
			}
			series.setName(taskName1.get(i));
			lineChart1.getData().add(series);
		}
		
		for(int i = 0; i < ops.size(); i ++)
		{
			XYChart.Series series = new XYChart.Series();
			for(int j = 0; j < ops.get(i).size(); j++)
			{
				Label label = new Label('(' + calculate(Long.parseLong(ops.get(i).get(j).getXValue().toString())) + " ," + ops.get(i).get(j).getYValue().toString() + ')');
				label.setVisible(false);
				Pane pane = new Pane(label);
				pane.setShape(new Circle(5.0));
				pane.setScaleShape(false);
				pane.setOnMouseEntered((event)->label.setVisible(true));
				pane.setOnMouseExited((event)->label.setVisible(false));
				ops.get(i).get(j).setNode(pane);
				
				series.getData().add(ops.get(i).get(j));
			}
			series.setName(taskName2.get(i));
			lineChart2.getData().add(series);
		}
		
        for(int i = 0; i < data.size(); i++)
		{
			final int num = i;
			Method method = new Method(new CheckBox(lineChart1.getData().get(i).getName()), new String[data.get(i).size()], new CheckBox[data.get(i).size()]);
			for(int j = 0; j < data.get(i).size(); j++)
			{
				CheckBox cb = new CheckBox();
				cb.setSelected(true);
				final int num2 = j;
				cb.selectedProperty().addListener(new ChangeListener<Boolean>()
				{
					public void changed(ObservableValue<? extends Boolean> obs, Boolean old, Boolean New)
					{
						if(New.booleanValue())
						{
							lineChart1.getData().get(num).getData().add(data.get(num).get(num2));
						}
						else
						{
							lineChart1.getData().get(num).getData().remove(data.get(num).get(num2));
						}
					}
				});
				method.setValue(j, data.get(i).get(j).getYValue().toString()); method.setCBox(j, cb);
			}
			method.getMethod().setSelected(true);
			method.getMethod().selectedProperty().addListener((obs, old, New) -> 
			{
				if(New.booleanValue())
					for(CheckBox c : method.getAllCBox())
						c.setSelected(true);
				else
					for(CheckBox c : method.getAllCBox())
						c.setSelected(false);
			});
			tableview1.getItems().add(method);
		}
		
		for(int i = 0; i < ops.size(); i++)
		{
			final int num = i;
			Method method = new Method(new CheckBox(lineChart2.getData().get(i).getName()), new String[ops.get(i).size()], new CheckBox[ops.get(i).size()]);
			for(int j = 0; j < ops.get(i).size(); j++)
			{
				CheckBox cb = new CheckBox();
				cb.setSelected(true);
				final int num2 = j;
				cb.selectedProperty().addListener(new ChangeListener<Boolean>()
				{
					public void changed(ObservableValue<? extends Boolean> obs, Boolean old, Boolean New)
					{
						if(New.booleanValue())
						{
							lineChart2.getData().get(num).getData().add(ops.get(num).get(num2));
						}
						else
						{
							lineChart2.getData().get(num).getData().remove(ops.get(num).get(num2));
						}
					}
				});
				method.setValue(j, ops.get(i).get(j).getYValue().toString()); method.setCBox(j, cb);
			}
			method.getMethod().setSelected(true);
			method.getMethod().selectedProperty().addListener((obs, old, New) -> 
			{
				if(New.booleanValue())
					for(CheckBox c : method.getAllCBox())
						c.setSelected(true);
				else
					for(CheckBox c : method.getAllCBox())
						c.setSelected(false);
			});
			tableview2.getItems().add(method);
		}
		
		
		TableColumn column2 = new TableColumn("method");

		column2.setCellValueFactory(new PropertyValueFactory("Method"));
			
		tableview1.getColumns().add(column2);
		ObservableList<Method> list = tableview1.getItems();
		if(data.size() > 0)
			for(int i = 0; i < data.get(0).size(); i++)
			{
				final int num = i;

				TableColumn column3 = new TableColumn("miliseconds");
				column3.setCellValueFactory(new Callback<CellDataFeatures<Method, String>, ObservableValue<String>>()
				{
					public ObservableValue<String> call(CellDataFeatures<Method, String> p) 
					{
						return new ReadOnlyObjectWrapper(p.getValue().getValue(num));
					}
				});
				
				TableColumn column1 = new TableColumn("\t\t");
				CheckBox cb = new CheckBox("isVisible");
				cb.setSelected(true);
				cb.selectedProperty().addListener((obs, old, New) ->
				{
					if(New.booleanValue())
						for(Method m : list)
							m.getCBox(num).setSelected(true);
					else
						for(Method m : list)
							m.getCBox(num).setSelected(false);
				});
				column1.setGraphic(cb);
				column1.setCellValueFactory(new Callback<CellDataFeatures<Method, CheckBox>, ObservableValue<CheckBox>>()
				{
					public ObservableValue<CheckBox> call(CellDataFeatures<Method, CheckBox> p) 
					{
						return new ReadOnlyObjectWrapper(p.getValue().getCBox(num));
					}
				});
				TableColumn column = new TableColumn(calculate(Long.parseLong(data.get(0).get(i).getXValue().toString())));
				column.getColumns().addAll(column1,column3);
				
				tableview1.getColumns().add(column);
			}
		
		column2 = new TableColumn("method");

		column2.setCellValueFactory(new PropertyValueFactory("Method"));
			
		tableview2.getColumns().add(column2);
		ObservableList<Method> list1 = tableview2.getItems();
		if(ops.size() > 0)
			for(int i = 0; i < ops.get(0).size(); i++)
			{
				final int num = i;

				TableColumn column3 = new TableColumn("Number of operations");
				column3.setCellValueFactory(new Callback<CellDataFeatures<Method, String>, ObservableValue<String>>()
				{
					public ObservableValue<String> call(CellDataFeatures<Method, String> p) 
					{
						return new ReadOnlyObjectWrapper(p.getValue().getValue(num));
					}
				});
				
				TableColumn column1 = new TableColumn("\t\t");
				CheckBox cb = new CheckBox("isVisible");
				cb.setSelected(true);
				cb.selectedProperty().addListener((obs, old, New) ->
				{
					if(New.booleanValue())
						for(Method m : list1)
							m.getCBox(num).setSelected(true);
					else
						for(Method m : list1)
							m.getCBox(num).setSelected(false);
				});
				column1.setGraphic(cb);
				column1.setCellValueFactory(new Callback<CellDataFeatures<Method, CheckBox>, ObservableValue<CheckBox>>()
				{
					public ObservableValue<CheckBox> call(CellDataFeatures<Method, CheckBox> p) 
					{
						return new ReadOnlyObjectWrapper(p.getValue().getCBox(num));
					}
				});
				TableColumn column = new TableColumn(calculate(Long.parseLong(ops.get(0).get(i).getXValue().toString())));
				column.getColumns().addAll(column1,column3);
				
				tableview2.getColumns().add(column);
			}
		
		if(data.size() == 0 && ops.size() > 0)
		{
			lineChart1.setVisible(false);
			tableview1.setVisible(false);
			lineChart2.setVisible(true);
			tableview2.setVisible(true);
			resultPane.setConstraints(lineChart2, 0, 0, 10, 1);
			resultPane.setConstraints(tableview2, 0, 1, 10, 1);
		}
		else if(ops.size() == 0 && data.size() > 0)
		{
			lineChart2.setVisible(false);
			tableview2.setVisible(false);
			lineChart1.setVisible(true);
			tableview1.setVisible(true);
			resultPane.setConstraints(lineChart1, 0, 0, 10, 1);
			resultPane.setConstraints(tableview1, 0, 1, 10, 1);
		}
		else
		{
			lineChart1.setVisible(true);
			tableview1.setVisible(true);
			lineChart2.setVisible(true);
			tableview2.setVisible(true);
			resultPane.setConstraints(lineChart2, 5, 0, 5, 1);
			resultPane.setConstraints(tableview2, 5, 1, 5, 1);
			resultPane.setConstraints(lineChart1, 0, 0, 5, 1);
			resultPane.setConstraints(tableview1, 0, 1, 5, 1);
		}
		
		progressPane.setVisible(false);
		resultPane.setVisible(true);
	}
}