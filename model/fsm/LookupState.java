package pt.isec.pa.apoio_poe.model.fsm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import pt.isec.pa.apoio_poe.model.data.Phase;
import pt.isec.pa.apoio_poe.model.data.Student;
import pt.isec.pa.apoio_poe.model.data.Proposal;

public class LookupState extends PhaseStateAdapter{

    LookupState(PhaseContext context, Phase phase){
        super(context, phase);
    }
    
    @Override
    protected void changeState(PhaseState state){}

    @Override 
    public PhaseState getState(){
        return PhaseState.LOOKUP;
    }
    @Override
    public boolean insert(Object o){
        return false;
    }
    public Options[] getPossibleOptions(){
        ArrayList<Options> options = new ArrayList<Options>();

        options.add(Options.EXPORT);
        options.add(Options.GET_STUDENTS_WITH_ASSIGNED_PROPOSAL);
        options.add(Options.GET_STUDENTS_WITH_CANDIDACY_AND_WITHOUT_PROPOSAL);
        options.add(Options.GET_AVAILABLE_PROPOSALS);
        options.add(Options.GET_ASSIGNED_PROPOSALS);
        options.add(Options.GET_NUMBER_OF_SUPERVISIONS_FOREACH_SUPERVISOR);
        options.add(Options.GET_MAX_SUPERVISIONS);
        options.add(Options.GET_AVERAGE_SUPERVISIONS);
        options.add(Options.GET_MIN_SUPERVISIONS);
        options.add(Options.GET_SUPERVISIONS_BY_SUPERVISER);
        options.add(Options.UNDO);
        options.add(Options.REDO);
        options.add(Options.EXIT);

        return options.toArray(new Options[0]);
    }

    @Override
    public void exportToCSV(){
        StringBuilder sb = new StringBuilder();
        for(Student s : phase.getStudents()){
            sb.append(String.format("%d",s.getNumStudent()));

            if(!phase.getStudentsWithCandidacy().contains(s)){
                sb.append(",NULL,");
            } else{
                for(Proposal p : phase.getCandidacyProposals(s)){
                    if(phase.getCandidacyProposals(s).size() == 1){
                        sb.append(String.format(",[%s],",p.getId()));
                        break;
                    }
                    if(phase.getCandidacyProposals(s).indexOf(p) == phase.getCandidacyProposals(s).size() - 1){
                        sb.append(String.format("%s],",p.getId()));
                        break;
                    }
                    if(phase.getCandidacyProposals(s).indexOf(p) == 0){
                        sb.append(String.format(",[%s,",p.getId()));
                    }
                    else{
                        sb.append(String.format("%s,",p.getId()));
                    }
                    
                }

            }
            Proposal assigned = phase.getProposals().stream().filter(obj->obj.getNumStudent() == s.getNumStudent()).findAny().orElse(null);
            if(assigned != null){
                sb.append(String.format("%s,",assigned.getId()));
            } else {
                sb.append("NULL,");
            }
            if(phase.getStudentsWithCandidacy().contains(s) && assigned != null && phase.getCandidacyProposals(s).contains(assigned)){
                sb.append(phase.getCandidacyProposals(s).indexOf(assigned)+1);
            } else{
                sb.append("-1");
            }
            if(phase.getSuperviser(s) != null){
                sb.append(String.format(",%s",phase.getSuperviser(s).getEmail()));
            } else{
                sb.append(",NULL");
            }
            sb.append("\n");
             
        }
        File phase5CSV = new File("phase5.csv");
        try {
            phase5CSV.createNewFile();
            FileWriter phase5CSVWrite = new FileWriter("phase5.csv");
            phase5CSVWrite.write(sb.toString());
            phase5CSVWrite.close();
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

     @Override
    public void exportToCSVFile(File file){
        StringBuilder sb = new StringBuilder();
        for(Student s : phase.getStudents()){
            sb.append(String.format("%d",s.getNumStudent()));

            if(!phase.getStudentsWithCandidacy().contains(s)){
                sb.append(",NULL,");
            } else{
                for(Proposal p : phase.getCandidacyProposals(s)){
                    if(phase.getCandidacyProposals(s).size() == 1){
                        sb.append(String.format(",[%s],",p.getId()));
                        break;
                    }
                    if(phase.getCandidacyProposals(s).indexOf(p) == phase.getCandidacyProposals(s).size() - 1){
                        sb.append(String.format("%s],",p.getId()));
                        break;
                    }
                    if(phase.getCandidacyProposals(s).indexOf(p) == 0){
                        sb.append(String.format(",[%s,",p.getId()));
                    }
                    else{
                        sb.append(String.format("%s,",p.getId()));
                    }
                    
                }

            }
            Proposal assigned = phase.getProposals().stream().filter(obj->obj.getNumStudent() == s.getNumStudent()).findAny().orElse(null);
            if(assigned != null){
                sb.append(String.format("%s,",assigned.getId()));
            } else {
                sb.append("NULL,");
            }
            if(phase.getStudentsWithCandidacy().contains(s) && assigned != null && phase.getCandidacyProposals(s).contains(assigned)){
                sb.append(phase.getCandidacyProposals(s).indexOf(assigned)+1);
            } else{
                sb.append("-1");
            }
            if(phase.getSuperviser(s) != null){
                sb.append(String.format(",%s",phase.getSuperviser(s).getEmail()));
            } else{
                sb.append(",NULL");
            }
            sb.append("\n");
             
        }
        File phase5CSV = file;
        try {
            phase5CSV.createNewFile();
            FileWriter phase5CSVWrite = new FileWriter(file);
            phase5CSVWrite.write(sb.toString());
            phase5CSVWrite.close();
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }
}
