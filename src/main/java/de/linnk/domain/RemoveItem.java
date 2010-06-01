package de.linnk.domain;








public class RemoveItem extends ItemChange  {

	final Item item;
	final String itemBefore;

	
	
	public String getItemBefore() {
		return this.itemBefore;
	}

	public Item getItem() {
		return this.item;
	}

	public RemoveItem(User user, Type type, final Item item) {
		super(user, type);
		if (item == null) throw new IllegalArgumentException("RemoveItem: Item must not be null!");
		this.item = item;
		final Item previousItem = this.item.getDocument().getPreviousItem(item);
		if ( previousItem == null) {
			this.itemBefore = null;
		} else {
			this.itemBefore = previousItem.getCompleteID();
		}
	}

	@Override
	public boolean doOnResource(Document doc) {
		//item.delete();
		//return true;
		return doc.removeItem(this.item.getCompleteID());
	}

	@Override
	public boolean undoOnResource(Document doc) {
		return doc.insertItem(this.item, this.itemBefore, InsertItem.Position.AFTER);
	}
	
	
}
