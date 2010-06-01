package de.linnk.domain;




public class InsertItem extends ItemChange  {

	private final String relativeItemID;
	private final Item item;
	private final Position position;
	
	
	
	public static enum Position {
		BEFORE,
		AFTER
	}
	
	@Override
	public boolean doOnResource(Document doc) {
		
		doc.insertItem(this.item, this.relativeItemID, this.position);
		return true;
	}

	@Override
	public boolean undoOnResource(Document doc) {
		return doc.removeItem(this.item.getCompleteID());
	}

	

	public InsertItem(User user, Type type, final Item item, final String relativeItemId, final Position position) {
		super(user, type);
		this.relativeItemID = relativeItemId;
		this.item = item;
		this.position = position;
	}

	public Position getPosition() {
		return this.position;
	}

	public String getRelativeItemID() {
		return this.relativeItemID;
	}


	public Item getItem() {
		return this.item;
	}

	
	
	
}
