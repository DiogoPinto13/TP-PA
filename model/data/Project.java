package pt.isec.pa.apoio_poe.model.data;

public class Project extends Proposal {
    
    private String teacher;

    public Project(String id, String branch, String title, String teacher){
        super(id, title, branch);
        this.teacher = teacher;
        this.setSuperviser("");
    }

    public Project(String id, String branch, String title, String teacher, Long numStudent){
        super(id, title, branch, numStudent);
        this.teacher = teacher;
        this.setSuperviser("");
    }

    public void setTeacher(String teacher){
        this.teacher = teacher;
    }
    public String getTeacher(){
        return teacher;
    }

    @Override
    public String toCSV(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("T2,%s,",getId()));
        for(String branch : getBranch()){
            sb.append(String.format("%s,",branch));
        }
        sb.append(String.format("%s,%s",getTitle(),getTeacher()));
        if(getNumStudent()!=0){
            sb.append(String.format(",%s",getNumStudent()));
        }

        return sb.toString();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Projeto(%s) %s  para ramo %s proposto pelo docente %s",super.getId(),super.getTitle(),super.getBranch(),teacher));
        if(super.getNumStudent() != 0){
            sb.append(String.format(" que tem o aluno cujo numero de estudante é %d associado",super.getNumStudent()));
        }

        return sb.toString();
    }

    @Override
    public Project clone(){
        if(isStudentNumberPreviouslyAssociated())
            return new Project(getId(),String.join("|", getBranch()),getTitle(),getTeacher(),getNumStudent());
        return new Project(getId(),String.join("|", getBranch()),getTitle(),getTeacher());
    }
}
