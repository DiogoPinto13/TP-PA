package pt.isec.pa.apoio_poe.model;

import pt.isec.pa.apoio_poe.model.data.Internship;
import pt.isec.pa.apoio_poe.model.data.Phase;
import pt.isec.pa.apoio_poe.model.data.Proposal;
import pt.isec.pa.apoio_poe.model.data.Student;
import pt.isec.pa.apoio_poe.model.data.Teacher;
import pt.isec.pa.apoio_poe.model.data.Phase.Filters;
import pt.isec.pa.apoio_poe.model.errors.ErrorLog;
import pt.isec.pa.apoio_poe.model.errors.ErrorType;
import pt.isec.pa.apoio_poe.model.fsm.ImportTypes;
import pt.isec.pa.apoio_poe.model.fsm.Options;
import pt.isec.pa.apoio_poe.model.fsm.PhaseContext;
import pt.isec.pa.apoio_poe.model.fsm.PhaseState;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import javafx.stage.FileChooser;

/**
*A class that provides access to the model from the UI
*@author Francisco Almeida
*@author Diogo Pinto
*@version 1.0
*/


public class PhaseManager {

    public static final String STUDENT_STRING = "_student_"; 
    public static final String TEACHER_STRING = "_teacher_";
    public static final String PROPOSAL_STRING = "_proposal_";
    public static final String UNDO_REDO_STRING = "_undo_redo_";
    public static final String LOCK_STRING = "_lock_";
    public static final String NEW_STATE_STRING = "_new_state_";
    public static final String LOAD_STRING = "_load_string_";
    public static final String CHANGE_STATE = "_change_state_";
    public static final String AUTOMATIC_PPROPOSAL_STRING = "_aumatic_proposal_";
    public static final String CANDIDACY_STRING = "_candidacy_";
    public static final String MANUAL_ASSIGN_SUPERVISER_STRING = "_manual_assign_superviser_";
    public static final String AUTOMATIC_ASSIGN_SUPERVISER_STRING = "_automatic_assign_superviser_";
    public static final String MANAGE_PROPOSALS_STRING = "_manage_proposals_";
    public static final String MANAGE_SUPERVISERS_STRING = "_manage_supervisers_";
    
    PhaseContext context;

    IOriginator originator;
    Deque<IMemento> history;
    Deque<IMemento> redoHist;

    PropertyChangeSupport pcs;

    public PhaseManager(){
        context = new PhaseContext();
        this.originator = context;
        history = new ArrayDeque<>();
        redoHist = new ArrayDeque<>();
        pcs = new PropertyChangeSupport(this);
    }

    
    /** 
     *A function to register views that wants to be updated
     *with a given property
     * @param property
     * @param listener
     */
    public void addObserver(String property, PropertyChangeListener listener){
        pcs.addPropertyChangeListener(property, listener);
    }

