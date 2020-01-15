
package model.pojo.TempoTeams;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Team {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("lead")
    @Expose
    private String lead;
    @SerializedName("program")
    @Expose
    private String program;
    @SerializedName("isPublic")
    @Expose
    private Boolean isPublic;
    @SerializedName("RemainingInAYear")
    @Expose
    private Integer RemainingInAYear;

    @SerializedName("TotalAvailabilityInAYear")
    @Expose
    private Integer TotalAvailabilityInAYear;

    public void setRemainingInAYear(Integer RemainingInAYear) {
        this.RemainingInAYear = RemainingInAYear;
    }

    public Integer getRemainingInAYear() { return RemainingInAYear; }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public Integer getTotalAvailabilityInAYear() {
        return TotalAvailabilityInAYear;
    }

    public void setTotalAvailabilityInAYear(Integer totalAvailabilityInAYear) {
        TotalAvailabilityInAYear = totalAvailabilityInAYear;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

}
