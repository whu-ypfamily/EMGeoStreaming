package cn.whu.ypfamily.sensorhubclient;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class IconNodeRenderer extends DefaultTreeCellRenderer 
{   
	public Component getTreeCellRendererComponent(JTree tree, Object value,boolean sel, boolean expanded, boolean leaf, int row,boolean hasFocus)
    {   
       super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);    
       Icon icon = ((IconNode) value).getIcon();
       // set icon
       if (icon != null) {
           this.setIcon(icon);
           this.setOpenIcon(icon);
           this.setClosedIcon(icon);
           this.setLeafIcon(icon);
       }
       return this;   
     }
}

// define node class
class IconNode extends DefaultMutableTreeNode 
{   
	
	protected Icon icon = null;  
	protected String txt = null;     
  
	public IconNode(String txt)
	{
		super(txt);
		this.txt=txt;
	}
  
	public IconNode(Icon icon,String txt)
	{
		super(txt);
		this.icon = icon;  
		this.txt = txt;
	}
 
	public void setIcon(Icon icon) 
	{   
		this.icon = icon;   
	}
  
	public Icon getIcon() 
	{   
		return icon;   
	}
 
	public void setText(String txt)
	{
		this.txt=txt;
		this.setUserObject(txt);
	}
 
	public String getText()
	{
		return txt;
	}   
}