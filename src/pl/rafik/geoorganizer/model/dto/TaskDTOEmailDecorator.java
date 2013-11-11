package pl.rafik.geoorganizer.model.dto;

/**
 * Created with IntelliJ IDEA.
 * Author:Rafal
 * Date: 18.05.13
 * Time: 13:12
 */
public class TaskDTOEmailDecorator {

    private TaskDTO taskDTO;

    public TaskDTOEmailDecorator(TaskDTO taskDTO) {

        this.taskDTO = taskDTO;
    }

    public TaskDTO getTaskDTO() {
        return taskDTO;
    }

    public void setTaskDTO(TaskDTO taskDTO) {
        this.taskDTO = taskDTO;
    }

    public String getMailContent() {
       return taskDTO.toString();
    }
}
