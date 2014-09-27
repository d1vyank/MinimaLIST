package ca.ualberta.cs.minimalist;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ca.ualberta.cs.minimalist.ItemModel;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AbsListView.MultiChoiceModeListener;



public class ItemListActivity extends Activity {
	customAdapter itemAdapter = null;
	List<Integer> positions = new ArrayList<Integer>();
	String recipient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ItemListManager.initManager(this.getApplicationContext());
		Collection<ItemModel> items = ItemListManager.getItemModelList().getItems();
		System.out.println(items);
		final ArrayList<ItemModel> list = new ArrayList<ItemModel>(items);
		itemAdapter = new customAdapter(this, R.layout.item_info, list);
		final ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(itemAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				// Respond to clicks on the actions in the CAB
				switch (item.getItemId()) {
				case R.id.delete:
					if(positions != null) {
						ItemListManager.getItemModelList().deleteSelectedItems(positions);
					}
					mode.finish(); // Action picked, so close the CAB
					return true;
				case R.id.archive:
					ArrayList<ItemModel> itemsToArchive = ItemListManager.getItemModelList().getSelectItems(positions);
					ItemListManager.getManager().archiveMany(itemsToArchive);
					ItemListManager.getItemModelList().deleteSelectedItems(positions);
					mode.finish();
					return true;
				case R.id.email:
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
				inflater.inflate(R.menu.selection_menu, menu);
				positions.clear();
				return true;
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

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// TODO Auto-generated method stub

			}
		});

		checkButtonClick();

		ItemListManager.getItemModelList().addListener(new Listener() {			
			@Override
			public void update() {
				list.clear();
				Collection<ItemModel> items = ItemListManager.getItemModelList().getItems();
				list.addAll(items);
				itemAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.view_archives) {
			viewArchives(item);
			return true;
		}
		if (id == R.id.delete_allItems) {
			delItems();
			return true;
		}
		if (id == R.id.delete_allArchives) {
			delArchives();
			return true;
		}
		if (id == R.id.email_all) {
			emailItems(null);
			return true;
		}
		if (id == R.id.Summary) {
			viewSummary();
			return true;
		}
		

		return super.onOptionsItemSelected(item);
	}
	public void viewArchives(MenuItem menu) {
		Toast.makeText(this, "Archives", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(ItemListActivity.this, ArchivedListActivity.class);
		startActivity(intent);
	}

	private void checkButtonClick() {
		Button myButton = (Button) findViewById(R.id.addItem);
		myButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popUp();
			} 
		});
	}
	private ItemModel newItem(String text) {
		ItemModel im = new ItemModel(text, false);
		return im;
	}

	// Code snippet from http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog

	private void popUp() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("New Task");
		alert.setMessage("Enter Task");
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				ItemListManager.getManager().addItemModel(newItem(value));
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();
	}
	// code from http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application
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
		
		ArrayList<ItemModel> itemsToEmail = ItemListManager.getItemModelList().getSelectItems(positions);
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , recipient);
		i.putExtra(Intent.EXTRA_SUBJECT, "ToDo list from MinimaLIST.app");
		int count = 1;
		for (ItemModel im : itemsToEmail) {
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
		    Toast.makeText(ItemListActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}
	private void delItems() {
		ItemListManager.getItemModelList().deleteAllItems();
	}
	private void delArchives() {
		ItemListManager.getItemModelList().deleteAllArchives();
	}
	private void viewSummary() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Summary");
		alert.setMessage("Number of Checked items: " + ItemListManager.getItemModelList().checkedSize()+ "\n"+
						"Number of UnChecked items: " + ItemListManager.getItemModelList().uncheckedSize()+ "\n"+
						"Number of Checked Archives: " + ItemListManager.getItemModelList().checkedArchiveSize()+ "\n"+
						"Number of UnChecked Archives: " + ItemListManager.getItemModelList().uncheckedArchiveSize());
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				return;
			}
		});
		alert.show();
	}


}	