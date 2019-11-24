package com.veniture;

import com.atlassian.sal.api.net.Request;
import com.atlassian.sal.api.net.RequestFactory;
import com.atlassian.sal.api.net.ResponseException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.veniture.constants.Constants;
import com.veniture.pojo.Team;
import org.apache.commons.httpclient.URIException;

import java.lang.reflect.Type;
import java.util.List;

public class RemoteSearcher {
    private static final String hostname = "jira.veniture.tk";
    private static final String scheme = "https://";
    private static final String QUERY = "/rest/tempo-teams/2/team";
    private final RequestFactory<?> requestFactory;

    public RemoteSearcher(final RequestFactory<?> requestFactory) {
        this.requestFactory = requestFactory;
    }

    public void search() throws URIException {
        //final String fullUrl = scheme + hostname + URIUtil.encodeWithinQuery(QUERY);
        final String fullUrl = scheme + hostname + QUERY;
        final Request request = requestFactory.createRequest(Request.MethodType.GET, fullUrl);
        request.addBasicAuthentication(hostname, Constants.adminUsername, Constants.adminPassword);

        try {
            // parse the response
            final String responseString = request.execute();
            jsonToObject(responseString);

            //BURADA KALDIM

            return;
        } catch (final ResponseException e) {
            throw new RuntimeException("Search for " + QUERY + " on " + fullUrl + " failed.", e);
        }
    }

    private void jsonToObject(String responseString) {
        Gson gson = new Gson();

        Type listType = new TypeToken<List<Team>>() {
        }.getType();
        List<Team> posts = gson.fromJson(responseString, listType);
    }


}
