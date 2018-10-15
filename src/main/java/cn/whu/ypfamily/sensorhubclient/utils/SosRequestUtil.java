package cn.whu.ypfamily.sensorhubclient.utils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.vast.sensorML.SMLUtils;
import org.vast.xml.XMLReaderException;

import cn.whu.ypfamily.sensorhubclient.entities.ObservationOffering;
import cn.whu.ypfamily.sensorhubclient.entities.Sensor;
import net.opengis.sensorml.v20.PhysicalSystem;

public class SosRequestUtil {
	/**
	 * get sensor from DescribeSensor Http-Get Url
	 * @param url
	 * @param param
	 * @return
	 */
	public static Sensor getSensor(String url, String param) {
		// get and parse DescribeSensor document
		Sensor sensor = new Sensor();
		Document sensorDoc = RequestMethodsUtil.GETDoc(url, "service=SOS&version=2.0.0&request=DescribeSensor" + param);
		Element sensorRoot = sensorDoc.getRootElement();
		String PhysicalSystemXml = sensorRoot
				.element("description")
				.element("SensorDescription")
				.element("data")
				.element("PhysicalSystem")
				.asXML();
		// get sensor
		ByteArrayInputStream bais = new ByteArrayInputStream(PhysicalSystemXml.getBytes());
		try {
			PhysicalSystem system = (PhysicalSystem) new SMLUtils(null).readProcess(bais);
			sensor = SensorUtil.PhysicalSystem2Sensor(system);
		} catch (XMLReaderException e) {
			e.printStackTrace();
		}
		return sensor;
	}
	
	/**
	 * get observition offeriing list from GetCapabilities Http-Get Url
	 * @param url
	 * @return
	 */
	public static List<ObservationOffering> getObsOfferiings(String url) {
		// get and parse GetCapabilities document
		List<ObservationOffering> obsOfferings = new ArrayList<ObservationOffering>();
		Document capDoc =  RequestMethodsUtil.GETDoc(url, "service=SOS&version=2.0.0&request=GetCapabilities");
		Element capRoot = capDoc.getRootElement();
		Element capContents = capRoot.element("contents").element("Contents");
		// get observation offering list
		List offeringNodes = capContents.elements("offering");
		for (Iterator it = offeringNodes.iterator(); it.hasNext();) {
			// get observation offering
			Element offeringNode = (Element) it.next();
			ObservationOffering obsOffering = new ObservationOffering();
			Element obsOfferingNode = offeringNode.element("ObservationOffering");
			obsOffering.setDescription(obsOfferingNode.elementText("description"));
			obsOffering.setIdentifier(obsOfferingNode.elementText("identifier"));
			obsOffering.setName(obsOfferingNode.elementText("name"));
			obsOffering.setProcedure(obsOfferingNode.elementText("procedure"));
			obsOfferings.add(obsOffering);
		}
		return obsOfferings;
	}
	
	public static String getResult(String url, String param) {
		return RequestMethodsUtil.GETStr(url + "?service=SOS&version=2.0&request=GetResult" + param);
	}
}
