package com.veniture.util;

import com.veniture.servlet.ProjectApprove;
import model.pojo.Program;
import model.pojo.TempoTeams.Team;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AddPrograms {
    private Map<String, Object> context;
    private List<Team> teams;

    public static Integer getTotalCapacity() {
        return totalCapacity;
    }

    public static Integer totalCapacity = 0;

    public AddPrograms(Map<String, Object> context, List<Team> teams) {
        this.context = context;
        this.teams = teams;
    }

    public Map<String, Object> invoke() {
        totalCapacity=0;

        Set<Program> BasicPrograms = getPrograms(teams);
        final Set<Program> programsWithCapacities = getProgramsWithCapacities(teams, BasicPrograms);
        for (Program program:programsWithCapacities){
            Integer programCapacity;
            if (program.getCapacity() > 0) {
                programCapacity= program.getCapacity();
                totalCapacity += programCapacity;
            }
            else {
                programCapacity=0;
            }
            //türkçe karakterler sorun çıkarıyordu
            String ProgramNameEscaped= Normalizer.normalize(program.getName().replaceAll("\\s",""), Normalizer.Form.NFD).replaceAll("\\p{Mn}", "").replaceAll("ı", "i");
            context.put(ProgramNameEscaped,programCapacity);
        }
        context.put("TotalCapacity",totalCapacity);
        return context;
    }

    private Set<Program> getPrograms(List<Team> teams) {
        HashSet<Program> programs = new HashSet<>();
        Set<String> programNames = teams.stream().map(Team::getProgram).collect(Collectors.toSet());
        programNames.remove(null);
        for (String programName: programNames){
            programs.add(new Program(programName,0));
        }
        return programs;
    }

    private Set<Program> getProgramsWithCapacities(List<Team> teams, Set<Program> Programs) {
        for (Team team:teams){
            for (Program program:Programs){
                try {
                    if (team.getProgram().equalsIgnoreCase(program.getName())){
                        Programs.remove(program);
                        program.addMoreCapacity(team.getRemainingInAYear());
                        Programs.add(program);
                        break;
                    }
                } catch (Exception e) {
                    ProjectApprove.logger.debug("This team does not have any program related to it");
                }
            }
        }
        return Programs;
    }
}
