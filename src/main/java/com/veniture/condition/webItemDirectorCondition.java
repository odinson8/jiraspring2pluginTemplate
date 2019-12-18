package com.veniture.condition;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.web.Condition;

import java.util.Map;

public class webItemDirectorCondition implements Condition {
    ApplicationUser loggedInUser;
    @Override
    public void init(Map<String, String> map) throws PluginParseException {
        //ComponentAccessor.getJiraAuthenticationContext().clearLoggedInUser();
        loggedInUser=ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        Boolean isLoggedin=ComponentAccessor.getJiraAuthenticationContext().isLoggedInUser();
        Boolean isLoggedin2=ComponentAccessor.getJiraAuthenticationContext().isLoggedInUser();
        //Bunuda kullan : isLoggedInUser()

    }

    @Override
    public boolean shouldDisplay(Map<String, Object> map) {
        boolean exceptionUsers = loggedInUser.getName().contains("ongul") || loggedInUser.getName().contains("enit") ||loggedInUser.getName().contains("eyhan") ;
        return ComponentAccessor.getGroupManager().getGroupNamesForUser(loggedInUser).contains("Direktörler") || exceptionUsers ;
    }
}
