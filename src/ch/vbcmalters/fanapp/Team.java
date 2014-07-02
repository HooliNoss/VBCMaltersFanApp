package ch.vbcmalters.fanapp;

public class Team 
{
	private String _Name;
	private int _Team_ID;
	private int _Group_ID;
	
	
	public Team()
	{
		
		
	}
	public Team(String name, int teamID, int groupID)
	{
		set_Name(name);
		set_Team_ID(teamID);
		set_Group_ID(groupID);
	}


	public String get_Name() {
		return _Name;
	}


	public void set_Name(String _Name) {
		this._Name = _Name;
	}


	public int get_Team_ID() {
		return _Team_ID;
	}


	public void set_Team_ID(int _Team_ID) {
		this._Team_ID = _Team_ID;
	}


	public int get_Group_ID() {
		return _Group_ID;
	}


	public void set_Group_ID(int _Group_ID) {
		this._Group_ID = _Group_ID;
	}
	
	
	
}
