package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import pt.isec.pa.apoio_poe.model.errors.ErrorLog;
import pt.isec.pa.apoio_poe.model.errors.ErrorType;
import pt.isec.pa.apoio_poe.model.fsm.ImportTypes;

public class Phase implements Serializable{

    private ArrayList<Project> projects;
    private Tie tieData;
    private ArrayList<Internship> internships;
    private ArrayList<Teacher> teachers;
    private ArrayList<Student> students;
    private ArrayList<Student> studentsNeedingManualProposal;
    private ArrayList<Autoproposal> autoproposals;
    private ArrayList<Candidacy> candidacies;
    private HashMap<Branches,HashMap<ImportTypes,Integer>> numbersByBranch;
    private HashMap<Confirmations,List<Proposal>> confirmedStudents;
    private HashMap<Confirmations,List<Proposal>> confirmedTeachers;


    public Phase(){
 
        teachers = new ArrayList<Teacher>();
        students = new ArrayList<Student>();
        projects = new ArrayList<Project>();
        internships = new ArrayList<Internship>();
        autoproposals = new ArrayList<Autoproposal>();
        candidacies = new ArrayList<Candidacy>();
        studentsNeedingManualProposal = new ArrayList<Student>();
        tieData = new Tie();

        numbersByBranch = new HashMap<Branches,HashMap<ImportTypes,Integer>>();

        HashMap<ImportTypes,Integer> temp = new HashMap<ImportTypes,Integer>();
        temp.put(ImportTypes.PROPOSAL, 0);
        temp.put(ImportTypes.STUDENT, 0);

        numbersByBranch.put(Branches.DA, new HashMap<ImportTypes,Integer>(temp));
        numbersByBranch.put(Branches.DA, new HashMap<ImportTypes,Integer>(temp));
        numbersByBranch.put(Branches.SI, new HashMap<ImportTypes,Integer>(temp));
        numbersByBranch.put(Branches.SI, new HashMap<ImportTypes,Integer>(temp));
        numbersByBranch.put(Branches.RAS, new HashMap<ImportTypes,Integer>(temp));
        numbersByBranch.put(Branches.RAS, new HashMap<ImportTypes,Integer>(temp));



        confirmedStudents = new HashMap<Confirmations,List<Proposal>>();
        confirmedStudents.put(Confirmations.CONFIRMED,new ArrayList<Proposal>());
        confirmedStudents.put(Confirmations.NOT_CONFIRMED,new ArrayList<Proposal>());

        confirmedTeachers = new HashMap<Confirmations,List<Proposal>>();
        confirmedTeachers.put(Confirmations.CONFIRMED,new ArrayList<Proposal>());
        confirmedTeachers.put(Confirmations.NOT_CONFIRMED,new ArrayList<Proposal>());

    }

    public Tie getTie(){
        return tieData;
    }

    public List<Internship> getInternships(){
        return internships;
    }

    public void initializeTie(Student one, Student two,Proposal p){
        tieData.setFirstStudent(one);
        tieData.setSecondStudent(two);
        tieData.setProposal(p);
    }

    public HashMap<Branches,HashMap<ImportTypes,Integer>> getBranchStatistics(){
        return numbersByBranch;
    }

    public List<Candidacy> getCandidacy(){
        return candidacies;
    }

    public HashMap<Confirmations,List<Proposal>> getConfirmedStudents(){
        return confirmedStudents;
    }

    public HashMap<Confirmations,List<Proposal>> getConfirmedTeachers(){
        return confirmedTeachers;
    }


    public List<Proposal> getRemovableProposals(){
        List<Proposal> result = new ArrayList<Proposal>();
        changeConfirmedStudentsStatus();
        for(Proposal p : confirmedStudents.get(Confirmations.CONFIRMED)){
            if(!p.isStudentNumberPreviouslyAssociated()){                
                result.add(p);
            }
        }
        return result;
    }

