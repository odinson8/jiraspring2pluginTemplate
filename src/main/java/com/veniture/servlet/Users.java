package com.veniture.servlet;

import com.atlassian.jira.bc.JiraServiceContext;
import com.atlassian.jira.bc.JiraServiceContextImpl;
import com.atlassian.jira.bc.user.search.UserSearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.UserPropertyManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.veniture.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class Users extends HttpServlet {
    @JiraImport
    private TemplateRenderer templateRenderer;
    @JiraImport
    private GroupManager groupManager;
    @JiraImport
    private UserSearchService userSearchService;
    @JiraImport
    private com.atlassian.jira.user.UserPropertyManager userPropertyManager;

    private static final String LIST_ISSUES_TEMPLATE = "/templates/projectApprove.vm";
    public static final Logger logger = LoggerFactory.getLogger(Users.class);

    public Users(TemplateRenderer templateRenderer,GroupManager groupManager,UserSearchService userSearchService,UserPropertyManager userPropertyManager) {
        this.userPropertyManager = userPropertyManager;
        this.templateRenderer = templateRenderer;
        this.userSearchService= userSearchService;
        this.groupManager = groupManager;
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

        ArrayList<User> userArrayList = new ArrayList<>();

        for (ApplicationUser user:allUsers.subList(1,4)){
            User tempUser = new User();
            tempUser.setFullName(user.getDisplayName());
            tempUser.setUsername(user.getUsername());
            tempUser.setEmail(user.getEmailAddress());
            tempUser.setGroups(groupManager.getGroupNamesForUser(user));
            tempUser.setIsActive(user.isActive());
//            tempUser.setEmail();
//            for (Map.Entry<String, String>:userPropertyManager.getPropertySet(user).getProperties()){
//            }
            userArrayList.add(tempUser);
        }

        Map<String, Object> context = new HashMap<>();
        context.put("users", userArrayList);
        return context;
    }
}
