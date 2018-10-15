package cn.whu.ypfamily.sensorhubclient.dlg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import cn.whu.ypfamily.sensorhubclient.entities.Sensor;
import cn.whu.ypfamily.sensorhubclient.ui.DecimalOnlyDocument;

public class SensorPropertyDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldPrcdId;
	private JTextField textFieldLongName;
	private JTextField textFieldShortName;
	private JTextField textFieldOfferingId;
	private JTextField textFieldLat;
	private JTextField textFieldLng;
	private JTextField textFieldAlt;
	private JTextField textFieldSensorName;

	/**
	 * Create the dialog.
	 */
	public SensorPropertyDlg() {
		setTitle("Sensor Properties");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 450, 420);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane);
			scrollPane.setViewportBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "SensorML Properties", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			{
				JPanel panel = new JPanel();
				panel.setBorder(new EmptyBorder(8, 8, 8, 8));
				scrollPane.setViewportView(panel);
				GridBagLayout gbl_panel = new GridBagLayout();
				gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
				gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
				gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				panel.setLayout(gbl_panel);
				{
					JLabel lblSensorID = new JLabel("SensorName");
					lblSensorID.setForeground(Color.BLACK);
					GridBagConstraints gbc_lblSensorID = new GridBagConstraints();
					gbc_lblSensorID.anchor = GridBagConstraints.WEST;
					gbc_lblSensorID.insets = new Insets(0, 0, 5, 5);
					gbc_lblSensorID.gridx = 0;
					gbc_lblSensorID.gridy = 0;
					panel.add(lblSensorID, gbc_lblSensorID);
				}
				{
					textFieldSensorName = new JTextField();
					textFieldSensorName.setEditable(false);
					GridBagConstraints gbc_textFieldSensorName = new GridBagConstraints();
					gbc_textFieldSensorName.gridwidth = 4;
					gbc_textFieldSensorName.insets = new Insets(0, 0, 5, 5);
					gbc_textFieldSensorName.fill = GridBagConstraints.HORIZONTAL;
					gbc_textFieldSensorName.gridx = 3;
					gbc_textFieldSensorName.gridy = 0;
					panel.add(textFieldSensorName, gbc_textFieldSensorName);
					textFieldSensorName.setColumns(10);
				}
				{
					JPanel panelIdentification = new JPanel();
					panelIdentification.setBorder(new TitledBorder(null, "Identification", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					GridBagConstraints gbc_panelIdentification = new GridBagConstraints();
					gbc_panelIdentification.gridwidth = 7;
					gbc_panelIdentification.insets = new Insets(0, 0, 5, 0);
					gbc_panelIdentification.fill = GridBagConstraints.BOTH;
					gbc_panelIdentification.gridx = 0;
					gbc_panelIdentification.gridy = 1;
					panel.add(panelIdentification, gbc_panelIdentification);
					GridBagLayout gbl_panelIdentification = new GridBagLayout();
					gbl_panelIdentification.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
					gbl_panelIdentification.rowHeights = new int[]{0, 0, 0, 0};
					gbl_panelIdentification.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
					gbl_panelIdentification.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
					panelIdentification.setLayout(gbl_panelIdentification);
					{
						JLabel lblProcedure = new JLabel("Procedure");
						GridBagConstraints gbc_lblProcedure = new GridBagConstraints();
						gbc_lblProcedure.anchor = GridBagConstraints.WEST;
						gbc_lblProcedure.insets = new Insets(0, 0, 5, 5);
						gbc_lblProcedure.gridx = 0;
						gbc_lblProcedure.gridy = 0;
						panelIdentification.add(lblProcedure, gbc_lblProcedure);
						lblProcedure.setForeground(Color.BLACK);
					}
					{
						textFieldPrcdId = new JTextField();
						textFieldPrcdId.setEditable(false);
						GridBagConstraints gbc_textFieldPrcdId = new GridBagConstraints();
						gbc_textFieldPrcdId.fill = GridBagConstraints.HORIZONTAL;
						gbc_textFieldPrcdId.gridwidth = 2;
						gbc_textFieldPrcdId.insets = new Insets(0, 0, 5, 0);
						gbc_textFieldPrcdId.gridx = 3;
						gbc_textFieldPrcdId.gridy = 0;
						panelIdentification.add(textFieldPrcdId, gbc_textFieldPrcdId);
						textFieldPrcdId.setColumns(10);
					}
					{
						JLabel lblLongname = new JLabel("LongName");
						GridBagConstraints gbc_lblLongname = new GridBagConstraints();
						gbc_lblLongname.anchor = GridBagConstraints.WEST;
						gbc_lblLongname.insets = new Insets(0, 0, 5, 5);
						gbc_lblLongname.gridx = 0;
						gbc_lblLongname.gridy = 1;
						panelIdentification.add(lblLongname, gbc_lblLongname);
					}
					{
						textFieldLongName = new JTextField();
						textFieldLongName.setEditable(false);
						GridBagConstraints gbc_textFieldLongName = new GridBagConstraints();
						gbc_textFieldLongName.gridwidth = 2;
						gbc_textFieldLongName.fill = GridBagConstraints.HORIZONTAL;
						gbc_textFieldLongName.insets = new Insets(0, 0, 5, 0);
						gbc_textFieldLongName.gridx = 3;
						gbc_textFieldLongName.gridy = 1;
						panelIdentification.add(textFieldLongName, gbc_textFieldLongName);
						textFieldLongName.setColumns(10);
					}
					{
						JLabel lblShortname = new JLabel("ShortName");
						GridBagConstraints gbc_lblShortname = new GridBagConstraints();
						gbc_lblShortname.anchor = GridBagConstraints.WEST;
						gbc_lblShortname.insets = new Insets(0, 0, 0, 5);
						gbc_lblShortname.gridx = 0;
						gbc_lblShortname.gridy = 2;
						panelIdentification.add(lblShortname, gbc_lblShortname);
					}
					{
						textFieldShortName = new JTextField();
						textFieldShortName.setEditable(false);
						GridBagConstraints gbc_textFieldShortName = new GridBagConstraints();
						gbc_textFieldShortName.gridwidth = 2;
						gbc_textFieldShortName.fill = GridBagConstraints.HORIZONTAL;
						gbc_textFieldShortName.gridx = 3;
						gbc_textFieldShortName.gridy = 2;
						panelIdentification.add(textFieldShortName, gbc_textFieldShortName);
						textFieldShortName.setColumns(10);
					}
				}
				{
					JPanel panelCapabilities = new JPanel();
					panelCapabilities.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Capabilities", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
					GridBagConstraints gbc_panelCapabilities = new GridBagConstraints();
					gbc_panelCapabilities.gridwidth = 7;
					gbc_panelCapabilities.insets = new Insets(0, 0, 5, 0);
					gbc_panelCapabilities.fill = GridBagConstraints.BOTH;
					gbc_panelCapabilities.gridx = 0;
					gbc_panelCapabilities.gridy = 2;
					panel.add(panelCapabilities, gbc_panelCapabilities);
					GridBagLayout gbl_panelCapabilities = new GridBagLayout();
					gbl_panelCapabilities.columnWidths = new int[]{0, 0, 0, 0, 0};
					gbl_panelCapabilities.rowHeights = new int[]{0, 0};
					gbl_panelCapabilities.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
					gbl_panelCapabilities.rowWeights = new double[]{0.0, Double.MIN_VALUE};
					panelCapabilities.setLayout(gbl_panelCapabilities);
					{
						JLabel lblOfferring = new JLabel("Offerring");
						GridBagConstraints gbc_lblOfferring = new GridBagConstraints();
						gbc_lblOfferring.insets = new Insets(0, 0, 0, 5);
						gbc_lblOfferring.gridx = 0;
						gbc_lblOfferring.gridy = 0;
						panelCapabilities.add(lblOfferring, gbc_lblOfferring);
						lblOfferring.setForeground(Color.BLACK);
					}
					{
						textFieldOfferingId = new JTextField();
						textFieldOfferingId.setEditable(false);
						GridBagConstraints gbc_textFieldOfferingId = new GridBagConstraints();
						gbc_textFieldOfferingId.fill = GridBagConstraints.HORIZONTAL;
						gbc_textFieldOfferingId.gridx = 3;
						gbc_textFieldOfferingId.gridy = 0;
						panelCapabilities.add(textFieldOfferingId, gbc_textFieldOfferingId);
						textFieldOfferingId.setColumns(10);
					}
				}
				{
					JPanel panelPosition = new JPanel();
					panelPosition.setBorder(new TitledBorder(null, "Position", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					GridBagConstraints gbc_panelPosition = new GridBagConstraints();
					gbc_panelPosition.fill = GridBagConstraints.BOTH;
					gbc_panelPosition.gridwidth = 7;
					gbc_panelPosition.insets = new Insets(0, 0, 5, 0);
					gbc_panelPosition.gridx = 0;
					gbc_panelPosition.gridy = 3;
					panel.add(panelPosition, gbc_panelPosition);
					GridBagLayout gbl_panelPosition = new GridBagLayout();
					gbl_panelPosition.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
					gbl_panelPosition.rowHeights = new int[]{0, 0, 0};
					gbl_panelPosition.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
					gbl_panelPosition.rowWeights = new double[]{0.0, 0.0, 0.0};
					panelPosition.setLayout(gbl_panelPosition);
					{
						JLabel lblLatitude = new JLabel("Latitude");
						GridBagConstraints gbc_lblLatitude = new GridBagConstraints();
						gbc_lblLatitude.anchor = GridBagConstraints.WEST;
						gbc_lblLatitude.gridwidth = 2;
						gbc_lblLatitude.insets = new Insets(0, 0, 5, 5);
						gbc_lblLatitude.gridx = 0;
						gbc_lblLatitude.gridy = 0;
						panelPosition.add(lblLatitude, gbc_lblLatitude);
					}
					{
						textFieldLat = new JTextField();
						textFieldLat.setEditable(false);
						textFieldLat.setText("0.0");
						GridBagConstraints gbc_textFieldLat = new GridBagConstraints();
						gbc_textFieldLat.gridwidth = 11;
						gbc_textFieldLat.fill = GridBagConstraints.HORIZONTAL;
						gbc_textFieldLat.insets = new Insets(0, 0, 5, 0);
						gbc_textFieldLat.gridx = 3;
						gbc_textFieldLat.gridy = 0;
						panelPosition.add(textFieldLat, gbc_textFieldLat);
						textFieldLat.setDocument(new DecimalOnlyDocument());
						textFieldLat.setColumns(10);
					}
					{
						JLabel lblLongitude = new JLabel("Longitude");
						GridBagConstraints gbc_lblLongitude = new GridBagConstraints();
						gbc_lblLongitude.anchor = GridBagConstraints.WEST;
						gbc_lblLongitude.insets = new Insets(0, 0, 5, 5);
						gbc_lblLongitude.gridx = 0;
						gbc_lblLongitude.gridy = 1;
						panelPosition.add(lblLongitude, gbc_lblLongitude);
					}
					{
						textFieldLng = new JTextField();
						textFieldLng.setEditable(false);
						textFieldLng.setText("0.0");
						GridBagConstraints gbc_textFieldLng = new GridBagConstraints();
						gbc_textFieldLng.fill = GridBagConstraints.HORIZONTAL;
						gbc_textFieldLng.gridwidth = 11;
						gbc_textFieldLng.insets = new Insets(0, 0, 5, 0);
						gbc_textFieldLng.gridx = 3;
						gbc_textFieldLng.gridy = 1;
						panelPosition.add(textFieldLng, gbc_textFieldLng);
						textFieldLng.setDocument(new DecimalOnlyDocument());
						textFieldLng.setColumns(10);
					}
					{
						JLabel lblAltitude = new JLabel("Altitude");
						GridBagConstraints gbc_lblAltitude = new GridBagConstraints();
						gbc_lblAltitude.anchor = GridBagConstraints.WEST;
						gbc_lblAltitude.insets = new Insets(0, 0, 5, 5);
						gbc_lblAltitude.gridx = 0;
						gbc_lblAltitude.gridy = 2;
						panelPosition.add(lblAltitude, gbc_lblAltitude);
					}
					{
						textFieldAlt = new JTextField();
						textFieldAlt.setEditable(false);
						textFieldAlt.setText("0.0");
						GridBagConstraints gbc_textFieldAlt = new GridBagConstraints();
						gbc_textFieldAlt.fill = GridBagConstraints.HORIZONTAL;
						gbc_textFieldAlt.gridwidth = 11;
						gbc_textFieldAlt.insets = new Insets(0, 0, 5, 0);
						gbc_textFieldAlt.gridx = 3;
						gbc_textFieldAlt.gridy = 2;
						panelPosition.add(textFieldAlt, gbc_textFieldAlt);
						textFieldAlt.setDocument(new DecimalOnlyDocument());
						textFieldAlt.setColumns(10);
					}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
	
	public void setSensor(Sensor sensor) {
		textFieldSensorName.setText(sensor.getSensorName());
		textFieldPrcdId.setText(sensor.getObsOffering().getProcedure());
		textFieldLongName.setText(sensor.getLongName());
		textFieldShortName.setText(sensor.getShortName());
		textFieldOfferingId.setText(sensor.getObsOffering().getIdentifier());
		textFieldLat.setText(sensor.getLat());
		textFieldLng.setText(sensor.getLng());
		textFieldAlt.setText(sensor.getAlt());
	}
}
