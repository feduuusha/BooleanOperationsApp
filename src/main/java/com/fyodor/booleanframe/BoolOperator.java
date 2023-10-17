package com.fyodor.booleanframe;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.InterpreterError;

import java.util.*;

public class BoolOperator {
    private String stringInput, expression;
    private int numberOfVars, rows = 1;
    private int[][] table;
    private final List<List<Integer>> abbrDNF = new ArrayList<>();
    private List<List<List<Integer>>> tupicDNF = new ArrayList<>();
    private final List<List<List<Integer>>> minimumDNF = new ArrayList<>();

    public BoolOperator(String str) {
        this.stringInput = str;
        if (inputStringRecognition()) {
            this.createTable();
            this.fillTableFByInt();
        } else {
            this.counterParameters();
            this.createTable();
            this.converter();
            this.inputStringIsCorrect();
            this.fillTableF();
        }
        this.fillListSDNF();
        this.abbreviatedDNF();
        this.engineOfTupicDNF();
        this.engineOfMinimalDNF();
    }

    private boolean inputStringRecognition() {
        int counter = 0;
        stringInput = stringInput.replace(" ", "");
        for (int i = 0; i < stringInput.length(); ++i) {
            if (stringInput.charAt(i) == '0' || stringInput.charAt(i) == '1') {
                counter += 1;
            } else {
                return false;
            }
        }
        double len = counter;
        counter = 0;
        while (len != 1.0) {
            counter += 1;
            len /= 2;
            if (len < 1.0) throw new InvalidInputStringException("The entered string is incorrect");
        }
        this.numberOfVars = counter;
        for (int i = 0; i < numberOfVars; ++i) this.rows *= 2;
        return true;
    }

    private void counterParameters() {
        int counter = 0;
        for (int i = 9; i >= 0; --i) {
            if (stringInput.contains("x" + (i))) {
                counter = i;
                break;
            }
        }
//        while (stringInput.contains("x" + (counter + 1))) ++counter;
        this.numberOfVars = counter;
        for (int i = 0; i < numberOfVars; ++i) this.rows *= 2;
    }

    private void createTable() {
        this.table = new int[rows][numberOfVars + 1];
        int x = rows, col = 0;
        while (x != 1) {
            x /= 2;
            boolean res = true;
            for (int i = 0; i < table.length; ++i) {
                if (i % x == 0) res = !res;
                table[i][col] = res ? 1 : 0;
            }
            col += 1;
        }
    }

    private void converter() {
        String expression = "if (";
        stringInput = stringInput.toLowerCase();
        stringInput = stringInput.replace("&&", "&");
        stringInput = stringInput.replace("&", "&&");
        stringInput = stringInput.replace("||", "|");
        stringInput = stringInput.replace("|", "||");
        stringInput = stringInput.replace("&&1", "&&true");
        stringInput = stringInput.replace("||1", "||true");
        stringInput = stringInput.replace("&&0", "&&false");
        stringInput = stringInput.replace("||0", "||false");
        stringInput = stringInput.replace("!0", "!false");
        stringInput = stringInput.replace("!1", "!true");
        expression += stringInput;
        expression += ") res = 1;";
        this.expression = expression;
    }

    private void inputStringIsCorrect() throws InvalidInputStringException {
        Interpreter inter = new Interpreter();
        boolean res = true;
        for (int j = 1; j <= numberOfVars; ++j) {
            try {
                inter.set("x" + j, false);
            } catch (EvalError ex) {
                res = false;
            }
        }
        try {
            inter.set("res", 0);
            inter.eval(expression);
        } catch (EvalError | InterpreterError ex) {
            res = false;
        }
        if (!res) throw new InvalidInputStringException("The entered string is incorrect");
    }

