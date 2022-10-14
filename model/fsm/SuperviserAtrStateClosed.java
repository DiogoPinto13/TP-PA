package pt.isec.pa.apoio_poe.model.fsm;

import java.util.ArrayList;

import pt.isec.pa.apoio_poe.model.data.Phase;
import pt.isec.pa.apoio_poe.model.errors.ErrorLog;

public class SuperviserAtrStateClosed extends SupreviserAtrState{
    SuperviserAtrStateClosed(PhaseContext context, Phase phase){
        super(context, phase);
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
    public ErrorLog setLock(){
        return null;
    }
    @Override
    public boolean getLock(){
        return true;
    }
}
