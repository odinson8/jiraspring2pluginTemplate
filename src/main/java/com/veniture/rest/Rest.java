package com.veniture.rest;


import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.sal.api.net.RequestFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("/rest")
public class Rest {
    @JiraImport
    private RequestFactory requestFactory;
//    private ApplicationProperties applicationProperties;
    @JiraImport
    private SearchService searchService;
    @JiraImport
    private JiraAuthenticationContext authenticationContext;
    @JiraImport
    private IssueManager issueManager;
    private static final Logger logger = LoggerFactory.getLogger(Rest.class);// The transition ID


    public Rest(RequestFactory requestFactory, SearchService searchService, JiraAuthenticationContext authenticationContext){
        this.requestFactory = requestFactory;
        this.issueManager= ComponentAccessor.getIssueManager();
        this.searchService = searchService;
        this.authenticationContext = authenticationContext;
    }


    @GET
    @Path("/test")
    public String getCfValueFromIssue(@Context HttpServletRequest req, @Context HttpServletResponse resp) {
        return "test";
    }
}
