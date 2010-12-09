package de.linnk.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import mx.gwtutils.MxroGWTUtils;

import de.linnk.gwt.LinnkGWTUtils;
import de.linnk.nx.CompositeNode;
import de.mxro.utils.distributedtree.ChangedLink;
import de.mxro.utils.domain.URIResource;
import de.mxro.utils.drm.Change;
import de.mxro.utils.log.UserError;



/*@XStreamImplicitCollection(value="items",item="item")*/
public abstract class Document implements URIResource, CompositeNode<Item> {
	protected final User creator;
	protected final Date created;
	protected final List<Item> items;
	
	protected Versions versions;
	/**
	 * Unique identifier for document
	 */
	protected String uri="";
	
	
	
	/**
	 * the folder that the document is contained in
	 */
	
	
	protected final String name;
	protected String filename;
	
	protected transient boolean altered;
	
	@Override
	public List<Item> getNodes() {
		return this.getItems();
	}
	
	@Override
	public Object getOwnerNode() {
		return null;
	}
	@Override
	public void addNode(Item n) {
		this.appendItem(n);
	}
	@Override
	public void removeNode(Item n) {
		this.removeItem(n);
	}
	
	public final boolean isAltered() {
		return this.altered;
	}
	
	/**
	 * set altered to true
	 *
	 */
	public void touch() {
		this.altered = true;
	}
	
	
	public void nukeVersions() {
		this.versions = Versions.newInstance();
	}
	
	
	
	/**
	 * returns the name of the file that the document
	 * is stored in without the path of the containing folder
	 * eg 'Test.xml'
	 * @return
	 */
	public String getFilename() {
		return this.filename;
	}
	
	

	public void setFilename(String filename) {
		this.filename = filename;
	}

	private Item getItemById(String id) {
		for (final Item i : this.getItems() ) {
			if (i.getId().equals(id))
				return i;
		}
		return null;
	}
	
	/**
	 * get an item that is directly a child of the document
	 * or inside a proxy. Adress it with Item Name or 
	 * ProxyName1/ProxyName2/.../ItemName
	 * 
	 * @param completeID
	 * @return
	 */
	public Item getItem(String completeID) {
		if (completeID == null)
			// UserError.singelton.log(this, "getItem: completeID should't be null!", UserError.Priority.HIGH);
			return null;;
		final String[] parts = completeID.split(LinnkConstants.ITEM_PATH_SEPARATOR);
		if (parts.length == 0) return null;
		
		final Item item = this.getItemById(parts[0]);
		if (item == null)
			return null;
		if (parts.length == 1)
			return item;
		if (!(item instanceof ProxyItem))
			return null;
		return ((ProxyItem) item).getItem(mx.gwtutils.MxroGWTUtils.removeFirstElement(completeID, LinnkConstants.ITEM_PATH_SEPARATOR));
	}
	
	public List<Item> getItems() {
		return this.items;
	}
	
	/**
	 * get ALL items of a certain type, also those who are below a proxy
	 * @param clazz
	 * @return
	 */
	public final Vector<Item> getItems(Class<? extends Item> clazz) {
		final Vector<Item> res = new Vector<Item>();
		for (final Item i : this.getItems()) {
			final Item itm = i.getItem(clazz);
			if (itm != null) {
				res.add(itm);
			}
		}
		return res;
	}
	
	

	public String getName() {
		return this.name;
	}

	protected Document(final User creator, 
			           final String name) {
		super();
		this.creator = User.newInstance(creator); // this is a trick to aviod that xstream uses references
		this.created = (Date) new Date().clone();
			  // this is a trick to aviod that xstream uses references
		
		this.name = name;
		
		this.items = new Vector<Item>();
		this.versions = Versions.newInstance();
		this.altered = false;
		
	}
	
	
	
	private static boolean hasItemHelper(Item root, Item item) {
		if (root == item) return true;
		//if (root.equals(item)) return true;
		if (!(root instanceof ProxyItem)) return false;
		return hasItemHelper(((ProxyItem) root).getItem(), item);
	}
	
