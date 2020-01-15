package com.veniture.util;

import com.atlassian.sal.api.net.RequestFactory;
import com.veniture.servlet.ProjectApprove;
import model.pojo.TempoTeams.Team;
import org.slf4j.Logger;

import java.util.List;

public class TeamsWithAvailabilityTimes {

    private Logger logger;
    private RequestFactory requestFactory;
    public TeamsWithAvailabilityTimes(Logger logger, RequestFactory requestFactory) {
        this.requestFactory=requestFactory;
        this.logger=logger;
    }

    public List<Team> invoke() throws Exception {
        List<Team> teams;
        try {
            RemoteSearcher remoteSearcher =  new RemoteSearcher(requestFactory);
            teams=remoteSearcher.getAllTeams();
            for (Team team:teams){
                Integer totalRemainingTimeInYearForTeam = remoteSearcher.getTotalRemainingTimeInYearForTeam(team.getId());
                Integer totalAvailabilityTimeInYearForTeam = remoteSearcher.getTotalAllocatedTimeInYearForTeam(team.getId())+totalRemainingTimeInYearForTeam;
                team.setRemainingInAYear(totalRemainingTimeInYearForTeam);
                team.setTotalAvailabilityInAYear(totalAvailabilityTimeInYearForTeam);
            }
        } catch (Exception e) {
            logger.error("Error at getTeamsAndSetRemaining");
            throw new Exception();
        }
        return teams;
    }
}