    public List<Proposal> getCandidacyProposals(Student student){
        List<Proposal> result = new ArrayList<Proposal>();
        Candidacy candidacy = candidacies.stream().filter(obj->obj.getNumStudent() == getStudentByID(student.getNumStudent()).getNumStudent()).findFirst().orElse(null);
        if(student != null){
            if(student.isAccessInternship()){
                for(Proposal p : internships){
                    if(candidacy.getPreferences().stream().filter(obj->obj.equals(p.getId())).findFirst().orElse(null) != null){
                        result.add(p);
                    }
                }
            }

            for(Proposal p : projects){
                if(candidacy.getPreferences().stream().filter(obj->obj.equals(p.getId())).findFirst().orElse(null) != null){
                    result.add(p);
                }
            }

            for(Proposal p : autoproposals){
                if(candidacy.getPreferences().stream().filter(obj->obj.equals(p.getId())).findFirst().orElse(null) != null){
                    result.add(p);
                }
            }
        }
        return result;
    }

    public ErrorLog removeAssignedProposal(Proposal p){
        if(!getProposals().contains(p)){
            return new ErrorLog(ErrorType.PROPOSAL_DOESNT_EXIST);
        }
        if(confirmedStudents.get(Confirmations.CONFIRMED).contains(p)){
            Student s = students.stream().filter(obj->obj.getNumStudent() == getProposalById(p.getId()).getNumStudent()).findFirst().orElse(null);
            if(s != null){
                studentsNeedingManualProposal.add(s);

            getProposalById(p.getId()).setNumStudent(0);
            confirmedStudents.get(Confirmations.CONFIRMED).remove(getProposalById(p.getId()));
            if(confirmedTeachers.get(Confirmations.CONFIRMED).contains(p)){
                confirmedTeachers.get(Confirmations.CONFIRMED).remove(getProposalById(p.getId()));
            }
            return new ErrorLog(ErrorType.SUCESS);
            }
        }
        return new ErrorLog(ErrorType.STUDENT_DOESNT_HAVE_PROPOSAL_ASSIGNED);
    }

    public List<Proposal> getAssignedProposals(){
        List<Proposal> result = new ArrayList<Proposal>();
        for(Proposal p : confirmedStudents.get(Confirmations.CONFIRMED)){
            result.add(p);
        }
        return result;
    }

    public ArrayList<Proposal> getAvailableProposals(Student student){
        ArrayList<Proposal> availableProposals = new ArrayList<>();
        if(getStudentByID(student.getNumStudent()).isAccessInternship()){
            for(Proposal p : internships){
                if(!confirmedStudents.get(Confirmations.CONFIRMED).contains(p))
                    if(p.getNumStudent() == 0){
                        if(p.getBranch().contains(getStudentByID(student.getNumStudent()).getSiglaBranch())){
                            availableProposals.add(p);
                        }
                    }
            }
        }

        for(Proposal p : projects){
            if(!confirmedStudents.get(Confirmations.CONFIRMED).contains(p))
                if(p.getNumStudent() == 0){
                    if(p.getBranch().contains(getStudentByID(student.getNumStudent()).getSiglaBranch())){
                        availableProposals.add(p);
                    }
                }
        }

        return availableProposals;
    }
    
    public List<Teacher> getUnassignedSupervisers(Student student){
        List<Teacher> result = new ArrayList<Teacher>();
        Proposal p = confirmedStudents.get(Confirmations.CONFIRMED).stream().filter(obj->obj.getNumStudent() == getStudentByID(student.getNumStudent()).getNumStudent()).findFirst().orElse(null);
        if(p != null){
            Teacher teacher = teachers.stream().filter(obj->obj.getEmail().equals(p.getSuperviser())).findFirst().orElse(null);
            if(teacher == null){
                return teachers;
            } else{
                for(Teacher t : teachers){
                    if(!t.equals(teacher)){
                        result.add(t);
                    }
                }
            }
        }

        return result;
    }

