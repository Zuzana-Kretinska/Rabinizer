package rabinizer.formulas;

import rabinizer.bdd.Valuation;
import net.sf.javabdd.*;
import rabinizer.bdd.BDDForFormulae;

/**
 * @author zuzana and jan
 *
 */
public class Literal extends FormulaNullary {

    public String atom;
    public int atomId;
    public boolean negated;

    public Literal(String atom, int atomId, boolean negated) {
        this.atom = atom;
        this.atomId = atomId;
        this.negated = negated;
    }

    @Override
    public String operator() {
        return null;
    }

    public Literal positiveLiteral() {
        return new Literal(this.atom, this.atomId, false);
    }

    public Literal negated() {
        return new Literal(atom, atomId, !negated);
    }

    @Override
    public BDD bdd() { 
        if (cachedBdd == null) { 
            int bddVar = BDDForFormulae.bijectionBooleanAtomBddVar.id(this.positiveLiteral()); // R3: just "this"
            if (BDDForFormulae.bddFactory.varNum() <= bddVar) {
                BDDForFormulae.bddFactory.extVarNum(1);
            }
            cachedBdd = (negated ? BDDForFormulae.bddFactory.nithVar(bddVar) : BDDForFormulae.bddFactory.ithVar(bddVar));
            BDDForFormulae.representativeOfBdd(cachedBdd, this);
        } 
        return cachedBdd;
    }

    @Override
    public int hashCode() {
        return atomId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Literal)) {
            return false;
        } else {
            return ((Literal) o).atomId == this.atomId && ((Literal) o).negated == this.negated;
        }
    }

    @Override
    public String toReversePolishString() {
        return cachedString = (negated ? "! " : "") + atom;
    }

    @Override
    public String toString() {
        if (cachedString == null) {
            cachedString = (negated ? "!" : "") + atom;
        }
        return cachedString;
    }

    @Override
    public Formula evaluateValuation(Valuation valuation) {
        return new BooleanConstant(valuation.get(atomId) ^ negated);
    }

    @Override
    public Formula evaluateLiteral(Literal literal) {
        if (literal.atomId != this.atomId) {
            return this;
        } else {
            return new BooleanConstant(literal.negated == this.negated);
        }
    }

    @Override
    public Formula negationToNNF() {
        return this.negated();
    }

    @Override
    public Literal getAnUnguardedLiteral() {
        return this;
    }

}
