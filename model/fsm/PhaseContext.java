package pt.isec.pa.apoio_poe.model.fsm;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import pt.isec.pa.apoio_poe.model.data.*;
import pt.isec.pa.apoio_poe.model.data.Phase.Filters;
import pt.isec.pa.apoio_poe.model.data.Proposal;
import pt.isec.pa.apoio_poe.model.errors.ErrorLog;
import pt.isec.pa.apoio_poe.model.IMemento;
import pt.isec.pa.apoio_poe.model.IOriginator;
import pt.isec.pa.apoio_poe.model.Memento;


public class PhaseContext implements Serializable, IOriginator  {
    
    private HashMap<PhaseState,PhaseState> stateMap;
    private IPhaseState state;
    private PhaseState enumState;
    private Phase phase;
    
    public PhaseContext(){
        phase = new Phase();
        state = new ConfigState(this,phase);
        enumState = PhaseState.CONFIG;
        stateMap = new HashMap<PhaseState,PhaseState>();
        for(PhaseState state : PhaseState.values()){
            stateMap.put(state, state);
        }
    }
    //UNDO E REDO!!!
    @Override
    public IMemento save() {
        return new Memento(this);
    }

    @Override
    public void restore(IMemento memento) {
        Object obj = memento.getSnapshot();
        if(obj instanceof PhaseContext p){
            phase = p.phase;
            enumState = p.enumState;
            state = enumState.createState(this, phase);
            stateMap = p.stateMap;
        }
    }

    void setStateMap(PhaseState key, PhaseState value){
        stateMap.put(key, value);
    }

    PhaseState getStateMap(PhaseState key){
        return stateMap.get(key);
    }

    public void next(){
        state.nextState();
    }

    public void previous(){
        state.previousState();
    }

    public ErrorLog lock(){
        return state.setLock();
    }

    public boolean insert(Object o){
        return state.insert(o);
    }

    public ErrorLog automaticTeacherStudentProposalAssociation(){
        return state.automaticTeacherStudentProposalAssociation();
    }

    public Options[] query(){
        return state.query();
    }

    public boolean edit(Object o){
        return state.insert(o);
    }
    
    public boolean delete(Object o){
        return state.insert(o);
    }

    public ErrorLog importStudent(String fileName,ImportTypes type){
        return state.importStudent(fileName,type);
    }

    public PhaseState getState(){
        return state.getState();
    }

    public List<Student> getStudentsAutoProposal(){
        return state.getStudentsAutoProposal();
    }

    public List<Student> getStudentsWithCandidacy(){
        return state.getStudentsWithCandidacy();
    }

    public List<Student> getStudentsWithoutCandidacy(){
        return state.getStudentsWithoutCandidacy();
    }

    public List<Proposal> getAutoProposals(){
        return state.getAutoProposals();
    }

    public List<Proposal> getTeacherProposals(){
        return state.getTeacherProposals();
    }

    public List<Proposal> getProposalsWithCandidacy(){
        return state.getProposalsWithCandidacy();
    }

    public List<Proposal> getProposalsWithoutCandidacy(){
        return state.getProposalsWithoutCandidacy();
    }

    public List<Proposal> getProposalsFiltered(Filters... filters){
        return state.getProposalsFiltered(filters);
    }

    public List<Student> getStudentsWithProposal(){
        return phase.getStudentsWithProposal();
    }

    public Student getStudentByID(long numStudent){
        return state.getStudentByID(numStudent);
    }

    public Teacher getTeacherByEmail(String email){
        return state.getTeacherByEmail(email);
    }
    
    public List<ImportTypes> getImports(){
        return state.getImports();
    }

    public Proposal getProposalByID(String id){
        return state.getProposalByID(id);
    }
    void changeState(IPhaseState state,PhaseState stateEnum){
        enumState = stateEnum;
        this.state = state;
    }

    public Options[] getOptions(){
        return state.getPossibleOptions();
    }

    public ErrorLog automaticAvailableProposals(){
        return state.automaticAvailableProposals();
    }

    public List<Filters> filter(){
        return state.filter();
    }

    public List<Student> getStudentsWithoutProposals(){
        return phase.getStudentsWithoutProposals();
    }

    public List<Proposal> getAvailableProposals(Student student){
        return phase.getAvailableProposals(student);
    }

    public List<Student> getAssignableStudents(){
        return phase.getAssignableStudents();
    }
    
    public ErrorLog manualAssignProposal(Student student, Proposal proposal){
        return state.manualAssignProposal(student, proposal);
    }
    
    public ErrorLog manualRemoveProposal(Proposal p){
        return state.manualRemoveProposal(p);
    } 

    public List<Proposal> getRemovableProposals(){
        return phase.getRemovableProposals();
    }

    public ErrorLog automaticSuperviserStudentProposalAssociation(){
        return state.automaticSuperviserStudentProposalAssociation();
    }

    public ErrorLog manualAssignSuperviser(Student student, String email){
        return state.manualAssignSuperviser(student, email);
    }

    public Teacher getSuperviser(Student student){
        return phase.getSuperviser(student);
    }

    public ErrorLog removeSuperviser(Student student){
        return state.removeSuperviser(student);
    }

    public List<Student> getStudentsWithProposalAndSuperviser(){
        return phase.getStudentsWithProposalAndSuperviser();
    }

    public List<Student> getStudentsWithProposalAndWithoutSuperviser(){
        return phase.getStudentsWithProposalAndWithoutSuperviser();
    }

    public Options[] manageSupervisers(){
        return state.manageSupervisers();
    }

    public List<Teacher> getUnassignedSupervisers(Student student){
        return phase.getUnassignedSupervisers(student);
    }

    public HashMap<String, Long> getSupervisionNumberBySuperviser(){
        return phase.getSupervisionNumberBySuperviser();
    }

    public Long getSuperviserWithMaxSupervisions(){
        return phase.getSuperviserWithMaxSupervisions();
    }

    public Long getSuperviserWithMinSupervisions(){
        return phase.getSuperviserWithMinSupervisions();
    }

    public Double getSuperviserWithAvegareSupervisions(){
        return phase.getSuperviserWithAvegareSupervisions();
    }

    public Long getSupervisionsFromSuperviser(Teacher teacher){
        return phase.getSupervisionsFromSuperviser(teacher);
    }

    public List<Teacher> getTeachers(){
        return phase.getTeachers();
    }

    public List<Student> getStudentsWithCandidacyAndWithoutProposals(){
        return phase.getStudentsWithCandidacyAndWithoutProposals();
    }

    public List<Internship> getInternships(){
        return phase.getInternships();  
    }
    
    public List<Proposal> getAvailable(){
        return phase.getAvailable();
    }

    public List<Proposal> getAssignedProposals(){
        return phase.getAssignedProposals();
    }

    public List<Proposal> getCandidacyProposals(Student student){
        return phase.getCandidacyProposals(student);
    }

    public ErrorLog changeConfirmedStudentsStatus(){
        return phase.changeConfirmedStudentsStatus();
    }

    public List<Student> getTiedStudents(){
        return state.getTiedStudents();
    }
    
    public ErrorLog solveTie(Student winner){
        return state.solveTie(winner);
    }

    public void exportToCSV(){
        state.exportToCSV();
    }

    public void exportToCSVFile(File file){
        state.exportToCSVFile(file);
    }
    public List<Student> getStudents(){
        return phase.getStudents();
    }

    public List<Proposal> getProposals(){
        return phase.getProposals();
    }
}
