package com.veniture.constants;

public class Constants {
    public static final String WFA = "project = FP AND issuetype = \"Project Card\" AND status = \"Waiting for approval\"";
    public static final String PLANLAMA = "project = FP AND issuetype = \"Project Card\" AND Departman = Planlama OR project = FP AND issuetype = \"Project Card\" AND Etiket = \"Satışı Arttıran\"";
    public static final String SATISARTTIRAN ="project = FP AND issuetype = \"Project Card\" AND status = \"Waiting for approval\" AND Etiket = \"Satışı Arttıran\"";
    public static final String PROJECTCARDS ="project = FP AND issuetype = \"Project Card\"";
    public static final long kapasiteSapCfId = 10808L;
    public static final long kapasiteAbapCfId = 10815L;
    public static final long gerekliAbapEforCfId = 10814L;
    public static final long öncelikBerkCfId = 11105L;
    public static final long gerekliSapEforCfId = 10813L;

    public static final Integer ApproveWorkflowTransitionId = 181;
    public static final Integer DeclineWorkflowTransitionId = 201;
    public static final String adminUsername = "berk.karabacak";
    public static final String adminPassword = "asd";
    public static final String hostname = "jira.veniture.tk";
    public static final String scheme = "https://";
    public static final String QUERY_TEAM = "/rest/tempo-teams/2/team";
    public static final String QUERY_AVAILABILITY = "/rest/tempo-planning/1/capacity/report/8?from=2019-11-19&to=2019-12-04&period=P1w";
}
