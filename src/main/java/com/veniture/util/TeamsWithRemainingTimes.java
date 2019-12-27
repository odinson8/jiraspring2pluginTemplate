package com.veniture.util;

import com.veniture.RemoteSearcher;
import com.veniture.servlet.ProjectApprove;
import model.pojo.TempoTeams.Team;

import java.util.List;

public class TeamsWithRemainingTimes {
    private ProjectApprove projectApprove;

    public TeamsWithRemainingTimes(ProjectApprove projectApprove) {
        this.projectApprove = projectApprove;
    }

    public List<Team> invoke() {
        List<Team> teams=null;
        try {
            RemoteSearcher remoteSearcher =  new RemoteSearcher(projectApprove.requestFactory);
            teams=remoteSearcher.getAllTeams();
            for (Team team:teams){
                    team.setRemainingInAYear(remoteSearcher.getTotalRemainingTimeInYearForTeam(team.getId()));
            }
        } catch (Exception e) {
            ProjectApprove.logger.error("Error at getTeamsAndSetRemaining");
        }
        return teams;
    }
}
