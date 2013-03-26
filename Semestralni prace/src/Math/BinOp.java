/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Math;

import java.util.ArrayList;

/**
 *
 * @author michalblazek
 */
public class BinOp extends Expr {

    public char operand;
    public Expr c1;
    public Expr c2;

    public BinOp(char operand, Expr constant1, Expr constant2) {
        this.operand = operand;
        this.c1 = constant1;
        this.c2 = constant2;
    }

    public BinOp() {
    }

    @Override
    public double evaluate() {
        switch (operand) {
            case '+': {
                return c1.evaluate() + c2.evaluate();
            }
            case '-': {
                return c1.evaluate() - c2.evaluate();
            }
            case '*': {
                return c1.evaluate() * c2.evaluate();
            }
            case '/': {
                return c1.evaluate() / c2.evaluate();
            }
            case '^': {
                return Math.pow(c1.evaluate(), c2.evaluate());
            }
            default: {
                return Double.NaN;
            }
        }
    }

    @Override
    public Expr derive(char var) {
        switch (operand) {
            case '+': {
                return new BinOp('+', c1.derive(var), c2.derive(var));
            }
            case '-': {
                return new BinOp('-', c1.derive(var), c2.derive(var));
            }
            case '*': {
                return new BinOp('+', new BinOp('*', c1, c2.derive(var)), new BinOp('*', c2, c1.derive(var)));
            }
            case '/': {
                return new BinOp('/', new BinOp('-', new BinOp('*', c1.derive(var), c2), new BinOp('*', c1, c2.derive(var))), new BinOp('*', c2, c2));
            }
            case '^': {
                if (c1.derive(var).evaluate() == 1) {
                    return new BinOp('*', new BinOp('^', new BinOp('*', c2, c1), new Constant(c2.evaluate())), c1.derive(var));
                } else {
                    return c1.derive(var);
                }
            }
            default: {
                return new Constant(Double.NaN);
            }
        }

    }

    @Override
    public Expr simplify() {
        Expr x1, x2;

        x1 = c1.simplify();
        x2 = c2.simplify();

        switch (operand) {
            case '+': {
                if ("Math.Constant".equals(x1.getClass().getName())) {
                    if (x1.evaluate() == 0) {
                        return x2;
                    }
                    if ("Math.Constant".equals(x2.getClass().getName())) {
                        if (x2.evaluate() == 0) {
                            return x1;
                        }
                    }
                }
                break;
            }
            case '-': {
                if ("Math.Constant".equals(x1.getClass().getName())) {
                    if (x1.evaluate() == 0) {
                        return x2;
                    }
                    if ("Math.Constant".equals(x2.getClass().getName())) {
                        if (x2.evaluate() == 0) {
                            return x1;
                        }
                    }
                    break;
                }
            }
            case '*': {
                if ("Math.Constant".equals(x1.getClass().getName())) {
                    if (x1.evaluate() == 0) {
                        return new Constant(0);
                    }
                }
                if ("Math.Constant".equals(x2.getClass().getName())) {
                    if (x2.evaluate() == 0) {
                        return new Constant(0);

                    }
                }
                if ("Math.Constant".equals(x1.getClass().getName())) {
                    if (x1.evaluate() == 1) {
                        return x2;

                    }
                }
                if ("Math.Constant".equals(x2.getClass().getName())) {
                    if (x2.evaluate() == 1) {
                        return x1;
                    }
                }

                break;

            }
            case '/': {
                if ("Math.Constant".equals(x1.getClass().getName())) {
                    if (x1.evaluate() == 0) {
                        return new Constant(0);
                    }
                }
                if ("Math.Constant".equals(x2.getClass().getName())) {
                    if (x2.evaluate() == 1) {
                        return x1;
                    }
                }
            }
            case '^': {
                if ("Math.Constant".equals(x1.getClass().getName())) {
                    if (x1.evaluate() == 0) {
                        return new Constant(0);
                    }
                }
                if ("Math.Constant".equals(x1.getClass().getName())) {
                    if (x1.evaluate() == 1) {
                        return new Constant(1);
                    }
                }
                if ("Math.Constant".equals(x2.getClass().getName())) {
                    if (x2.evaluate() == 1) {
                        return x1;
                    }
                }
                if ("Math.Constant".equals(x2.getClass().getName())) {
                    if (x2.evaluate() == 0) {
                        return new Constant(1);
                    }
                }
            }


        }
        if ("Math.Constant".equals(x2.getClass().getName()) && "Math.Constant".equals(x1.getClass().getName())) {
            return new Constant(new BinOp(operand, x1, x2).evaluate());
        } else {
            return new BinOp(operand, x1, x2);
        }
    }

