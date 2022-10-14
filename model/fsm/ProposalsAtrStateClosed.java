package pt.isec.pa.apoio_poe.model.fsm;

import java.util.ArrayList;
import pt.isec.pa.apoio_poe.model.data.Phase;
import pt.isec.pa.apoio_poe.model.data.Proposal;
import pt.isec.pa.apoio_poe.model.data.Student;
import pt.isec.pa.apoio_poe.model.errors.ErrorLog;

public class ProposalsAtrStateClosed extends ProposalsAtrState{
    
    ProposalsAtrStateClosed(PhaseContext context, Phase phase){
        super(context, phase);
    }
    @Override
    public boolean getLock(){
        return true;
    }
    @Override
    public PhaseState getState(){
        return PhaseState.PROPOSAL_ATR_CLOSED;
    }
    @Override
    public ErrorLog setLock(){
        return null;
    }
    @Override
    public ErrorLog manualAssignProposal(Student student, Proposal proposal){
        return null;
    }
    @Override
    public ErrorLog manualRemoveProposal(Proposal p){
        return null;
    }

    @Override
    public Options[] getPossibleOptions(){
        ArrayList<Options> options = new ArrayList<Options>();

        options.add(Options.LOOKUP);
        options.add(Options.NEXT);
        options.add(Options.PREVIOUS);
        options.add(Options.SAVE);
        options.add(Options.UNDO);
        options.add(Options.REDO);
        options.add(Options.EXIT);

        return options.toArray(new Options[0]);
    }
    @Override
    public ErrorLog automaticTeacherStudentProposalAssociation(){
        return null;
    }

}
