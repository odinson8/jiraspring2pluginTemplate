package com.veniture.rest;


import com.atlassian.crowd.embedded.api.Group;
import com.atlassian.jira.bc.JiraServiceContext;
import com.atlassian.jira.bc.JiraServiceContextImpl;
import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.bc.user.search.UserSearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.sal.api.net.RequestFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.stream.Collectors;

@Path("/datatable")
public class DataTable {
    @JiraImport
    private RequestFactory requestFactory;
//    private ApplicationProperties applicationProperties;
    @JiraImport
    private SearchService searchService;
    @JiraImport
    private JiraAuthenticationContext authenticationContext;
    @JiraImport
    private IssueManager issueManager;
    @JiraImport
    private GroupManager groupManager;
    @JiraImport
    private UserSearchService userSearchService;
    private static final Logger logger = LoggerFactory.getLogger(DataTable.class);// The transition ID


    public DataTable(UserSearchService userSearchService, GroupManager groupManager, RequestFactory requestFactory, SearchService searchService, JiraAuthenticationContext authenticationContext){
        this.requestFactory = requestFactory;
        this.issueManager= ComponentAccessor.getIssueManager();
        this.searchService = searchService;
        this.authenticationContext = authenticationContext;
        this.userSearchService = userSearchService;
        this.groupManager = groupManager;
    }

    @GET
    @Path("/getallusers")
    public String getallusers(@Context HttpServletRequest req, @Context HttpServletResponse resp) throws JsonProcessingException {
        ApplicationUser loggedInUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        JiraServiceContext jiraServiceContext = new JiraServiceContextImpl( loggedInUser );
        List<ApplicationUser> allUsers= userSearchService.findUsersAllowEmptyQuery(jiraServiceContext,"");
        // create `ObjectMapper` instance
        ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

        // create `ArrayNode` object
        ArrayNode arrayNode = mapper.createArrayNode();

        for (ApplicationUser user: allUsers.subList(1,8)){
            // create three JSON objects
            ObjectNode userObj = mapper.createObjectNode();
            userObj.put("DisplayName", user.getDisplayName());
            userObj.put("Username", user.getUsername());
            userObj.put("EmailAddress", user.getEmailAddress());
            userObj.put("Id", user.getId());
            userObj.put("DirectoryUser", user.getDirectoryUser().toString());
            userObj.put("Groups", groupManager.getGroupsForUser(user).stream()
                    .map(Group::getName)
                    .collect(Collectors.toList()).toString());
            arrayNode.add(userObj);
        }

        // convert `ArrayNode` to pretty-print JSON
        // without pretty-print, use `arrayNode.toString()` method
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
        // print json
        logger.error(json);
        return json;
    }    
    
    @GET
    @Path("/test")
    public String test(@Context HttpServletRequest req, @Context HttpServletResponse resp)   {
        return "json";
    }
}
