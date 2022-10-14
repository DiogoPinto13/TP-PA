package pt.isec.pa.apoio_poe.model.fsm;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


import pt.isec.pa.apoio_poe.model.data.Branches;
import pt.isec.pa.apoio_poe.model.data.Confirmations;
import pt.isec.pa.apoio_poe.model.data.Degrees;
import pt.isec.pa.apoio_poe.model.data.Phase;
import pt.isec.pa.apoio_poe.model.data.Proposal;
import pt.isec.pa.apoio_poe.model.data.Student;
import pt.isec.pa.apoio_poe.model.data.Teacher;
import pt.isec.pa.apoio_poe.model.errors.ErrorLog;
import pt.isec.pa.apoio_poe.model.errors.ErrorType;


class ConfigState extends PhaseStateAdapter {
    
    public ConfigState(PhaseContext context,Phase phase){
        super(context, phase);
    }

    @Override
    public Options[] getPossibleOptions(){
        ArrayList<Options> options = new ArrayList<Options>();

        options.add(Options.IMPORT);
        options.add(Options.EXPORT);
        options.add(Options.LOOKUP);
        options.add(Options.NEXT);
        options.add(Options.LOCK);
        options.add(Options.SAVE);
        options.add(Options.UNDO);
        options.add(Options.REDO);
        options.add(Options.EXIT);

        return options.toArray(new Options[0]);

    }

    public Options[] query(){
        ArrayList<Options> options = new ArrayList<Options>();
        options.add(Options.GET_STUDENT_BY_ID);
        options.add(Options.GET_TEACHER_BY_EMAIL);
        options.add(Options.GET_PROPOSALS_BY_ID);
        return options.toArray(new Options[0]);

    }

    @Override
    public List<ImportTypes> getImports(){
        List<ImportTypes> options = new ArrayList<ImportTypes>();
        options.add(ImportTypes.STUDENT);
        options.add(ImportTypes.TEACHER);
        options.add(ImportTypes.PROPOSAL);

        return options;
    }

