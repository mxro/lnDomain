package de.linnk.domain;





public class ExchangeItems extends ItemChange  {
	
	private final String itemId2;
	private final String itemId1;
	
	

	public String getItemId1() {
		return this.itemId1;
	}

	public String getItemId2() {
		return this.itemId2;
	}

	public ExchangeItems(User user, Type type, final String itemId2, final String itemId1) {
		super(user, type);
		this.itemId2 = itemId2;
		this.itemId1 = itemId1;
	}

	@Override
	public boolean doOnResource(Document doc) {
		return doc.exchangeItems(this.itemId1, this.itemId2);
	}

	@Override
	public boolean undoOnResource(Document doc) {
		return doc.exchangeItems(this.itemId2, this.itemId1);
	}

	
	
	

}
