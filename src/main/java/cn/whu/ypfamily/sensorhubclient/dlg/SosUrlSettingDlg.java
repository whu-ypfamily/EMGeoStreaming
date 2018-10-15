package cn.whu.ypfamily.sensorhubclient.dlg;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SosUrlSettingDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtSosUrl;
	
	private boolean clickOK = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SosUrlSettingDlg dialog = new SosUrlSettingDlg();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SosUrlSettingDlg() {
		setTitle("Set SOS Url");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 450, 160);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblSosUrl = new JLabel("SOS URL");
			GridBagConstraints gbc_lblSosUrl = new GridBagConstraints();
			gbc_lblSosUrl.insets = new Insets(0, 0, 0, 5);
			gbc_lblSosUrl.gridx = 0;
			gbc_lblSosUrl.gridy = 1;
			contentPanel.add(lblSosUrl, gbc_lblSosUrl);
		}
		{
			txtSosUrl = new JTextField();
			txtSosUrl.setText("http://localhost:8181/sensorhub/sos");
			GridBagConstraints gbc_txtSosUrl = new GridBagConstraints();
			gbc_txtSosUrl.gridwidth = 2;
			gbc_txtSosUrl.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtSosUrl.gridx = 2;
			gbc_txtSosUrl.gridy = 1;
			contentPanel.add(txtSosUrl, gbc_txtSosUrl);
			txtSosUrl.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						clickOK = true;
						setVisible(false);
					}
				});
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						clickOK = false;
						setVisible(false);
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public boolean clickOK() {
		return this.clickOK;
	}
	
	public String getSosUrl() {
		return this.txtSosUrl.getText();
	}
	
	public void setSosUrl(String sosUrl) {
		this.txtSosUrl.setText(sosUrl);
	}
}
