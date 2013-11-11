package pl.rafik.geoorganizer.services.impl;

import android.app.Activity;
import android.content.Intent;
import pl.rafik.geoorganizer.model.dto.EmailModel;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import pl.rafik.geoorganizer.model.dto.TaskDTOEmailDecorator;
import pl.rafik.geoorganizer.services.IEmailService;

/**
 * Created with IntelliJ IDEA.
 * Author:Rafal
 * Date: 18.05.13
 * Time: 13:15
 */
public class EmailService implements IEmailService {
    @Override
    public void sendEmail(EmailModel emailModel, Activity activity) {
        String to = emailModel.getEmail();
        String subject = "Zadania wyslane z Twojej aplikacji";
        StringBuilder builder = new StringBuilder();
        for (TaskDTO dto : emailModel.getTaskDTOList()) {
            TaskDTOEmailDecorator taskDTOEmailDecorator = new TaskDTOEmailDecorator(dto);
            builder.append(taskDTOEmailDecorator.getMailContent()).append(System.getProperty("line.separator"));
        }
        builder.append("mail from your Appicaion");

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});

        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, builder.toString());

        //need this to prompts email client only
        email.setType("message/rfc822");

        activity.startActivity(Intent.createChooser(email, "Wybierz klienta email :"));
    }
}