    public HashMap<Student,Integer> proposalCompetition(Proposal mainProposal, Student mainStudent,Candidacy mainCandidacy){
        HashMap<Student,Integer> result = new HashMap<Student,Integer>();
        for(int i = students.indexOf(mainStudent) + 1; i < students.size();i++){
            int aux = i;
            Candidacy candidacy = candidacies.stream().filter(a->a.getNumStudent() == students.get(aux).getNumStudent()).findFirst().orElse(null);
            if(candidacy == null){
                continue;
            }
            if(candidacy.getPreferences().contains(mainProposal.getId())){
                if(candidacy.getPreferences().indexOf(mainProposal.getId()) < mainCandidacy.getPreferences().indexOf(mainProposal.getId()) ){
                    mainStudent = students.get(i);
                } 
                else if( candidacy.getPreferences().indexOf(mainProposal.getId()) > mainCandidacy.getPreferences().indexOf(mainProposal.getId()) ){
                    continue;
                } 
                else{
                    if(mainStudent.getClassification() < students.get(i).getClassification()){
                        mainStudent = students.get(i);
                    } 
                    else if(mainStudent.getClassification() == students.get(i).getClassification()){
                        initializeTie(mainStudent, students.get(i),mainProposal);
                        result.put(students.get(i), 1);
                        return result;
                    }
                    else{
                        continue;
                    }
                }
            } 
            else {
                continue;
            } 
        }
        result.put(mainStudent, 0);
        return result;
    }

    public List<Student> getStudentsWithProposalAndSuperviser(){
        List<Student> result = new ArrayList<Student>();
        for(Proposal p : confirmedStudents.get(Confirmations.CONFIRMED)){
            Proposal found = confirmedTeachers.get(Confirmations.CONFIRMED).stream().filter(obj -> obj.getId().equals(p.getId())).findFirst().orElse(null);
            if(found != null){
                Student student = students.stream().filter(obj -> obj.getNumStudent() == p.getNumStudent()).findFirst().orElse(null);
                if(student != null){
                    result.add(student);
                }
            }
        }
        return result;
    }

    public List<Student> getStudentsWithProposalAndWithoutSuperviser(){
        List<Student> result = new ArrayList<Student>();
        changeConfirmedStudentsStatus();
        for(Proposal p : confirmedStudents.get(Confirmations.CONFIRMED)){
            
            Proposal found = confirmedTeachers.get(Confirmations.CONFIRMED).stream().filter(obj -> obj.getId().equals(p.getId())).findFirst().orElse(null);
            if(found == null){
                Student student = students.stream().filter(obj -> obj.getNumStudent() == p.getNumStudent()).findFirst().orElse(null);
                if(student != null){
                    result.add(student);
                }
            }
        }
        return result;
    }

    public ErrorLog setSuperviser(Student student, String email){
        if(teachers.stream().filter(obj->obj.getEmail().equals(email)).findFirst().orElse(null) != null){
            Proposal found = confirmedStudents.get(Confirmations.CONFIRMED).stream().filter(obj->obj.getNumStudent() == getStudentByID(student.getNumStudent()).getNumStudent()).findFirst().orElse(null);
            if(found != null){
                found.setSuperviser(email);
                Proposal isInConfirmedTeachers = confirmedTeachers.get(Confirmations.CONFIRMED).stream().filter(obj->obj.getId().equals(found.getId())).findFirst().orElse(null);
                if(isInConfirmedTeachers== null){
                    confirmedTeachers.get(Confirmations.CONFIRMED).add(found);
                }
                return new ErrorLog(ErrorType.SUCESS);
            } else {
                return new ErrorLog(ErrorType.STUDENT_DOESNT_HAVE_PROPOSAL_ASSIGNED);
            }
        }
        return new ErrorLog(ErrorType.TEACHER_NOT_PRESENT);
    }

    public HashMap<String, Long> getSupervisionNumberBySuperviser(){
        HashMap<String, Long> result = new HashMap<String, Long>();

        for(Teacher t : teachers){
            Long numberOfSupervisions = confirmedTeachers.get(Confirmations.CONFIRMED).stream().filter(obj->obj.getSuperviser().equals(t.getEmail())).count();
            result.put(t.getEmail(),numberOfSupervisions);
        }

        return result;

    }
    public Long getSuperviserWithMaxSupervisions(){
        HashMap<String, Long> supervisions = getSupervisionNumberBySuperviser();
        return supervisions.get(Collections.max(supervisions.entrySet(),Map.Entry.comparingByValue()).getKey());

    }
    public Long getSuperviserWithMinSupervisions(){
        HashMap<String, Long> supervisions = getSupervisionNumberBySuperviser();
        return supervisions.get(Collections.min(supervisions.entrySet(),Map.Entry.comparingByValue()).getKey());
    }

