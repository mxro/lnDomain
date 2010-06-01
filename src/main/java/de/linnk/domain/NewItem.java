package de.linnk.domain;




public class NewItem extends ItemChange  {

	final Item item;
	
	public Item getItem() {
		return this.item;
	}

	public NewItem( Item item, User user, Type type) {
		super( user, type);
		this.item = item;
		// de.mxro.UserError.singelton.log("created new item"+this.getItem().getId());
	}
	
	@Override
	public boolean doOnResource(Document doc) {
		return doc.appendItem(this.item);
	}
	
	@Override
	public boolean undoOnResource(Document doc) {
		
		return doc.removeItem(this.item.getCompleteID());
	}

	
	
	
}
