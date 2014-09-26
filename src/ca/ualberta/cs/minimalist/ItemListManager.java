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
		ItemModelList imt= new ItemModelList();
		ArrayList<ItemModel> items = null;
		try {
			
			FileInputStream fis = context.openFileInput(FILENAME);
			InputStreamReader irs = new InputStreamReader(fis);
			// from javacreed.com gson example 09/22/2014
			Gson gson = new GsonBuilder().create();
			items = gson.fromJson(irs, new TypeToken<ArrayList<ItemModel>>() {}.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (items != null) {
			for(ItemModel item : items) {
				imt.addItem(item);
			}
		}	
		return imt;
	}
	protected void saveToFile(ItemModelList imt) {
		try {
			
			FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			// from javacreed.com gson example 09/22/2014
			Gson gson = new GsonBuilder().create();
			gson.toJson(imt.getItems(), osw);
			osw.flush();
			osw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
