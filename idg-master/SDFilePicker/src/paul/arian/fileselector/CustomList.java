package paul.arian.fileselector;

/**
 * Created by Paul on 3/7/14.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;

public class CustomList extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] web;
	String ParentFolder;

	public CustomList(Activity context, String[] web, String path) {
		super(context, R.layout.list_single, web);
		this.context = context;
		this.web = web;
		ParentFolder = path;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		ViewHolder holder = null;
		if (view == null) {
			view = inflater.inflate(R.layout.list_single, null, true);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) view.findViewById(R.id.txt);
			holder.imageView = (ImageView) view.findViewById(R.id.img);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// View rowView= inflater.inflate(R.layout.list_single, null, true);

		holder.txtTitle.setText(web[position]);
		holder.imageView.setImageResource(R.drawable.document);
//		Picasso.with(context).load(new File(ParentFolder + "/" + web[position])).placeholder(R.drawable.document).resize(50, 50).into(holder.imageView);
		return view;
	}

	public static class ViewHolder {
		TextView txtTitle;
		ImageView imageView;
	}
}
