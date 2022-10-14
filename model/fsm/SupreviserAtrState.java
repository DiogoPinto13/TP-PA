package pt.isec.pa.apoio_poe.model.fsm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import pt.isec.pa.apoio_poe.model.data.Phase;
import pt.isec.pa.apoio_poe.model.data.Student;
import pt.isec.pa.apoio_poe.model.errors.ErrorLog;
import pt.isec.pa.apoio_poe.model.errors.ErrorType;
import pt.isec.pa.apoio_poe.model.data.Proposal;

public class SupreviserAtrState extends PhaseStateAdapter {
    SupreviserAtrState(PhaseContext context, Phase phase){
        super(context, phase);
    }
    @Override
    public Options[] getPossibleOptions(){
        ArrayList<Options> options = new ArrayList<Options>();

        options.add(Options.AUTOMATIC_SUPERVISERS);
        options.add(Options.MANAGE_SUPERVISERS);
        options.add(Options.EXPORT);
        options.add(Options.LOOKUP);
        options.add(Options.NEXT);
        options.add(Options.PREVIOUS);
        options.add(Options.LOCK);
        options.add(Options.SAVE);

        return options.toArray(new Options[0]);
    }
    public Options[] query(){
        ArrayList<Options> options = new ArrayList<Options>();

        options.add(Options.GET_STUDENTS_WITH_ASSIGNED_PROPOSAL_AND_SUPERVISER);
        options.add(Options.GET_STUDENTS_WITH_ASSIGNED_PROPOSAL_AND_WITHOUT_SUPERVISER);
        options.add(Options.GET_NUMBER_OF_SUPERVISIONS_FOREACH_SUPERVISOR);
        options.add(Options.GET_MAX_SUPERVISIONS);
        options.add(Options.GET_AVERAGE_SUPERVISIONS);
        options.add(Options.GET_MIN_SUPERVISIONS);
        options.add(Options.GET_SUPERVISIONS_BY_SUPERVISER);

        return options.toArray(new Options[0]);
    }
    
    @Override 
    public void previousState(){
        changeState(context.getStateMap(PhaseState.PROPOSAL_ATR));
    } 

    @Override
    public ErrorLog automaticSuperviserStudentProposalAssociation(){
        return phase.changeConfirmedTeachersStatus();
    }

    @Override
    public ErrorLog manualAssignSuperviser(Student student, String email){
        return phase.setSuperviser(student,email);
    }

    @Override
    public ErrorLog removeSuperviser(Student student){
        return phase.removeSuperviser(student);
    }

    @Override 
    public void nextState() {}

    @Override
    public ErrorLog setLock(){
        changeState(PhaseState.SUPREVISER_ATR_CLOSED);
        context.setStateMap(PhaseState.SUPREVISER_ATR, PhaseState.SUPREVISER_ATR_CLOSED);
        changeState(context.getStateMap(PhaseState.LOOKUP));
        return new ErrorLog(ErrorType.SUCESS);
    }
    @Override
    public boolean getLock(){
        return false;
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
        File phase4CSV = new File("phase4.csv");
        try {
            phase4CSV.createNewFile();
            FileWriter phase4CSVWrite = new FileWriter("phase4.csv");
            phase4CSVWrite.write(sb.toString());
            phase4CSVWrite.close();
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
        File phase4CSV = file;
        try {
            phase4CSV.createNewFile();
            FileWriter phase4CSVWrite = new FileWriter(file);
            phase4CSVWrite.write(sb.toString());
            phase4CSVWrite.close();
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    @Override
    public Options[] manageSupervisers(){
        ArrayList<Options> options = new ArrayList<Options>();

        options.add(Options.MANUAL_ASSIGN_SUPERVISER);
        options.add(Options.MANUAL_REMOVE_SUPERVISER);
        options.add(Options.LOOKUP_SUPERVISER);
        options.add(Options.EDIT_SUPERVISER);

        return options.toArray(new Options[0]);
    }

    @Override
    public PhaseState getState(){
        return PhaseState.SUPREVISER_ATR;
    }
}
