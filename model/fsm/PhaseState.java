package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.IPhaseState;
import pt.isec.pa.apoio_poe.model.data.Phase;

public enum PhaseState{
    
    CONFIG,CONFIG_CLOSED, APPLICATON_OPT,APPLICATON_OPT_CLOSED, PROPOSAL_ATR, PROPOSAL_ATR_CLOSED, SUPREVISER_ATR, SUPREVISER_ATR_CLOSED, LOOKUP, TIESTATE;
        IPhaseState createState(PhaseContext context, Phase phase){
        return switch(this){
            case CONFIG -> new ConfigState(context, phase);
            case CONFIG_CLOSED -> new ConfigStateClosed(context, phase);
            case APPLICATON_OPT -> new ApplicationOptState(context, phase);
            case APPLICATON_OPT_CLOSED  -> new ApplicationOptStateClosed(context, phase);
            case PROPOSAL_ATR -> new ProposalsAtrState(context, phase);
            case PROPOSAL_ATR_CLOSED -> new ProposalsAtrStateClosed(context, phase);
            case SUPREVISER_ATR -> new SupreviserAtrState(context, phase);
            case SUPREVISER_ATR_CLOSED -> new SuperviserAtrStateClosed(context, phase);
            case LOOKUP -> new LookupState(context, phase);
            case TIESTATE -> new TieState(context, phase);
            default -> null;
        };
    }
    

}
