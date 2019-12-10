package com.veniture.condition;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.web.Condition;

import java.util.Map;

public class webItemConditions implements Condition {
    @Override
    public void init(Map<String, String> map) throws PluginParseException {
    }

    @Override
    public boolean shouldDisplay(Map<String, Object> map) {
        return ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser().getDisplayName().contains("enitur");
    }
}
