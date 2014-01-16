package org.deel.dao;

import org.deel.domain.Team;

public interface TeamDao {
	public void insertTeam(Team t);
	public void deleteTeam(Team t);
	public void updateTeam(Team t);
}
