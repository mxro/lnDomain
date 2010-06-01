package de.linnk.gwt;

import java.util.Vector;

import de.linnk.domain.Item;
import de.linnk.domain.ProxyItem;



public class LinnkGWTUtils {
	
	
	public static boolean isSuperclass(Class<? extends Object> superclass, Class<? extends Object> clazz) {
		if (superclass.equals(clazz)) return true;
		
		// all classes are superlcass of Object
		if (clazz.equals(Object.class)) return false;
		
		return isSuperclass(superclass, clazz.getSuperclass());
	}
	
	/**
	 * necessary for GWT as Class.isInstance is not supported
	 * @param clazz
	 * @param object
	 * @return
	 */
	public static boolean instanceOf(Class<? extends Object> clazz, Object object) {
		return isSuperclass(clazz, object.getClass());
		
	}
	
	/**
	 * goes through all the items in the list and returns
	 * all proxy items that are equal to the class or
	 * a superclass
	 * @param forItem TODO
	 */
	public static <PI extends ProxyItem> Vector<PI> getProxies(Class<PI> proxy, Item forItem) {
		if (forItem instanceof ProxyItem) {
			final Vector<PI> proxies = new Vector<PI>();
			
			if (instanceOf(proxy, forItem)) {
			  proxies.add((PI) forItem);
			}
			proxies.addAll(LinnkGWTUtils.getProxies(proxy, ((ProxyItem) forItem).getItem()));
			return proxies;
		}
		if (forItem instanceof Item) {
			return new Vector<PI>();
		}
		return null;
	}

}
