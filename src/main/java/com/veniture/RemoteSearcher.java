package com.veniture;

import com.atlassian.sal.api.net.Request;
import com.atlassian.sal.api.net.RequestFactory;
import com.atlassian.sal.api.net.ResponseException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.veniture.constants.Constants;
import model.pojo.TempoPlanner.FooterTotalAvailabilityInfos;
import model.pojo.TempoPlanner.IssueTableData;
import model.pojo.TempoTeams.Team;
import org.apache.commons.httpclient.URIException;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class RemoteSearcher {
    private final RequestFactory<?> requestFactory;
    private static final Gson GSON = new Gson();

    public RemoteSearcher(final RequestFactory<?> requestFactory) {
        this.requestFactory = requestFactory;
    }

    public Double getTotalRemainingTimeInYearForTeam(Integer teamId) throws URIException {
        Gson gson = new Gson();
        List<FooterTotalAvailabilityInfos> totals = gson.fromJson(getResponseString(Constants.QUERY_AVAILABILITY_YEAR.replace("XXX",teamId.toString())), IssueTableData.class).getFooter().getColumns();
        Double totalRemaining= totals.stream().map(FooterTotalAvailabilityInfos::getRemaining).reduce( (a, b) -> a + b).orElse(0.0);
        return totalRemaining;
    }

    public List<Integer> getAllTeamIds() throws URIException {
        Type tempoTeamDataType = new TypeToken<List<Team>>() {}.getType();
        List<Team> tempoTeamData = GSON.fromJson(getResponseString(Constants.QUERY_TEAM), tempoTeamDataType);
        List<Integer> ids = tempoTeamData.stream().map(Team::getId).collect(Collectors.toList());
        return ids;
    }

    public List<Team> getAllTeams() throws URIException {
        Type tempoTeamDataType = new TypeToken<List<Team>>() {}.getType();
        List<Team> tempoTeamData = GSON.fromJson(getResponseString(Constants.QUERY_TEAM), tempoTeamDataType);
      //  List<String> names = tempoTeamData.stream().map(Team::getName).collect(Collectors.toList());
        return tempoTeamData;
    }

    public String getResponseString(String Query) throws URIException {
        //final String fullUrl = scheme + hostname + URIUtil.encodeWithinQuery(QUERY);
        final String fullUrl = Constants.scheme + Constants.hostname + Query;
        final Request request = requestFactory.createRequest(Request.MethodType.GET, fullUrl);
        request.addBasicAuthentication(Constants.hostname, Constants.adminUsername, Constants.adminPassword);

        try {
            return request.execute();
        } catch (final ResponseException e) {
            throw new RuntimeException("Search for " + Query + " on " + fullUrl + " failed.", e);
        }
    }
}
