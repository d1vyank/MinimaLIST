package ca.ualberta.cs.minimalist;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class customArchiveAdapter extends ArrayAdapter<ItemModel>
{
	private ArrayList<ItemModel> items;
    private LayoutInflater inflater;
    
	public customArchiveAdapter(Context context, int textViewResourceId, 
			ArrayList<ItemModel> items) {
		super(context, textViewResourceId, items);
		this.items = new ArrayList<ItemModel>();
		this.items.addAll(items);
		inflater = LayoutInflater.from(context) ;
	}

	private class ViewHolder {
		TextView code;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.archive_info, null);

			holder = new ViewHolder();
			holder.code = (TextView) convertView.findViewById(R.id.code1);
			convertView.setTag(holder);
			
		}
		ItemModel item;
		items.clear();
		Collection<ItemModel> it = ItemListManager.getItemModelList().getArchives();
		items.addAll(it); 
	
		item = items.get(position);
		try {
			if(item.isChecked())
				holder.code.setText("Completed: "+item.getText());
			else
				holder.code.setText("InComplete: "+item.getText());
		} catch (NullPointerException np) {
			np.printStackTrace();
		}
		return convertView;
	}	
}
