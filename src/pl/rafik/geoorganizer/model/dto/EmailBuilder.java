package pl.rafik.geoorganizer.model.dto;

import android.content.Context;
import pl.rafik.geoorganizer.services.ITaskService;
import pl.rafik.geoorganizer.services.impl.TaskService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author:Rafal
 * Date: 18.05.13
 * Time: 13:06
 */
public class EmailBuilder {
    private Context context;
    private ITaskService taskService;

    public EmailBuilder(Context context) {
        this.context = context;
        taskService = new TaskService(context);
    }

    public EmailModel buildEmailWithDoneTask() {

        EmailModel emailModel = new EmailModel();
        emailModel.setEmail("");
        emailModel.setTaskDTOList(taskService.getDoneTasks());
        return emailModel;
    }

    public EmailModel buildEmailWithNotDoneTask() {
        EmailModel emailModel = new EmailModel();
        emailModel.setEmail("");
        emailModel.setTaskDTOList(taskService.getNotDoneTasks());
        return emailModel;
    }

    public EmailModel buildCurrentTask(Long id) {
        EmailModel emailModel = new EmailModel();
        emailModel.setEmail("");
        List<TaskDTO> taskDTOList = new ArrayList<TaskDTO>();
        taskDTOList.add(taskService.getTask(id));
        emailModel.setTaskDTOList(taskDTOList);
        return emailModel;
    }

}
