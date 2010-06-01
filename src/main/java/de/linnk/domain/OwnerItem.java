package de.linnk.domain;


import de.mxro.utils.domain.Styled;


public class OwnerItem extends Item implements Styled, AlwaysPublish {
	
	//public final SimpleLink backlink;
	
	public final String backlink;
	
	public OwnerItem(User creator,  String id, Document document, String backlink) {
		super(creator, id, document);
		this.backlink = backlink;
	}

	
	
}
