package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

abstract public class Proposal implements Serializable{

    private String id;
    private String title;
    private List<String> branch;
    private long numStudent;
    private String superviser;
    private boolean studentNumberPreviouslyAssociated;

    public boolean isStudentNumberPreviouslyAssociated() {
        return studentNumberPreviouslyAssociated;
    }

    protected Proposal(String id, String title, String branch){
        this.id = id;
        this.title = title;
        this.branch = Arrays.asList(branch.split("\\|"));
        this.numStudent = 0;
        studentNumberPreviouslyAssociated = false;
        superviser = new String("");
    }

    protected Proposal(String id, String title, String branch, Long numStudent){
        this.id = id;
        this.title = title;
        this.branch = Arrays.asList(branch.split("\\|"));
        this.numStudent = numStudent;
        studentNumberPreviouslyAssociated=true;
        superviser = new String("");
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setNumStudent(long numStudent){
        this.numStudent = numStudent;
    }

    public long getNumStudent(){
        return numStudent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setSuperviser(String superviser){
        this.superviser = superviser;
    }
    public String getSuperviser(){
        return superviser;
    }

    public List<String> getBranch() {
        return branch;
    }

    public String toCSV(){
        return null;
    }

    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }

        if(this == o){
            return true;
        }

        if(this.getClass() != o.getClass()){
            return false;
        }
        Proposal proposal = (Proposal) o;

        if(proposal.getId() == this.getId()){
            return true;
        }
        return false;

    }
    @Override
    public int hashCode(){
        return id.hashCode();
    }

    @Override
    public Proposal clone(){
        return null;
    }
}