    private void fillTableF() {
        for (int i = 0; i < table.length; ++i) {
            Interpreter inter = new Interpreter();
            for (int j = 1; j <= table[0].length; ++j) {
                try {
                    inter.set("x" + j, table[i][j - 1] == 1);
                } catch (EvalError ex) {
                    System.out.println(ex.getMessage());
                }
            }
            try {
                inter.set("res", 0);
                inter.eval(expression);
                table[i][(table[i].length) - 1] = (int) inter.get("res");
            } catch (EvalError ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void fillTableFByInt() {
        for (int i = 0; i < stringInput.length(); ++i) {
            table[i][numberOfVars] = Integer.parseInt(String.valueOf(stringInput.charAt(i)));
        }
    }

    private void fillListSDNF() {
        for (int[] row : table) {
            if (row[row.length - 1] == 1) {
                abbrDNF.add(new ArrayList<>());
                for (int j = 0; j < row.length - 1; ++j) {
                    abbrDNF.get(abbrDNF.size() - 1).add(row[j]);
                }
            }
        }
    }

    public String table() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < numberOfVars; ++i) output.append("x").append(i + 1).append(" ");
        output.append("   F\n");
        for (int[] ints : table) {
            for (int i = 0; i < ints.length; ++i) {
                if (i == ints.length - 1) {
                    output.append("| ").append(ints[i]).append("\n");
                } else output.append(ints[i]).append("   ");
            }
        }
        return output.toString();
    }

    public String scnf() {
        StringBuilder output = new StringBuilder();
        int counter = 0;
        for (int[] variable : table) {
            if (variable[variable.length - 1] == 0) {
                output.append("(");
                for (int j = 0; j < variable.length - 1; ++j) {
                    boolean item = variable[j] == 1;
                    output.append(item ? "¬" + "x" + (j + 1) : "x" + (j + 1)).append(" ∨ ");
                }
                output.delete(output.length()-3, output.length());
                output.append(")∧");
                counter++;
                if (counter % 5 == 0) output.append("\n");

            }
        }
        if (output.length() > 2) {
            if (output.charAt(output.length() - 1) == '\n') output.delete(output.length() - 2, output.length());
            else output.delete(output.length() - 1, output.length());
        }
        output.append("\n");
        return output.toString();
    }

    public String sdnf() {
        StringBuilder output = new StringBuilder();
        int counter = 0;
        for (int[] variable : table) {
            if (variable[variable.length - 1] == 1) {
                output.append("(");
                for (int j = 0; j < variable.length - 1; ++j) {
                    boolean item = variable[j] == 1;
                    output.append(item ? "x" + (j + 1) : "¬" + "x" + (j + 1)).append(" ∧ ");
                }
                output.delete(output.length()-3, output.length());
                output.append(")∨");
                counter++;
                if (counter % 5 == 0) output.append("\n");

            }
        }
        if (2 < output.length()) {
            if (output.charAt(output.length() - 1) == '\n') output.delete(output.length() - 2, output.length());
            else output.delete(output.length() - 1, output.length());
        }
        output.append("\n");
        return output.toString();
    }

    private void abbreviatedDNF() {
        int counter;
        do {
            List<List<Integer>> clone = new ArrayList<>(abbrDNF);
            List<Integer> removable = new ArrayList<>();
            int count = 0;
            for (int k = 0; k < numberOfVars; ++k) {
                for (int j = 0; j < abbrDNF.size(); ++j) {
                    for (int i = j + 1; i < abbrDNF.size(); ++i) {
                        if (abbrDNF.get(j).size() == abbrDNF.get(i).size()) {
                            if (k < abbrDNF.get(j).size()) {
                                if (!abbrDNF.get(j).get(k).equals(abbrDNF.get(i).get(k))) {
                                    List<Integer> lst1 = new ArrayList<>(abbrDNF.get(j));
                                    List<Integer> lst2 = new ArrayList<>(abbrDNF.get(i));
                                    lst1.set(k, -1);
                                    lst2.set(k, -1);
                                    if (lst1.equals(lst2)) {
                                        clone.add(lst1);
                                        count++;
                                        removable.add(i);
                                        removable.add(j);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Set<Integer> set = new HashSet<>(removable);
            removable.clear();
            removable.addAll(set);
            Collections.sort(removable);
            Collections.reverse(removable);
            for (int var : removable) clone.remove(var);
            abbrDNF.clear();
            abbrDNF.addAll(clone);
            Set<List<Integer>> setClone = new HashSet<>(abbrDNF);
            abbrDNF.clear();
            abbrDNF.addAll(setClone);
            counter = count;
        }
        while (counter != 0);
    }

    public String abbrDNF() {
        int counter = 0;
        StringBuilder output = new StringBuilder();
        for (List<Integer> conjunction : abbrDNF) {
            output.append("(");
            for (int j = 0; j < conjunction.size(); ++j) {
                Integer el = conjunction.get(j);
                if (el != -1) output.append(el == 1 ? "x" + (j + 1) : "¬x" + (j + 1)).append(" ∧ ");
            }
            if (output.length() > 3) {
                output.delete(output.length() - 3, output.length());
            }
            output.append(")∨");
            counter++;
            if (counter % 5 == 0) output.append("\n");

        }
        if (output.length() > 2) {
            if (output.charAt(output.length() - 1) == '\n') output.delete(output.length() - 2, output.length());
            else output.delete(output.length() - 1, output.length());
        }
        output.append("\n");
        return output.toString();
    }

    private List<List<Integer>> engineQuineTable() {
        List<List<Integer>> qTable = new ArrayList<>();
        for (int i = 0; i < abbrDNF.size(); ++i) {
            qTable.add(new ArrayList<>());
            for (int[] row : table) {
                int res = 1;
                if (row[row.length - 1] == 1) {
                    for (int k = 0; k < row.length - 1; ++k)
                        if (!abbrDNF.get(i).get(k).equals(-1)) {
                            if (!abbrDNF.get(i).get(k).equals(row[k])) res *= 0;
                        }
                    qTable.get(i).add(res);
                }
            }
        }
        return qTable;
    }

    public String quineTable() {
        StringBuilder output = new StringBuilder();
        List<List<Integer>> qTable = new ArrayList<>(engineQuineTable());
        output.append(" ".repeat(abbrDNF.get(0).size() * 3));
        for (int[] row : table) {
            if (row[row.length - 1] == 1) {
                for (int j = 0; j < row.length - 1; ++j) output.append(row[j]);
                output.append(" ");
            }

        }
        output.append("\n");
        for (int j = 0; j < abbrDNF.size(); ++j) {
            int counter = 0;
            for (int k = 0; k < abbrDNF.get(j).size(); ++k) {
                if (abbrDNF.get(j).get(k) != -1) {
                    if (abbrDNF.get(j).get(k) == 1) {
                        output.append("x").append(k + 1);
                        counter += 2;

                    } else {
                        output.append("¬x").append(k + 1);
                        counter += 3;
                    }
                }
            }
            output.append(" ".repeat(abbrDNF.get(0).size() * 3 - counter));
            for (int i = 0; i < qTable.get(0).size(); ++i) {
                output.append("  ").append(qTable.get(j).get(i)).append("  ");

            }
            output.append("\n");
        }
        return output.toString();
    }

    @SuppressWarnings("SuspiciousListRemoveInLoop")
    private void engineOfTupicDNF() {
        List<List<List<Integer>>> tupic = new ArrayList<>();
        tupic.add(abbrDNF);
        int j = 0;
        while (j != tupic.size()) {
            List<List<Integer>> list = new ArrayList<>(tupic.get(j));
            for (int i = 0; i < list.size(); ++i) {
                List<List<Integer>> copy = new ArrayList<>(list);
                copy.remove(i);
                if (checker(copy)) tupic.add(copy);
            }
            ++j;
        }
        tupic = cleaner(tupic);
        this.tupicDNF = tupic;
    }

    private boolean checker(List<List<Integer>> list) {
        int res = 1;
        for (int[] row : table) {
            if (row[row.length - 1] == 1) {
                int result = 0;
                for (List<Integer> integers : list) {
                    int res1 = 1;
                    for (int j = 0; j < integers.size(); ++j) {
                        if (integers.get(j) != -1) {
                            if (integers.get(j) != row[j]) res1 *= 0;
                        }
                    }
                    if (res1 != 0) ++result;
                }
                result = result > 0 ? 1 : 0;
                res *= result;
            }
        }
        return res == 1;
    }

    private List<List<List<Integer>>> cleaner(List<List<List<Integer>>> list) {
        List<List<List<Integer>>> copy = new ArrayList<>(list);
        List<Integer> removable = new ArrayList<>();
        for (int i = list.size() - 1; i > 0; --i) {
            for (int j = 0; j < i; j++) {
                List<List<Integer>> lst1 = new ArrayList<>(list.get(i));
                List<List<Integer>> lst2 = new ArrayList<>(list.get(j));
                boolean res = true;
                for (List<Integer> vars : lst1) {
                    if (!lst2.contains(vars)) {
                        res = false;
                        break;
                    }
                }
                if (res) {
                    removable.add(j);
                }
            }
        }
        Set<Integer> set = new HashSet<>(removable);
        removable.clear();
        removable.addAll(set);
        Collections.sort(removable);
        Collections.reverse(removable);
        for (Integer index : removable) copy.remove((int) index);
        return copy;
    }

    public String tupicDNF() {
        StringBuilder output = new StringBuilder();
        for (List<List<Integer>> dnf : tupicDNF) {
            for (List<Integer> conjunction : dnf) {
                output.append("(");
                for (int i = 0; i < conjunction.size(); ++i) {
                    if (conjunction.get(i) != -1)
                        output.append(conjunction.get(i) == 1 ? "x" + (i + 1) : "¬x" + (i + 1)).append(" ∧ ");
                }
                if (output.length() > 3) {
                    output.delete(output.length() - 3, output.length());
                }
                output.append(")∨");

            }
            if (output.length() > 2) {
                if (output.charAt(output.length() - 1) == '\n') output.delete(output.length() - 2, output.length());
                else output.delete(output.length() - 1, output.length());
            }
            output.append("\n");
        }
        return output.toString();
    }
    private void engineOfMinimalDNF() {
        int minimum = 1000000000;
        for (List<List<Integer>> dnf : tupicDNF) {
            int counter = 0;
            for (List<Integer> conjunction : dnf) {
                for (Integer literal : conjunction) {
                    if (literal != -1) ++counter;
                }
            }
            if (counter < minimum) minimum = counter;
        }
        for (List<List<Integer>> dnf : tupicDNF) {
            int counter = 0;
            for (List<Integer> conjunction : dnf) {
                for (Integer literal : conjunction) {
                    if (literal != -1) ++counter;
                }
            }
            if (counter == minimum) minimumDNF.add(dnf);
        }
    }
    public String minimumDNF() {
        StringBuilder output = new StringBuilder();
        for (List<List<Integer>> dnf : minimumDNF) {
            for (List<Integer> conjunction : dnf) {
                output.append("(");
                for (int i = 0; i < conjunction.size(); ++i) {
                    if (conjunction.get(i) != -1) output.append((conjunction.get(i) == 1? "x" + (i+1): "¬x" + (i+1))).append(" ∧ ");
                }
                if (output.length() > 3) {
                    output.delete(output.length() - 3, output.length());
                }
                output.append(")∨");
            }
            if (output.length() > 2) {
                if (output.charAt(output.length() - 1) == '\n') output.delete(output.length() - 2, output.length());
                else output.delete(output.length() - 1, output.length());
            }
            output.append("\n");
        }
        return output.toString();
    }
}