    @Override
    public String toString() {
        return c1.toString() + " " + operand + " " + c2.toString();
    }

    public static Expr fromArrayList(ArrayList List) throws ArrayIndexOutOfBoundsException {
        if (List.size()==1 && List.get(0).getClass()==BinOp.class){
            return (Expr) List.get(0);
        }

        int pocetzavorek = 0;
        int indexKZ = 0; // Index konečné závorky
        int indexZZ = Integer.MAX_VALUE; // Index začátku závorky

        //Spočítá počet závorek, uzavřených dvojic.
        for (int i = 0; List.size() > i; i++) {
            if (List.get(i).toString().charAt(0) == '(') {
                pocetzavorek++;
            }
        }

        // Jestliže je první znak řetězce znak '-', bude první čílso záporné.
        if ((List.get(0).toString().charAt(0) == '-') && List.get(0).toString().length() == 1) {
            List.add(1, new Constant(Double.valueOf(List.get(1).toString()) * -1));
            List.remove(0);
            List.remove(1); // odepbere se index 0, to co bylo index 1 je 0, 2 je 1
        }
        if ((List.get(0).toString().charAt(0) == '+') && List.get(0).toString().length() == 1) {           
            List.remove(0);          
        }
        // Převede čísla v řetězci na konstanty.
        for (int i = 0; i < List.size(); i++) {
            if (Character.isDigit(List.get(i).toString().charAt(0)) && List.get(i).getClass().equals(Constant.class) == false) {
                List.set(i, new Constant(Double.valueOf(List.get(i).toString())));
            }
        }

        while (pocetzavorek != 0) {
            //Najde index nejvnitřejší konečné závorky.
            for (int i = 0; indexKZ == 0; i++) {
                if (List.get(i).toString().charAt(0) == ')') {
                    indexKZ = i;
                }
            }
            //Najde index nejvnitřejší začínající závorky.
            for (int i = indexKZ; indexZZ == Integer.MAX_VALUE; i--) {
                if (List.get(i).toString().charAt(0) == '(') {
                    indexZZ = i;
                }
            }
            //Vytvoří nový ArrayList obsahujicí řetězec nejvnitřejší závorky.
            ArrayList ListP = new ArrayList();
            for (int j = indexZZ + 1; j != indexKZ; j++) {
                ListP.add(List.get(j));
            }
            for (int j = indexKZ; j != indexZZ - 1; j--) {
                List.remove(j);
            }
            //Zavolá znovu metodu výpočet, jako vstup bude ArrayList vnitřní závorky.
            //List.add(indexZZ, BinOp.fromArrayList(ListP));
            List.add(indexZZ, BinOp.fromArrayList(ListP));
            ListP.clear();
            pocetzavorek--;
            indexKZ = 0;
            indexZZ = Integer.MAX_VALUE;
        }
        /**
         * Vlastní výpočet, vložený řetězec skládá na class BinOp podle
         * matematických zákonů. Nejprve se provede násobení a dělení a poté
         * teprve sčítání a odčítání.
         */
        for (int i = 0; i < List.size(); i++) {
            if (List.get(i).getClass() == "".getClass() && (List.get(i).toString().charAt(0) == '^')) {
                List.set(i, new BinOp(String.valueOf(List.get(i)).toCharArray()[0], (Expr) List.get(i - 1), (Expr) List.get(i + 1)));
                List.remove(i + 1);
                List.remove(i - 1);
                i--;
            }
        }

        for (int i = 0; i < List.size(); i++) {
            if (List.get(i).getClass() == "".getClass() && (List.get(i).toString().charAt(0) == '*' || List.get(i).toString().charAt(0) == '/')) {
                List.set(i, new BinOp(String.valueOf(List.get(i)).toCharArray()[0], (Expr) List.get(i - 1), (Expr) List.get(i + 1)));
                List.remove(i + 1);
                List.remove(i - 1);
                i--;
            }
        }

        for (int i = 0; i < List.size(); i++) {
            if (List.get(i).getClass() == "".getClass()) {
                List.set(i, new BinOp(String.valueOf(List.get(i)).toCharArray()[0], (Expr) List.get(i - 1), (Expr) List.get(i + 1)));
                List.remove(i + 1);
                List.remove(i - 1);
                i--;
            }
        }
        return new Bracers('(', (Expr) List.get(0), ')');
    }
}
