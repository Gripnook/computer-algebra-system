package calculator;

import java.math.BigDecimal;

import calculator.Calculator;

/**
 * A class that contains various general purpose static methods for working with variables of type String.
 * 
 * @author Andrei Purcarus
 *
 */
public class Utility
{

    /**
     * Converts all characters stored within the string to lower case, other than the special characters '\u03A0' (PI)
     * and '\u03A3' (SIGMA).
     * 
     * @param str
     *            - The string to be converted to lower case.
     * @return the string with all characters other than exceptions converted to lower case.
     */
    public static String toLowerCase(String str)
    {
        String strLowerCase = "";
        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) != '\u03A0' && str.charAt(i) != '\u03A3')
            {
                strLowerCase += str.substring(i, i + 1).toLowerCase();
            } else
            {
                strLowerCase += str.substring(i, i + 1);
            }
        }
        return strLowerCase;
    }

    /**
     * Removes whitespace from a String.
     * 
     * @param str
     *            - the String in which to remove whitespace.
     * @return the String with whitespace removed.
     */
    public static String removeWhiteSpace(String str)
    {
        StringBuilder result = new StringBuilder(str);

        for (int i = 0; i < result.length(); i++)
        {
            if (result.charAt(i) == ' ')
            {
                result.deleteCharAt(i);
                i--;
            }
        }

        return result.toString();
    }

    /**
     * Checks if all open brackets in the String have a corresponding close bracket.
     * 
     * @param str
     *            - the String to be checked for bracket pairs.
     * @return true if the brackets in the String all come in valid pairs, false otherwise.
     */
    public static boolean checkBracketPairs(String str)
    {
        int bracketCount = 0;
        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) == '(')
                bracketCount++;
            if (str.charAt(i) == ')')
                bracketCount--;
            if (bracketCount < 0)
                break;
        }
        if (bracketCount == 0)
        {
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * Checks if the String is composed of only bracket characters '(' and ')'.
     * 
     * @param str
     *            - the String to be checked.
     * @return true if the String contains only the specified characters, false otherwise.
     */
    public static boolean isAllBrackets(String str)
    {
        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) != '(' && str.charAt(i) != ')')
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes any pairs of brackets at the start and end of a String.
     * 
     * @param str
     *            - the String in which to remove any pairs of end brackets.
     * @return the String with any end brackets removed.
     */
    public static String removeEndBrackets(String str)
    {
        if (str.isEmpty() || str.charAt(0) != '(' || str.charAt(str.length() - 1) != ')')
        {
            return str;
        } else if (checkBracketPairs(str.substring(1, str.length() - 1)) && str.charAt(0) == '('
                && str.charAt(str.length() - 1) == ')')
        {
            return removeEndBrackets(str.substring(1, str.length() - 1));
        } else
        {
            return str;
        }
    }

    /**
     * Checks if the String can be parsed into a type BigDecimal.
     * 
     * @param str
     *            - The String to be checked.
     * @return true if the String can be parsed into a BigDecimal, false otherwise.
     */
    public static boolean isNumerical(String str)
    {
        try
        {
            new BigDecimal(str);
        } catch (NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    /**
     * Checks if the character is a numeral or any of the '.', 'e', '+' or '-' characters used in exponential notation.
     * 
     * @param character
     *            - the character to be checked.
     * @return true if the character is any of the specified characters, false otherwise.
     */
    public static boolean isNum(char character)
    {
        return (character == '0' || character == '1' || character == '2' || character == '3' || character == '4'
                || character == '5' || character == '6' || character == '7' || character == '8' || character == '9'
                || character == '.' || character == 'e' || character == '-' || character == '+');
    }

    /**
     * Checks if the character is a numeral or the '.' character used in decimal numbers.
     * 
     * @param character
     *            - character to be checked.
     * @return true if the character is any of the specified characters, false otherwise.
     */
    public static boolean isActualNum(char character)
    {
        return (character == '0' || character == '1' || character == '2' || character == '3' || character == '4'
                || character == '5' || character == '6' || character == '7' || character == '8' || character == '9' || character == '.');
    }

    /**
     * Checks if the character is a lower case letter.
     * 
     * @param character
     *            - character to be checked.
     * @return true if the character is a lower case letter, false otherwise.
     */
    public static boolean isActualLetter(char character)
    {
        return ((int) (character) >= (int) ('a') && (int) (character) <= (int) ('z'));
    }

    /**
     * Checks if the character is a special character that can have implied multiplication to the left.
     * 
     * @param character
     *            - character to be checked.
     * @return true if the character is a special left character, false otherwise.
     */
    public static boolean isSpecialLeft(char character)
    {
        boolean result = false;
        for (int i = 0; i < Calculator.specialCharactersLeft.length; i++)
        {
            if (character == Calculator.specialCharactersLeft[i])
            {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Checks if the character is a special character that can have implied multiplication to the right.
     * 
     * @param character
     *            - character to be checked.
     * @return true if the character is a special right character, false otherwise.
     */
    public static boolean isSpecialRight(char character)
    {
        boolean result = false;
        for (int i = 0; i < Calculator.specialCharactersRight.length; i++)
        {
            if (character == Calculator.specialCharactersRight[i])
            {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Checks for the usage of '+' or '-' in exponential notation. The function assumes that the character at position
     * in the String is one of these two characters, and proceeds to check the surrounding characters to determine
     * whether they are used in the context of exponential notation.
     * 
     * @param str
     *            - the String to be checked.
     * @param position
     *            - the position of the '+' or '-' character.
     * 
     * @return true if the character is used in the context of exponential notation, false otherwise.
     */
    public static boolean sumAndDiffCheckForExponentialNotation(String str, int position)
    {
        if (position > 1 && position != str.length() - 1 && str.charAt(position - 1) == 'e'
                && Utility.isActualNum(str.charAt(position + 1)) && str.charAt(position + 1) != '.')
        {
            if (Utility.isActualNum(str.charAt(position - 2)))
            {
                if (str.charAt(position - 2) == '.')
                {
                    if (position > 2 && Utility.isActualNum(str.charAt(position - 3))
                            && str.charAt(position - 3) != '.')
                    {
                        return true;
                    } else
                    {
                        return false;
                    }
                } else
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds the '*' character where implicit multiplication occurs in the String.
     * 
     * The 'e' character is special due to its usage in exponential notation, and so implicit multiplication should be
     * avoided with this character.
     * 
     * @param str
     *            - the String for which to make implicit multiplication explicit.
     * @return the String with '*' characters added in place of implicit multiplication.
     */
    public static String addImplicitMultiplication(String str)
    {
        StringBuilder result = new StringBuilder(str);

        for (int i = 0; i < result.length(); i++)
        {
            if (isActualLetter(result.charAt(i)) || result.charAt(i) == '(' || isSpecialLeft(result.charAt(i)))
            {
                if (i > 0 && (result.charAt(i - 1) == ')' || isActualNum(result.charAt(i - 1))))
                {
                    if (result.charAt(i) == 'e' && isActualNum(result.charAt(i - 1)))
                    {
                        if (i + 1 == result.length()
                                || (result.charAt(i + 1) != '+' && result.charAt(i + 1) != '-' && !isActualNum(result
                                        .charAt(i + 1))))
                        {
                            result.insert(i, '*');
                        }
                    } else
                    {
                        result.insert(i, '*');
                    }
                }
            }
            if (result.charAt(i) == 'x' || result.charAt(i) == 'i' || result.charAt(i) == 'e'
                    || isSpecialRight(result.charAt(i)))
            {
                if (i + 1 < result.length()
                        && (result.charAt(i + 1) == '(' || result.charAt(i + 1) == 'x' || result.charAt(i + 1) == 'i'
                                || result.charAt(i + 1) == 'e' || result.charAt(i + 1) == 'p' || isSpecialLeft(result
                                    .charAt(i + 1))))
                {
                    result.insert(i + 1, '*');
                }
            }
        }

        return result.toString();
    }
}
