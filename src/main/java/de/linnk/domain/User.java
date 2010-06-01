package de.linnk.domain;




/*@XStreamConverter(UserConverter.class)
@XStreamAlias("v01.user")*/
public class User  {
	
	private final String uri;
	public String getURI() {
		
		return this.uri;
	}

	public User(final String uri) {
		super();
		this.uri = uri;
		
	}
	
	public static User newInstance(final User user) {
		return new User(user.getURI());
	}

	
	
}
