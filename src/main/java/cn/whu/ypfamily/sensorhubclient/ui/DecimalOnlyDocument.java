package cn.whu.ypfamily.sensorhubclient.ui;

import java.awt.Toolkit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class DecimalOnlyDocument extends PlainDocument {
    private boolean dot = false;
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        StringBuffer tmp = new StringBuffer(super.getText(0, super.getLength()));
        tmp.insert(offs, str);
        Pattern p = Pattern.compile("^-?\\d*(\\.)?\\d*$");
        Matcher m = p.matcher(tmp.toString());
        if (m.find()) {
            super.insertString(offs, str, a);
        }
        else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}