    /**
    *A function that changes to the next state  
    */
    public void next(){
        saveMemento();
        context.next();
        pcs.firePropertyChange(CHANGE_STATE, null, null);
    }
    /**
    *A function that changes to the previous state  
    */
    public void previous(){
        saveMemento();
        context.previous();
        pcs.firePropertyChange(CHANGE_STATE, null, null);
    }

    
    /** 
     * A funciton that locks the current state
     * @return ErrorLog
     */
    public ErrorLog lock(){
        saveMemento();
        ErrorLog log = context.lock();
        if(log.getErrorType().get(0) == ErrorType.SUCESS){
            pcs.firePropertyChange(LOCK_STRING, null, null);
            pcs.firePropertyChange(CHANGE_STATE, null, null);
        }

        return log;
    }

    
    /**
     * @param o
     * @return boolean
     */
    public boolean insert(Object o){
        saveMemento();
        boolean result = context.insert(o);
        return result;
    }


    
    /** 
     * @param o
     * @return boolean
     */
    public boolean lookup(Object o){
        saveMemento();
        boolean result = context.insert(o);
        return result;
    }

    
    /** 
     * @param o
     * @return boolean
     */
    public boolean edit(Object o){
        saveMemento();
        boolean result = context.insert(o);
        return result;
    }

    
    /** 
     * @param o
     * @return boolean
     */
    public boolean delete(Object o){
        saveMemento();
        boolean result = context.insert(o);
        return result;
    }

    
    /** 
     *A function to import Students
     * @param fileName
     * @param type
     * @return ErrorLog
     */
    public ErrorLog importStudent(String fileName, ImportTypes type){
        saveMemento();
        ErrorLog log = context.importStudent(fileName,type);
        pcs.firePropertyChange(STUDENT_STRING, null, null);
        pcs.firePropertyChange(CANDIDACY_STRING, null, null);
        return log;
    }

    
    /** 
     *A function that returns a list with the import types
     * @return List<ImportTypes>
     */
    public List<ImportTypes> getImports(){
        return context.getImports();
    }

    
    /** 
     *A function that returns a list of students with autoproposals
     * @return List<Student>
     */
    public List<Student> getStudentsAutoProposal(){
        List<Student> clone = new ArrayList<>();
        for(Student s : context.getStudentsAutoProposal()){
            if(s!= null)
                clone.add(s.clone());
        }
        return clone;
    }

    
    /** 
     *A function that returns a list of students with candidacy
     * @return List<Student>
     */
    public List<Student> getStudentsWithCandidacy(){
        List<Student> clone = new ArrayList<>();
        for(Student s : context.getStudentsWithCandidacy()){
            if(s!= null)
                clone.add(s.clone());
        }
        return clone;
    }

    
    /** 
     *A function that returns a list of students without candidacy
     * @return List<Student>
     */
    public List<Student> getStudentsWithoutCandidacy(){
        List<Student> clone = new ArrayList<>();
        for(Student s : context.getStudentsWithoutCandidacy()){
            if(s!= null)
                clone.add(s.clone());
        }
        return clone;
    }

    
    /** 
     *A function that returns a list of students with proposals
     * @return List<Student>
     */
    public List<Student> getStudentsWithProposal(){
        List<Student> clone = new ArrayList<>();
        for(Student s : context.getStudentsWithProposal()){
            if(s!= null)
                clone.add(s.clone());
        }
        return clone;
    }

    
    /** 
     *A function that returns a list of autoproposals
     * @return List<Proposal>
     */
    public List<Proposal> getAutoProposals(){
        List<Proposal> clone = new ArrayList<>();
        for(Proposal p : context.getAutoProposals()){
            if(p!= null)
                clone.add(p.clone());
        }
        return clone;
    }

    
    /** 
     *A function that returns a list of teacher proposals
     * @return List<Proposal>
     */
    public List<Proposal> getTeacherProposals(){
        List<Proposal> clone = new ArrayList<>();
        for(Proposal p : context.getTeacherProposals()){
            clone.add(p.clone());
        }
        return clone;
    }

    
    /** 
     *A function that returns a list of proposals with candidacy
     * @return List<Proposal>
     */
    public List<Proposal> getProposalsWithCandidacy(){
        List<Proposal> clone = new ArrayList<>();
        for(Proposal p : context.getProposalsWithCandidacy()){
            if(p!= null)
                clone.add(p.clone());
        }
        return clone;
    }

    
    /** 
     *A function that returns a list of proposals without candidacy
     * @return List<Proposal>
     */
    public List<Proposal> getProposalsWithoutCandidacy(){
        List<Proposal> clone = new ArrayList<>();
        for(Proposal p : context.getProposalsWithoutCandidacy()){
            if(p!= null)
                clone.add(p.clone());
        }
        return clone;
    }

    
    /** 
    *A function that returns a list of proposals with given filters
     * @param filters
     * @return List<Proposal>
     */
    public List<Proposal> getProposalsFiltered(Phase.Filters... filters){
        List<Proposal> clone = new ArrayList<>();
        for(Proposal p : context.getProposalsFiltered(filters)){
            if(p!= null)
                clone.add(p.clone());
        }
        return clone;
    }

    
    /** 
    *A function that returns a student with a given id
     * @param numStudent
     * @return Student
     */
    public Student getStudentByID(long numStudent){
        return (context.getStudentByID(numStudent) == null ? null : context.getStudentByID(numStudent).clone());
    }

    
    /** 
    *A function that returns a teacher with a given email
     * @param email
     * @return Teacher
     */
    public Teacher getTeacherByEmail(String email){
        return (context.getTeacherByEmail(email) == null ? null : context.getTeacherByEmail(email).clone());
    }

    
    /** 
    *A function that returns a proposal with a given id
     * @param id
     * @return Proposal
     */
    public Proposal getProposalByID(String id){
        return (context.getProposalByID(id) == null ? null : context.getProposalByID(id).clone());
    }

    
    /** 
     * @param context
     */
    void changecontext(PhaseContext context){
        saveMemento();
        this.context = context;
    }

    
    /** 
     *Returns the options that the current state may have
     * @return Options[]
     */
    public Options[] getOptions(){
        return context.getOptions();
    }

    
    /** 
     *Gets the current state
     * @return PhaseState
     */
    public PhaseState getState(){
        return context.getState();
    }

    
    /** 
     *A function that returns the options for the lookup 
     * @return Options[]
     */
    public Options[] query(){
        return context.query();
    }

    
    /** 
    *A function that automatically associates students with proposals
     * @return ErrorLog
     */
    public ErrorLog automaticTeacherStudentProposalAssociation(){
        saveMemento();
        ErrorLog result = context.automaticTeacherStudentProposalAssociation();
        pcs.firePropertyChange(AUTOMATIC_ASSIGN_SUPERVISER_STRING, null, null);
        return result;
    }

    
    /** 
     *A function that returns filters to be used by the lookup function
     * @return List<Filters>
     */
    public List<Filters> filter(){
        saveMemento();
        List<Filters> result = context.filter();
        return result;
    }

    
    /** 
     *A function that gives automatically available proposals
     * @return ErrorLog
     */
    public ErrorLog automaticAvailableProposals(){
        saveMemento();
        ErrorLog result = context.automaticAvailableProposals();
        pcs.firePropertyChange(AUTOMATIC_PPROPOSAL_STRING, null, null);
        return result;
    }

    
    /** 
     *A function that returns a list of students witout proposals
     * @return List<Student>
     */
    public List<Student> getStudentsWithoutProposals(){
        List<Student> clone = new ArrayList<>();
        for(Student s : context.getStudentsWithoutProposals()){
            if(s!= null)
                clone.add(s.clone());
        }
        
        return clone;
    }

    
    /** 
    *A function that returns a list of the available proposals for a given student
     * @param student
     * @return List<Proposal>
     */
    public List<Proposal> getAvailableProposals(Student student){
        List<Proposal> clone = new ArrayList<>();
        for(Proposal p : context.getAvailableProposals(student)){
            if(p!= null)
                clone.add(p.clone());
        }
        return clone;
    }

    
    /** 
    *A function that returns a list with the students that can be assigned to a proposal
     * @return List<Student>
     */
    public List<Student> getAssignableStudents(){
        List<Student> clone = new ArrayList<>();
        for(Student s : context.getAssignableStudents()){
            if(s!= null)
                clone.add(s.clone());
        }
        return clone;
    }

    
    /** 
     *A function that manual assign proposals to a student
     * @param choosenStudent
     * @param choosenProposal
     * @return ErrorLog
     */
    public ErrorLog manualAssignProposal(Student choosenStudent, Proposal choosenProposal){
        saveMemento();
        ErrorLog result = context.manualAssignProposal(choosenStudent,choosenProposal);
        pcs.firePropertyChange(MANAGE_PROPOSALS_STRING, null, null);
        return result;
    }

    
    /**
     *A function that manual remove proposals 
     * @param p
     * @return ErrorLog
     */
    public ErrorLog manualRemoveProposal(Proposal p){
        saveMemento();
        ErrorLog result = context.manualRemoveProposal(p);
        pcs.firePropertyChange(MANAGE_PROPOSALS_STRING, null, null);
        return result;
    }

    
    /** 
    *A function that returns a list of proposals that can be removed
     * @return List<Proposal>
     */
    public List<Proposal> getRemovableProposals(){
        List<Proposal> clone = new ArrayList<>();
        for(Proposal p : context.getRemovableProposals()){
            if(p!= null)
                clone.add(p.clone());
        }
        return clone;
    }

    
    /**
     *A function that automatically associates students with available proposals 
     * @return ErrorLog
     */
    public ErrorLog automaticSuperviserStudentProposalAssociation(){
        saveMemento();
        ErrorLog result = context.automaticSuperviserStudentProposalAssociation();
        pcs.firePropertyChange(AUTOMATIC_ASSIGN_SUPERVISER_STRING, null, null);
        return result;
    }

    
    /** 
    *A function that allows to manually assign supervisors to a specific student     
     * @param student
     * @param email
     * @return ErrorLog
     */
    public ErrorLog manualAssignSuperviser(Student student, String email){
        saveMemento();
        ErrorLog result = context.manualAssignSuperviser(student, email);
        pcs.firePropertyChange(MANAGE_SUPERVISERS_STRING, null, null);
        return result;
    }

    
    /** 
    *A function that returns the superviser of a given student
     * @param student
     * @return Teacher
     */
    public Teacher getSuperviser(Student student){
        Teacher result = (context.getSuperviser(student) == null ? null : context.getSuperviser(student).clone());
        return result;
    }

    
    /** 
     *A function that removes Supervisors from a particular student
     * @param student
     * @return ErrorLog
     */
    public ErrorLog removeSuperviser(Student student){
        saveMemento();
        ErrorLog result = context.removeSuperviser(student);
        pcs.firePropertyChange(MANAGE_SUPERVISERS_STRING, null,null);
        return result;
    }

    
    /**
     *A function that returns a list of students with proposals and a superviser 
     * @return List<Student>
     */
    public List<Student> getStudentsWithProposalAndSuperviser(){
        List<Student> clone = new ArrayList<>();
        for(Student s : context.getStudentsWithProposalAndSuperviser()){
            if(s!= null)
                clone.add(s.clone());
        }
        return clone;
    }

    
    /** 
     *A function that returns a list of students with proposals and without supervisers
     * @return List<Student>
     */
    public List<Student> getStudentsWithProposalAndWithoutSuperviser(){
        List<Student> clone = new ArrayList<>();
        for(Student s : context.getStudentsWithProposalAndWithoutSuperviser()){
            if(s!= null)
                clone.add(s.clone());
        }
        return clone;
    }

    
    /** 
     *A function to manage supervisers
     * @return Options[]
     */
    public Options[] manageSupervisers(){
        saveMemento();
        Options[] result = context.manageSupervisers();
        return result;
    }

    
    /** 
     *A function that returns unassigned supervisers for a given student 
     * @param student
     * @return List<Teacher>
     */
    public List<Teacher> getUnassignedSupervisers(Student student){
        List<Teacher> clone = new ArrayList<>();
        for(Teacher t : context.getUnassignedSupervisers(student)){
            if(t!= null)
                clone.add(t.clone());
        }
        return clone;
    }

    
    /** 
     *A function that returns the number of supervisions by a superviser
     * @return HashMap<String, Long>
     */
    public HashMap<String, Long> getSupervisionNumberBySuperviser(){
        return (HashMap<String, Long>) (context.getSupervisionNumberBySuperviser() == null ? null : context.getSupervisionNumberBySuperviser().clone());
    }

