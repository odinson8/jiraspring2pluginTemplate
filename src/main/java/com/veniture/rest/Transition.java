package com.veniture.rest;


import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.workflow.TransitionOptions;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.net.RequestFactory;
import com.veniture.RemoteSearcher;
import com.veniture.constants.Constants;
import org.apache.commons.httpclient.URIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.util.Arrays;



@Path("/transition")
public class Transition {
    @JiraImport
    private RequestFactory requestFactory;
    @JiraImport
    private ApplicationProperties applicationProperties;
    private static final Logger logger = LoggerFactory.getLogger(Transition.class);// The transition ID

    public Transition(RequestFactory requestFactory){
        this.requestFactory = requestFactory;
    }

    @GET
    @Path("/transitionissues")
    public String transitionIssues(@Context HttpServletRequest req, @Context HttpServletResponse resp) {
        IssueService issueService = ComponentAccessor.getIssueService();
        String[] issues = req.getParameterValues("issues");
        String[] action = req.getParameterValues("action");
        ApplicationUser currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if (action[0].equals("approve")){
            Arrays.stream(issues).forEach(issue->transitionIssue(issueService, currentUser, issueService.getIssue(currentUser, issue).getIssue(), Constants.ApproveWorkflowTransitionId));
        }
        else if (action[0].equals("decline")){
            Arrays.stream(issues).forEach(issue->transitionIssue(issueService, currentUser, issueService.getIssue(currentUser, issue).getIssue(), Constants.DeclineWorkflowTransitionId));
        }
        return "true";
    }

    @GET
    @Path("/debug")
    public String debug(@Context HttpServletRequest req, @Context HttpServletResponse resp) throws URIException {
        getAllTeamsfromTempo();
        return null;
    }

    private void getAllTeamsfromTempo() throws URIException {

        RemoteSearcher remoteSearcher =  new RemoteSearcher("asd",requestFactory);
        remoteSearcher.search("","");


        //Request request = requestFactory.createRequest(Request.MethodType.GET, getCurrentAppBaseUrl());
    }

    private void transitionIssue(IssueService issueService, ApplicationUser currentUser, Issue issue, Integer workflowTransitionId) {

        final TransitionOptions transitionOptions = new TransitionOptions.Builder().skipPermissions().skipValidators().setAutomaticTransition().skipConditions().build();

        IssueService.TransitionValidationResult result = issueService.validateTransition(currentUser,
                issue.getId(),
                workflowTransitionId,
                issueService.newIssueInputParameters(),
                transitionOptions);

        if (result.isValid()) {
            issueService.transition(currentUser, result);
        } else {
            logger.error(result.getErrorCollection().toString());
        }
    }

}
