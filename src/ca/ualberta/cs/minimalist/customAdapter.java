package ca.ualberta.cs.minimalist;


import java.util.ArrayList;
import java.util.Collection;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


public class customAdapter extends ArrayAdapter<ItemModel> {

	private ArrayList<ItemModel> items;
    private LayoutInflater inflater;
    
	public customAdapter(Context context, int textViewResourceId, 
			ArrayList<ItemModel> items) {
		super(context, textViewResourceId, items);
		this.items = new ArrayList<ItemModel>();
		this.items.addAll(items);
		inflater = LayoutInflater.from(context) ;
	}

	private class ViewHolder {
		TextView code;
		CheckBox name;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_info, null);

			holder = new ViewHolder();
			holder.code = (TextView) convertView.findViewById(R.id.code);
			holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
			convertView.setTag(holder);
			
		} 
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		ItemModel item;
		items.clear();
		Collection<ItemModel> it = ItemListManager.getItemModelList().getItems();
		items.addAll(it); 
	
		item = items.get(position);
		holder.code.setText(item.getText());
		holder.name.setChecked(item.isChecked());
		holder.name.setTag(item);
		
		holder.name.setOnClickListener( new View.OnClickListener() {  
			public void onClick(View v) {  
				CheckBox cb = (CheckBox) v ;
				ItemModel item = (ItemModel) cb.getTag();
				if(cb.isChecked()) {
					ItemListManager.getManager();
					ItemListManager.getItemModelList().checkItem(item);
				}
				else {
					ItemListManager.getItemModelList().uncheckItem(item);
				}
				
			}  
		});  
		
		
	
		return convertView;

	}
	

}