    public Double getSuperviserWithAvegareSupervisions(){
        HashMap<String, Long> supervisions = getSupervisionNumberBySuperviser();
        return supervisions.entrySet().stream().mapToDouble(a->a.getValue()).average().orElse(0);
    }

    public Long getSupervisionsFromSuperviser(Teacher teacher){
        return getSupervisionNumberBySuperviser().get(getTeacherByEmail(teacher.getEmail()).getEmail());
    }

    public Teacher getSuperviser(Student student){
        Proposal found = confirmedStudents.get(Confirmations.CONFIRMED).stream().filter(obj->obj.getNumStudent() == getStudentByID(student.getNumStudent()).getNumStudent()).findFirst().orElse(null);
        if(found != null){
            return teachers.stream().filter(obj->obj.getEmail().equals(found.getSuperviser())).findFirst().orElse(null);
        }
        return null;
    }



    public ErrorLog removeSuperviser(Student student){
        Proposal found = confirmedStudents.get(Confirmations.CONFIRMED).stream().filter(obj->obj.getNumStudent() == getStudentByID(student.getNumStudent()).getNumStudent()).findFirst().orElse(null);
        if(found != null){
            if(found.getSuperviser().equals("")){
                return new ErrorLog(ErrorType.PROPOSAL_HAS_NO_SUPERVISER);
            }
            found.setSuperviser("");
            studentsNeedingManualProposal.add(getStudentByID(student.getNumStudent()));
            confirmedTeachers.get(Confirmations.CONFIRMED).remove(found);
            return new ErrorLog(ErrorType.SUCESS);
        }
        return new ErrorLog(ErrorType.STUDENT_DOESNT_HAVE_PROPOSAL_ASSIGNED);
    }

    public ErrorLog manualAssignProposal(Student student, Proposal proposal){
        ErrorLog e = new ErrorLog();
        if(!students.contains(student)){
            e.addErrorType(ErrorType.STUDENT_NOT_PRESENT);
        }

        if(!getProposals().contains(proposal)){
            e.addErrorType(ErrorType.PROPOSAL_DOESNT_EXIST);
            return e;
        }
        getProposalById(getProposalById(proposal.getId()).getId()).setNumStudent(getStudentByID(student.getNumStudent()).getNumStudent());
        confirmedStudents.get(Confirmations.CONFIRMED).add(getProposalById(getProposalById(proposal.getId()).getId()));
        studentsNeedingManualProposal.remove(getStudentByID(getStudentByID(student.getNumStudent()).getNumStudent()));

        e.addErrorType(ErrorType.SUCESS);
        return e ;
    }

    public Student getStudentByID(long numStudent){
        return students.stream().filter(student->student.getNumStudent() == numStudent).findFirst().orElse(null);
    }
    public boolean isStudentPresent(long numStudent){
        for(Student s : students){
            if(s.getNumStudent() == numStudent){
                return true;
            }
        } 
        return false;
    }
    public boolean hasAutoproposal(Long studentNum){
        if(autoproposals.stream().filter(obj->obj.getNumStudent() == studentNum).findAny().orElse(null) == null){
            return false;
        }
        return true;
    }

    public boolean isTeacherPresent(String email){
        for(Teacher t : teachers){
            if(t.getEmail().equals(email)){
                return true;
            }
        }
        return false;
    }
    
    public ErrorLog changeConfirmedStudentsStatus(){
        for(Proposal p : confirmedStudents.get(Confirmations.NOT_CONFIRMED)){
            confirmedStudents.get(Confirmations.CONFIRMED).add(p);
        }
        confirmedStudents.get(Confirmations.NOT_CONFIRMED).clear();
       return new ErrorLog(ErrorType.SUCESS);
    }

