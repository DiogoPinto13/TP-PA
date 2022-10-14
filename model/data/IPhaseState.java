package pt.isec.pa.apoio_poe.model.data;

import java.io.File;
import java.util.List;
import pt.isec.pa.apoio_poe.model.data.Proposal;
import pt.isec.pa.apoio_poe.model.data.Student;
import pt.isec.pa.apoio_poe.model.data.Teacher;
import pt.isec.pa.apoio_poe.model.data.Phase.Filters;
import pt.isec.pa.apoio_poe.model.errors.ErrorLog;
import pt.isec.pa.apoio_poe.model.fsm.ImportTypes;
import pt.isec.pa.apoio_poe.model.fsm.Options;
import pt.isec.pa.apoio_poe.model.fsm.PhaseState;

public interface IPhaseState {
    boolean insert(Object o);
    Options[] query();
    boolean edit(Object o);
    boolean delete(Object o);
    boolean getLock();
    ErrorLog importStudent(String fileName, ImportTypes type);

    List<Student> getStudentsWithProposal();
    List<Student> getStudentsAutoProposal();
    List<Student> getStudentsWithCandidacy();
    List<Student> getStudentsWithoutCandidacy();
    List<Proposal> getAutoProposals();
    List<Proposal> getTeacherProposals();
    List<Proposal> getProposalsWithCandidacy();
    List<Proposal> getProposalsWithoutCandidacy();
    Student getStudentByID(long numStudent);
    Teacher getTeacherByEmail(String email);
    Proposal getProposalByID(String id);
    PhaseState getState();
    void nextState();
    void previousState();
    List<Proposal> getProposalsFiltered(Filters... filters);
    Options[] getPossibleOptions();
    ErrorLog automaticTeacherStudentProposalAssociation();
    ErrorLog setLock();
    ErrorLog automaticAvailableProposals();
    List<Student> getStudentsWithoutProposals();
    ErrorLog manualAssignProposal(Student student, Proposal proposal);
    ErrorLog manualRemoveProposal(Proposal p);
    ErrorLog automaticSuperviserStudentProposalAssociation();
    ErrorLog manualAssignSuperviser(Student student, String email);
    Teacher getSuperviser(Student student);
    ErrorLog removeSuperviser(Student student);
    List<Student> getStudentsWithProposalAndSuperviser();
    List<Student> getStudentsWithProposalAndWithoutSuperviser();
    Options[] manageSupervisers();
    List<Student> getTiedStudents();
    void exportToCSV();
    void exportToCSVFile(File file);
    List<Filters> filter();
    List<ImportTypes> getImports();
    ErrorLog solveTie(Student winner);
}
