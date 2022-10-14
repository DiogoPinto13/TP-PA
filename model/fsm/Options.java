package pt.isec.pa.apoio_poe.model.fsm;

public enum Options {
    NEXT{
       @Override
        public String toString(){
            return "Next";
        }
    }, PREVIOUS{
        @Override
        public String toString(){
            return "Previous";
        }
    }, LOCK{
        @Override
        public String toString(){
            return "Lock";
        }
    }, INSERT{
        @Override
        public String toString(){
            return "Insert";
        }
    }, LOOKUP{
        @Override
        public String toString(){
            return "Lookup";
        }
    }, EDIT{
        @Override
        public String toString(){
            return "Edit";
        }
    }, DELETE{
        @Override
        public String toString(){
            return "Delete";
        }
    }, IMPORT{
        @Override
        public String toString(){
            return "Import CSV";
        }
    }, GET_STATE{
        @Override
        public String toString(){
            return "Get State";
        }
    }, 
    GET_STUDENT_AUTOPROPOSAL{
        @Override
        public String toString(){
            return "Get Student Autoproposal";
        }
    }, GET_STUDENTS_WITH_CANDIDACY{
        @Override
        public String toString(){
            return "Get Students With Candidacy";
        }
    }, GET_STUDENTS_WITHOUT_CANDIDACY{
        @Override
        public String toString(){
            return "Get Students Without Candidacy";
        }
    },
    GET_AUTOPROPOSAL{
        @Override
        public String toString(){
            return "Get Auto Proposal";
        }
    }, GET_TEACHER_PROPOSAL{
        @Override
        public String toString(){
            return "Get Teacher Proposal";
        }
    }, GET_PROPOSALS_WITH_CANDIDACY{
        @Override
        public String toString(){
            return "Get Proposals With Candidacy";
        }
    }, GET_PROPOSALS_WITHOUT_CANDIDACY{
        @Override
        public String toString(){
            return "Get Proposals Without Candidacy";
        }
    },
    GET_PROPOSALS_FILTERED{
        @Override
        public String toString(){
            return "Get Proposals Filtered";
        }
    }, GET_STUDENT_BY_ID{
        @Override
        public String toString(){
            return "Get Student By Id";
        }
    }, GET_TEACHER_BY_EMAIL{
        @Override
        public String toString(){
            return "Get Teacher By Email";
        }
    }, GET_PROPOSALS_BY_ID{
        @Override
        public String toString(){
            return "Get Proposals By Id";
        }
    },
        AUTOMATIC_PROPOSALS_ASSOCIATION{
        @Override
        public String toString(){
            return "Automatically confirm the previously inserted Teacher student proposal association ";
        }
       },
       AUTOMATIC_PROPOSALS{
        @Override
        public String toString(){
            return "Automatically assign proposals";
        }
       },
       MANUAL_ASSIGNED_PROPOSALS{
        @Override
        public String toString(){
            return "Manual assign proposals";
        }
       },
       MANUAL_REMOVE_PROPOSALS{
        @Override
        public String toString(){
            return "Manual remove assigned proposals";
        }
       },
       GET_STUDENTS_WITH_ASSIGNED_PROPOSAL{
        @Override
        public String toString(){
            return "Get Students with an assigned proposal";
        }
       },
       GET_STUDENTS_WITHOUT_ASSIGNED_PROPOSAL{
        @Override
        public String toString(){
            return "Get Students without an assigned proposal";
        }
       },
       GET_STUDENTS_WITH_ASSIGNED_PROPOSAL_AND_SUPERVISER{
        @Override
        public String toString(){
            return "Get Students with an assigned proposal and an assigned superviser";
        }
       },
       GET_STUDENTS_WITH_ASSIGNED_PROPOSAL_AND_WITHOUT_SUPERVISER{
        @Override
        public String toString(){
            return "Get Students with an assigned proposal and without an assigned superviser";
        }
       },
       AUTOMATIC_SUPERVISERS{
        @Override
        public String toString(){
            return "Automatically assign supervisers";
        }
       },
       MANAGE_SUPERVISERS{
        @Override
        public String toString(){
            return "Manage supervisers";
        }
       },
       MANUAL_ASSIGN_SUPERVISER{
        @Override
        public String toString(){
            return "Assign a superviser to a student";
        }
       },
       MANUAL_REMOVE_SUPERVISER{
        @Override
        public String toString(){
            return "Remove an assigned superviser from a student";
        }
       },
       LOOKUP_SUPERVISER{
        @Override
        public String toString(){
            return "Find a superviser by student";
        }
       },
       EDIT_SUPERVISER{
        @Override
        public String toString(){
            return "Change a student superviser";
        }
       },
       GET_NUMBER_OF_SUPERVISIONS_FOREACH_SUPERVISOR{
        @Override
        public String toString(){
            return "Get number of supervisions for each superviser";
        }
       },
       GET_MAX_SUPERVISIONS{
        @Override
        public String toString(){
            return "Get maximum number of supervisions";
        }
       },
       GET_MIN_SUPERVISIONS{
        @Override
        public String toString(){
            return "Get minimum number of supervisions";
        }
       },
       GET_AVERAGE_SUPERVISIONS{
        @Override
        public String toString(){
            return "Get average number of supervisions";
        }
       },
       GET_SUPERVISIONS_BY_SUPERVISER{
        @Override
        public String toString(){
            return "Get number of supervisions of a specific supervisor";
        }
       },
       GET_STUDENTS_WITH_CANDIDACY_AND_WITHOUT_PROPOSAL{
        @Override
        public String toString(){
            return "Get students with candidacy and without proposal";
        }
       },
       GET_AVAILABLE_PROPOSALS{
        @Override
        public String toString(){
            return "Get available proposals";
        }
       },
       GET_ASSIGNED_PROPOSALS{
        @Override
        public String toString(){
            return "Get assigned proposals";
        }
       },
       GET_CANDIDACY_PROPOSALS{
        @Override
        public String toString(){
            return "Get proposals each student has on their candidacy";
        }
       },
       SOLVE_TIE{
        @Override
        public String toString(){
            return "Solve tie";
        }
       },
       EXPORT{
        @Override
        public String toString(){
            return "Export to csv";
        }
       },
       SAVE{
        @Override
        public String toString(){
            return "Save";
        }
       }, LOAD{
        @Override
        public String toString(){
               return "Load";
           }
       },
       EXIT{
        @Override
        public String toString(){return "Exit";}
       },
        UNDO{
            public String toString(){return "Undo";}
        },
        REDO{
            public String toString(){return "Redo";}
        }

}
