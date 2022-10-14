package pt.isec.pa.apoio_poe.model.errors;
public enum ErrorType {
    INVALID_IMPORT_TYPE{
        @Override
        public String toString(){
            return "The choosen import type is invalid";
        }
    },
    FAILED_OPEN_FILE{
        @Override
        public String toString(){
            return "Failed to open file / file doesn't exist";
        }
    },
    CSV_WRONG_LINE_LENGHT{
        @Override
        public String toString(){
            return "The parsed line doesn't have the correct lenght to be valid";
        }
    },
    STUDENT_PRESENT{
        @Override
        public String toString(){
            return "The inserted student is already present";
        }
    },
    STUDENT_NOT_PRESENT{
        @Override
        public String toString(){
            return "The student is not present";
        }
    },
    INVALID_BRANCH{
        @Override
        public String toString(){
            return "The branch is invalid";
        }
    },
    INVALID_CLASSIFICATION{
        @Override
        public String toString(){
            return "The classification is invalid, it must be between 0 and 1";
        }
    },
    INVALID_EMAIL{
        @Override
        public String toString(){
            return "The email is not valid";
        }
    },
    INVALID_DEGREE{
        @Override
        public String toString(){
            return "The degree is not valid";
        }
    },
    EXCEPTION_ERROR{
        @Override
        public String toString(){
            return "There was an exception!";
        }
    },
    TEACHER_PRESENT{
        @Override
        public String toString(){
            return "The inserted teacher is already present";
        }
    },
    TEACHER_NOT_PRESENT{
        @Override
        public String toString(){
            return "Teacher email doesn't exist";
        }
    },
    PROPOSAL_DOESNT_EXIST{
        @Override
        public String toString(){
            return "The proposal doesn't exist";
        }
    },
    PROPOSAL_ALREADY_EXISTS{
        @Override
        public String toString(){
            return "The proposal already exists";
        }
    },
    MISSING_FIELD{
        @Override
        public String toString(){
            return "A field is empty/missing";
        }
    },
    CONFIG_STATE_NOT_CLOSED{
        @Override
        public String toString(){
            return "The configuration phase must be closed before closing this phase";
        }
    },
    STUDENT_HAS_AUTOPROPOSAL{
        @Override
        public String toString(){
            return "The student already has an autoproposal and it cant apply anymore";
        }
    },
    STUDENT_ALREADY_HAS_CANDIDACY{
        @Override
        public String toString(){
            return "The student has already applied before";
        }
    },
    STUDENT_CANT_APPLY_TO_INTERNSHIPS{
        @Override
        public String toString(){
            return "The student cant apply to internships";
        }
    },
    STUDENT_PREVIOUSLY_ASSOCIATED{
        @Override
        public String toString(){
            return "A student already has been previously associated with that proposal";
        }
    },
    STUDENT_ALREADY_PRESENTED_AUTOPROPOSAL{
        @Override
        public String toString(){
            return "The autoproposal has a student whose number is on another autoproposal";
        }
    },
    PROPOSAL_SMALLER_THAN_STUDENT{
        @Override
        public String toString(){
            return "The number of proposals must be >= than the number of students for each branch to lock this state";
        }
    },
    CANDIDACY_NOT_COMPLETED{
        @Override
        public String toString(){
            return "All students with a candidacy must have an assigned proposal to lock this phase";
        }
    },
    STUDENT_DOESNT_HAVE_PROPOSAL_ASSIGNED{
        @Override
        public String toString(){
            return "That proposal isn't assigned to that student so it cant be removed";
        }
    },
    PROPOSAL_HAS_NO_SUPERVISER{
        @Override
        public String toString(){
            return "The proposal assigned to that student has no supervisor to remove";
        }
    },
    TIE{
        @Override
        public String toString(){
            return "A tie happend while automatically assigning proposals";
        }
    },
    SUCESS{
        @Override
        public String toString(){
            return "No errors found";
        }
    },

    
}
