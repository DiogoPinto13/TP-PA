package pt.isec.pa.apoio_poe.model.fsm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pt.isec.pa.apoio_poe.model.data.Candidacy;
import pt.isec.pa.apoio_poe.model.data.Confirmations;
import pt.isec.pa.apoio_poe.model.data.Phase;
import pt.isec.pa.apoio_poe.model.data.Proposal;
import pt.isec.pa.apoio_poe.model.data.Student;
import pt.isec.pa.apoio_poe.model.data.Phase.Filters;
import pt.isec.pa.apoio_poe.model.errors.ErrorLog;
import pt.isec.pa.apoio_poe.model.errors.ErrorType;

public class ProposalsAtrState extends PhaseStateAdapter{

    ProposalsAtrState(PhaseContext context, Phase phase){
        super(context,phase);
    }

    @Override 
    public void previousState(){
        changeState(context.getStateMap(PhaseState.APPLICATON_OPT));
    } 

    @Override 
    public void nextState() {
        changeState(context.getStateMap(PhaseState.SUPREVISER_ATR));
    }

    @Override
    public PhaseState getState(){
        return PhaseState.PROPOSAL_ATR;
    }
    @Override
    public ErrorLog manualAssignProposal(Student student, Proposal proposal){
        return phase.manualAssignProposal(student,proposal);
    }
    @Override
    public ErrorLog manualRemoveProposal(Proposal p){
        return phase.removeAssignedProposal(p);
    }

    @Override
    public Options[] getPossibleOptions(){
        ArrayList<Options> options = new ArrayList<Options>();

        options.add(Options.AUTOMATIC_PROPOSALS_ASSOCIATION);
        options.add(Options.AUTOMATIC_PROPOSALS);
        options.add(Options.MANUAL_ASSIGNED_PROPOSALS);
        options.add(Options.MANUAL_REMOVE_PROPOSALS);
        options.add(Options.EXPORT);
        options.add(Options.LOOKUP);
        options.add(Options.NEXT);
        options.add(Options.PREVIOUS);
        options.add(Options.LOCK);
        options.add(Options.SAVE);
        options.add(Options.UNDO);
        options.add(Options.REDO);
        options.add(Options.LOAD);
        options.add(Options.EXIT);

        return options.toArray(new Options[0]);
    }
    @Override
    public ErrorLog setLock(){
        if(phase.isCandidacyCompleted()){
            changeState(PhaseState.SUPREVISER_ATR);
            context.setStateMap(PhaseState.PROPOSAL_ATR,PhaseState.PROPOSAL_ATR_CLOSED);
            return new ErrorLog(ErrorType.SUCESS);
        }
        return new ErrorLog(ErrorType.CANDIDACY_NOT_COMPLETED);
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
            if(phase.getStudentsWithCandidacy().contains(s) && assigned != null){
                sb.append(phase.getCandidacyProposals(s).indexOf(assigned)+1);
            } else{
                sb.append("-1");
            }
            sb.append("\n");
             
        }
        File phase3CSV = new File("phase3.csv");
        try {
            phase3CSV.createNewFile();
            FileWriter phase3CSVWrite = new FileWriter("phase3.csv");
            phase3CSVWrite.write(sb.toString());
            phase3CSVWrite.close();
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
            if(phase.getStudentsWithCandidacy().contains(s) && assigned != null){
                sb.append(phase.getCandidacyProposals(s).indexOf(assigned)+1);
            } else{
                sb.append("-1");
            }
            sb.append("\n");
             
        }
        File phase3CSV = file;
        try {
            phase3CSV.createNewFile();
            FileWriter phase3CSVWrite = new FileWriter(file);
            phase3CSVWrite.write(sb.toString());
            phase3CSVWrite.close();
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }


    @Override
    public ErrorLog automaticAvailableProposals(){
        phase.getStudentsWithoutProposals().clear(); 
        phase.changeConfirmedStudentsStatus();
        for(Student s : phase.getStudents()){
            if(phase.getConfirmedStudents().get(Confirmations.CONFIRMED).stream().filter(a->a.getNumStudent() == s.getNumStudent()).findFirst().orElse(null) != null){
                continue;
            }
            Candidacy candidacy = phase.getCandidacy().stream().filter(a->a.getNumStudent() == s.getNumStudent()).findFirst().orElse(null);
            for(Candidacy c : phase.getCandidacy()){
            }
            if(candidacy == null)
                continue;
            
            ArrayList<Proposal> availableProposals = phase.getAvailableProposals(s);

            Proposal proposal = null;
            boolean flagFound=false;
            for(String preference : candidacy.getPreferences()){
                proposal = availableProposals.stream().filter(p-> preference.contains(p.getId())).findFirst().orElse(null);
                if(proposal != null){
                    HashMap<Student,Integer> studentmap = phase.proposalCompetition(proposal, s, candidacy);
                    Map.Entry<Student, Integer> entry = studentmap.entrySet().iterator().next();
                    Student studentcmp = entry.getKey();
                    Integer tie = entry.getValue();
                    if(tie == 1){
                        changeState(context.getStateMap(PhaseState.TIESTATE));
                        return new ErrorLog(ErrorType.TIE);
                    } else {
                        if(!studentcmp.equals(s))
                            continue;
                        else{
                            proposal.setNumStudent(studentcmp.getNumStudent());
                            phase.getConfirmedStudents().get(Confirmations.CONFIRMED).add(proposal);
                            flagFound = true;
                            break;
                        }
                    }
                }    
            }
            if(!flagFound)
                phase.getStudentsWithoutProposals().add(s);
        }
        return new ErrorLog(ErrorType.SUCESS);
    }
    
    @Override
    public ErrorLog automaticTeacherStudentProposalAssociation(){
        return phase.changeConfirmedStudentsStatus();
    }

    @Override
    public List<Student> getStudentsWithoutProposals(){
        return phase.getStudentsWithoutProposals();
    }

    @Override
    public List<Student> getStudentsAutoProposal(){
        return phase.getStudentAutoProposals();
    }

    @Override
    public List<Student> getStudentsWithCandidacy(){
        return phase.getStudentsWithCandidacy();
    }

    @Override
    public List<Student> getStudentsWithProposal (){
        return phase.getStudentsWithProposal();
    }

    @Override 
    public List<Proposal> getProposalsFiltered(Phase.Filters... filters){
        return phase.getProposalsFiltered(filters);
    }
    @Override
    public Options[] query(){
        ArrayList<Options> options = new ArrayList<Options>();
        options.add(Options.GET_STUDENT_AUTOPROPOSAL);
        options.add(Options.GET_STUDENTS_WITH_CANDIDACY);
        options.add(Options.GET_STUDENTS_WITH_ASSIGNED_PROPOSAL);
        options.add(Options.GET_STUDENTS_WITHOUT_ASSIGNED_PROPOSAL);
        options.add(Options.GET_PROPOSALS_FILTERED);

        return options.toArray(new Options[0]);

    }

    @Override
    public List<Filters> filter(){
        List<Filters> filters = new ArrayList<Filters>();
        filters.add(Filters.AUTOPROPOSALS);
        filters.add(Filters.TEACHERPROPOSALS);
        filters.add(Filters.AVAILABLEPROPOSALS);
        filters.add(Filters.ASSIGNEDPROPOSALS);
        
        return filters;
    }
}
