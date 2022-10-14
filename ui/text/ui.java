package pt.isec.pa.apoio_poe.ui.text;

import pt.isec.pa.apoio_poe.model.PhaseManager;
import pt.isec.pa.apoio_poe.model.data.Proposal;
import pt.isec.pa.apoio_poe.model.data.Student;
import pt.isec.pa.apoio_poe.model.data.Teacher;
import pt.isec.pa.apoio_poe.model.data.Phase.Filters;
import pt.isec.pa.apoio_poe.model.errors.ErrorType;
import pt.isec.pa.apoio_poe.model.fsm.ImportTypes;
import pt.isec.pa.apoio_poe.model.fsm.Options;
import pt.isec.pa.apoio_poe.ui.PAInput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class ui {
    PhaseManager pm;

    public ui(PhaseManager pm){
        this.pm = pm;
    }

    public int buildOptionMenu(Object[] options,boolean isLooping){
        
        System.out.println("\n***** Menu: *****\n");
        int input;
        do{
            int i = 1;
            for(Object option : options){
                System.out.print((i++) + " => ");
                System.out.println(option);
                System.out.println("");
            }
            System.out.printf("(The option number has to be between 1 and %d)\n", options.length);
            input = getUserInput();
            if(!isLooping && (input > options.length || input <=0) )
                return -1;
        }while(input > options.length || input <=0);
        return input;
    }

    public int getUserInput(){
        int input = PAInput.readInt("Option => ");
        return input;
    }

    public void printMap(HashMap<?,?> map){
       Set<?> keys = map.keySet();

       for(Object key : keys){
           System.out.printf("%s : %s\n",key,map.get(key));
       }

    }

    public int parseInput(int option,Options[] rcvOptions){
        final Options[] options = Options.values();
        
        switch (options[rcvOptions[option-1].ordinal()]) {
            case IMPORT->{
                List<ImportTypes> imports = pm.getImports();
                if(imports.isEmpty()){
                    System.out.println("The import return empty");
                    break;
                }
                int input = buildOptionMenu(imports.toArray(),true);
                ImportTypes choosenImport = imports.get(input-1);
                System.out.println("Enter the CSV filename: ");
                for(ErrorType e : pm.importStudent(PAInput.readString("", true), choosenImport).getErrorType()){
                    System.out.println(e);
                }

            }
            case NEXT->{
                System.out.println("Changing to the next state");
                pm.next();
            }
            case PREVIOUS->{
                System.out.println("Going back to the last state");
                pm.previous();
            }
            case LOCK->{
                System.out.println("Locking the state");
                for(ErrorType e : pm.lock().getErrorType()){
                    System.out.println(e);
                }
            }
            case INSERT->{

            }
            case LOOKUP->{
                int input = buildOptionMenu(pm.query(),true);
                parseInput(input,pm.query());
            }
            case EDIT->{

            }
            case DELETE->{

            }
            case GET_STUDENT_AUTOPROPOSAL->{
                System.out.println("Students with autoproposal: ");
               for(Student s : pm.getStudentsAutoProposal()){
                   System.out.println(s);
               }

            }
            case GET_STUDENTS_WITH_CANDIDACY->{
                System.out.println("Students with candidacy: ");
                for(Student s : pm.getStudentsWithCandidacy()){
                    System.out.println(s);
                }

            }
            case GET_STUDENTS_WITHOUT_CANDIDACY->{
                System.out.println("Students without candidacy: ");
                for(Student s : pm.getStudentsWithoutCandidacy()){
                    System.out.println(s);
                }
            }
            case GET_AUTOPROPOSAL->{
                System.out.println("Showing the autoproposals: ");
                for(Proposal p : pm.getAutoProposals()){
                    System.out.println(p);
                }
            }
            case GET_TEACHER_PROPOSAL->{
                System.out.println("Showing the teachers proposal: ");
                for(Proposal p : pm.getTeacherProposals()){
                    System.out.println(p);
                }
            }
            case GET_PROPOSALS_WITH_CANDIDACY->{
                System.out.println("Showing the proposals with candidacy: ");
                for(Proposal p : pm.getProposalsWithCandidacy()){
                    System.out.println(p);
                }
            }
            case GET_PROPOSALS_WITHOUT_CANDIDACY->{
                System.out.println("Showing the proposals without candidacy: ");
                for(Proposal p : pm.getProposalsWithoutCandidacy()){
                    System.out.println(p);
                }
            }
            case GET_PROPOSALS_FILTERED->{
                System.out.println("Choose one or multiple filters (-1 to stop): ");
                List<Filters> filters = pm.filter();
                List<Filters> choosenFilters = new ArrayList<Filters>();
                int i = 0;
                do {
                    int input = buildOptionMenu(filters.toArray(),false);
                    if(input>0){
                        choosenFilters.add(filters.get(input-1));
                        filters.remove(input-1);
                    }
                    if(filters.isEmpty() || input == -1){
                        break;
                    }
                    
                } while (i != -1);
                for(Proposal p : pm.getProposalsFiltered(choosenFilters.toArray(new Filters[0]))){
                    System.out.println(p);
                }
            }
            case GET_STUDENT_BY_ID->{
                Long numStudent = Long.valueOf(PAInput.readInt("Write a number to search the student with specific ID: "));
                Student studentToPrint = pm.getStudentByID(numStudent);
                if(studentToPrint != null)
                    System.out.println(studentToPrint);
                else
                    System.out.println("There isnt a student with that ID");
            }
            case GET_TEACHER_BY_EMAIL->{
                String email = PAInput.readString("Type an email to search the Teacher: ", true);
                Teacher teacherToPrint = pm.getTeacherByEmail(email);
                if(teacherToPrint != null)
                    System.out.println(teacherToPrint);
                else
                    System.out.println("There isnt a teacher with that email");
            }
            case GET_PROPOSALS_BY_ID->{
                String id = PAInput.readString("Type an id to show the proposals: ", true);
                Proposal proposalToPrint = pm.getProposalByID(id);
                if(proposalToPrint != null)
                    System.out.println(proposalToPrint);
                else
                    System.out.println("There isnt a proposal with that id");
            }
            case GET_STUDENTS_WITH_ASSIGNED_PROPOSAL->{
                System.out.println("Showing students with an assigned proposal: ");
                for(Student s : pm.getStudentsWithProposal()){
                    System.out.println(s);
                }
            }
            case GET_STUDENTS_WITHOUT_ASSIGNED_PROPOSAL->{
                System.out.println("Showing students without assigned proposal: ");
                for(Student s : pm.getStudentsWithoutProposals()){
                    System.out.println(s);
                }
            }
            case AUTOMATIC_PROPOSALS->{
                for(ErrorType error : pm.automaticAvailableProposals().getErrorType() ){
                    System.out.println(error);
                }
            }
            case AUTOMATIC_PROPOSALS_ASSOCIATION->{
                pm.changeConfirmedStudentsStatus();
            }
            case GET_CANDIDACY_PROPOSALS->{
                List<Student> tiedStudents = pm.getTiedStudents();
                if(tiedStudents.isEmpty()){
                    System.out.println("There are no tied students");
                }
                int input = buildOptionMenu(tiedStudents.toArray(),true);
                Student choosenStudent = tiedStudents.get(input-1);
                System.out.printf("Showing candidatures related to: %s",choosenStudent);
                for(Proposal p : pm.getCandidacyProposals(choosenStudent)){
                    System.out.println(p);
                }
            }
            case MANUAL_ASSIGNED_PROPOSALS ->{
                List<Student> studentsWithoutProposal = pm.getAssignableStudents(); 
                if(studentsWithoutProposal.isEmpty()){
                    System.out.println("There are no students to assign proposals to");
                    break;
                }
                int input = buildOptionMenu(studentsWithoutProposal.toArray(),true);
                Student choosenStudent = studentsWithoutProposal.get(input-1);
                List<Proposal> availableProposals = pm.getAvailableProposals(choosenStudent);
                if(availableProposals.isEmpty()){
                    System.out.println("There are no available proposals");
                    break;
                }
                input = buildOptionMenu(availableProposals.toArray(),true);
                Proposal choosenProposal = availableProposals.get(input-1);
                for(ErrorType e : pm.manualAssignProposal(choosenStudent,choosenProposal).getErrorType()){
                    System.out.println(e);
                }
               
            }
            case MANUAL_REMOVE_PROPOSALS->{
                List<Proposal> removableProposals = pm.getRemovableProposals();
                if(removableProposals.isEmpty()){
                    System.out.println("There are no students to remove proposals");
                    break;
                }
                int input = buildOptionMenu(removableProposals.toArray(),true);
                pm.manualRemoveProposal(removableProposals.get(input-1)); 

            }
            case AUTOMATIC_SUPERVISERS->{
                for(ErrorType e : pm.automaticSuperviserStudentProposalAssociation().getErrorType()){
                    System.out.println(e);
                }
            }
            case MANUAL_ASSIGN_SUPERVISER->{
                List<Student> studentsWithoutSuperviser = pm.getStudentsWithProposalAndWithoutSuperviser();
                if(studentsWithoutSuperviser.isEmpty()){
                    System.out.println("There are no students without supervisers");
                    break;
                }
                int input = buildOptionMenu(studentsWithoutSuperviser.toArray(),true);
                Student choosenStudent= studentsWithoutSuperviser.get(input-1);
                System.out.println("Choose a superviser to assign: ");
                List<Teacher> unassignedSupervisers = pm.getUnassignedSupervisers(choosenStudent);
                if(unassignedSupervisers.isEmpty()){
                    System.out.println("there are no unassigned supersivers");
                    break;
                }
                input = buildOptionMenu(unassignedSupervisers.toArray(),true);
                pm.manualAssignSuperviser(choosenStudent, unassignedSupervisers.get(input-1).getEmail());

            }
            case MANUAL_REMOVE_SUPERVISER->{
                List<Student> studentsWithSuperviser = pm.getStudentsWithProposalAndSuperviser();
                if(studentsWithSuperviser.isEmpty()){
                    System.out.println("There are no students without supervisers");
                    break;
                }
                int input = buildOptionMenu(studentsWithSuperviser.toArray(),true);
                Student choosenStudent= studentsWithSuperviser.get(input-1);
                pm.removeSuperviser(choosenStudent);
            }
            case LOOKUP_SUPERVISER->{
                List<Student> studentsWithSuperviser = pm.getStudentsWithProposalAndSuperviser();
                if(studentsWithSuperviser.isEmpty()){
                    System.out.println("There are no students with proposal and superviser");
                    break;
                }
                int input = buildOptionMenu(studentsWithSuperviser.toArray(),true);
                Student choosenStudent= studentsWithSuperviser.get(input-1);
                System.out.println(pm.getSuperviser(choosenStudent));
            }
            case EDIT_SUPERVISER->{
                List<Student> studentsWithSuperviser = pm.getStudentsWithProposalAndSuperviser();
                if(studentsWithSuperviser.isEmpty()){
                    System.out.println("There are no students with proposal and superviser");
                    break;
                }
                int input = buildOptionMenu(studentsWithSuperviser.toArray(),true);
                Student choosenStudent= studentsWithSuperviser.get(input-1);
                System.out.println("Choose a superviser to exchange for: ");
                List<Teacher> unassignedSupervisers = pm.getUnassignedSupervisers(choosenStudent);
               if(unassignedSupervisers.isEmpty()){
                   System.out.println("There are no unasssigned supervisers");
                   break;
               }
                input = buildOptionMenu(unassignedSupervisers.toArray(),true);
                pm.manualAssignSuperviser(choosenStudent, unassignedSupervisers.get(input-1).getEmail());
            }
            case GET_STUDENTS_WITH_ASSIGNED_PROPOSAL_AND_SUPERVISER ->{
                System.out.println("Showing the students with assigned proposal and superviser: ");
                for(Student s : pm.getStudentsWithProposalAndSuperviser()){
                    System.out.println(s);
                }
            }
            case GET_STUDENTS_WITH_ASSIGNED_PROPOSAL_AND_WITHOUT_SUPERVISER->{
                System.out.println("Showing students with assigned proposal and without superviser: ");
                for(Student s : pm.getStudentsWithProposalAndWithoutSuperviser()){
                    System.out.println(s);
                }
            }
            case GET_NUMBER_OF_SUPERVISIONS_FOREACH_SUPERVISOR->{
                System.out.println("SUPERVISOR : SUPERVISIONS");
                printMap(pm.getSupervisionNumberBySuperviser());
            }
            case GET_MAX_SUPERVISIONS->{
                System.out.println("Max supervisions: " + pm.getSuperviserWithMaxSupervisions());
            }
            case GET_AVERAGE_SUPERVISIONS->{
                System.out.println("Average supervisions: " + pm.getSuperviserWithAvegareSupervisions());
            }
            case GET_MIN_SUPERVISIONS->{
                System.out.println("Minimum supervisions: " + pm.getSuperviserWithMinSupervisions());
            }
            case GET_SUPERVISIONS_BY_SUPERVISER->{
                List<Teacher> supervisers = pm.getTeachers();
                if(supervisers.isEmpty()){
                    System.out.printf("There are no supervisers");
                    break;
                }
                int input = buildOptionMenu(supervisers.toArray(),true);
                Teacher choosenTeacher = supervisers.get(input-1);
                if(choosenTeacher != null)
                    System.out.printf("The superviser %s(%s) has %d supervisions\n",choosenTeacher.getName(),choosenTeacher.getEmail(),pm.getSupervisionsFromSuperviser(choosenTeacher));
                else
                System.out.printf("The superviser doesnt have any supervisions");
            }
            case MANAGE_SUPERVISERS ->{
                int input = buildOptionMenu(pm.manageSupervisers(),true);
                parseInput(input,pm.manageSupervisers());
            }
            case GET_ASSIGNED_PROPOSALS->{
                System.out.println("Assigned proposals: ");
                for(Proposal p : pm.getAssignedProposals()){
                    System.out.println(p);
                }
            }
            case GET_AVAILABLE_PROPOSALS->{
                System.out.println("Available proposals: ");
                for(Proposal p : pm.getAvailable()){
                    System.out.println(p);
                }
            }
            case GET_STUDENTS_WITH_CANDIDACY_AND_WITHOUT_PROPOSAL->{
                System.out.println("Students with candidacy and without proposal: ");
                for(Student s : pm.getStudentsWithCandidacyAndWithoutProposals()){
                    System.out.println(s);
                }
            }
            case SOLVE_TIE->{
                List<Student> tiedStudents = pm.getTiedStudents();
                for(Student s : tiedStudents){
                    System.out.println(s);
                }
                if(tiedStudents.isEmpty()){
                    System.out.println("There are no tied students");
                    break;
                }
                int input = buildOptionMenu(tiedStudents.toArray(),true);
                System.out.println("Choose a student to solve the tie:");
                Student choosenStudent = tiedStudents.get(input-1);
                pm.solveTie(choosenStudent);
                pm.automaticAvailableProposals();

            }
            case EXPORT->{
                System.out.println("Exporting to CSV file...");
                pm.exportToCSV();
            }
            case SAVE->{
                System.out.println("Saving the data...");
                pm.save();
            }
            case LOAD-> {
                System.out.println("Loading the data...");
                pm.load();
            }
            case EXIT -> {
                System.exit(0);
            }
            case UNDO -> {
                System.out.println("Undo the last operation...");
                pm.undo();
            }
            case REDO -> {
                System.out.println("Redo the last operation...");
                pm.redo();
            }
        }
        return 0;
    }

    public void start(){
        if(pm.isLoadFilePresent()){
            String choice = new String();
            do{
               choice = PAInput.readString("Save located. Do you wish to load it?(y/n)", true);

            }while(!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n"));
            if(choice.equalsIgnoreCase("y")){
                pm.load();
                System.out.println("Loaded sucessfully");
            }

        }
        do{
            Options[] options = pm.getOptions();
            int input = buildOptionMenu(options,true);
            parseInput(input, options);
            
        }while(true);
    }

}
