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

public class team2Program {
    private Map<String, Object> context;
    private List<Team> teams;

    public static Integer getTotalRemainingCapacityOfAllPrograms() {
        return totalRemainingCapacityOfAllPrograms;
    }

    public static Integer totalRemainingCapacityOfAllPrograms = 0;
    public static Integer totalCapacityOfAllPrograms =0;

    public team2Program(Map<String, Object> context, List<Team> teams) {
        this.context = context;
        this.teams = teams;
    }

    public Map<String, Object> invoke() {
        totalRemainingCapacityOfAllPrograms =0;
        totalCapacityOfAllPrograms =0;

        Set<Program> BasicPrograms = getPrograms(teams);
        final Set<Program> programsWithCapacities = createProgramsWithCapacitiesFromTeams(teams, BasicPrograms);
        for (Program program:programsWithCapacities){
            Integer programRemainingCapacity;
            if (program.getRemainingCapacity() > 0) {
                programRemainingCapacity= program.getRemainingCapacity();
                totalRemainingCapacityOfAllPrograms += programRemainingCapacity;
            }
            else {
                programRemainingCapacity=0;
            }

            Integer programTotalCapacity;
            if (program.getTotalCapacity() > 0) {
                programTotalCapacity= program.getTotalCapacity();
                totalCapacityOfAllPrograms += programTotalCapacity;
            }
            else {
                programTotalCapacity=0;
            }

            //Türkçe karakterler sorun çıkarıyordu.

            String ProgramNameEscaped= Normalizer.normalize(program.getName().replaceAll("\\s",""), Normalizer.Form.NFD).replaceAll("\\p{Mn}", "").replaceAll("ı", "i");
            context.put(ProgramNameEscaped+"Remaining",com.veniture.util.functions.calculateManCountFromHour(programRemainingCapacity));
            context.put(ProgramNameEscaped+"Total",com.veniture.util.functions.calculateManCountFromHour(programTotalCapacity));
        }
        context.put("TotalRemainingCapacity", com.veniture.util.functions.calculateManCountFromHour(totalRemainingCapacityOfAllPrograms));
        context.put("totalCapacityOfAllPrograms",com.veniture.util.functions.calculateManCountFromHour( totalCapacityOfAllPrograms));
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

    private Set<Program> createProgramsWithCapacitiesFromTeams(List<Team> teams, Set<Program> Programs) {
        for (Team team:teams){
            for (Program program:Programs){
                try {
                    if (team.getProgram().equalsIgnoreCase(program.getName())){
                        Programs.remove(program);
                        program.addRemainingCapacity(team.getRemainingInAYear());
                        program.addTotalCapacity(team.getTotalAvailabilityInAYear());
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
