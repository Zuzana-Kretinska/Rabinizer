package rabinizer.formulas;

import net.sf.javabdd.*;
import rabinizer.bdd.BDDForFormulae;

/**
 * @author zuzana and jan
 *
 */
public class Disjunction extends FormulaBinaryBoolean {

    public Disjunction(Formula left, Formula right) {
        super(left, right);
    }

    @Override
    public Disjunction ThisTypeBoolean(Formula left, Formula right) {
        return new Disjunction(left, right);
    }

    @Override
    public String operator() {
        return "|";
    }

    @Override
    public BDD bdd() {
        if (cachedBdd == null) {
            cachedBdd = left.bdd().or(right.bdd());
            BDDForFormulae.representativeOfBdd(cachedBdd, this);
        }
        return cachedBdd;
    }

    @Override
    public Formula removeConstants() {
        Formula new_left = left.removeConstants();
        if (new_left instanceof BooleanConstant) {
            if (((BooleanConstant) new_left).value) {
                return new BooleanConstant(true);
            } else {
                return right.removeConstants();
            }
        } else {
            Formula new_right = right.removeConstants();
            if (new_right instanceof BooleanConstant) {
                if (((BooleanConstant) new_right).value) {
                    return new BooleanConstant(true);
                } else {
                    return new_left;
                }
            } else {
                return new Disjunction(new_left, new_right);
            }
        }
    }

    @Override
    public boolean ignoresG(Formula f) {
//        return (!left.hasSubformula(f) || left.ignoresG(f))
//            && (!right.hasSubformula(f) || right.ignoresG(f));
        if (!hasSubformula(f)) {
            return true;
        } else {
            return left.ignoresG(f) && right.ignoresG(f);
        }
    }

    @Override
    public Formula toNNF() {
        return new Disjunction(left.toNNF(), right.toNNF());
    }

    @Override
    public Formula negationToNNF() {
        return new Conjunction(left.negationToNNF(), right.negationToNNF());
    }

    // ============================================================
    @Override
    public boolean isUnfoldOfF() {
        if (right instanceof XOperator) {
            if (((XOperator) right).operand instanceof FOperator) {
                return true;
            }
        }
        return false;
    }

}
