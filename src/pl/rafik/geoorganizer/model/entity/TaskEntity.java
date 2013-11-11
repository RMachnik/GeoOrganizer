package pl.rafik.geoorganizer.model.entity;

public class TaskEntity {

	private Long id;
	private String note;
	private String data;
	private String latitude;
	private String longitude;
	private String localistationAddress;
	private String priority;
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}



	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLocalistationAddress() {
		return localistationAddress;
	}

	public void setLocalistationAddress(String localistationAdress) {
		this.localistationAddress = localistationAdress;
	}

}
