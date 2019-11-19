package com.veniture;

import com.atlassian.sal.api.net.Request;
import com.atlassian.sal.api.net.RequestFactory;
import com.atlassian.sal.api.net.ResponseException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.httpclient.URIException;

import java.lang.reflect.Type;
import java.util.List;

public class RemoteSearcher
{
    private final String url;
    private static final String SEARCH_PLUGIN_PATH = "/plugins/servlet/studio/search";
    private final RequestFactory<?> requestFactory;

    public RemoteSearcher(final String url, final RequestFactory<?> requestFactory)
    {
        this.url = url;
        this.requestFactory = requestFactory;
    }

    public void search(final String remoteUser, final String query) throws URIException {
        //final String fullUrl = url + SEARCH_PLUGIN_PATH + "?query=" + URIUtil.encodeWithinQuery(query);
        final String fullUrl = "https://jira.veniture.tk/rest/tempo-teams/2/team";

        final Request request = requestFactory.createRequest(Request.MethodType.GET, fullUrl);
        request.addBasicAuthentication("jira.veniture.tk","berk.karabacak","asd");

        try
        {
            // parse the response
            final String responseString = request.execute();
            Gson gson = new Gson();

            Type listType = new TypeToken<List<Team>>(){}.getType();
            List<Team> posts = gson.fromJson(responseString, listType);

            System.out.println("asd" + posts.get(0).getName());
           // return (SearchResults) XStreamUtils.fromXML(responseString);
            return;
        }
        catch (final ResponseException e)
        {
            throw new RuntimeException("Search for " + query + " on " + fullUrl + " failed.", e);
        }
    }
}
