package de.linnk.domain;

import de.mxro.utils.URI;
import de.mxro.utils.log.UserError;



public class SimpleLink {
	
	public final String link;

	public SimpleLink(final String link) {
		super();
		this.link = link.substring(0);
	}
	
	public SimpleLink(final SimpleLink link) {
		this(link.link);
	}
	
	public URI toURI() {
		try {
			return new de.mxro.utils.URIImpl(this.link);
		} catch (final java.net.URISyntaxException e) {
			UserError.singelton.log(this, "Illegal path in SimpleLink '"+this.link+"'", UserError.Priority.LOW);
		}
		return null;
	}
	

	
	public static SimpleLink fromURI(URI elementURI) {
		return new SimpleLink(elementURI.toString());
	}
//	
//	public static SimpleLink fromURIs(URI source, URI destination) {
//		//UserError.singelton.log("fromURIS");
//		//UserError.singelton.log(source.toString());
//		//UserError.singelton.log(destination.toString());
//		final URI relative = source.relativize(destination);
//		//UserError.singelton.log(relative.toString());
//		//UserError.singelton.log(source.resolve(relative).toString());
//		return fromURI(relative);
//	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final SimpleLink other = (SimpleLink) obj;
		if (this.link == null) {
			if (other.link != null)
				return false;
		} else if (!this.link.equals(other.link))
			return false;
		return true;
	}

	
	
	
}
