package ca.ualberta.cs.minimalist;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import ca.ualberta.cs.minimalist.ItemModel;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.graphics.Color;


public class ItemListActivity extends Activity {
	customAdapter itemAdapter = null;
	List<Integer> positions = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		ItemListManager.initManager(this.getApplicationContext());
		Collection<ItemModel> items = ItemListManager.getItemModelList().getItems();
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
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.view_archives) {
        	return true;
        }
        return super.onOptionsItemSelected(item);
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
		


}	