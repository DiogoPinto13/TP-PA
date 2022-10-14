package pt.isec.pa.apoio_poe.model.fsm;

public enum ImportTypes {
    STUDENT{
        @Override
        public String toString(){
            return "Student";
        }
    },TEACHER{
        @Override
        public String toString(){
            return "Teacher";
        }
    },PROPOSAL{
       @Override
        public String toString(){
            return "Proposal";
        } 
    },CANDIDACY{
        @Override
        public String toString(){
            return "Candidacy";
        }
    },INTERNSHIP{
        @Override
        public String toString(){
            return "Internship";
        }
    },PROJECT{
        @Override
        public String toString(){
            return "Project";
        }
    },AUTOPROPOSAL{
        @Override
        public String toString(){
            return "Autoproposal";
        }
    }
}
