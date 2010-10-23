package de.linnk.domain;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import de.linnk.gwt.LinnkGWTUtils;
import de.linnk.nx.CompositeNode;




public abstract class Item implements CompositeNode<Object> {
	protected final User creator;
	protected final Date created;
	protected String id;
	protected transient Document document;
	protected String completeID;
	
	
	@Override
	public List<Object> getNodes() {	
		return new Vector<Object>();
	}
	
	private Item findRootItemHelper(Item i) {
		if (i == this) return i;
		if (i instanceof ProxyItem) return this.findRootItemHelper(((ProxyItem) i).getItem());
		return null;
	}
	
	@Override
	public Object getOwnerNode() {
		if (this.getDocument().getRootItem(this) == this) return this.getDocument();
		return findRootItemHelper(this.getDocument().getRootItem(this));
	}
	
	@Override
	public void addNode(Object n){
		
	}
	@Override
	public void removeNode(Object n) {
		
	}
	
	
	
	public Item(final User creator, 
			   final String id,
			   final Document document) {
		super();
		//this.nodeManager = new CompositeNodeManager();
		
		this.creator = User.newInstance(creator); // this is a trick to aviod that xstream uses references
		this.created = (Date) new Date().clone();  // this is a trick to aviod that xstream uses reference
		this.id = id;
		this.document = document;
	}

	public Date getCreated() {
		return this.created;
	}

	public User getCreator() {
		return this.creator;
	}
	
	
	public String getId() {
		if (this.id == null) this.id = "";
		return this.id;
	}
	
	private static ProxyItem getProxy(ProxyItem root, Item i) {
		if (root.getItem() == i) return root;
		//if (root.getItem().equals(i)) return root;
		if (!(root.getItem() instanceof ProxyItem)) {
			
			return null;
		}
		return getProxy((ProxyItem) root.getItem(), i);
	}
	
	private static ProxyItem getProxy(Item i) {
		final Item rootItem = i.getDocument().getRootItem(i);
		if (i == rootItem) return null;
		if (!(rootItem instanceof ProxyItem)) {
			
			return null;
		}
		return getProxy((ProxyItem) rootItem, i);
		
	}
	
	private String computeCompleteID() {
		if (this.getDocument() == null) return null;
		final Item root = this.getDocument().getRootItem(this);
		if (root == null) return null;
		
		if (root == this) return this.getId();
		
		
		return getProxy(this).getCompleteID()+LinnkConstants.ITEM_PATH_SEPARATOR+this.getId();
	}
	
	/**
	 * get the id including the ids of the owning proxies
	 * @return
	 */
	public String getCompleteID() {
		final String computedID = this.computeCompleteID();
		if ( computedID != null) {
			this.completeID = computedID;
		}
		return this.completeID;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	

	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof Item))
			return false;
		
		if (obj == this) return true;
		
		return false;
	}


	public Document getDocument() {
		return this.document;
	}

	
	public void setDocument(Document document) {
		this.document = document;
		if (this.document != null) {
			// generate completeID, if possible 
			this.getCompleteID();
		}
	}
	
	
	/*public static PrimaryLinkedByItem newPrimaryLinkdedByItem(final User creator, 
			final Date created,  
			final Document document,
			final SimpleLink link) {
		return new PrimaryLinkedByItem(creator, created,  document, link);
	}*/
	
	public boolean hasItem(Item i) {
		return this.equals(i);
	}
	
	// TODO should return a vector ...
	/**
	 * an item can be an item or a proxy item
	 * this function traverses the list and returns
	 * the first item with a matching type
	 */
	public <I extends Item> I getItem(Class<I> clazz) {
		if (LinnkGWTUtils.instanceOf(clazz, this)) return (I) this;
		
		return null;
	}
	
	/*public boolean removeDependendItems(User user) {
		return this.getDocument().removeDependendItemsFor(user, this);
	}*/

	
	public final void afterFromString(Document newDocument) {
		this.setDocument(newDocument);
		this.setId(newDocument.getUniqueItemName(this.getId()));
		if (this instanceof Copyable) {
			((Copyable) this).afterInsert();
			((Copyable) this).afterToString();
		}
	}
	
	
	
	
}
