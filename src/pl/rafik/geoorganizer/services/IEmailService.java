package pl.rafik.geoorganizer.services;

import android.app.Activity;
import pl.rafik.geoorganizer.model.dto.EmailModel;

/**
 * Created with IntelliJ IDEA.
 * Author:Rafal
 * Date: 18.05.13
 * Time: 13:01
 */
public interface IEmailService {

    public void sendEmail(EmailModel emailModel, Activity activity);
}
