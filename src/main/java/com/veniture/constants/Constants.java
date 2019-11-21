package com.veniture.constants;

public class Constants {
    public static final String WFA = "project = FP AND issuetype = \"Project Card\" AND status = \"Waiting for approval\"";
    public static final String PLANLAMA = "project = FP AND issuetype = \"Project Card\" AND Departman = Planlama OR project = FP AND issuetype = \"Project Card\" AND Etiket = \"Satışı Arttıran\"";
    public static final String SATISARTTIRAN ="project = FP AND issuetype = \"Project Card\" AND status = \"Waiting for approval\" AND Etiket = \"Satışı Arttıran\"";
    public static final String PROJECTCARDS ="project = FP AND issuetype = \"Project Card\"";
    public static final long kapasiteSapCfId = 10808L;
    public static final long kapasiteAbapCfId = 10815L;
    public static final long gerekliAbapEforCfId = 10814L;
    public static final long gerekliSapEforCfId = 10813L;

    public static final Integer ApproveWorkflowTransitionId = 181;
    public static final Integer DeclineWorkflowTransitionId = 201;
    public static final String adminUsername = "berk.karabacak";
    public static final String adminPassword = "asd";
}
