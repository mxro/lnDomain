package de.linnk.domain;



import de.linnk.nx.ContentNode;
import de.mxro.utils.domain.Styled;




public final class TextItem extends Item implements Styled, EasyEditItem,
		Copyable, ContentNode<String> {

	private final String text;
   
	@SuppressWarnings("unused")
	private String textXML;

	@SuppressWarnings("unused")
	private transient String onlyText = null;

	public TextItem(User creator, String id, Document document,
			final String text) {
		super(creator, id, document);
		this.text = text;
		this.textXML = text;
	}

	/**
	 * text including html elements
	 * 
	 * @return
	 */
	public final String getTextData() {
		return this.text;
	}


	/**
	 * use this only to prepare the streaming of this item
	 * 
	 * necessary that HTML output works correctly
	 * 
	 * @param xml
	 */
	public final void setTextXML(String xml) {
		this.textXML = xml;
	}

	public void afterInsert() {

	}

	public void afterToString() {

	}

	public void beforeToString() {

	}

	@Override
	public String getContent() {
		
		return text;
	}

	

	

}
