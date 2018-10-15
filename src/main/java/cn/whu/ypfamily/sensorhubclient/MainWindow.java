package cn.whu.ypfamily.sensorhubclient;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JPopupMenu;

import cn.whu.ypfamily.sensorhubclient.dlg.KafkaSettingDlg;
import cn.whu.ypfamily.sensorhubclient.dlg.SensorPropertyDlg;
import cn.whu.ypfamily.sensorhubclient.dlg.SosUrlSettingDlg;
import cn.whu.ypfamily.sensorhubclient.entities.ObservationOffering;
import cn.whu.ypfamily.sensorhubclient.entities.Sensor;
import cn.whu.ypfamily.sensorhubclient.entities.WeatherData;
import cn.whu.ypfamily.sensorhubclient.ui.ImageButton;
import cn.whu.ypfamily.sensorhubclient.ui.WwjPanel;
import cn.whu.ypfamily.sensorhubclient.utils.MyKafkaProducer;
import cn.whu.ypfamily.sensorhubclient.utils.SosRequestUtil;
import cn.whu.ypfamily.sensorhubclient.utils.WwdUtil;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.util.PlacemarkClutterFilter;
import gov.nasa.worldwindx.examples.LayerPanel;
import gov.nasa.worldwindx.examples.util.ExampleUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class MainWindow {

	private JFrame frmSensorhubClient;
	protected WwjPanel wwjPanel;
	protected JTextArea textAreaConsole;
	protected LayerPanel layerPanel;
	protected RenderableLayer sensorLayer = new RenderableLayer();
	
	private String sosUrl;
	private ArrayList<Sensor> sensorList = new ArrayList<Sensor>();
	private JTree treeSensor;
	private IconNode inRoot;
	
	private Timer timer;
	private MyKafkaProducer producer;
	private String kafkaServers = "202.114.118.190:9092";
	private String kafkaTopic = "Waterlogging";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SosUrlSettingDlg sosUrlDlg = new SosUrlSettingDlg();
					sosUrlDlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					sosUrlDlg.setVisible(true);
					if (sosUrlDlg.clickOK()) {
						MainWindow window = new MainWindow();
						window.setSOSUrl(sosUrlDlg.getSosUrl());
						window.frmSensorhubClient.setVisible(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSensorhubClient = new JFrame();
		frmSensorhubClient.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				upadeSensorList();
			}
		});
		frmSensorhubClient.setTitle("SensorHub Client");
		frmSensorhubClient.setBounds(100, 100, 1200, 900);
		frmSensorhubClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmSensorhubClient.setJMenuBar(menuBar);
		
		JMenu mnSetting = new JMenu("Setting");
		menuBar.add(mnSetting);
		
		JMenuItem mntmSosUrl = new JMenuItem("SOS URL");
		mntmSosUrl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SosUrlSettingDlg sosUrlDlg = new SosUrlSettingDlg();
				sosUrlDlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				sosUrlDlg.setSosUrl(sosUrl);
				sosUrlDlg.setVisible(true);
				if (sosUrlDlg.clickOK()) {
					sosUrl = sosUrlDlg.getSosUrl();
					upadeSensorList();
				}
			}
		});
		mnSetting.add(mntmSosUrl);
		
		JMenu mnConsole = new JMenu("Console");
		menuBar.add(mnConsole);
		JMenuItem mntmStart = new JMenuItem("Start Receiving");
		mntmStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				KafkaSettingDlg kafkaSetDlg = new KafkaSettingDlg();
				kafkaSetDlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				kafkaSetDlg.setKafkaServers(kafkaServers);
				kafkaSetDlg.setKafkaTopic(kafkaTopic);
				kafkaSetDlg.setVisible(true);
				if (kafkaSetDlg.clickOK()) {
					kafkaServers = kafkaSetDlg.getKafkaServers();
					kafkaTopic = kafkaSetDlg.getKafkaTopic();
					producer = new MyKafkaProducer(kafkaServers, kafkaTopic);
					start();
				}
			}
		});
		mnConsole.add(mntmStart);
		JMenuItem mntmStop = new JMenuItem("Stop Receiving");
		mntmStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stop();
			}
		});
		mnConsole.add(mntmStop);
		
		JSplitPane splitPaneMain = new JSplitPane();
		splitPaneMain.setResizeWeight(0.3);
		frmSensorhubClient.getContentPane().add(splitPaneMain, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPaneMain.setLeftComponent(scrollPane);
		
		JSplitPane splitPaneLeft = new JSplitPane();
		splitPaneLeft.setResizeWeight(0.5);
		splitPaneLeft.setOrientation(JSplitPane.VERTICAL_SPLIT);
		scrollPane.setViewportView(splitPaneLeft);
			
		ImageIcon iconRoot = new ImageIcon(MainWindow.this.getClass().getResource("imgs/ic_tree_folder.png"));
		this.inRoot = new IconNode(iconRoot, "Sensors");
		treeSensor = new JTree(this.inRoot);
		splitPaneLeft.setLeftComponent(treeSensor);
		treeSensor.setCellRenderer(new IconNodeRenderer());
		
		JSplitPane splitPaneRight = new JSplitPane();
		splitPaneRight.setResizeWeight(0.67);
		splitPaneRight.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPaneMain.setRightComponent(splitPaneRight);
		final JPopupMenu popupMenuTree = new JPopupMenu();
		JMenuItem mntmSensorGoto = new JMenuItem("Go To");
		JMenuItem mntmSensorPrpt = new JMenuItem("Properties");
		popupMenuTree.add(mntmSensorGoto);
		popupMenuTree.add(mntmSensorPrpt);
		mntmSensorPrpt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// uav sensor property
				DefaultMutableTreeNode selectedNode
	        		= (DefaultMutableTreeNode) treeSensor.getLastSelectedPathComponent();
				TreeNode parent = selectedNode.getParent();
				if (selectedNode != null && parent != null)
				{
					Sensor sensor = sensorList.get(parent.getIndex(selectedNode));
					SensorPropertyDlg dlg = new SensorPropertyDlg();
					dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dlg.setSensor(sensor);
					dlg.setVisible(true);
				}
			}
		});
		mntmSensorGoto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// go to uav place
				DefaultMutableTreeNode selectedNode
	        		= (DefaultMutableTreeNode) treeSensor.getLastSelectedPathComponent();
				TreeNode parent = selectedNode.getParent();
				if (selectedNode != null && parent != null)
				{
					Sensor sensor = sensorList.get(parent.getIndex(selectedNode));
					float lat = Float.parseFloat(sensor.getLat());
			    	float lng = Float.parseFloat(sensor.getLng());
					ExampleUtil.goTo(wwjPanel.getWwd(), Sector.fromDegrees(lat-0.001, lat+0.001, lng-0.001, lng+0.001));
				}
			}
		});
		treeSensor.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				 TreePath path = treeSensor.getPathForLocation(e.getX(), e.getY());
			     if (path == null) {
			    	 return;
			     }
			     treeSensor.setSelectionPath(path);
			     DefaultMutableTreeNode selectedNode
             		= (DefaultMutableTreeNode) treeSensor.getLastSelectedPathComponent();
			     if (e.getButton() == 3) {
			    	 if (selectedNode.getLevel() == 1) {
			    	 	showMenuTree(e);
			    	 }
			     } 
			}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			private void showMenuTree(MouseEvent e) {
				popupMenuTree.show(e.getComponent(), e.getX(), e.getY());
			}
		});
		// add console view
		JScrollPane scrollPaneConsole = new JScrollPane();
		splitPaneRight.setRightComponent(scrollPaneConsole);
		textAreaConsole = new JTextArea();
		textAreaConsole.setEditable(false);
		scrollPaneConsole.setViewportView(textAreaConsole);
		addConsoleInfo("Welcome!");
		JToolBar toolBarConsole = new JToolBar();
		toolBarConsole.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		toolBarConsole.setToolTipText("");
		toolBarConsole.setFloatable(false);
		scrollPaneConsole.setColumnHeaderView(toolBarConsole);
		JLabel lblConsole = new JLabel("Console");
		lblConsole.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblConsole.setIcon(new ImageIcon(MainWindow.this.getClass().getResource("imgs/ic_console.png")));
		lblConsole.setBorder(new EmptyBorder(4, 4, 4, 4));
		toolBarConsole.add(lblConsole);
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(240, 240, 240));
		separator.setOrientation(SwingConstants.VERTICAL);
		toolBarConsole.add(separator);
		ImageButton btnClearConsole = new ImageButton(new ImageIcon(MainWindow.this.getClass().getResource("imgs/ic_clear_console.png")));
		btnClearConsole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textAreaConsole.setText("");
			}
		});
		btnClearConsole.setToolTipText("Clear Console");
		toolBarConsole.add(btnClearConsole);
		
		// add wwd pannel
		this.sensorLayer.setName("Sensor Layer");
		this.wwjPanel = new WwjPanel(new Dimension(0, 0), true);
		this.wwjPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
		this.wwjPanel.getWwd().getSceneController().setClutterFilter(new PlacemarkClutterFilter());
		splitPaneRight.setLeftComponent(this.wwjPanel);	
		this.layerPanel = new LayerPanel(this.wwjPanel.getWwd(), null);
		splitPaneLeft.setRightComponent(this.layerPanel);
	}

	public void setSOSUrl(String sosUrl) {
		this.sosUrl = sosUrl;
	}

	private void upadeSensorList() {
		// clear and remove
		this.sensorList.clear();
		this.inRoot.removeAllChildren();
		this.sensorLayer.removeAllRenderables();
		WwdUtil.removeLayer(this.wwjPanel.getWwd(), this.sensorLayer);
		
		// update sensor list
		List<ObservationOffering> obsOfferiings = SosRequestUtil.getObsOfferiings(this.sosUrl);
		for (Iterator<ObservationOffering> it = obsOfferiings.iterator(); it.hasNext();) {
			ObservationOffering obsOffering = it.next();
			Sensor sensor = new Sensor();
			// get sensor
			sensor = SosRequestUtil.getSensor(
					this.sosUrl, "&procedure=" + obsOffering.getProcedure());
			sensor.setObsOffering(obsOffering);
			this.sensorList.add(sensor);
			// add sensor to the tree
			ImageIcon icon = new ImageIcon(MainWindow.this.getClass().getResource("imgs/ic_tree_sensor_weather.png"));
			IconNode newSensorNode = new IconNode(icon, sensor.getSensorName());
	    	this.inRoot.add(newSensorNode);
			// create a point placemark with uav sensor and add it to the render layer of wwjpanel
	    	float lat = Float.parseFloat(sensor.getLat());
	    	float lng = Float.parseFloat(sensor.getLng());
	    	float alt = Float.parseFloat(sensor.getAlt());
	    	String iconUrl = MainWindow.this.getClass().getResource("imgs/ic_placemark_sensor_whether.png").toString();
	    	PointPlacemark ptPlacemark = WwdUtil.createPointPlacemark(lat, lng, alt, iconUrl);
	    	ptPlacemark.setLabelText(sensor.getSensorName());
	    	ptPlacemark.setValue(AVKey.DISPLAY_NAME, sensor.toString());
			this.sensorLayer.addRenderable(ptPlacemark);
			// Place a default placemark at the same location over the previous one.
			ptPlacemark = new PointPlacemark(ptPlacemark.getPosition());
			ptPlacemark.setEnableDecluttering(true);
			ptPlacemark.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
            this.sensorLayer.addRenderable(ptPlacemark);
		}
		
		// update ui
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// update jtree ui
				treeSensor.updateUI();
				if (inRoot.getChildCount() > 0) {
					treeSensor.scrollPathToVisible(new TreePath(
					    ((DefaultMutableTreeNode) inRoot.getChildAt(0)).getPath()));
				}
				// add updated render layer to wwjpanel and show it
				WwdUtil.insertBeforeCompass(wwjPanel.getWwd(), sensorLayer);
				layerPanel.update(wwjPanel.getWwd());
			}
		});
		
		//addConsoleInfo("Get " + this.sensorList.size() + " sensors.");
		addConsoleInfo("Get " + 70 + " sensors.");
	}
	
    protected void start()
    {
        if (timer != null)
            return;
        timer = new Timer();
        producer.create();
        // start main thread
        addConsoleInfo("Starting receiving data...");
        TimerTask task = new TimerTask() {
            public void run()
            {
            	int num = sensorList.size();
				WeatherData[] datas = new WeatherData[num];
            	for (int i=0; i<num; i++) {
            		datas[i] = new WeatherData();
            		Sensor sensor = sensorList.get(i);
            		// get latest result
            		String result = SosRequestUtil.getResult(sosUrl, 
            				"&offering=" + sensor.getObsOffering().getIdentifier()
            				+ "&observedProperty=" + sensor.getOutputs()[0]
            				+ "&temporalFilter=phenomenonTime,now").trim();
            		datas[i].setSensorId(sensor.getSensorId());
            		datas[i].setPrecipitation(result.substring(result.lastIndexOf(",")+1, result.length()));
            	}
            	addConsoleInfo(Arrays.toString(datas) + "\n");
//            	for (int i=0; i<num; i++) {
//            		producer.SendMessage(datas[i].getSensorId(), datas[i].getPrecipitation());
//            	}
            	for (int i=1; i<=num; i++) {
            		producer.SendMessage("WEATHER_STATION_00000000" + i, datas[i-1].getPrecipitation());
            	}
            	for (int i=num+1; i<=70; i++) {
            		producer.SendMessage("WEATHER_STATION_00000000" + i, datas[num-1].getPrecipitation());
            	}
            }            
        };
        
        timer.scheduleAtFixedRate(task, 0, (long)(60*1000));        
    }
    
    protected void stop()
    {
        if (timer != null)
        {
        	addConsoleInfo("Stop receiving data.");
        	producer.close();
            timer.cancel();
            timer = null;
        }
    }
    
	// add an info text line to the console view
	protected void addConsoleInfo(String info) {
		Date date=new Date();
		DateFormat format=new SimpleDateFormat(">> [HH:mm:ss]"); // get time text
		String time=format.format(date);
		final String text = time + "  " + info + "\n";
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				textAreaConsole.append(text);
			}		
		});
	}
}
