package com.veniture.util;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.event.type.EventDispatchOption;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.user.ApplicationUser;

import java.util.List;

public class functions {

    public static void updateCustomFieldValue(MutableIssue issue, CustomField customField, Object value, ApplicationUser user) {
        issue.setCustomFieldValue(customField,value);
        ComponentAccessor.getIssueManager().updateIssue(user, issue, EventDispatchOption.ISSUE_UPDATED, false);
    }

    public static List<CustomField> getCustomFieldsInProject(String projectKey){
        Long projectId = ComponentAccessor.getProjectManager().getProjectByCurrentKey(projectKey).getId();
        return ComponentAccessor.getCustomFieldManager().getCustomFieldObjects(projectId, ConstantsManager.ALL_ISSUE_TYPES);
    }
}
