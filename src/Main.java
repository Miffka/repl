import java.util.*;

public class Main {
    public static boolean isServiceString(String input){
        return input.startsWith("/");
    }


    public static boolean answerToServiceCommand(String input){
        boolean stop_iteration = false;
        if (input.equals("/exit")) {
            System.out.println("Bye!");
            stop_iteration = true;

        } else if (input.equals("/help")) {
            System.out.println("The program serves as microcalculator\nType /exit to exit the program");

        } else {
            System.out.println("Unknown command");
        }
        return stop_iteration;
    }

    public static boolean isAssignment(String input) {
        return input.matches(".+=.+");
    }

    public static boolean isValidExpression(String input) {
        return input.matches("[A-Za-z\\d\\-+\\s]+");
    }

    public static boolean isValidIdentifier(String input) {
        boolean a = input.matches("[A-Za-z ]+");
        if (!a) {
            System.out.println("Invalid identifier");
        }
        return a;
    }

    public static boolean isValidValue(String input){
        boolean a = input.matches("([A-Za-z]+|\\d+)");
        if (!a) {
            System.out.println("Invalid value");
        }
        return a;
    }

    public static boolean isValidAssignment(String[] list){
        boolean a = list.length == 2;
        if (!a) {
            System.out.println("Invalid assignment");
        }
        return a;
    }

    public static boolean isKnownVariable(String value, Map<String, Integer> variables){
        boolean a = variables.containsKey(value);
        if (!a) {
            System.out.println("Unknown variable");
        }
        return a;
    }

    public static void tryAssignment(String input, Map<String, Integer> variables) {
        String[] variableAndValue = input.split(" *= *");
        if (isValidAssignment(variableAndValue) & isValidIdentifier(variableAndValue[0]) & isValidValue(variableAndValue[1])) {
            boolean sussess = assign(variableAndValue, variables);
        }
    }

    public static boolean assign(String[] variableAndValue, Map<String, Integer> variables) {
        int value = 0;
        String variable = variableAndValue[0].trim();
        String valueS = variableAndValue[1].trim();
        try {
            value = Integer.parseInt(valueS);
        } catch (NumberFormatException e) {
            if (isKnownVariable(valueS, variables)) value = variables.get(valueS);
            else return false;
        }
        variables.put(variable, value);
        return true;
    }

    public static void calculateAndPrintExpression(String input, Map<String, Integer> variables){
        String[] numbersAndOperators = input.split("\\s+");
        int ans = 0;
        int number = 0;
        String value;
        int nMinuses = 0;
        boolean nextMinus = false;
        for (int i = 0; i < numbersAndOperators.length; i++) {
            if (i % 2 == 0) {
                value = numbersAndOperators[i].trim();

                try {
                    number = Integer.parseInt(value);
                    ans = makeCalculation(ans, number, nextMinus);
                } catch (NumberFormatException e) {

                    if (isValidIdentifier(value)) {
                        if (isKnownVariable(value, variables)) {
                            number = variables.get(value);
                            ans = makeCalculation(ans, number, nextMinus);

                        } else {
                            break;
                        }

                    } else {
                        System.out.println("Invalid expression");
                        break;
                    }
                }
            } else {

                if (numbersAndOperators[i].matches("[+\\-]+")) {
                    nMinuses = numbersAndOperators[i].replaceAll("[^\\-]", "").length();
                    nextMinus = nMinuses % 2 == 1;

                } else {
                    System.out.println("Invalid expression");
                    break;
                }
            }

            if (i == numbersAndOperators.length - 1) {
                System.out.println(ans);
            }
        }
    }

    public static int makeCalculation(int ans, int number, boolean nextMinus){
        if (nextMinus) {
            ans -= number;
        } else {
            ans += number;
        }
        return ans;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String str;
        Map<String, Integer> variables = new HashMap<String, Integer>();
        while (true) {
            str = scanner.nextLine().trim();

            if (!str.isEmpty()) {

                if (isServiceString(str)) {
                    if (answerToServiceCommand(str)) {
                        break;
                    }

                } else if (isAssignment(str)) {
                    tryAssignment(str, variables);

                } else if (isValidExpression(str)) {
                    calculateAndPrintExpression(str, variables);

                } else { // Invalid string input
                    System.out.println("Invalid expression");
                }
            }
        }
    }

}