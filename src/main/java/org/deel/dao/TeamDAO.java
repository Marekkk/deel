package org.deel.dao;

import org.deel.domain.Team;

public interface TeamDAO {
	public void insertTeam(Team t);
	public void deleteTeam(Team t);
	public void updateTeam(Team t);
	public Team findTeamByName(String name);
}
