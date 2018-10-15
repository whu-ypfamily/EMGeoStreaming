package cn.whu.ypfamily.sensorhubclient.ui;

import java.awt.Insets;  

import javax.swing.ImageIcon;  
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;  
   
public class ImageButton extends JButton {

    public ImageButton(ImageIcon icon){
        setSize(icon.getImage().getWidth(null),
                icon.getImage().getHeight(null));
        setIcon(icon);
        setMargin(new Insets(0,0,0,0));
        setIconTextGap(0);
        setBorderPainted(false);
        //setBorder(null);
        setBorder(new EmptyBorder(4, 4, 4, 4));
        setText(null);
        setFocusPainted(false);
        //setContentAreaFilled(false);
    }  
}  