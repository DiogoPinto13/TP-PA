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
import java.util.List;
import java.util.Scanner;

import pt.isec.pa.apoio_poe.model.data.Candidacy;
import pt.isec.pa.apoio_poe.model.data.Internship;
import pt.isec.pa.apoio_poe.model.data.Phase;
import pt.isec.pa.apoio_poe.model.data.Proposal;
import pt.isec.pa.apoio_poe.model.data.Student;
import pt.isec.pa.apoio_poe.model.data.Phase.Filters;
import pt.isec.pa.apoio_poe.model.errors.ErrorLog;
import pt.isec.pa.apoio_poe.model.errors.ErrorType;

public class ApplicationOptState extends PhaseStateAdapter{
    
    ApplicationOptState(PhaseContext context, Phase phase){
        super(context,phase);
    }

    @Override
    public PhaseState getState(){
        return PhaseState.APPLICATON_OPT;
    }

    @Override 
    public void previousState(){
        changeState(context.getStateMap(PhaseState.CONFIG));
    } 

    @Override 
    public void nextState() {
        changeState(context.getStateMap(PhaseState.PROPOSAL_ATR));
    }

    @Override
    public ErrorLog setLock(){
        if(context.getStateMap(PhaseState.CONFIG) != PhaseState.CONFIG_CLOSED)
            return new ErrorLog(ErrorType.CONFIG_STATE_NOT_CLOSED);

        changeState(PhaseState.PROPOSAL_ATR);
        context.setStateMap(PhaseState.APPLICATON_OPT, PhaseState.APPLICATON_OPT_CLOSED);
        return new ErrorLog(ErrorType.SUCESS);
    }
    @Override
    public boolean getLock(){
        return false;
    }
    @Override 
    public Options[] query(){
        ArrayList<Options> options = new ArrayList<Options>();
        options.add(Options.GET_STUDENT_AUTOPROPOSAL);
        options.add(Options.GET_STUDENTS_WITH_CANDIDACY);
        options.add(Options.GET_STUDENTS_WITHOUT_CANDIDACY);
        options.add(Options.GET_PROPOSALS_FILTERED);


        return options.toArray(new Options[0]);

    }

    @Override
    public List<ImportTypes> getImports(){
        List<ImportTypes> options = new ArrayList<ImportTypes>();
        options.add(ImportTypes.CANDIDACY);

        return options;
    }

    @Override
    public Options[] getPossibleOptions(){
        ArrayList<Options> options = new ArrayList<Options>();

        options.add(Options.IMPORT);
        options.add(Options.EXPORT);
        options.add(Options.LOOKUP);
        options.add(Options.NEXT);
        options.add(Options.PREVIOUS);
        options.add(Options.LOCK);
        options.add(Options.SAVE);
        options.add(Options.UNDO);
        options.add(Options.REDO);
        options.add(Options.EXIT);

        return options.toArray(new Options[0]);
    }
    @Override
    public void exportToCSV(){
        File candidaturesCSV = new File("candidatures.csv");
        try {
            candidaturesCSV.createNewFile();
            FileWriter candidaturesCSVWrite = new FileWriter("candidatures.csv");
            for(Candidacy c : phase.getCandidacy()){
                candidaturesCSVWrite.write(c.toCSV()+"\n");
            }
            candidaturesCSVWrite.close();
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    @Override
    public void exportToCSVFile(File file){
        File candidaturesCSV = file;
        try {
            candidaturesCSV.createNewFile();
            FileWriter candidaturesCSVWrite = new FileWriter(file);
            for(Candidacy c : phase.getCandidacy()){
                candidaturesCSVWrite.write(c.toCSV()+"\n");
            }
            candidaturesCSVWrite.close();
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }
    
    @Override 
    public ErrorLog importStudent(String fileName,ImportTypes type){
        if(!type.equals(ImportTypes.CANDIDACY)){
            return new ErrorLog(ErrorType.INVALID_IMPORT_TYPE);
        }
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
                            System.err.println(e);
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
    protected ErrorLog errorCheck(ImportTypes classType, Object[] args){
        ErrorLog errorLog = new ErrorLog();
        switch (classType) {
            case CANDIDACY->{
                if(!phase.isStudentPresent((Long) args[0])){
                    return new ErrorLog(ErrorType.STUDENT_NOT_PRESENT);
                }
                if(phase.getCandidacy().stream().filter(obj->obj.getNumStudent() == (Long) args[0]).findAny().orElse(null) != null){
                    return new ErrorLog(ErrorType.STUDENT_ALREADY_HAS_CANDIDACY);
                }
                if(((ArrayList<String>) args[1]).isEmpty()){
                    return new ErrorLog(ErrorType.MISSING_FIELD);
                }
                for(int i=0;i<((ArrayList<String>)args[1]).size();i++){

                    if(phase.getProposalById(((ArrayList<String>) args[1]).get(i)) == null){
                        return new ErrorLog(ErrorType.PROPOSAL_DOESNT_EXIST);
                    } 
                    else {
                        if(phase.getProposalById(((ArrayList<String>) args[1]).get(i)).isStudentNumberPreviouslyAssociated()){
                            return new ErrorLog(ErrorType.STUDENT_PREVIOUSLY_ASSOCIATED);
                        }
                        if(phase.hasAutoproposal((Long)args[0])){
                            return new ErrorLog(ErrorType.STUDENT_HAS_AUTOPROPOSAL);
                        }
                        if(phase.getProposalById(((ArrayList<String>) args[1]).get(i)).getClass() == Internship.class){
                            if(phase.getStudentByID((Long) args[0]).isAccessInternship() == false){
                                return new ErrorLog(ErrorType.STUDENT_CANT_APPLY_TO_INTERNSHIPS);
                            }
                        }
                    }



                }
           }
        }
        return errorLog;
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
    public List<Student> getStudentsWithoutCandidacy(){
        return phase.getStudentsWithoutCandidacy();
    }

    @Override
    public List<Proposal> getAutoProposals(){
       return phase.getProposalsFiltered(Phase.Filters.AUTOPROPOSALS);
    }

    @Override
    public List<Proposal> getTeacherProposals(){
        return phase.getProposalsFiltered(Phase.Filters.TEACHERPROPOSALS);
    }

    @Override
    public List<Proposal> getProposalsWithCandidacy(){
        return phase.getProposalsFiltered(Phase.Filters.PROPOSALS_WITH_CANDIDACY);
    }

    @Override
    public List<Proposal> getProposalsWithoutCandidacy(){
        return phase.getProposalsFiltered(Phase.Filters.PROPOSALS_WITHOUT_CANDIDACY);
    }

    @Override
    public List<Filters> filter(){
        List<Filters> filters = new ArrayList<Filters>();
        filters.add(Filters.AUTOPROPOSALS);
        filters.add(Filters.TEACHERPROPOSALS);
        filters.add(Filters.PROPOSALS_WITH_CANDIDACY);
        filters.add(Filters.PROPOSALS_WITHOUT_CANDIDACY);
        
        return filters;
    }
    @Override 
    public List<Proposal> getProposalsFiltered(Filters... filters){
        return phase.getProposalsFiltered(filters);
    }

}
