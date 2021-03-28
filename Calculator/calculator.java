package Calculator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class calculator {
    static boolean isArabic = true;
    static int convertedNumbers = 0;
    static Converter converter = new Converter();

    // Пример: 8 - 3
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        StringTokenizer tokenizer = new StringTokenizer(line, " \t\n\r");

        String aString = tokenizer.nextToken();
        String sign = tokenizer.nextToken();
        String bString = tokenizer.nextToken();
        int result = 0;

        if (sign.equals("+")) {
            int a = extractAndCheck(aString);
            int b = extractAndCheck(bString);
            result = a + b;
        } else if (sign.equals("-")) {
            int a = extractAndCheck(aString);
            int b = extractAndCheck(bString);
            result = a - b;
        } else if (sign.equals("/")) {
            int a = extractAndCheck(aString);
            int b = extractAndCheck(bString);
            result = a / b;
        } else if (sign.equals("*")) {
            int a = extractAndCheck(aString);
            int b = extractAndCheck(bString);
            result = a * b;
        }

        if (isArabic) {
            System.out.println(result);
        } else {
            System.out.println(converter.arabicToRoman(result));
        }

        scanner.close();
    }

    private static int extractAndCheck(String input) {
        int result = extractInt(input);
        checkBounds(result);
        return result;
    }

    private static void checkBounds(int a) {
        if (a > 10) {
            throw new RuntimeException("Число должно быть меньше 11!");
        }
    }

    private static int extractInt(String stringFromLine) {
        int result = 0;
        if (converter.canConvertRomainToArabic(stringFromLine)) {
            result = converter.romanToArabic(stringFromLine);
            if (convertedNumbers == 0) {
                isArabic = false;
            } else if (isArabic) {
                throw new RuntimeException("Нельзя работать с арабскими и римскими числами одновременно!");
            }
        } else {
            result = Integer.parseInt(stringFromLine);
            if (convertedNumbers == 0) {
                isArabic = true;
            } else if (!isArabic) {
                throw new RuntimeException("Нельзя работать с арабскими и римскими числами одновременно!");
            }
        }
        convertedNumbers += 1;
        return result;
    }

    static class Converter {
        public boolean canConvertRomainToArabic(String input) {
            try {
                romanToArabic(input);
                return true;
            } catch (IllegalArgumentException ex) {
                return false;
            }
        }

        public String arabicToRoman(int number) {
            if ((number <= 0) || (number > 4000)) {
                throw new IllegalArgumentException(number + " не входит в (0,4000]");
            }

            List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

            int i = 0;
            StringBuilder sb = new StringBuilder();

            while ((number > 0) && (i < romanNumerals.size())) {
                RomanNumeral currentSymbol = romanNumerals.get(i);
                if (currentSymbol.getValue() <= number) {
                    sb.append(currentSymbol.name());
                    number -= currentSymbol.getValue();
                } else {
                    i++;
                }
            }

            return sb.toString();
        }

        public int romanToArabic(String input) {
            String romanNumeral = input.toUpperCase();
            int result = 0;

            List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

            int i = 0;

            while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
                RomanNumeral symbol = romanNumerals.get(i);
                if (romanNumeral.startsWith(symbol.name())) {
                    result += symbol.getValue();
                    romanNumeral = romanNumeral.substring(symbol.name().length());
                } else {
                    i++;
                }
            }

            if (romanNumeral.length() > 0) {
                throw new IllegalArgumentException(input + " не может быть сконвертированно в арабское число");
            }

            return result;
        }

        static enum RomanNumeral {
            I(1),
            IV(4),
            V(5),
            IX(9),
            X(10),
            XL(40),
            L(50),
            XC(90),
            C(100),
            CD(400),
            D(500),
            CM(900),
            M(1000);

            private int value;

            RomanNumeral(int value) {
                this.value = value;
            }

            public static List<RomanNumeral> getReverseSortedValues() {
                return Arrays.stream(values())
                        .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                        .collect(Collectors.toList());
            }

            public int getValue() {
                return value;
            }
        }
    }
}