    public List<Internship> getInternships(){
        List<Internship> clone = new ArrayList<>();
        for(Internship t : context.getInternships()){
            if(t!= null)
                clone.add(t.clone());
        }
        return clone;
    }
    
    /** 
     *A function that returns the number of supervisions of the teacher that has the maximum amount
     * @return Long
     */
    //nao precisa de clones uma vez que os objetos que representam variaveis primitivas ja sao imutaveis
    public Long getSuperviserWithMaxSupervisions(){
        return context.getSuperviserWithMaxSupervisions();
    }

    
    /** 
     *A function that returns the number of supervisions of the teacher with the least amount 
     * @return Long
     */
    public Long getSuperviserWithMinSupervisions(){
        return context.getSuperviserWithMinSupervisions();
    }

    
    /** 
     *A function that returns the number of supervisions of the teacher with the average amount
     * @return Double
     */
    public Double getSuperviserWithAvegareSupervisions(){
        return context.getSuperviserWithAvegareSupervisions();
    }

    
    /** 
     *A function that returns the number of supervisions of a given superviser
     * @param teacher
     * @return Long
     */
    public Long getSupervisionsFromSuperviser(Teacher teacher){
        return context.getSupervisionsFromSuperviser(teacher);
    }

    
    /** 
     *A function that returns a list of teachers
     * @return List<Teacher>
     */
    public List<Teacher> getTeachers(){
        List<Teacher> clone = new ArrayList<>();
        for(Teacher t : context.getTeachers()){
            if(t!= null)
                clone.add(t.clone());
        }
        return clone;
    }

    
    /** 
     *A function that returns a list of proposals
     * @return List<Proposal>
     */
    public List<Proposal> getProposals(){
        List<Proposal> clone = new ArrayList<>();
        for(Proposal p : context.getProposals()){
            if(p!= null)
                clone.add(p.clone());
        }
        return clone;
    }

    
    /**
     *A function that returns a list of students with candidacy and without proposals 
     * @return List<Student>
     */
    public List<Student> getStudentsWithCandidacyAndWithoutProposals(){
        List<Student> clone = new ArrayList<>();
        for(Student s : context.getStudentsWithCandidacyAndWithoutProposals()){
            if(s!= null)
                clone.add(s.clone());
        }
        return clone;
    }

    
    /** 
     *A function that returns a list of available proposals
     * @return List<Proposal>
     */
    public List<Proposal> getAvailable(){
        List<Proposal> clone = new ArrayList<>();
        for(Proposal p : context.getAvailable()){
           if(p!= null)
                clone.add(p.clone());
        }
        return clone;
    }

    
    /** 
     *A function that returns a list of assigned proposals
     * @return List<Proposal>
     */
    public List<Proposal> getAssignedProposals(){
        List<Proposal> clone = new ArrayList<>();
        for(Proposal p : context.getAssignedProposals()){
            if(p!= null)
                clone.add(p.clone());
        }
        return clone;
    }

    
    /** 
     *A function that returns a list of candidacy and proposals for a given student 
     * @param student
     * @return List<Proposal>
     */
    public List<Proposal> getCandidacyProposals(Student student){
        List<Proposal> clone = new ArrayList<>();
        for(Proposal p : context.getCandidacyProposals(student)){
            if(p!= null)
                clone.add(p.clone());
        }
        return clone;
    }

    
    /** 
     *A function that returns wether the student have been assined to a superviser
     * @return ErrorLog
     */
    public ErrorLog changeConfirmedStudentsStatus(){
        saveMemento();
        ErrorLog result = context.changeConfirmedStudentsStatus();
        return result;
    }

    
    /** 
     *A function that returns a list of tied students
     * @return List<Student>
     */
    public List<Student> getTiedStudents(){
        List<Student> clone = new ArrayList<>();
        for(Student s : context.getTiedStudents()){
            if(s!= null)
                clone.add(s.clone());
        }
        return clone;
    }

    
    /** 
     *A function that gives the winner the proposal that he wanted
     * @param winner
     * @return ErrorLog
     */
    public ErrorLog solveTie(Student winner){
        saveMemento();
        ErrorLog result = context.solveTie(winner);
        pcs.firePropertyChange(CHANGE_STATE, null, null);
        return result;
    }
    /**
    *A function to export to a csv file
    */
    public void exportToCSV(){
        saveMemento();
        context.exportToCSV();
    }

