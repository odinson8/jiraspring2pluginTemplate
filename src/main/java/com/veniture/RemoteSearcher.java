package com.veniture;

import com.atlassian.sal.api.net.Request;
import com.atlassian.sal.api.net.RequestFactory;
import com.atlassian.sal.api.net.ResponseException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.veniture.constants.Constants;
import com.veniture.pojo.Team;
import com.veniture.pojo.TempoPlanner.IssueTableData;
import org.apache.commons.httpclient.URIException;

import java.lang.reflect.Type;
import java.util.List;

public class RemoteSearcher {
    private final RequestFactory<?> requestFactory;

    public RemoteSearcher(final RequestFactory<?> requestFactory) {
        this.requestFactory = requestFactory;
    }

    public void search() throws URIException {
        //final String fullUrl = scheme + hostname + URIUtil.encodeWithinQuery(QUERY);
        final String fullUrl = Constants.scheme + Constants.hostname + Constants.QUERY_AVAILABILTY;
        final Request request = requestFactory.createRequest(Request.MethodType.GET, fullUrl);
        request.addBasicAuthentication(Constants.hostname, Constants.adminUsername, Constants.adminPassword);

        try {
            // parse the response
            final String responseString = request.execute();
            jsonToObject(responseString);

            //BURADA KALDIM

            return;
        } catch (final ResponseException e) {
            throw new RuntimeException("Search for " + Constants.QUERY_AVAILABILTY + " on " + fullUrl + " failed.", e);
        }
    }

    private void jsonToObject(String responseString) {
        Gson gson = new Gson();

        //Type tempoTeamAvailabilityDataType = new TypeToken<List<com.veniture.IssueTableData>>() {}.getType();
       // List<com.veniture.IssueTableData> tempoTeamAvailabilityData = gson.fromJson(responseString, tempoTeamAvailabilityDataType);
        //List<Team> posts = gson.fromJson(responseString, listType);

        IssueTableData issueTableData = gson.fromJson(responseString, IssueTableData.class);
    }
}
