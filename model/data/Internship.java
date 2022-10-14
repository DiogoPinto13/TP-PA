package pt.isec.pa.apoio_poe.model.data;

public class Internship extends Proposal{

    private String college;

    public Internship(String id, String branch, String title, long numStudent, String college){
      super(id, title, branch, numStudent);
      this.college = college;
    }

    public Internship(String id, String branch, String title, String college){
        super(id, title, branch);
        this.college = college;
    }

    public void setCollege(String college){
        this.college = college;
    }
    public String getCollege(){
        return college;
    }

    @Override
    public String toCSV(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("T1,%s,",getId()));
        for(String branch : getBranch()){
            sb.append(String.format("%s,",branch));
        }
        sb.append(String.format("%s,%s",getTitle(),getCollege()));

        return sb.toString();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Estagio(%s) %s  para ramo %s em %s",super.getId(),super.getTitle(),super.getBranch(),college));
        if(super.getNumStudent() != 0){
            sb.append(String.format(" que tem o aluno cujo numero de estudante é %d associado",super.getNumStudent()));
        }
        return sb.toString();
    }

    @Override
    public Internship clone(){
        if(isStudentNumberPreviouslyAssociated())
            return new Internship(getId(),String.join("|", getBranch()),getTitle(),getNumStudent(),college);
        return new Internship(getId(),String.join("|", getBranch()),getTitle(),college);
    }
}
