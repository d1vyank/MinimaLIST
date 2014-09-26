package ca.ualberta.cs.minimalist;

import java.util.Date;

// Some code borrowed from LonelyTweetModel by Joshua Charles Campbell 
// (https://github.com/joshua2ua/lonelyTwitter)

public class ItemModel {
	private String text;
	private boolean checked;
	private Date timestamp;
	private boolean selected;
	
	public ItemModel(String text, boolean checked) {
		super();
		this.text = text;
		this.checked = checked;
		timestamp = new Date();
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getText() {
		return text;
	}
	public void checkItem(boolean checked) {
		this.checked = checked;
	}
	public void selectItem(boolean sel) {
		this.selected=sel;
	}
	public boolean isSelected() {
		return selected;
	}
	public boolean isChecked() {
		return checked;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String toString() {
		return new String(timestamp.toString() + " | " + text);
	}
}
