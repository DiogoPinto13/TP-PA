package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;

public class Student implements Serializable{

    private long numStudent;
    private String name;
    private String email;
    private String siglaDegree;
    private String siglaBranch;
    private double classification;
    private boolean accessInternship;

    public Student(Long numStudent, String name, String email, String siglaDegree, String siglaBranch,
    Double classification, Boolean accessInternship){
        this.numStudent = numStudent;
        this.name = name;
        this.email = email;
        this.siglaDegree = siglaDegree;
        this.siglaBranch = siglaBranch;
        this.classification = classification;
        this.accessInternship = accessInternship;
    }

    public Student(){

    }
    //GETTERS AND SETTERS

    public long getNumStudent() {
        return numStudent;
    }

    public void setNumStudent(long numStudent) {
        this.numStudent = numStudent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSiglaDegree() {
        return siglaDegree;
    }
    public void setSiglaDegree(String siglaDegree) {
        this.siglaDegree = siglaDegree;
    }

    public String getSiglaBranch() {
        return siglaBranch;
    }

    public double getClassification() {
        return classification;
    }

    public void setClassification(double classification) {
        this.classification = classification;
    }

    public boolean isAccessInternship() {
        return accessInternship;
    }

    public void setAccessInternship(boolean accessInternship) {
        this.accessInternship = accessInternship;
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

        Student student = (Student) o;

        if(student.getNumStudent() == this.getNumStudent()){
            return true;
        }
        return false;

    }
    public String toCSV(){
        return String.format("%d,%s,%s,%s,%s,%f,%s",numStudent,name,email,siglaDegree,siglaBranch,classification,accessInternship);
    }
    
    @Override
    public int hashCode(){
        return Long.hashCode(numStudent);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s",name)+" ".repeat(40 - name.length()));
        sb.append(String.format("|%d|%s|", numStudent,siglaBranch));
        

        return sb.toString();
    }

    @Override
    public Student clone(){
        return new Student(numStudent,name,email,siglaDegree,siglaBranch,classification,accessInternship);
    }

    public String deepToString() {
        return null;
    }
}
