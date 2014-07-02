package ch.vbcmalters.fanapp;

public class Club 
{
	private String _Name;
	private int _ClubID;
	
	
	public Club()
	{
		_Name = "VBC Malters";
		_ClubID = 911360;
	}
	
	public String getName()
	{
		return _Name;
	}
	
	public int getClubID()
	{
		return _ClubID;
	}
}
