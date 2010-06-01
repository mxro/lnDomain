package de.linnk.domain;



public class DependsOnItemProxy extends ProxyItem {
	
	private transient Item onItem;
	private String onItemId;
	
	public Item getOnItem() {
		return this.onItem;
	}

	@Override
	public void afterToString() {
		super.afterToString();
		this.onItem = this.getDocument().getItem(this.onItemId);
	}




	@Override
	public void beforeToString() {
		super.beforeToString();
		this.onItemId =  this.getDocument().getRootItem(this.onItem).getId();
	}




	public DependsOnItemProxy(User creator, String id, Document document, Item item, final Item onItem) {
		super(creator, id, document, item);
		this.onItem = onItem;
	}
	
}
