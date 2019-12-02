package com.veniture.constants;

public class Constants {

    //Genel
    public enum Environments {
        VenitureJira, FloProd, FloTest
    }
    public static final String scheme = "https://";
    public static final String QUERY_TEAM = "/rest/tempo-teams/2/team";
    public static final String QUERY_AVAILABILITY = "/rest/tempo-planning/1/capacity/report/8?from=2019-11-19&to=2019-12-04&period=P1w";
    public static final long TRUE_OPTION_ID = 11200L;

    //Test Ortamı
    public static final String testOrtamıSorgusu ="project = \"IT-PMO Portfolyo Talep Yönetimi Test\" and \"İlgili GMY\" =currentUser()";
    public static final long OncelikDepartmaId = 11403L;
    public static final long onceliklendirildimiId = 11500L;
    public static final long GMYOncelikID = 11501L;
    //Prod Ortamı

    //Veniture Jira yeni Ortam
    public static final String testJQL ="project = PF";

    //Veniture Jira eski Ortam
    public static final String WFA = "project = FP AND issuetype = \"Project Card\" AND status = \"Waiting for approval\"";
    public static final String PLANLAMA = "project = FP AND issuetype = \"Project Card\" AND Departman = Planlama OR project = FP AND issuetype = \"Project Card\" AND Etiket = \"Satışı Arttıran\"";
    public static final String SATISARTTIRAN ="project = FP AND issuetype = \"Project Card\" AND status = \"Waiting for approval\" AND Etiket = \"Satışı Arttıran\"";
    public static final String PROJECTCARDS ="project = FP AND issuetype = \"Project Card\"";
    public static final String adminUsername = "berk.karabacak";
    public static final String adminPassword = "asd";
    public static final String hostname = "jira.veniture.tk";
    public static final Integer ApproveWorkflowTransitionId = 181;
    public static final Integer DeclineWorkflowTransitionId = 201;

    public static final long kapasiteSapCfId = 10808L;
    public static final long kapasiteAbapCfId = 10815L;
    public static final long gerekliAbapEforCfId = 10814L;
    public static final long gerekliSapEforCfId = 10813L;
    public static final long öncelikBerkCfId = 11105L;
}
