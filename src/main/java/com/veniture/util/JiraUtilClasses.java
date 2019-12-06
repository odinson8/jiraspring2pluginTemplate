package com.veniture.util;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.search.SearchContext;
import com.atlassian.jira.jql.parser.JqlParseException;
import com.atlassian.jira.jql.parser.JqlQueryParser;
import com.veniture.constants.Constants;
import com.veniture.servlet.Priority;

import java.util.List;

public class JiraUtilClasses {
    public static class GetCustomFieldsInProjectContext {
        private JqlQueryParser jqlQueryParser= ComponentAccessor.getComponent(JqlQueryParser.class);
        private CustomFieldManager cfMgr=ComponentAccessor.getCustomFieldManager();

        public GetCustomFieldsInProjectContext() {
        }

        public List<CustomField> invoke() throws JqlParseException {
            SearchContext searchContext= Priority.searchService.getSearchContext(Priority.authenticationContext.getLoggedInUser(),jqlQueryParser.parseQuery(Constants.SC_SORGUSU));
            return cfMgr.getCustomFieldObjects(searchContext);
        }
    }
}
