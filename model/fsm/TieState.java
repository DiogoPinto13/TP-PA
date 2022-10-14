package pt.isec.pa.apoio_poe.model.fsm;

import java.util.ArrayList;
import java.util.List;
import pt.isec.pa.apoio_poe.model.data.Phase;
import pt.isec.pa.apoio_poe.model.data.Student;
import pt.isec.pa.apoio_poe.model.errors.ErrorLog;

public class TieState extends PhaseStateAdapter{
    
    TieState(PhaseContext context, Phase phase){
        super(context, phase);
    }

    @Override 
    public PhaseState getState(){
        return PhaseState.TIESTATE;
    }

    @Override
    public Options[] getPossibleOptions(){
        ArrayList<Options> options = new ArrayList<Options>();

        options.add(Options.SOLVE_TIE);
        options.add(Options.LOOKUP);
        options.add(Options.SAVE);
        options.add(Options.EXIT);
        options.add(Options.UNDO);
        options.add(Options.REDO);

        return options.toArray(new Options[0]);
    }

    @Override
    public Options[] query(){
        ArrayList<Options> options = new ArrayList<Options>();
        options.add(Options.GET_CANDIDACY_PROPOSALS);
        return options.toArray(new Options[0]);

    }


    @Override
    public List<Student> getTiedStudents(){
        List<Student> result = new ArrayList<Student>();
        result.add(phase.getTie().getFirstStudent());
        result.add(phase.getTie().getSecondStudent());
        return result;
    }

    @Override
    public ErrorLog solveTie(Student winner){
        changeState(context.getStateMap(PhaseState.PROPOSAL_ATR));
        
        return phase.manualAssignProposal(winner, phase.getTie().getProposal());
    }
}