    @Override
    public Student getStudentByID(long numStudent){
        return phase.getStudentByID(numStudent);
    }
    //docentes
    @Override 
    public Teacher getTeacherByEmail(String email){
        return phase.getTeacherByEmail(email);
    }
    //propostas
    @Override
    public Proposal getProposalByID(String id){
        return phase.getProposalById(id);
    }
    @Override
    public ErrorLog importStudent(String fileName,ImportTypes type){
        
        if(type.equals(ImportTypes.CANDIDACY)){
            return new ErrorLog(ErrorType.INVALID_IMPORT_TYPE);
        }
        int g = 1;
        
        Class studentClass;
        int projectInternshipIndex=0;
        ErrorLog error = new ErrorLog();
        try{
            File file = new File(fileName);

            if(!file.exists()){
                return new ErrorLog(ErrorType.FAILED_OPEN_FILE);
            }

            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                if(projectInternshipIndex == 1){
                    projectInternshipIndex=0;
                    type=ImportTypes.PROPOSAL;
                }
                Constructor constructor=null;
                Object[] line = (Object[]) sc.nextLine().split(",");
                if(type.equals(ImportTypes.PROPOSAL)){
                    projectInternshipIndex++;
                    switch (line[0].toString()) {
                        case "T1" -> type = ImportTypes.INTERNSHIP;
                        case "T2" -> type = ImportTypes.PROJECT;
                        case "T3" -> type = ImportTypes.AUTOPROPOSAL;
                    }
                }
                studentClass = Class.forName("pt.isec.pa.apoio_poe.model.data."+type);
                Constructor[] ctors = studentClass.getDeclaredConstructors();
                if(ctors.length == 1){
                    constructor = ctors[0];
                } else{
                    for(Constructor ctor : ctors){
                        if(ctor.getParameterCount() == line.length - projectInternshipIndex){
                            constructor  = ctor;
                            break;
                        }
                    }
                }

                if(constructor == null){
                    return new ErrorLog(ErrorType.CSV_WRONG_LINE_LENGHT);
                }
                Class<?>[] parameters = constructor.getParameterTypes();
                Object[] args = new Object[parameters.length];
                
                try {
                    //convert Object array to specific constructor needed type
                
                    for(int i=projectInternshipIndex;i<parameters.length + projectInternshipIndex;i++){
                        Class<?> parameterClass = Class.forName(parameters[i-projectInternshipIndex].getName());
                       
                        if(CharSequence.class.isAssignableFrom(parameterClass)){
                            args[i-projectInternshipIndex] = line[i];
                        }
                        else if(AbstractList.class.isAssignableFrom(parameterClass)){
                            args[i-projectInternshipIndex] = new ArrayList<String>();
                                int k=i-projectInternshipIndex;
                                for(ArrayList<String> j= (ArrayList<String>) args[i-projectInternshipIndex];k<line.length;k++){
                                    j.add(line[k].toString());
                                }
                        } else{
                            Method valueOfMethod = parameterClass.getMethod("valueOf", Class.forName("java.lang.String"));
                            args[i-projectInternshipIndex] = parameterClass.cast(valueOfMethod.invoke(null, line[i]));
                        }
                    }
                    if(!errorCheck(type, args).getErrorType().isEmpty()){
                        for(ErrorType e : errorCheck(type, args).getErrorType()){
                            error.addErrorType(e);
                        }
                        continue;
                    }

                    Object student = constructor.newInstance(args);
                    insert(student);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
                    System.err.println(e);
                    return new ErrorLog(ErrorType.EXCEPTION_ERROR);
                }
            }
        } catch(FileNotFoundException | ClassNotFoundException e){
            System.err.println(e);
            return new ErrorLog(ErrorType.EXCEPTION_ERROR);
        }        
        if(error.getErrorType().isEmpty()){
            error.addErrorType(ErrorType.SUCESS);
        }
        return error;
    }

    @Override
    public void exportToCSV(){
        File studentCSV = new File("students.csv");
        File teachersCSV = new File("teachers.csv");
        File proposalsCSV = new File("proposals.csv");

        try {
            studentCSV.createNewFile();
            teachersCSV.createNewFile();
            proposalsCSV.createNewFile();
            FileWriter studentCSVWrite = new FileWriter("students.csv");
            for(Student s : phase.getStudents()){
                studentCSVWrite.write(s.toCSV()+"\n");
            }
            studentCSVWrite.close();

            FileWriter teachersCSVWrite = new FileWriter("teachers.csv");
            for(Teacher t : phase.getTeachers()){
                teachersCSVWrite.write(t.toCSV()+"\n");
            }
            teachersCSVWrite.close();

            FileWriter proposalsCSVWrite = new FileWriter("proposals.csv");
            for(Proposal p : phase.getProposals()){
                proposalsCSVWrite.write(p.toCSV()+"\n");
            }
            proposalsCSVWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportToCSVFile(File file){
        File studentCSV = new File("students.csv");
        File teachersCSV = new File("teachers.csv");
        File proposalsCSV = new File("proposals.csv");

        try {
            studentCSV.createNewFile();
            teachersCSV.createNewFile();
            proposalsCSV.createNewFile();
            FileWriter studentCSVWrite = new FileWriter("students.csv");
            for(Student s : phase.getStudents()){
                studentCSVWrite.write(s.toCSV()+"\n");
            }
            studentCSVWrite.close();

            FileWriter teachersCSVWrite = new FileWriter("teachers.csv");
            for(Teacher t : phase.getTeachers()){
                teachersCSVWrite.write(t.toCSV()+"\n");
            }
            teachersCSVWrite.close();

            FileWriter proposalsCSVWrite = new FileWriter("proposals.csv");
            for(Proposal p : phase.getProposals()){
                proposalsCSVWrite.write(p.toCSV()+"\n");
            }
            proposalsCSVWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected ErrorLog errorCheck(ImportTypes classType, Object[] args){
        ErrorLog errorlog = new ErrorLog();
        switch (classType) {
            case STUDENT->{
                if(phase.isStudentPresent((Long) args[0])){
                    errorlog.addErrorType(ErrorType.STUDENT_PRESENT);
                }
                boolean flag = true;
                for(String branch : ((String) args[4]).split("\\|")){
                    if(Arrays.stream(Branches.values()).anyMatch(e->e.name().equals(branch))){
                        
                    } else{
                        errorlog.addErrorType(ErrorType.INVALID_BRANCH);
                    }
                }
                if((Double) args[5] < 0 || (Double) args[5] > 1 ){
                
                    errorlog.addErrorType(ErrorType.INVALID_CLASSIFICATION);
                }
                if(((String)args[1]).split(".").length > 2){

                    errorlog.addErrorType(ErrorType.INVALID_EMAIL);
                }
                if(!Arrays.stream(Degrees.values()).anyMatch(e->e.name().equals(((String)args[3]).replaceAll("-","")))){
                    errorlog.addErrorType(ErrorType.INVALID_DEGREE);
                }
        
           }
           
           case TEACHER->{
               if(phase.isTeacherPresent((String) args[1])){
                    errorlog.addErrorType(ErrorType.TEACHER_PRESENT);
                }
                if(((String)args[1]).split(".").length > 2){
                    errorlog.addErrorType(ErrorType.INVALID_EMAIL);
                }
            }
            case INTERNSHIP->{
                if( phase.getProposalById((String) args[0]) != null){
                    errorlog.addErrorType(ErrorType.PROPOSAL_DOESNT_EXIST);
                }

                for(String branch : ((String) args[1]).split("\\|")){
                    if(Arrays.stream(Branches.values()).anyMatch(e->e.name().equals(branch))){

                    } else{
                        errorlog.addErrorType(ErrorType.INVALID_BRANCH);
                    }
                }

                if(args.length==5){
                   if(!phase.isStudentPresent((Long) args[3])){
                        errorlog.addErrorType(ErrorType.STUDENT_NOT_PRESENT);
                   }
                   
                        
                }

    
            }
            case AUTOPROPOSAL->{
                if( phase.getProposalById((String) args[1]) != null){
                    errorlog.addErrorType(ErrorType.PROPOSAL_ALREADY_EXISTS);
                }
                
                if(!phase.isStudentPresent((Long) args[2])){
                    errorlog.addErrorType(ErrorType.STUDENT_NOT_PRESENT);
                }

                if(phase.getConfirmedStudents().get(Confirmations.NOT_CONFIRMED).stream().filter(obj->obj.getNumStudent() == (Long) args[2]).findAny().orElse(null) != null){
                    errorlog.addErrorType(ErrorType.STUDENT_ALREADY_PRESENTED_AUTOPROPOSAL);
                }

            }
            case PROJECT->{
                if( phase.getProposalById((String) args[0]) != null){
                    errorlog.addErrorType(ErrorType.PROPOSAL_ALREADY_EXISTS);
                }
                for(String branch : ((String) args[1]).split("\\|")){
                    if(Arrays.stream(Branches.values()).anyMatch(e->e.name().equals(branch))){

                    } else{
                        errorlog.addErrorType(ErrorType.INVALID_BRANCH);
                    }
                }

                if(!phase.isTeacherPresent((String)args[3])){
                    errorlog.addErrorType(ErrorType.TEACHER_NOT_PRESENT);
                }

                if(args.length==5){
                    if(!phase.isStudentPresent((Long) args[4])){
                        errorlog.addErrorType(ErrorType.STUDENT_NOT_PRESENT);
                    }
                 }

            }
            
        }
        return errorlog;
        
    }

    @Override 
    public boolean getLock(){
        return false;
    }

    @Override
    public ErrorLog setLock(){
        HashMap<Branches,HashMap<ImportTypes,Integer>> statistics = phase.getBranchStatistics();
        if(statistics.get(Branches.DA).get(ImportTypes.STUDENT) > statistics.get(Branches.DA).get(ImportTypes.PROPOSAL)){
            return new ErrorLog(ErrorType.PROPOSAL_SMALLER_THAN_STUDENT);
        }

        if(statistics.get(Branches.SI).get(ImportTypes.STUDENT) > statistics.get(Branches.SI).get(ImportTypes.PROPOSAL)){
            return new ErrorLog(ErrorType.PROPOSAL_SMALLER_THAN_STUDENT);
        }

        if(statistics.get(Branches.RAS).get(ImportTypes.STUDENT) > statistics.get(Branches.RAS).get(ImportTypes.PROPOSAL)){
            return new ErrorLog(ErrorType.PROPOSAL_SMALLER_THAN_STUDENT);
        }

        changeState(PhaseState.CONFIG_CLOSED);
        context.setStateMap(PhaseState.CONFIG, PhaseState.CONFIG_CLOSED);

        return new ErrorLog(ErrorType.SUCESS);
    }

    @Override 
    public PhaseState getState(){
        return PhaseState.CONFIG;
    }

    @Override 
    public void nextState() {
        changeState(context.getStateMap(PhaseState.APPLICATON_OPT));
    }

}
