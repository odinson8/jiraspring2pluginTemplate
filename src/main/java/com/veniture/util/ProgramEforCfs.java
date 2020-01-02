package com.veniture.util;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;

import java.util.ArrayList;
import java.util.Map;

public class ProgramEforCfs {
    private Map<String, Object> context;

    public ProgramEforCfs(Map<String, Object> context) {
        this.context = context;
    }

    public Map<String, Object> invoke() {
        CustomFieldManager cfMgr= ComponentAccessor.getCustomFieldManager();
        CustomField projeYonetimEforCf = cfMgr.getCustomFieldObject(11802l);
        CustomField sapAbapEforCf = cfMgr.getCustomFieldObject(11803l);
        CustomField yazılımGeliştirmeEforCf = cfMgr.getCustomFieldObject(11805l);
        CustomField sapUygulamaEforCf = cfMgr.getCustomFieldObject(11804l);
        CustomField işZekasıVeRaporlamaEforCf = cfMgr.getCustomFieldObject(11806l);

        ArrayList<CustomField> customFieldArrayList= new ArrayList<>();
        customFieldArrayList.add(projeYonetimEforCf);
        customFieldArrayList.add(sapAbapEforCf);
        customFieldArrayList.add(yazılımGeliştirmeEforCf);
        customFieldArrayList.add(sapUygulamaEforCf);
        customFieldArrayList.add(işZekasıVeRaporlamaEforCf);

        context.put("eforCfs",customFieldArrayList);
        return context;
    }


    public ProgramEforCfs() {
    }

    public ArrayList<CustomField> berk() {
        CustomFieldManager cfMgr= ComponentAccessor.getCustomFieldManager();
        CustomField projeYonetimEforCf = cfMgr.getCustomFieldObject(11802l);
        CustomField sapAbapEforCf = cfMgr.getCustomFieldObject(11803l);
        CustomField yazılımGeliştirmeEforCf = cfMgr.getCustomFieldObject(11805l);
        CustomField sapUygulamaEforCf = cfMgr.getCustomFieldObject(11804l);
        CustomField işZekasıVeRaporlamaEforCf = cfMgr.getCustomFieldObject(11806l);

        ArrayList<CustomField> customFieldArrayList= new ArrayList<>();
        customFieldArrayList.add(projeYonetimEforCf);
        customFieldArrayList.add(sapAbapEforCf);
        customFieldArrayList.add(yazılımGeliştirmeEforCf);
        customFieldArrayList.add(sapUygulamaEforCf);
        customFieldArrayList.add(işZekasıVeRaporlamaEforCf);


        return customFieldArrayList;
        //context.put("eforCfs",customFieldArrayList);
        //return context;
    }
}