package pl.rafik.geoorganizer.util;

import android.util.Log;

import java.util.Calendar;

/**
 * User: rafik991@gmail.com
 * Date: 11/24/13
 */
public class DateUtil {
    public static void parseDate(Calendar tmp, String[] dtime) {
        if (dtime.length > 1) {
            String data[] = dtime[0].split("-");
            tmp.set(Calendar.DAY_OF_MONTH,
                    Integer.parseInt(data[0]));
            tmp.set(Calendar.MONTH, Integer.parseInt(data[1]));
            tmp.set(Calendar.YEAR, Integer.parseInt(data[2]));
            String[] time = dtime[1].split(":");
            tmp.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
            tmp.set(Calendar.MINUTE, Integer.parseInt(time[1]));
        } else {
            Log.d("DAO", "blad parsowania daty");
        }
    }
}
