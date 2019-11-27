
package com.veniture.pojo.TempoPlanner;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Footer {

    @SerializedName("columns")
    @Expose
    private List<AvailabilityInfos2> columns = null;

    public List<AvailabilityInfos2> getColumns() {
        return columns;
    }

    public void setColumns(List<AvailabilityInfos2> columns) {
        this.columns = columns;
    }

}
