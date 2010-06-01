package de.linnk.domain;






public class ModifyItem extends ItemChange  {
	
	private final Item changedItem;
	private final Item oldItem; 
	
	
	
	public Item getChangedItem() {
		return this.changedItem;
	}

	public Item getOldItem() {
		return this.oldItem;
	}

	public ModifyItem(User user, Type type, final Item changedItem, final Item oldItem) {
		super(user, type);
		this.changedItem = changedItem;
		this.oldItem = oldItem;
//		System.out.println("create modify "+this.oldItem.getCompleteID());
	}

	@Override
	public boolean doOnResource(Document doc) {
		
		return doc.changeItem(this.changedItem, this.oldItem.getCompleteID()) != null;
	}

	@Override
	public boolean undoOnResource(Document doc) {
		return doc.changeItem(this.oldItem, this.changedItem.getCompleteID()) != null;
	}

	

}
