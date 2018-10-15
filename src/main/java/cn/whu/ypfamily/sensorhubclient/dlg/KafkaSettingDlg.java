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

public class KafkaSettingDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtServers;
	
	private boolean clickOK = false;
	private JTextField txtTopic;

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
	public KafkaSettingDlg() {
		setTitle("Kafka Setting");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 450, 160);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblServers = new JLabel("bootstrap.servers");
			GridBagConstraints gbc_lblServers = new GridBagConstraints();
			gbc_lblServers.anchor = GridBagConstraints.WEST;
			gbc_lblServers.insets = new Insets(0, 0, 5, 5);
			gbc_lblServers.gridx = 0;
			gbc_lblServers.gridy = 1;
			contentPanel.add(lblServers, gbc_lblServers);
		}
		{
			txtServers = new JTextField();
			txtServers.setText("");
			GridBagConstraints gbc_txtServers = new GridBagConstraints();
			gbc_txtServers.insets = new Insets(0, 0, 5, 0);
			gbc_txtServers.gridwidth = 2;
			gbc_txtServers.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtServers.gridx = 2;
			gbc_txtServers.gridy = 1;
			contentPanel.add(txtServers, gbc_txtServers);
			txtServers.setColumns(10);
		}
		{
			JLabel lblTopic = new JLabel("Topic");
			GridBagConstraints gbc_lblTopic = new GridBagConstraints();
			gbc_lblTopic.anchor = GridBagConstraints.WEST;
			gbc_lblTopic.insets = new Insets(0, 0, 0, 5);
			gbc_lblTopic.gridx = 0;
			gbc_lblTopic.gridy = 2;
			contentPanel.add(lblTopic, gbc_lblTopic);
		}
		{
			txtTopic = new JTextField();
			txtTopic.setText("");
			txtTopic.setColumns(10);
			GridBagConstraints gbc_txtTopic = new GridBagConstraints();
			gbc_txtTopic.gridwidth = 2;
			gbc_txtTopic.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtTopic.gridx = 2;
			gbc_txtTopic.gridy = 2;
			contentPanel.add(txtTopic, gbc_txtTopic);
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
	
	public String getKafkaServers() {
		return this.txtServers.getText();
	}
	
	public String getKafkaTopic() {
		return this.txtTopic.getText();
	}
	
	public void setKafkaServers(String servers) {
		this.txtServers.setText(servers);
	}
	
	public void setKafkaTopic(String topics) {
		this.txtTopic.setText(topics);
	}
}
