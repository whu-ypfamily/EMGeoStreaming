package cn.whu.ypfamily.sensorhubclient.entities;

public class ObservationOffering {
	
	private String description;
	private String identifier;
	private String name;
	private String procedure;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProcedure() {
		return procedure;
	}
	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}
	
	@Override
	public String toString() {
		return "ObservationOffering [description=" + description + ", identifier=" + identifier + ", name=" + name
				+ ", procedure=" + procedure + "]";
	}
}
