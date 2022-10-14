package pt.isec.pa.apoio_poe.model.fsm;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import pt.isec.pa.apoio_poe.model.data.*;
import pt.isec.pa.apoio_poe.model.data.Phase.Filters;
import pt.isec.pa.apoio_poe.model.data.Proposal;
import pt.isec.pa.apoio_poe.model.errors.ErrorLog;


abstract class PhaseStateAdapter implements IPhaseState, Serializable {
    
    protected PhaseContext context;
    protected Phase phase;

    protected PhaseStateAdapter(PhaseContext context,Phase phase){
        this.context = context;
        this.phase = phase;   
    }

    protected void changeState(PhaseState state){
        context.changeState(state.createState(context,phase),state);
    }


    @Override
    public void nextState(){}

    @Override
    public void previousState(){}
    
    @Override
    public boolean getLock() {
        return false;
    }
    @Override 
    public ErrorLog setLock(){
        return null;
    }

    @Override 
    public PhaseState getState(){
        return null;
    }
    @Override
    public boolean insert(Object o){
        Class className = o.getClass();

        try{
            Method m = phase.getClass().getMethod("insert", className);
            try{
                return (boolean) m.invoke(phase, o);
            } catch(InvocationTargetException | IllegalAccessException e){
                return false;
            }
        } catch(NoSuchMethodException e){
            return false;
        }
    }
    @Override
    public Options[] query(){
        return null;
    }
    @Override
    public boolean edit(Object o){
        return false;
    }
    @Override
    public boolean delete(Object o){
        return false;
    }

    @Override
    public ErrorLog importStudent(String fileName,ImportTypes type){
        return null;
    }
    @Override
    public  List<Student> getStudentsWithProposal(){
        return null;}
    @Override    
    public List<Student> getStudentsAutoProposal(){
        return null;
    }

    @Override
    public List<Student> getStudentsWithCandidacy(){
        return null;
    }

    @Override
    public List<Student> getStudentsWithoutCandidacy(){
        return null;
    }

    @Override
    public List<Proposal> getAutoProposals(){
       return null;
    }

    @Override
    public List<Proposal>getTeacherProposals(){
        return null;
    }

    @Override
    public List<Proposal> getProposalsWithCandidacy(){
        return null;
    }

    @Override
    public List<Proposal> getProposalsWithoutCandidacy(){
        return null;
    }
    @Override
    public List<Student> getStudentsWithoutProposals(){
        return null;
    }
    @Override
    public List<Proposal> getProposalsFiltered(Filters... filters){
        return null;
    }

    @Override 
    public Student getStudentByID(long studentNum){
        return null;
    }
    @Override
    public Teacher getTeacherByEmail(String email){
        return null;
    }
    @Override
    public Proposal getProposalByID(String id){
        return null;
    }

    @Override
    public Options[] getPossibleOptions(){
        return null;
    }

    @Override
    public ErrorLog automaticTeacherStudentProposalAssociation(){
        return null;
    }
    @Override 
    public ErrorLog automaticAvailableProposals(){
        return null;
    }

    @Override
    public ErrorLog manualAssignProposal(Student student, Proposal proposal){
        return null;
    }
    @Override
    public ErrorLog manualRemoveProposal(Proposal p){
        return null;
    }

    @Override
    public ErrorLog automaticSuperviserStudentProposalAssociation(){
        return null;
    }

    @Override
    public ErrorLog manualAssignSuperviser(Student student, String email){
        return null;
    }

    @Override
    public Teacher getSuperviser(Student student){
        return null;
    }

    @Override
    public ErrorLog removeSuperviser(Student student){
        return null;
    }

    @Override
    public List<Student> getStudentsWithProposalAndSuperviser(){
        return null;
    }

    @Override
    public List<Student> getStudentsWithProposalAndWithoutSuperviser(){
        return null;
    }
    @Override 
    public Options[] manageSupervisers(){
        return null;
    }

    @Override
    public List<Student> getTiedStudents(){
        return null;
    }

    @Override
    public ErrorLog solveTie(Student winner){
        return null;
    }

    @Override
    public void exportToCSV(){
        return;
    }

    @Override
    public void exportToCSVFile(File file){
        return;
    }

    @Override 
    public List<Filters> filter(){
        return null;
    }

    @Override
    public List<ImportTypes> getImports(){
        return null;
    }
}