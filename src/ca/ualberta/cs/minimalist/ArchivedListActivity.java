package ca.ualberta.cs.minimalist;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AbsListView.MultiChoiceModeListener;

public class ArchivedListActivity extends ItemListActivity {
	customArchiveAdapter archiveAdapter = null;
	List<Integer> positions = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archived_activity);
		ItemListManager.initManager(this.getApplicationContext());
		Collection<ItemModel> items = ItemListManager.getItemModelList().getArchives();
		final ArrayList<ItemModel> list = new ArrayList<ItemModel>(items);
		archiveAdapter = new customArchiveAdapter(this, R.layout.archive_info, list);
		final ListView listView = (ListView) findViewById(R.id.listView2);
		listView.setAdapter(archiveAdapter);
		
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
			
			
			@Override
		    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		        // Respond to clicks on the actions in the CAB
		        switch (item.getItemId()) {
		            case R.id.delete1:
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
		            	return true;
		            case R.id.email1:
		            	emailItems(positions);
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
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {				
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {
				// Capture total checked items
				final int checkedCount = listView.getCheckedItemCount();
				// Set the CAB title according to total checked items
				mode.setTitle(checkedCount + " Selected");
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
    private void emailItems(List<Integer> positions) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("E-mail Items");
		alert.setMessage("Enter Recipient");
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				recipient = value;
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				return;
			}
		});

		alert.show();
		
		ArrayList<ItemModel> archivesToEmail = ItemListManager.getItemModelList().getSelectArchives(positions);
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , recipient);
		i.putExtra(Intent.EXTRA_SUBJECT, "ToDo list from MinimaLIST.app");
		int count = 1;
		for (ItemModel im : archivesToEmail) {
			i.putExtra(Intent.EXTRA_TEXT   , count);
			i.putExtra(Intent.EXTRA_TEXT   , " ");
			i.putExtra(Intent.EXTRA_TEXT   , im.getText());
			i.putExtra(Intent.EXTRA_TEXT   , " ");
			if (im.isChecked())
				i.putExtra(Intent.EXTRA_TEXT   , "Completed");
			else
				i.putExtra(Intent.EXTRA_TEXT   , "Incomplete");
		}
		
		try {
		    startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(ArchivedListActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}
}
