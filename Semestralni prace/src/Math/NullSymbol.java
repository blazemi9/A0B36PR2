/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Math;

import GUI.BoundingBox;
import System.MathList;

/**
 *
 * @author michalblazek
 */
public class NullSymbol extends Expr {

    @Override
    public String toString() {
        return " ";
    }
    
    @Override
    public double evaluate() {
        return 0;
    }

    @Override
    public Expr derive(char var) {
        return new NullSymbol();
    }

    @Override
    public Expr simplify() {
        return new NullSymbol();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(this, 0, 0, this.length(), this.height());
    }

    @Override
    public BoundingBox getBoundingBox(int x, int y) {
        return new BoundingBox(this, x, y, this.length(), this.height());
    }   

    @Override
    public int length() {
        return 0;
    }

    @Override
    public int missingItemInBinOp() {
        return 1;
    }

    @Override
    public int missingItemInBinOp(int hloubka) {
        return 1;
    }

    @Override
    public boolean containNull() {
        return true;
    }

    @Override
    public MathList<Variable> variablesInList() {
        return new MathList<Variable>();
    }

    @Override
    public void changeVariable(Variable v) {       
    }

    @Override
    public int height() {
        return 1;
    }

    @Override
    public MathList<BoundingBox> getAllBoundingBoxs() {
        MathList<BoundingBox> list = new MathList<BoundingBox>();
        list.add(this.getBoundingBox());
        return list;
    }

    @Override
    public MathList<BoundingBox> getAllBoundingBoxs(int x, int y) {
        MathList<BoundingBox> list = new MathList<BoundingBox>();
        list.add(this.getBoundingBox(x, y));
        return list;
    }
}