    public ErrorLog changeConfirmedTeachersStatus(){
        for(Proposal p : confirmedTeachers.get(Confirmations.NOT_CONFIRMED)){
            if(p.getClass().equals(Project.class)){
                confirmedTeachers.get(Confirmations.CONFIRMED).add(p);
                p.setSuperviser(((Project)p).getTeacher());
                
            }
        }
        return new ErrorLog(ErrorType.SUCESS);
    }

    public Teacher getTeacherByEmail(String email){
        return teachers.stream().filter(teacher->teacher.getEmail().equals(email)).findFirst().orElse(null);
    }

    public List<Teacher> getTeachers(){
        return teachers;
    }

    public boolean insert(Student student){
        if(students.contains(student)){
            return false;
        }
        students.add(student);
        studentsNeedingManualProposal.add(student);
        numbersByBranch.get(Branches.valueOf(student.getSiglaBranch())).put(ImportTypes.STUDENT, numbersByBranch.get(Branches.valueOf(student.getSiglaBranch())).get(ImportTypes.STUDENT)+1);
        return true;
    }

    public boolean insert(Teacher teacher){
        if(teachers.contains(teacher)){
            return false;
        }
        teachers.add(teacher);
        return true;
    }

    public boolean insert(Project project){
        if(projects.contains(project)){
            return false;
        }
        projects.add(project);

        for(String branch : project.getBranch()){
            numbersByBranch.get(Branches.valueOf(branch)).put(ImportTypes.PROPOSAL, numbersByBranch.get(Branches.valueOf(branch)).get(ImportTypes.PROPOSAL)+1);
        }

        if(project.isStudentNumberPreviouslyAssociated()){
            confirmedStudents.get(Confirmations.NOT_CONFIRMED).add(project);
            studentsNeedingManualProposal.removeIf(obj->obj.getNumStudent() == project.getNumStudent());   
        }
        confirmedTeachers.get(Confirmations.NOT_CONFIRMED).add(project);


        return true;
    }

    public boolean insert(Internship internship){
        if(internships.contains(internship)){
            return false;
        }
        internships.add(internship);

        for(String branch : internship.getBranch()){
            numbersByBranch.get(Branches.valueOf(branch)).put(ImportTypes.PROPOSAL, numbersByBranch.get(Branches.valueOf(branch)).get(ImportTypes.PROPOSAL)+1);
         }
        confirmedTeachers.get(Confirmations.NOT_CONFIRMED).add(internship);
        return true;
    }

    public boolean insert(Autoproposal autoproposal){
        if(autoproposals.contains(autoproposal) || !isStudentPresent(autoproposal.getNumStudent())){
            return false;
        }
        autoproposals.add(autoproposal);

        confirmedStudents.get(Confirmations.NOT_CONFIRMED).add(autoproposal);
        confirmedTeachers.get(Confirmations.NOT_CONFIRMED).add(autoproposal);
        studentsNeedingManualProposal.removeIf(obj->obj.getNumStudent() == autoproposal.getNumStudent());
        return true;
    }

    public boolean insert(Candidacy candidacy){
        if(candidacies.contains(candidacy)){
            return false;
        }
        candidacies.add(candidacy);
        return true;
    }



    public List<Student> getStudentAutoProposals(){
        List<Student> result = new ArrayList<>();
        for(Autoproposal a : autoproposals){
            students.forEach(student -> {
                if(student.getNumStudent() == a.getNumStudent())
                    result.add(student);
            });
        }
        return result;
    }
    
    public List<Student> getStudentsWithCandidacy(){
        List<Student> result = new ArrayList<>();
        for(Candidacy c : candidacies){
            students.forEach(student -> {
                if(student.getNumStudent() == c.getNumStudent())
                    result.add(student);
            });
        }
        
        return result;
    }
    
    public List<Student> getStudentsWithoutCandidacy(){
        List<Student> result = new ArrayList<>();
        for(Student s : students){
            Optional<Candidacy> found = candidacies.stream().filter(obj -> obj.getNumStudent() == s.getNumStudent()).findFirst();
            if(found.isEmpty())
                result.add(s);
        }
        return result;
    }

