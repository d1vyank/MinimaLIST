package ca.ualberta.cs.minimalist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/*
 * Code modeled after StudentPicker by Dr. Abram Hindle
 */

public class ItemModelList {
	protected ArrayList<ItemModel> items = null;
	protected ArrayList<ItemModel> archives = null;
	protected transient ArrayList<Listener> listeners = null;
	public ItemModelList() {
		items = new ArrayList<ItemModel>();	
		archives = new ArrayList<ItemModel>();
		listeners = new ArrayList<Listener>();
	}
	
	private ArrayList<Listener> getListeners() {
		if (listeners == null ) {
			listeners = new ArrayList<Listener>();
		}
		return listeners;
	}
	public Collection<ItemModel> getItems() {
		System.out.println("getItenms: "+items);
		return items;		
	}
	public Collection<ItemModel> getArchives() {
		return archives;
	}
	public ArrayList<ItemModel> getSelectItems(List<Integer> positions) {
		if (positions == null)
			return items;
		ArrayList<ItemModel> it = new ArrayList<ItemModel>();
		for (int i : positions) {
			try{	
				it.add(items.get(i));
			}
			catch (IndexOutOfBoundsException iob) {
				iob.printStackTrace();
			}
		}	
		return it;
	}
	public ArrayList<ItemModel> getSelectArchives(List<Integer> positions) {
		ArrayList<ItemModel> it = new ArrayList<ItemModel>();
		for (int i : positions)
			it.add(archives.get(i));
		return it;
	}
	public void addArchive(ItemModel newItem) {
		archives.add(newItem);
		notifyListeners();
	}
	
	public void addItem(ItemModel newItem) {
		items.add(newItem);
		notifyListeners();
	}
	public void checkItem(ItemModel item) {
		if (items != null) {
			for (ItemModel i : items) {
				if (i.getTimestamp().equals(item.getTimestamp())) {
					i.checkItem(true);
					notifyListeners();
				}	
			}
		}
	}
	
	public void uncheckItem(ItemModel item) {
		if (items != null) {
			for (ItemModel i : items) {
				if (i.getTimestamp().equals(item.getTimestamp())) {
					i.checkItem(false);
					notifyListeners();
				}	
			}
		}
	}
	

	
	private void notifyListeners() {
		for (Listener  listener : getListeners()) {
			listener.update();
		}
	}
	
	public void removeItem(ItemModel delItem) {
		items.remove(delItem);
		notifyListeners();
	}
	
	public void removeArchive(ItemModel delItem) {
		archives.remove(delItem);
		notifyListeners();
	}
	
	public void deleteSelectedItems(List<Integer> positions) {
		ArrayList<ItemModel> it = new ArrayList<ItemModel>();
		for (int i : positions)
			it.add(items.get(i));
		items.removeAll(it);
		notifyListeners();

	}
	public void deleteAllItems() {
		items.clear();
		notifyListeners();
	}
	public void deleteAllArchives() {
		archives.clear();
		notifyListeners();
	}
	public void deleteSelectedArchives(List<Integer> positions) {
		ArrayList<ItemModel> it = new ArrayList<ItemModel>();
		for (int i: positions) {
			it.add(archives.get(i));
		}
		archives.removeAll(it);
		notifyListeners();
	}
	
	public int checkedSize() {
		int count = 0;
		for (ItemModel i : items) {
			if(i.isChecked()==true)
				count++;
		}
		return count;
	}
	public int uncheckedSize() {
		return (items.size() - checkedSize());
	}
	public int checkedArchiveSize() {
		int count = 0;
		for (ItemModel i : archives) {
			if(i.isChecked()==true)
				count++;
		}
		return count;
	}
	public int uncheckedArchiveSize() {
		return (archives.size() - checkedArchiveSize());
	}

	public void addListener(Listener l) {
		getListeners().add(l);
	}

	public void removeListener(Listener l) {
		getListeners().remove(l);
	}
	
}
