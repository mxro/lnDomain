package de.linnk.domain;

import java.util.Date;

import de.mxro.utils.drm.Change;




public abstract class ItemChange implements Change<Document> {
	
	//private Boolean reversible=true;
	//public boolean implicit=false;
	
	
	
	public Type type;
	
	protected ItemChange(final User user, final Type type) {
		super();
		
		this.user = User.newInstance(user); // this is a trick to aviod that xstream uses references
		this.date = new Date();
		this.type = type;
		
	}


	private final User user;
	private final Date date;
	

	public User getUser() {
		return this.user;
	}
	
	public Date getDate() {
		return this.date;
	}

	/* (non-Javadoc)
	 * @see de.linnk.gwt.Change#doOnResource(de.linnk.gwt.Document)
	 */
	public abstract boolean doOnResource(de.linnk.domain.Document doc);
	/* (non-Javadoc)
	 * @see de.linnk.gwt.Change#undoOnResource(de.linnk.gwt.Document)
	 */
	public abstract boolean undoOnResource(de.linnk.domain.Document doc);
	
	public final Type getType() {
		return this.type;
	}

	
	
	public static NewItem newNewItem( final Item item, final User user, final Type type) {
		return new NewItem(item, user, type);
	}
	
	public static ModifyItem newModifyItem( final Item item, final Item olditem, final User user, final Type type) {
		return new ModifyItem(user, type, item, olditem);
	}
	
	public static RemoveItem newRemoveItem( final Item item, final User user, final Type type) {
		
		return new RemoveItem(user, type, item);
	}
	
	public static ItemChange newExchangeItems(Item item,  final Item item2, User user, final Type type) {
		return new ExchangeItems( user, type, item.getCompleteID(), item2.getCompleteID());
	}
	
	public static ItemChange newInsertItem(Item item, final Item relativeItem, final InsertItem.Position position, final User user, final Type type) {
		return new InsertItem(  user, type, item, relativeItem.getCompleteID(), position);
	}
	
	public static NewItem newNewItem( final Item item, final User user) {
		return newNewItem(item, user, Type.REVERSIBLE);
	}
	
	public static ModifyItem newModifyItem( final Item item, final Item olditem, final User user) {
		return newModifyItem(item, olditem, user, Type.REVERSIBLE);
	}
	
	public static RemoveItem newRemoveItem( final Item item, final User user) {
		return newRemoveItem(item, user, Type.REVERSIBLE);
	}
	
	public static ItemChange newExchangeItems(Item item,  final Item item2, User user) {
		return newExchangeItems(item, item2, user, Type.REVERSIBLE);
	}
	
	
	
}
