package ca.ualberta.cs.minimalist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AbsListView.MultiChoiceModeListener;

public class ArchivedListActivity extends ItemListActivity {
	List<Integer> positions = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archived_activity);
		ItemListManager.initManager(this.getApplicationContext());
		Collection<ItemModel> items = ItemListManager.getItemModelList().getArchives();
		final ArrayList<ItemModel> list = new ArrayList<ItemModel>(items);
		final ArrayAdapter<ItemModel> itemAdapter = new ArrayAdapter<ItemModel>(this, android.R.layout.simple_list_item_1, list);
        ListView listView = (ListView) findViewById(R.id.listView2);
		listView.setAdapter(itemAdapter);
		
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
			
			
			@Override
		    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		        // Respond to clicks on the actions in the CAB
		        switch (item.getItemId()) {
		            case R.id.delete:
		            	if(positions != null) {
		            		ItemListManager.getItemModelList().deleteSelectedArchives(positions);
		            	}
		                mode.finish(); // Action picked, so close the CAB
		                return true;
		            case R.id.unarchive:
		            	ArrayList<ItemModel> itemsToArchive = ItemListManager.getItemModelList().getSelectArchives(positions);
		            	ItemListManager.getManager().unarchiveMany(itemsToArchive);
		            	ItemListManager.getItemModelList().deleteSelectedArchives(positions);
		            	mode.finish();
		            default:
		                return false;
		        }
				
		    }

		    @Override
		    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		        // Inflate the menu for the CAB
		        MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.archive_menu, menu);
		        positions.clear();
		        return true;
		    }


			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {
                if (positions.contains(position))
                	positions.remove(Integer.valueOf(position));
                else
                	positions.add(position);
			}
		});
		ItemListManager.getItemModelList().addListener(new Listener() {			
			@Override
			public void update() {
				list.clear();
				Collection<ItemModel> items = ItemListManager.getItemModelList().getArchives();
				list.addAll(items);
				itemAdapter.notifyDataSetChanged();
			}
		});
		
    }
}
