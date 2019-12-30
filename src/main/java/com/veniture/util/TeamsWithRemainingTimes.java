package com.veniture.util;

import com.atlassian.sal.api.net.RequestFactory;
import com.veniture.servlet.ProjectApprove;
import model.pojo.TempoTeams.Team;
import org.slf4j.Logger;

import java.util.List;

public class TeamsWithRemainingTimes {



    private Logger logger;
    private RequestFactory requestFactory;
    public TeamsWithRemainingTimes(Logger logger, RequestFactory requestFactory) {
        this.requestFactory=requestFactory;
        this.logger=logger;
    }

    public List<Team> invoke() {
        List<Team> teams=null;
        try {
            RemoteSearcher remoteSearcher =  new RemoteSearcher(requestFactory);
            teams=remoteSearcher.getAllTeams();
            for (Team team:teams){
                    team.setRemainingInAYear(remoteSearcher.getTotalRemainingTimeInYearForTeam(team.getId()));
            }
        } catch (Exception e) {
            logger.error("Error at getTeamsAndSetRemaining");
        }
        return teams;
    }
}
