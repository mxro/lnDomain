package de.linnk.domain;

import java.util.List;
import java.util.Vector;

import de.linnk.gwt.LinnkGWTUtils;
import de.mxro.utils.domain.Styled;

public abstract class ProxyItem extends Item implements Styled, Copyable {
	
	public Item item;
	
	public ProxyItem(User creator,  String id,
			Document document, Item item) {
		super(creator,  id, document);
		this.item = item;
	}

	public Item getItem() {
		return this.item;
	}

	@Override
	public List<Object> getNodes() {	
		Vector<Object> v = new Vector<Object>();
		v.add(this.getItem());
		return v;
	}
	
	public Item getItem(String id) {
		final String[] parts = id.split(LinnkConstants.ITEM_PATH_SEPARATOR);
		if (parts.length == 0)
			return null;
		if (!(this.getItem().getId().equals(parts[0])))
			return null;
		if (parts.length == 1)
			return this.getItem();
		if (!(this.getItem() instanceof ProxyItem))
			return null;
		return ((ProxyItem) this.getItem()).getItem(mx.gwtutils.MxroGWTUtils.removeFirstElement(id, LinnkConstants.ITEM_PATH_SEPARATOR));
		
	}
	
	public void afterInsert() {
		
	}

	
	public void afterToString() {
		if (this.item instanceof Copyable) {
			((Copyable) this.item).afterToString();
		}
	}
	
	public void beforeToString() {
		if (this.item instanceof Copyable) {
			((Copyable) this.item).beforeToString();
		}	
	}

	@Override
	public void setDocument(Document document) {
		this.item.setDocument(document);
		super.setDocument(document);
	}

	public void setItem(Item item) {
		this.item = item;
	}

	
	
	
	
	@Override
	public boolean hasItem(Item i) {
		
		if (this.equals(i))
			return true;
		return this.getItem().hasItem(i); 
	}

	
	
	@Override
	public <I extends Item> I getItem(Class<I> clazz) {
		// if (clazz.isAssignableFrom(this.getClass()))
		//		this.getClass().equals(clazz)) 
		//if (clazz.isInstance(this))
		if (LinnkGWTUtils.instanceOf(clazz, this))
			return (I) this;
		return this.getItem().getItem(clazz);
	}

	/*@Override
	public boolean removeDependendItems(User user) {
		return this.item.removeDependendItems(user) & super.removeDependendItems(user);  // & is better here
			
	}*/
	
	
}
