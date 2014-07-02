package ch.vbcmalters.fanapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class TeamFactory 
{ 
	
	
	private static ArrayList<Team> getTeams()
	{
		ArrayList<Team> teamList = new ArrayList<Team>();
		
		teamList.add(new Team("Damen 1", 19246, 7004));
		teamList.add(new Team("Damen 2", 19731, 7002));
		teamList.add(new Team("Herren 1", 20302, 6956));
		teamList.add(new Team("Herren 2", 19936, 7012));
		teamList.add(new Team("Herren 3", 19943, 7014));
		teamList.add(new Team("Juniorinnen 1", 19826, 7016));
		teamList.add(new Team("Juniorinnen 2", 19816, 7009));
		teamList.add(new Team("Juniorinnen 3", 20525, 7011));
		teamList.add(new Team("Junioren 1", 20352, 7089));
		
		return teamList;
	}
	
    
    public static Team getTeamByTeamID(int teamID)
    {
    	ArrayList<Team> teamList = getTeams();
    	Iterator<Team> listIterator = teamList.iterator();
    	
    	while(listIterator.hasNext())
    	{
    		Team team = listIterator.next();
    		
    		if (team.get_Team_ID() == teamID)
    		{
    			return team;
    		}
    	}
    	
    	return new Team();
    }
	
    public static Team getTeamByName(String name)
    {  	
    	
    	ArrayList<Team> teamList = getTeams();
    	Iterator<Team> listIterator = teamList.iterator();
    	
    	while(listIterator.hasNext())
    	{
    		Team team = listIterator.next();
    		
    		if (team.get_Name().equals(name))
    		{
    			return team;
    		}
    	}
    	
    	return new Team();
    	
    	/*
    	Team toReturn = new Team();
    	
    	if (name.equals("Damen 1"))
    	{
    		toReturn = new Team("Damen 1", 19246, 7004);
    	}
    	else if (name.equals("Damen 2"))
    	{
    		toReturn = new Team("Damen 2", 19731, 7002);
    	}
    	else if (name.equals("Herren 1"))
    	{
    		toReturn = new Team("Herren 1", 20302, 6956);
    	}
    	else if (name.equals("Herren 2"))
    	{
    		toReturn = new Team("Herren 2", 19936, 7012);
    	}
    	else if (name.equals("Herren 3"))
    	{
    		toReturn = new Team("Herren 3", 19943, 7014);
    	}
    	else if (name.equals("Juniorinnen 1"))
    	{
    		toReturn = new Team("Juniorinnen 1", 19826, 7016);
    	}
    	else if (name.equals("Juniorinnen 2"))
    	{
    		toReturn = new Team("Juniorinnen 2", 19816, 7009);
    	}
    	else if (name.equals("Juniorinnen 3"))
    	{
    		toReturn = new Team("Juniorinnen 3", 20525, 7011);
    	}
    	else if (name.equals("Junioren 1"))
    	{
    		toReturn = new Team("Junioren 1", 20352, 7089);
    	}
    	
    	return toReturn;
    	*/
    }


}
