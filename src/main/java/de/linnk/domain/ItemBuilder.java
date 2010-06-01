package de.linnk.domain;


import de.mxro.utils.drm.Change;






public class ItemBuilder {
	
	protected final Document document;
	protected final User user;
	
	
	protected Document getDocument() {
		return this.document;
	}

	public ItemBuilder(final Document document, final User user) {
		super();
		this.document = document;
		this.user = user;
	}
	
	public ItemChange newChange(Item item) {
		return ItemChange.newNewItem(item, this.user);
	}
	
	
	
	
	

	
	/*public Item newLinnkProxy(SimpleLink link, String title) {
		Item textItem = this.newTextItem(title);
		return new LinnkProxy(this.user, this.document.getUniqueItemName("LinnkProxy"), this.document, textItem, link);
	}*/
	
	
	
	/**
	 * automatically add <html> ...
	 * @param text
	 * @return
	 */
	public Item newTextItem(String text) {
		return new TextItem(this.user, this.document.getUniqueItemName("TextItem"), this.document, "<html><body>"+text+"<br></body></html>");
	}
	
	/**
	 * create from raw text data
	 * @param text
	 * @return
	 */
	public Item newTextItemFromTextData(String text) {
		return new TextItem(this.user, this.document.getUniqueItemName("TextItem"), this.document, text);
	}
	
	
	
	/*public Item newJPEGPictureItem(BufferedImage image) {
		return new JPEGPictureItem(user, document.getUniqueItemName("ImageItem"), document, image);
	}*/
	
	
	
	
	
	
	
	
	
	
	
}
