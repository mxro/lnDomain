package de.linnk.domain;

import java.util.Vector;

import de.mxro.utils.drm.Change;


/*@XStreamImplicitCollection(value="changes",item="change")
@XStreamAlias("v04.versions")*/
public class Versions {
	private Integer currentVersion;
	private Vector<ItemChange> changes;
	
	
	private final Vector<ItemChange> getChanges() {
		if (this.changes == null) {
			this.changes = new Vector<ItemChange>();
		}
		return this.changes;
	}

	public Versions() {
		super();
		this.currentVersion = 0;
		this.changes = new Vector<ItemChange>();
	}

	public Integer getNumOfVersions() { return this.changes.size(); }
	
	public int getCurrentVersion() {
		return this.currentVersion;
	}
	
	
	public void addChange(ItemChange change) {
		this.getChanges().add(change);
		this.currentVersion++;
	}
	
	public Change undo() {
		if (!this.getChanges().get(this.currentVersion-1).getType().equals( Change.Type.IRREVERSIBLE))
			throw new de.linnk.domain.NotReversibleException();
		
		this.currentVersion--;
		return this.getChanges().get(this.currentVersion);
	}
	
	public Change redo() {
		if (this.currentVersion==this.getNumOfVersions())
			throw new de.linnk.domain.NoFurtherRedoPossibleException();
		
		this.currentVersion++;
		return this.getChanges().get(this.currentVersion-1);
	}
	
	public static Versions newInstance() {
		return new Versions();
	}
}
