package de.linnk.domain;




public class TitleItem extends Item implements de.mxro.utils.domain.Styled, EasyEditItem, AlwaysPublish {
	
	protected String title;
	
	public TitleItem(User creator, String id, Document document, String title) {
		super(creator, id, document);
		this.title = title;
	}

	public String getTitle() {
		if ( this.title == null) this.title = "";
		return this.title;
	}

	
}
