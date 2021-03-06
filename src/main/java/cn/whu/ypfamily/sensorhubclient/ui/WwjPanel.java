package cn.whu.ypfamily.sensorhubclient.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwindx.examples.ClickAndGoSelectListener;
import gov.nasa.worldwindx.examples.util.HighlightController;
import gov.nasa.worldwindx.examples.util.ToolTipController;

public class WwjPanel extends JPanel{
	
	protected WorldWindow wwd;
    protected StatusBar statusBar;
    protected ToolTipController toolTipController;
    protected HighlightController highlightController;

    public WwjPanel(Dimension canvasSize, boolean includeStatusBar)
    {
    	super(new BorderLayout());

        this.wwd = this.createWorldWindow();
        ((Component) this.wwd).setPreferredSize(canvasSize);

        // Create the default model as described in the current worldwind properties.
        Model m = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
        this.wwd.setModel(m);

        // Setup a select listener for the worldmap click-and-go feature
        this.wwd.addSelectListener(new ClickAndGoSelectListener(this.getWwd(), WorldMapLayer.class));

        this.add((Component) this.wwd, BorderLayout.CENTER);
        if (includeStatusBar)
        {
        	this.statusBar = new StatusBar();
            this.add(statusBar, BorderLayout.PAGE_END);
            this.statusBar.setEventSource(wwd);
        }

        // Add controllers to manage highlighting and tool tips.
        this.toolTipController = new ToolTipController(this.getWwd(), AVKey.DISPLAY_NAME, null);
        this.highlightController = new HighlightController(this.getWwd(), SelectEvent.ROLLOVER);
    }

    protected WorldWindow createWorldWindow()
    {
        return new WorldWindowGLCanvas();
    }

    public WorldWindow getWwd()
    {
        return wwd;
    }

    public StatusBar getStatusBar()
    {
        return statusBar;
    }
}