	private static Item getRootItemHelper(Document doc, Item item) {
		for (final Item i : doc.getItems()) {
			if (i == item) return i;
			if (hasItemHelper(i, item))
				return i;
		}
		return null;
	}
	
	public Item getRootItem(Item item) {
		
		return getRootItemHelper(this, item);
	}
	
	/* only access from current package !!! */
	public boolean appendItem(Item item) {
		if (this.getItem(item.getId()) != null) {
			UserError.singelton.log(this, "appendItem: Insertion of item with not unique id: "+item.getId(), UserError.Priority.HIGH);
		}
		item.setDocument(this);
		return this.items.add(item);
	}
	
	public boolean removeItem(String itemID) {
		final Item item = this.getItem(itemID);
		if ( item == null) {
			UserError.singelton.log(this, "removeItem: could not find item to delete "+itemID, UserError.Priority.NORMAL);
			return false;
		}
		return this.removeItem(item);
	}

	private boolean removeItem(Item item) {
		this.removeDependendItemsFor(item.getCreator(), item);
		
		if ( !this.items.remove(item))
			return this.items.remove(this.getItem(item.getId()));
		return true;
	}
	
	
	public boolean insertItem(Item item, String relativeItemID, InsertItem.Position position) {
		final Item relativeItem = this.getItem(relativeItemID);
		return this.insertItem(item, relativeItem, position);
	}
	
	private boolean insertItem(Item item, Item relativeItem, InsertItem.Position position) {
		final int relativeIndex = this.items.indexOf(relativeItem);
		int newIndex;
		if (relativeIndex == -1) {
			de.mxro.utils.log.UserError.singelton.log("Document.insertItem: relative Item could not be found!", UserError.Priority.HIGH);
			return false;
		}
		if (position.equals(InsertItem.Position.AFTER)) {
			newIndex = relativeIndex+1;
		} else {
			newIndex = relativeIndex;
		}
		if (this.getItem(item.getId()) != null) {
			UserError.singelton.log(this, "insertItem: Insertion of item with not unique id: "+item.getId(), UserError.Priority.HIGH);
		}
		
		this.items.add(newIndex, item);
		item.setDocument(this);
		return true;
	}
	
	
	public Item changeItem(Item changedItem, String oldItemID) {
		final Item oldItem = this.getItem(oldItemID);
		if ( oldItem == null ) {
			UserError.singelton.log(this, "changItem: Cannot find old item: "+oldItemID, UserError.Priority.NORMAL);
			return null;
		}
		return this.changeItem(changedItem, oldItem);
	}
	
	private Item changeItem(Item item, Item olditem) {
		if (olditem == null)
			throw new IllegalArgumentException("Document.changeItem: oldItem is null");
		// try to find item:
		int olditemidx = this.items.indexOf(olditem);
		if (olditemidx < 0) {
			final Item changedItem = this.getItem(olditem.getCompleteID());
			olditemidx = this.items.indexOf(changedItem);
		}
		if (olditemidx < 0) {
			UserError.singelton.log(this, "changItem: Cannot find old item: "+olditem.getId(), UserError.Priority.HIGH);
			return null;
			//throw new IndexOutOfBoundsException();
		}
		
		//olditem.setDocument(null); item.setDocument(this); 
		
		return this.items.set(olditemidx, item);
	}
	
	public boolean exchangeItems(String itemID1, String itemID2) {
		if ( itemID1 == null || itemID2 == null ) return false;
		final Item item1 = this.getItem(itemID1);
		final Item item2 = this.getItem(itemID2);
		if ( item1 == null || item2 == null) {
			UserError.singelton.log(this, "exchangeItems: one of the items "+itemID1+", "+itemID2+" not found!", UserError.Priority.NORMAL);
			return false;
		}
		return this.exchangeItems(item1, item2);
	}
	
	private boolean exchangeItems(Item item1, Item item2) {
		final int index1 = this.getItems().indexOf(item1);
		final int index2 = this.getItems().indexOf(item2);
		return this.getItems().set(index1, item2)!=null &&
		       this.getItems().set(index2, item1)!=null;
	}
	
