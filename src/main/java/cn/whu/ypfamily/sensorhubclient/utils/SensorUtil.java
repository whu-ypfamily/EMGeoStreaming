package cn.whu.ypfamily.sensorhubclient.utils;

import java.util.List;

import cn.whu.ypfamily.sensorhubclient.entities.Sensor;
import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.Point;
import net.opengis.sensorml.v20.IOPropertyList;
import net.opengis.sensorml.v20.IdentifierList;
import net.opengis.sensorml.v20.PhysicalSystem;
import net.opengis.sensorml.v20.Term;

public class SensorUtil {
	
	/**
	 * get a Sensor object from a PhysicalSystem object
	 * @param system
	 * @return
	 */
	public static Sensor PhysicalSystem2Sensor(PhysicalSystem system) {
		Sensor sensor = new Sensor();
		sensor.setSensorId(system.getId());
		sensor.setSensorName(system.getName());
		// get indentifacation list
		OgcPropertyList<IdentifierList> identificationList = system.getIdentificationList();
		if (identificationList != null && identificationList.size() > 0) {
			List<Term> identifier2List = identificationList.get(0).getIdentifier2List();
			if (identifier2List != null) {
				for (Term term :identifier2List) {
					if (term.getDefinition().equals("urn:ogc:def:identifier:OGC:longname")) {
						sensor.setLongName(term.getValue());
					}
					else if (term.getDefinition().equals("urn:ogc:def:identifier:OGC:shortname")) {
						sensor.setShortName(term.getValue());
					}
				}
			}
		}
		// get position
		if (system.getNumPositions() > 0) {
			Point point = (Point)system.getPositionList().get(0);
			double[] pos = point.getPos();
			sensor.setLat(String.valueOf(pos[0]));
			sensor.setLng(String.valueOf(pos[1]));
			sensor.setAlt(String.valueOf(pos[2]));
		}
		// output def
		IOPropertyList outputList = system.getOutputList();
		if (outputList != null && outputList.size() > 0) {
			String[] outputs = new String[outputList.size()];
			for (int i=0; i<outputList.size(); i++) {
				outputs[i] = outputList.getComponent(i).getDefinition();
			}
			sensor.setOutputs(outputs);
		}
		return sensor;
	}
	
}
