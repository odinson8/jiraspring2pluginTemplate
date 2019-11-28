package com.veniture;

import com.atlassian.sal.api.net.Request;
import com.atlassian.sal.api.net.RequestFactory;
import com.atlassian.sal.api.net.ResponseException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.veniture.constants.Constants;
import com.veniture.pojo.TempoPlanner.IssueTableData;
import com.veniture.pojo.TempoTeams.Team;
import org.apache.commons.httpclient.URIException;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class RemoteSearcher {
    private final RequestFactory<?> requestFactory;

    public RemoteSearcher(final RequestFactory<?> requestFactory) {
        this.requestFactory = requestFactory;
    }

    public IssueTableData getProjectTableData() throws URIException {
        Gson gson = new Gson();
        return gson.fromJson(getResponseString(Constants.QUERY_AVAILABILITY), IssueTableData.class);
    }

    public List<Integer> getAllTeamIds() throws URIException {
        Gson gson = new Gson();
        Type tempoTeamDataType = new TypeToken<List<Team>>() {}.getType();
        List<Team> tempoTeamData = gson.fromJson(getResponseString(Constants.QUERY_TEAM), tempoTeamDataType);
        List<Integer> ids = tempoTeamData.stream().map(x -> x.getId()).collect(Collectors.toList());
        return ids;
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