    /**
    *A function to export to a csv file given a specific name
    */
    public void exportToCSVFile(File file){
        saveMemento();
        context.exportToCSVFile(file);
    }

    
    /** 
     *A function that returns a list of students
     * @return List<Student>
     */
    public List<Student> getStudents(){
        List<Student> clone = new ArrayList<>();
        for(Student s : context.getStudents()){
            if(s!= null)
                clone.add(s.clone());
        }
        return clone;
    }


    //decorator
    static final String FILENAME = "Phase.dat";

    
    /** 
     * @return boolean
     */
    public boolean isLoadFilePresent(){
        File f = new File(FILENAME);
        if(f.exists())
            return true;

        return false;
    }
    /**
     *A function to load from a save
     */
    public void load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))){
            context = (PhaseContext) ois.readObject();
        }
        catch(Exception e){
            System.err.println("Erro no load");
        }
    }
    /**
     *A function to save
     */
    public void save() {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(context);
        }
        catch (Exception e){
            System.err.println("Erro no save");
        }
    }

    
    /** 
     *A function that saves all data to a given file
     * @param file
     */
    public void saveFile(File file){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(context);
        }
        catch (Exception e){
            System.err.println("Erro no save");
        }
    }

    
    /**
     *A function that loads all data from a given file 
     * @param file
     */
    public void loadFile(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            context = (PhaseContext) ois.readObject();
            pcs.firePropertyChange(LOAD_STRING, null, null);
            pcs.firePropertyChange(CHANGE_STATE, null, null);
        }
        catch(Exception e){
            System.err.println("Erro no load");
        }
    }

    //para o memento
    /**
    *A function to save a snapshot
    */
    public void saveMemento() {
        redoHist.clear();
        history.push(originator.save());
        pcs.firePropertyChange(UNDO_REDO_STRING, null, null);
    }
    /**
     *A function that undo the last operation
     */
    public void undo() {
        if (history.isEmpty())
            return;
        redoHist.push(originator.save());
        originator.restore(history.pop());
        pcs.firePropertyChange(LOAD_STRING, null, null);
    }
    /**
    *A function that redo the last operation
    */
    public void redo() {
        if (redoHist.isEmpty())
            return;
        history.push(originator.save());
        originator.restore(redoHist.pop());
        pcs.firePropertyChange(LOAD_STRING, null, null);
    }
    public void reset() {
        history.clear();
        redoHist.clear();
    }
    
    /** 
     * @return boolean
     */
    public boolean hasUndo() {
        return !history.isEmpty();
    }
    
    /** 
     * @return boolean
     */
    public boolean hasRedo() {
        return !redoHist.isEmpty();
    }
}
