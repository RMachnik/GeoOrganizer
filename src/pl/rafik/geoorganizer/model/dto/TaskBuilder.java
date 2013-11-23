package pl.rafik.geoorganizer.model.dto;

/**
 * Created with IntelliJ IDEA.
 * Author:Rafal
 * Date: 18.05.13
 * Time: 12:27
 */
public class TaskBuilder {

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

    public GeoLocalisation getLocalisation() {
        return localisation;
    }

    public void setLocalisation(GeoLocalisation localisation) {
        this.localisation = localisation;
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

    public TaskDTO build() {
        TaskDTO dto = new TaskDTO();
        dto.setDate(this.getDate());
        dto.setId(this.getId());
        dto.setLocalisation(this.getLocalisation());
        dto.setPriority(this.getPriority());
        dto.setDate(this.getDate());
        dto.setNote(this.getNote());
        dto.setStatus(this.getStatus());
        return dto;
    }

    public TaskDTO buildEmpty() {
        TaskDTO dto = new TaskDTO();
        dto.setId(null);
        dto.setStatus("");
        dto.setDate("");
        dto.setLocalisation(new GeoLocalisation());
        dto.getLocalisation().setLatitude("");
        dto.getLocalisation().setLongitude("");
        dto.getLocalisation().setLocalistationAddress("");
        dto.setPriority("");
        dto.setNote("");
        return dto;

    }
}
