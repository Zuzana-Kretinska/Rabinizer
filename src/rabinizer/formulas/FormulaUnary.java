/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rabinizer.formulas;

import java.util.Set;
import net.sf.javabdd.BDD;
import rabinizer.bdd.BDDForFormulae;

/**
 *
 * @author zuzana and jan
 */
public abstract class FormulaUnary extends Formula {
    
    public Formula operand;
    
    public FormulaUnary(Formula operand) {
        this.operand = operand;
    }
    
    public abstract FormulaUnary ThisTypeUnary(Formula operand);
    
    @Override
    public BDD bdd() {
        if (cachedBdd == null) {
            Formula booleanAtom = ThisTypeUnary(operand.representative());
            int bddVar = BDDForFormulae.bijectionBooleanAtomBddVar.id(booleanAtom);
            if (BDDForFormulae.bddFactory.varNum() <= bddVar) {
                BDDForFormulae.bddFactory.extVarNum(1);
            }
            cachedBdd = BDDForFormulae.bddFactory.ithVar(bddVar);
            BDDForFormulae.representativeOfBdd(cachedBdd, this);
        }
        return cachedBdd;
    }

    @Override
    public int hashCode() {
        return 3 * operand.hashCode() + operator().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FormulaUnary)) {
            return false;
        } else {
            return o.getClass().equals(getClass()) && ((FormulaUnary) o).operand.equals(operand);
        }
    }
    
    @Override
    public String toString() {
        if (cachedString == null) {
            cachedString = operator() + operand.toString();
        }
        return cachedString;
    }

    @Override
    public String toReversePolishString() {
        return operator() + " " + operand.toReversePolishString();
    }

    @Override
    public boolean hasSubformula(Formula f) {
        return this.equals(f) || operand.hasSubformula(f);
    }
    
    @Override
    // to be overrridden by GOperator
    public boolean containsG() {
        return operand.containsG();
    }

    @Override
    // to be overrridden by GOperator
    public Set<Formula> gSubformulas() {
        return operand.gSubformulas();
    }

    @Override
    // to be overrridden by GOperator
    public Set<Formula> topmostGs() {
        return operand.topmostGs();
    }

    
}
