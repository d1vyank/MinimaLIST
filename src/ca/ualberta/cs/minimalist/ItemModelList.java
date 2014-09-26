package ca.ualberta.cs.minimalist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/*
 * Code modeled after StudentPicker by Dr. Abram Hindle
 */

public class ItemModelList {
	protected ArrayList<ItemModel> items = null;
	protected transient ArrayList<Listener> listeners = null;
	public ItemModelList() {
		items = new ArrayList<ItemModel>();		
		listeners = new ArrayList<Listener>();
	}
	
	private ArrayList<Listener> getListeners() {
		if (listeners == null ) {
			listeners = new ArrayList<Listener>();
		}
		return listeners;
	}
	public Collection<ItemModel> getItems() {
		return items;		
	}
	public ItemModelList getSelectItems(List<Integer> positions) {
		ItemModelList it = new ItemModelList();
		for (int i : positions)
			it.addItem(items.get(i));
		return it;
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
	
	public void clearSelections() {
		for(ItemModel i:items) {
			i.selectItem(false);
		}
	}
	public void selectItem(int index) {
		ItemModel im=items.get(index);
		im.selectItem(true);
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
	
	public void deleteSelectedItems(List<Integer> positions) {
		ArrayList<ItemModel> it = new ArrayList<ItemModel>();
		for (int i : positions)
			it.add(items.get(i));
		items.removeAll(it);
		notifyListeners();

	}
	
	
	public int size() {
		return items.size();
	}


	public void addListener(Listener l) {
		getListeners().add(l);
	}

	public void removeListener(Listener l) {
		getListeners().remove(l);
	}
	
}