    public List<Proposal> getAvailable(){
        return getProposalsFiltered(Filters.AVAILABLEPROPOSALS);
    }

    public List<Student> getStudentsWithCandidacyAndWithoutProposals(){
        List<Student> studentsWithCandidacy = getStudentsWithCandidacy();
        List<Student> studentsWithoutProposal = getStudentsWithoutProposals();
        List<Student> result = new ArrayList<>();
            for(Student s : studentsWithCandidacy){
                if(studentsWithoutProposal.contains(s)){
                    result.add(s);
                }
            }
        return result;
    }

    public List<Student> getStudentsWithoutProposals(){
        return studentsNeedingManualProposal;
    }

    public List<Proposal> getProposals(){
        List<Proposal> result = new ArrayList<Proposal>();

        for(Internship i : internships){
            result.add(i);
        }
        for(Project p : projects){
            result.add(p);
        }
        for(Autoproposal a : autoproposals){
            result.add(a);
        }

        return result;


    }
    public List<Student> getAssignableStudents(){
        List<Student> result = new ArrayList<Student>();
        changeConfirmedStudentsStatus();
        for(Student s : studentsNeedingManualProposal){
            if(!getAvailableProposals(s).isEmpty()){
                result.add(s);
            }
        }
        return result;
    }

    public List<Student> getStudents(){
        return students;
    }



    static public enum Filters {

        AUTOPROPOSALS{
            public List<Autoproposal> getAutoProposals(Phase phase){         
                return phase.autoproposals;
            }
            @Override
            public String toString(){
                return "Get autoproposals";
            }
        },
        TEACHERPROPOSALS{
            public List<Project> getTeacherProposals(Phase phase){
                return phase.projects;
            }
            @Override
            public String toString(){
                return "Get Teacher Proposals";
            }
        },
        PROPOSALS_WITH_CANDIDACY{
            public List<Proposal> getProposalsWithCandidacy(Phase phase){
                List<Proposal> result = new ArrayList<>();
                phase.projects.forEach(p ->{
                    boolean hasCandidacy = false;
                    for(Candidacy candidacy : phase.candidacies){
                        if((candidacy.getPreferences().contains(p.getId()))){
                            hasCandidacy = true;
                            result.add(p);
                            break;
                        }
                    }
                });
            
                phase.internships.forEach(p ->{
                    boolean hasCandidacy = false;
                    for(Candidacy candidacy : phase.candidacies){
                        if(candidacy.getPreferences().contains(p.getId())){
                            hasCandidacy = true;
                            result.add(p);
                            break;
                        }
                    }
                });
        
                phase.autoproposals.forEach(p ->{
                    boolean hasCandidacy = false;
                    for(Candidacy candidacy : phase.candidacies){
                        if(!candidacy.getPreferences().contains(p.getId())){
                            hasCandidacy = true;
                            result.add(p);
                            break;
                        }
                    }
                });
                return result;
            }
            @Override
                public String toString(){
                return "Get proposals with candidacy";
            }
        
        },
        PROPOSALS_WITHOUT_CANDIDACY{
            public List<Proposal> getProposalsWithoutCandidacy(Phase phase){
                List<Proposal> result = new ArrayList<>();
                    
                    phase.projects.forEach(p ->{
                        boolean hasCandidacy = false;
                        for(Candidacy candidacy : phase.candidacies){
                            if(candidacy.getPreferences().contains(p.getId())){
                                hasCandidacy = true;
                                break;
                            }
                        }
                       if(!hasCandidacy)
                           result.add(p);
                    });
                
                    phase.internships.forEach(p ->{
                        boolean hasCandidacy = false;
                        for(Candidacy candidacy : phase.candidacies){
                            if(candidacy.getPreferences().contains(p.getId())){
                                hasCandidacy = true;
                                break;
                            }
                        }
                       if(!hasCandidacy)
                           result.add(p);
                    });
        
                    phase.autoproposals.forEach(p ->{
                        boolean hasCandidacy = false;
                        for(Candidacy candidacy : phase.candidacies){
                            if(candidacy.getPreferences().contains(p.getId())){
                                hasCandidacy = true;
                                break;
                            }
                        }
                       if(!hasCandidacy)
                           result.add(p);
                    });
                return result;
            }
            @Override
            public String toString(){
                return "Get proposals without candidacy";
            }
        },
        AVAILABLEPROPOSALS{
            public List<Proposal> getAvailable(Phase phase){
              List<Proposal> result = new ArrayList<>();
                for(Autoproposal a : phase.autoproposals){
                    Student studentFound = phase.students.stream().filter(obj -> obj.getNumStudent() == a.getNumStudent()).findFirst().orElse(null);
                    if(studentFound == null){
                        result.add(a);
                    }
                }
                for(Project a : phase.projects){
                    Student studentFound = phase.students.stream().filter(obj -> obj.getNumStudent() == a.getNumStudent()).findFirst().orElse(null);
                    if(studentFound == null){
                        result.add(a);
                    }
                }
                for(Internship a : phase.internships){
                    Student studentFound = phase.students.stream().filter(obj -> obj.getNumStudent() == a.getNumStudent()).findFirst().orElse(null);
                    if(studentFound == null){
                        result.add(a);
                    }
                }
                return result;  
            }
            @Override
            public String toString(){
                return "Get proposals with candidacy";
            }
        },
        ASSIGNEDPROPOSALS{
            public List<Proposal> getAssignedProposals(Phase phase){
                return phase.getConfirmedStudents().get(Confirmations.CONFIRMED);
            } 
            @Override
            public String toString(){
                return "Get assigned proposals";
            } 
        };

