package rabinizer.formulas;

import rabinizer.bdd.Valuation;
import java.util.*;

import net.sf.javabdd.*;
import rabinizer.exec.*;
import rabinizer.bdd.BDDForFormulae;

/**
 * Represents a until formula.
 *
 * @author zuzana and jan
 *
 */
public class UOperator extends FormulaBinary {

    @Override
    public String operator() {
        return "U";
    }

    public UOperator(Formula left, Formula right) {
        super(left, right);
    }

    public BDD bdd() {
        if (cachedBdd == null) {
            Formula booleanAtom = new UOperator(
                left.representative(),
                right.representative()
            );
            int bddVar = BDDForFormulae.bijectionBooleanAtomBddVar.id(booleanAtom);
            if (BDDForFormulae.bddFactory.varNum() <= bddVar) {
                BDDForFormulae.bddFactory.extVarNum(bddVar);
            }
            cachedBdd = BDDForFormulae.bddFactory.ithVar(bddVar);
            BDDForFormulae.representativeOfBdd(cachedBdd, this);
        }
        return cachedBdd;
    }

    @Override
    public Formula unfold() {
        // unfold(a U b) = unfold(b) v (unfold(a) ^ X (a U b))
        return new Disjunction(right.unfold(), new Conjunction(left.unfold(), /*new XOperator*/ (this)));
    }

    @Override
    public Formula unfoldNoG() {
        // unfold(a U b) = unfold(b) v (unfold(a) ^ X (a U b))
        return new Disjunction(right.unfoldNoG(), new Conjunction(left.unfoldNoG(), /*new XOperator*/ (this)));
    }

    public Formula toNNF() {
        return new UOperator(left.toNNF(), right.toNNF());
    }

    public Formula negationToNNF() {
        return new Disjunction(new GOperator(right.negationToNNF()),
            new UOperator(right.negationToNNF(), new Conjunction(left.negationToNNF(), right.negationToNNF())));
    }

}
