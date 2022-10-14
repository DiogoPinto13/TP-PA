package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;

public class Tie implements Serializable{
    Student firstStudent;
    Student secondStudent;
    Proposal proposal;

    public Tie(Student firstStudent, Student secondStudent, Proposal proposal){
        this.firstStudent = firstStudent;
        this.secondStudent = secondStudent;
        this.proposal = proposal;
    }
    public Tie(){

    }
    public Student getFirstStudent() {
        return firstStudent;
    }
    public void setFirstStudent(Student firstStudent) {
        this.firstStudent = firstStudent;
    }
    public Student getSecondStudent() {
        return secondStudent;
    }
    public void setSecondStudent(Student secondStudent) {
        this.secondStudent = secondStudent;
    }
    public Proposal getProposal() {
        return proposal;
    }
    public void setProposal(Proposal propsal) {
        this.proposal = propsal;
    }

}