        public static Filters getEnum(String value) {
            for(Filters v : values()){
                String[] string = value.split(" ");
                string[0] = "";
                String finalString = String.join(" ", string);
                finalString = finalString.replaceAll(" ", "");
                
                if(v.toString().equalsIgnoreCase(value)){
                    return v;
                }
            }
                
            throw new IllegalArgumentException();
        }
    }
    public List<Proposal> getProposalsFiltered(Phase.Filters... filters){
        if(filters.length > Phase.Filters.values().length){
            return null;
        }
        List<Proposal> result = new ArrayList<Proposal>();
        for(Phase.Filters filter : filters){
            Method method = filter.getClass().getMethods()[1];
            try {
                for(Proposal p : (List<Proposal>) method.invoke(filter, this)){
                    if(!result.contains(p)){
                        result.add(p);
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                System.err.println(e);
            }
        }
        return result;
    }

    public Proposal getProposalById(String id){
        for(Project p : projects){
            if(p.getId().equals(id)){
                return p;
            }
        }
        for(Internship i : internships){
            if(i.getId().equals(id)){
                return i;
            }
        }
        for(Autoproposal a : autoproposals){
            if(a.getId().equals(id)){
                return a;
            }
        }
        return null;
    }

    public List<Student> getStudentsWithProposal(){
        List<Student> result = new ArrayList<>();
        for(Student s : students){
            Autoproposal foundAutoPro = autoproposals.stream().filter(obj -> obj.getNumStudent() == s.getNumStudent()).findFirst().orElse(null);
            if(foundAutoPro != null)
                result.add(s);
            Internship foundIntern = internships.stream().filter(obj -> obj.getNumStudent() == s.getNumStudent()).findFirst().orElse(null);
            if(foundIntern != null)
                result.add(s);
            Project foundProj = projects.stream().filter(obj -> obj.getNumStudent() == s.getNumStudent()).findFirst().orElse(null);
            if(foundProj != null)
                result.add(s);
        }
        return result;
    }

    public boolean isCandidacyCompleted(){   
        for(Candidacy c : candidacies){
            Proposal proposal = confirmedStudents.get(Confirmations.CONFIRMED).stream().filter(obj-> obj.getNumStudent() == c.getNumStudent()).findFirst().orElse(null);
            if(proposal==null)
                return false;
        }
        return true;
    }
}
