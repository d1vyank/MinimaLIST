package ca.ualberta.cs.minimalist;

import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.content.Context;


public class ItemListManager {
	
	private static ItemModelList itemModelList = null;
	Context context;
	private static final String FILENAME = "Items.sav";
	static private ItemListManager itemListManager = null;
	
	public static void initManager(Context context) {
		if (itemListManager == null) {
			if (context==null) {
				throw new RuntimeException("missing context for ItemListManager");
			}
			itemListManager = new ItemListManager(context);
		}		
	}
	
	static public ItemModelList getItemModelList() {
		if (itemModelList == null) {
			itemModelList = getManager().loadFromFile();
			System.out.println("a" + itemModelList);
			itemModelList.addListener(new Listener() {					
				@Override
				public void update() {
					getManager().saveToFile(itemModelList);
				}
			});
		}
		return itemModelList;			
	}
	
	public void addItemModel(ItemModel item) {
		getItemModelList().addItem(item);
	}
	public void addArchiveModel(ItemModel item) {
		getItemModelList().addArchive(item);
	}
	
	
	public ItemListManager(Context context) {
		this.context = context;
	}
	
	public static ItemListManager getManager() {
		if (itemListManager==null) {
			throw new RuntimeException("Did not initialize Manager");
		}
		return itemListManager;
	}
	
	protected ItemModelList loadFromFile() {
		System.out.println("HREE");
		ItemModelList imt1 = new ItemModelList();
		try {
			FileInputStream fis = context.openFileInput(FILENAME);
			InputStreamReader irs = new InputStreamReader(fis);
			// from javacreed.com gson example 09/22/2014
			Gson gson = new GsonBuilder().create();
			ItemModelList target = new ItemModelList();
			//String json = gson.toJson(target); // serializes target to Json
			ItemModelList imt = gson.fromJson(irs, ItemModelList.class);
			// deserializes json into target2
			return imt;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return imt1;			 
		//System.out.println(imt.size());
	}
	protected void saveToFile(ItemModelList imt) {
		try {
			
			FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			// from javacreed.com gson example 09/22/2014
			Gson gson = new GsonBuilder().create();
			gson.toJson(imt, osw);
			osw.flush();
			osw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	protected void archiveMany(ArrayList<ItemModel> items) {
		for (ItemModel i : items) {
			addArchiveModel(i); 
		}
	}
	protected void unarchiveMany(ArrayList<ItemModel> items) {
		for (ItemModel i : items) {
			addItemModel(i);
		}
	}
}
