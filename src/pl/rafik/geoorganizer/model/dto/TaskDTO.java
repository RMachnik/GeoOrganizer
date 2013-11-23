package pl.rafik.geoorganizer.model.dto;

public class TaskDTO {

	private String id;
	private String note;
	private GeoLocalisation localisation;
	private String date;
	private String priority;
	private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public GeoLocalisation getLocalisation() {
		return localisation;
	}

	public void setLocalisation(GeoLocalisation localisation) {
		this.localisation = localisation;
	}

    @Override
    public String toString() {
        return "Your task{" +
                "note='" + note + '\'' +
                ", localisation=" + localisation +
                ", date='" + date + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
