package pl.rafik.geoorganizer.activities.list;

import java.util.List;

import pl.rafik.geoorganizer.R;
import pl.rafik.geoorganizer.model.dto.TaskDTO;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter do listy taskow.
 * 
 * @author rafal.machnik
 * 
 */
public class TaskArrayAdapter extends BaseAdapter {
	private final Context context;
	private List<TaskDTO> taskList;

	public TaskArrayAdapter(Context c, List<TaskDTO> dtoList) {
		this.context = c;
		this.taskList = dtoList;
	}

	public List<TaskDTO> getCurrentTaskList() {
		return taskList;
	}

	@Override
	public int getCount() {
		if (taskList.isEmpty() || taskList == null) {
			return 0;
		} else {
			return taskList.size();
		}
	}

	@Override
	public Object getItem(int pos) {
		return taskList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return taskList.get(pos).getId();
	}

	@Override
	public View getView(int pos, View content, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.list_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.tv_taskNote);
		ImageView image = (ImageView) rowView.findViewById(R.id.ic_statucIcon);
		if (taskList.get(pos).getStatus().equals("DONE"))
			image.setImageResource(R.drawable.ic_ok);
		else
			image.setImageResource(R.drawable.ic_not);
		textView.setText(taskList.get(pos).getNote());
		Log.d("data", taskList.get(pos).getDate());
		return rowView;

	}
}
