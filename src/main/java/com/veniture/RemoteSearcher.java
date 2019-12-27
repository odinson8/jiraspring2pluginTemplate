package com.veniture;

import com.atlassian.sal.api.net.Request;
import com.atlassian.sal.api.net.RequestFactory;
import com.atlassian.sal.api.net.ResponseException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.pojo.TempoPlanner.FooterTotalAvailabilityInfos;
import model.pojo.TempoPlanner.IssueTableData;
import model.pojo.TempoTeams.Team;
import org.apache.commons.httpclient.URIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;

import static com.veniture.constants.Constants.*;

public class RemoteSearcher {
    private final RequestFactory<?> requestFactory;
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
    private static final Logger logger = LoggerFactory.getLogger(RemoteSearcher.class);// The transition ID


    public RemoteSearcher(final RequestFactory<?> requestFactory) {
        this.requestFactory = requestFactory;
    }

    public Integer getTotalRemainingTimeInYearForTeam(Integer teamId) {
        List<FooterTotalAvailabilityInfos> totals = null;
        totals = GSON.fromJson(getResponseString(QUERY_AVAILABILITY_YEAR.replace("XXX",teamId.toString())), IssueTableData.class).getFooter().getColumns();
        Double totalRemaining= null;
        try {
            totalRemaining = totals.stream().map(FooterTotalAvailabilityInfos::getRemaining).reduce( (a, b) -> a + b).orElse(0.0);
        } catch (Exception e) {
            //Buraya giriyorsa takımlarin kapasitesi set edilmemiştir demekttir, o halde kapasiteyi sıfır yap.
            totalRemaining=0.0;
        }
        return totalRemaining.intValue();
    }

//    public List<Integer> getAllTeamIds() throws URIException {
//        Type tempoTeamDataType = new TypeToken<List<model.pojo.Team>>() {}.getType();
//        List<Team> tempoTeamData = GSON.fromJson(getResponseString(Constants.QUERY_TEAM), tempoTeamDataType);
//        List<Integer> ids = tempoTeamData.stream().map(model.pojo.Team::getId).collect(Collectors.toList());
//        return ids;
//    }

    public List<Team> getAllTeams() throws URIException {
        Type tempoTeamDataType = new TypeToken<List<Team>>() {}.getType();
        List<Team> tempoTeamData = GSON.fromJson(getResponseString(QUERY_TEAM), tempoTeamDataType);
        // List<String> names = tempoTeamData.stream().map(Team::getName).collect(Collectors.toList());
        return tempoTeamData;
    }

    public String getResponseString(String Query) {
        //final String fullUrl = scheme + hostname + URIUtil.encodeWithinQuery(QUERY);
        String hostname;
        String scheme;
        assert JIRA_BASE_URL != null;
        if (JIRA_BASE_URL.contains("veniture")){
            hostname= venitureHostname;
            scheme = schemeHTTPS;
        }else {
            hostname= floHostname;
            scheme = schemeHTTP;
        }

        final String fullUrl = scheme + hostname + Query;
        final Request request = requestFactory.createRequest(Request.MethodType.GET, fullUrl);
        request.addBasicAuthentication(hostname, adminUsername, adminPassword);

        try {
            return request.execute();
        } catch (final ResponseException e) {
            logger.error(e.getMessage() + e.getLocalizedMessage());
            throw new RuntimeException("Search for " + Query + " on " + fullUrl + " failed.", e);
        }
    }
}
