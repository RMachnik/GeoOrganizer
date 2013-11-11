package pl.rafik.geoorganizer.model.dto;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author:Rafal
 * Date: 18.05.13
 * Time: 13:05
 */
public class EmailModel {
    private List<TaskDTO> taskDTOList;
    private String email;

    public List<TaskDTO> getTaskDTOList() {
        return taskDTOList;
    }

    public void setTaskDTOList(List<TaskDTO> taskDTOList) {
        this.taskDTOList = taskDTOList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