	public List<Item> getDependendOn(Item item) {
		final List<Item> res = new Vector<Item>();
		for (final Item i : this.items) {
			final List<DependsOnItemProxy> proxies = LinnkGWTUtils.getProxies(DependsOnItemProxy.class, i);
			for (final ProxyItem proxy : proxies) {
				if ( ((DependsOnItemProxy) proxy).getOnItem() != null) {
					if (((DependsOnItemProxy) proxy).getOnItem().equals(item)) {
						res.add(this.getRootItem(i));
					}
				} 
			}
		}
		return res;
	}
	
	public Item getPreviousItem(Item item) {
		final int idx = this.getItems().indexOf(item);
		if (idx > 0)
			return this.getItems().get(idx-1);
		else
			return null;
	}
	
	public boolean removeDependendItemsFor(User user, Item item) {
		final List<ItemChange> changes = new Vector<ItemChange>();
		for (final Item i : this.getDependendOn(item)) {
			
			changes.add(ItemChange.newRemoveItem(this.getRootItem(i), user, Change.Type.IMPLICIT));
				
		}
		for (final ItemChange change : changes) {
			this.doChange(change);
		}
		return true;
	}
	

	public Date getCreated() {
		return this.created;
	}

	public User getCreator() {
		return this.creator;
	}
	
	

	public abstract boolean doChange(ItemChange change);
	
	public abstract boolean undoChange(ItemChange change);
	
	public String getUniqueItemName(String base) {
		String newName=base;
		int counter=0;
		while (this.getItemById(newName) != null) {
			counter++;
			newName = base + counter;
		}
		return newName;
	}
	
	
	
	public boolean updateItems() {
		Vector<Item> lItems = new Vector<Item>();
		lItems.addAll(items);
		for (Item i : lItems) {
			if ( i instanceof Updatable) {
				int oldidx = this.items.indexOf(i);
				this.items.remove(oldidx);
				Item updated = (Item) ((Updatable) i).update();
				if (updated != null)
				  this.items.add(oldidx, updated);
				this.touch();
			}
		}
		return true;
	}

	public Versions getVersions() {
		if (this.versions == null) {
			this.versions = new Versions();
		}
		return this.versions;
	}
	
	
	public static String getSimpleName(String forName) {
		return MxroGWTUtils.getSimpleName(forName);
	}
	
	public String getSimpleName() {
		return getSimpleName(this.getName());
		
	}
	
	public void setOwner(User user, String link) {
		
		ItemChange ic=null;
		for (final Item i : this.getItems()) {
			if (i instanceof OwnerItem) {
				if (link == null) {
					final ItemChange deleteItem = ItemChange.newRemoveItem(i, user);
					ic = deleteItem;
					
				} else {
					final ItemChange modifyItem = ItemChange.newModifyItem(new OwnerItem(user, i.getId(), this, link), i, user, Change.Type.SKIP);
					ic = modifyItem;
				}
				
			}
		}
		if (ic != null) {
			this.doChange(ic);
			return;
		}
		if (link == null)
			return;
			// if nothing was found
		
		final ItemChange newItem = ItemChange.newNewItem(new OwnerItem(user, this.getUniqueItemName("linkedby"), this, link), user, Change.Type.SKIP);
		this.doChange(newItem);
	}
	
	public String getOwnerLink() {
		for (final Item i : this.getItems()) {
			if (i instanceof OwnerItem)
				return ((OwnerItem) i).backlink;
		}
		return null;
	}
	
	/**
	 * For update purposes
	 * @param versions
	 */
	@Deprecated
	public void setVersions(Versions versions) {
		this.versions = versions;
	}

	/**
	 * returns an unique URI address for this document
	 */
	/*@Uri*/ 
	public String getUniqueURI() {
		return uri;
	}
	
	public void setUniqueURI(String uri) {
		if (uri == null) { this.uri = ""; return; }
		this.uri = uri;
	}

	

	

	public boolean updateLinks(Vector<ChangedLink> changedLinks) {
		
		return false;
	}

	
	
	
	
}
