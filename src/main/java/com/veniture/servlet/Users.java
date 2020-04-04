package com.veniture.servlet;

import com.atlassian.jira.bc.JiraServiceContext;
import com.atlassian.jira.bc.JiraServiceContextImpl;
import com.atlassian.jira.bc.user.search.UserSearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.templaterenderer.TemplateRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Users extends HttpServlet {
    @JiraImport
    private TemplateRenderer templateRenderer;
    private static final String LIST_ISSUES_TEMPLATE = "/templates/projectApprove.vm";
    public static final Logger logger = LoggerFactory.getLogger(Users.class);
    public Users(TemplateRenderer templateRenderer) {
        this.templateRenderer = templateRenderer;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> context = null;
        try {
            context = createContext();
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        resp.setContentType("text/html;charset=utf-8");
        templateRenderer.render(LIST_ISSUES_TEMPLATE, context, resp.getWriter());
    }

    private Map<String, Object> createContext() {
        ApplicationUser loggedInUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        JiraServiceContext jiraServiceContext = new JiraServiceContextImpl( loggedInUser );
        UserSearchService userSearchService = ComponentAccessor.getUserSearchService();
        List<ApplicationUser> allUsers= userSearchService.findUsersAllowEmptyQuery(jiraServiceContext,"");
        Map<String, Object> context = new HashMap<>();
        context.put("allUsers", allUsers);
        return context;
    }
}
