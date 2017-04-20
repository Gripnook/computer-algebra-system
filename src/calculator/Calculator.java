package calculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import calculator.Math;
import calculator.Utility;

/**
 * A class that contains methods to perform mathematical operations on variables of type String which store mathematical
 * expressions.
 * 
 * @author Andrei Purcarus
 *
 */
public class Calculator
{

    /**
     * The modes the calculator can use to evaluate trigonometric functions.
     * 
     * @author Andrei Purcarus
     *
     */
    public static enum Mode
    {
        radians, degrees
    }

    /**
     * The modes the calculator can use to evaluate definite integrals.
     * 
     * @author Andrei Purcarus
     *
     */
    public static enum IntegralMode
    {
        rectangle, midpoint, trapezoid, simpsonQuad, simpsonCube
    }

    /**
     * Special characters which can have implied multiplication to their left.
     */
    public static char[] specialCharactersLeft = { '\u03A3', // Upper case sigma.
            '\u03C0', // Lower case pi.
            '\u03A0', // Upper case pi.
            '\u222B', // Integral symbol.
            '\u221A' // Square root symbol.
            };

    /**
     * Special characters which can have implied multiplication to their right.
     */
    public static char[] specialCharactersRight = { '\u03C0', // Lower case pi.
    };

    /**
     * Initializes the calculator to Mode.radians mode, with 1000 divisions for approximate integration.
     */
    public Calculator()
    {
        mode = Mode.radians;
        integralMode = IntegralMode.simpsonCube;
        divisions = 1000;
        answer = new BigDecimal("0");
        fanswer = " ";
        memory = new BigDecimal("0");
        fmemory = " ";
    }

    /**
     * Sets the calculator to newMode. Accepts Mode.radians and Mode.degrees.
     * 
     * @param newMode
     *            - the mode to set the calculator in.
     * @throws CalculatorError
     */
    public void setMode(Mode newMode) throws CalculatorError, ArithmeticException
    {
        if (newMode == Mode.radians || newMode == Mode.degrees)
        {
            mode = newMode;
        } else
        {
            throw new CalculatorError(ErrorType.invalidMode);
        }
    }

    /**
     * Returns the current mode.
     * 
     * @return mode.
     */
    public Mode getMode()
    {
        return mode;
    }

    /**
     * Sets the calculator's integration method to newMode.
     * 
     * @param newMode
     *            - the integration mode to use. Accepts IntegralMode.rectangle, IntegralMode.midpoint,
     *            IntegralMode.trapezoid, IntegralMode.simpsonQuad and IntegralMode.simpsonCube
     * @throws CalculatorError
     */
    public void setIntegralMode(IntegralMode newMode) throws CalculatorError, ArithmeticException
    {
        if (newMode == IntegralMode.rectangle || newMode == IntegralMode.midpoint || newMode == IntegralMode.trapezoid
                || newMode == IntegralMode.simpsonQuad || newMode == IntegralMode.simpsonCube)
        {
            integralMode = newMode;
        } else
        {
            throw new CalculatorError(ErrorType.invalidMode);
        }
    }

    /**
     * Returns the current integration method.
     * 
     * @return integralMode.
     */
    public IntegralMode getIntegralMode()
    {
        return integralMode;
    }

    /**
     * Sets the value of answer to value.
     * 
     * @param value
     *            - the value to store as answer.
     */
    public void setAnswer(BigDecimal value)
    {
        answer = value;
    }

    /**
     * Returns the current stored answer.
     * 
     * @return answer.
     */
    public BigDecimal getAnswer()
    {
        return answer;
    }

    /**
     * Sets the functional answer to value.
     * 
     * @param function
     *            - the functional answer to store as fanswer.
     */
    public void setFAnswer(String function)
    {
        fanswer = function;
    }

    /**
     * Returns the value of the current stored functional answer.
     * 
     * @return fanswer.
     */
    public String getFAnswer()
    {
        return fanswer;
    }

    /**
     * Sets the value of memory to value.
     * 
     * @param value
     *            - the value to store in memory.
     */
    public void setMemory(BigDecimal value)
    {
        memory = value;
    }

    /**
     * Returns memory.
     */
    /**
     * Returns the value of memory.
     * 
     * @return memory.
     */
    public BigDecimal getMemory()
    {
        return memory;
    }

    /**
     * Sets the value of the function memory to function.
     * 
     * @param function
     *            - the function to store in fmemory.
     */
    public void setFMemory(String function)
    {
        fmemory = function;
    }

    /**
     * Returns the value of the function memory.
     * 
     * @return fmemory
     */
    public String getFMemory()
    {
        return fmemory;
    }

    /**
     * Sets the divisions used for the integration method to newDivisions.
     * 
     * @param newDivisions
     *            - the number of divisions to use for approximate integration.
     */
    public void setDivisions(int newDivisions)
    {
        divisions = newDivisions;
    }

    /**
     * Returns the number of divisions used for the integration method.
     * 
     * @return divisions.
     */
    public int getDivisions()
    {
        return divisions;
    }

    /**
     * Returns the value of the expression stored in the string as a type BigDecimal. If an error occurred during
     * computation, the error is stored in the calculator's error variable and can be accessed using getError().
     * 
     * @param str
     *            - the string containing an expression to be evaluated.
     * 
     * @return the numerical value of the string's expression.
     * @throws CalculatorError
     */
    public BigDecimal compute(String str) throws CalculatorError, ArithmeticException
    {
        BigDecimal result = new BigDecimal("1");
        if (Utility.isAllBrackets(str))
        {
            throw new CalculatorError(ErrorType.missingArg);
        } else if (!Utility.checkBracketPairs(str))
        {
            throw new CalculatorError(ErrorType.bracketMismatch);
        } else if (checkSum(str))
        {
            result = computeSum(str);
        } else if (checkDiff(str))
        {
            result = computeDiff(str);
        } else if (checkProd(str))
        {
            result = computeProd(str);
        } else if (checkDiv(str))
        {
            result = computeDiv(str);
        } else if (checkPow(str))
        {
            result = computePow(str);
        } else if (checkMod(str))
        {
            result = computeMod(str);
        } else if (checkOther(str))
        {
            result = computeOther(str);
        } else
        {
            result = numeric(str);
        }
        return result;
    }

    /**
     * Replaces all occurrences of "fans" and "fmem" with the String values fanswer and fmemory. Also makes the String
     * lower case.
     * 
     * @param str
     *            - the String for which to replace "fans" and "fmem" with their stored values.
     * 
     * @return the String in lower case with "fans" and "fmem" replaced with their values.
     */
    public String fReplace(String str)
    {
        str = Utility.toLowerCase(str);
        for (int i = 0; i < str.length() - 3; i++)
        {
            if (str.charAt(i) == 'f')
            {
                if (str.substring(i + 1, i + 4).equals("ans"))
                {
                    StringBuilder temp = new StringBuilder(str);
                    temp.replace(i, i + 4, fanswer);
                    str = temp.toString();
                }
                if (str.substring(i + 1, i + 4).equals("mem"))
                {
                    StringBuilder temp = new StringBuilder(str);
                    temp.replace(i, i + 4, fmemory);
                    str = temp.toString();
                }
            }
        }
        return str;
    }

    /**
     * The mode to use for the computation of trig functions.
     */
    private Mode mode;

    /**
     * The mode to use for the computation of integrals.
     */
    private IntegralMode integralMode;

    /**
     * The number of divisions to be used for integration.
     */
    private int divisions;

    /**
     * The last answer found.
     */
    private BigDecimal answer;

    /**
     * The last functional answer found.
     */
    private String fanswer;

    /**
     * The value stored in memory.
     */
    private BigDecimal memory;

    /**
     * The functional value stored in memory.
     */
    private String fmemory;

    // Evaluation functions:

    /**
     * Checks if the String contains any '+' characters outside of brackets.
     * 
     * @param str
     *            - the String to be checked.
     * 
     * @return true if input contains any '+' characters outside of brackets, false otherwise.
     */
    private boolean checkSum(String str)
    {
        int bracketCount = 0;
        str = Utility.removeEndBrackets(str);
        if (Utility.isNumerical(str))
        {
            return false;
        }
        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) == '(')
            {
                bracketCount++;
            }
            if (str.charAt(i) == ')')
            {
                bracketCount--;
            }
            if (bracketCount == 0 && str.charAt(i) == '+')
            {
                if (!Utility.sumAndDiffCheckForExponentialNotation(str, i))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the value of the first sum outside of brackets in the String.
     * 
     * @param str
     *            - the String to be evaluated.
     * 
     * @return the value of the first sum outside of brackets in the String.
     * @throws CalculatorError
     */
    private BigDecimal computeSum(String str) throws CalculatorError, ArithmeticException
    {
        int bracketCount = 0;
        str = Utility.removeEndBrackets(str);
        BigDecimal sum = new BigDecimal("0");
        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) == '(')
            {
                bracketCount++;
            }
            if (str.charAt(i) == ')')
            {
                bracketCount--;
            }
            if (bracketCount == 0 && str.charAt(i) == '+')
            {
                if (!Utility.sumAndDiffCheckForExponentialNotation(str, i))
                {
                    if (i == 0 || i == str.length() - 1)
                    {
                        throw new CalculatorError(ErrorType.missingArg);
                    } else
                    {
                        sum = compute(str.substring(0, i)).add(compute(str.substring(i + 1, str.length())));
                    }
                    break;
                }
            }
        }
        return sum;
    }

    /**
     * Checks if the String contains any '-' characters outside of brackets.
     * 
     * @param str
     *            - the String to be checked.
     * 
     * @return true if input contains any '-' characters outside of brackets, false otherwise.
     */
    private boolean checkDiff(String str)
    {
        int bracketCount = 0;
        str = Utility.removeEndBrackets(str);
        if (Utility.isNumerical(str))
        {
            return false;
        }
        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) == '(')
            {
                bracketCount++;
            }
            if (str.charAt(i) == ')')
            {
                bracketCount--;
            }
            if (bracketCount == 0 && str.charAt(i) == '-')
            {
                if (!Utility.sumAndDiffCheckForExponentialNotation(str, i))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the value of the first difference outside of brackets in the String.
     * 
     * @param str
     *            - the String to be evaluated.
     * 
     * @return the value of the first difference outside of brackets in the String.
     * @throws CalculatorError
     */
    private BigDecimal computeDiff(String str) throws CalculatorError, ArithmeticException
    {
        int bracketCount = 0;
        str = Utility.removeEndBrackets(str);
        BigDecimal diff = new BigDecimal("0");
        for (int i = str.length() - 1; i >= 0; i--)
        {
            if (str.charAt(i) == ')')
            {
                bracketCount++;
            }
            if (str.charAt(i) == '(')
            {
                bracketCount--;
            }
            if (bracketCount == 0 && str.charAt(i) == '-')
            {
                if (!Utility.sumAndDiffCheckForExponentialNotation(str, i))
                {
                    if ((i == 0 && str.length() == 1) || i == str.length() - 1)
                    {
                        throw new CalculatorError(ErrorType.missingArg);
                    } else if (i == 0)
                    {
                        diff = compute(str.substring(1, str.length())).negate();
                    } else
                    {
                        diff = compute(str.substring(0, i)).subtract(compute(str.substring(i + 1, str.length())));
                    }
                    break;
                }
            }
        }
        return diff;
    }

    /**
     * Checks if the String contains any '*' characters outside of brackets.
     * 
     * @param str
     *            - the String to be checked.
     * 
     * @return true if input contains any '*' characters outside of brackets, false otherwise.
     */
    private boolean checkProd(String str)
    {
        int bracketCount = 0;
        str = Utility.removeEndBrackets(str);
        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) == '(')
            {
                bracketCount++;
            }
            if (str.charAt(i) == ')')
            {
                bracketCount--;
            }
            if (bracketCount == 0 && str.charAt(i) == '*')
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the value of the first product outside of brackets in the String.
     * 
     * @param str
     *            - the String to be evaluated.
     * 
     * @return the value of the first product outside of brackets in the String.
     * @throws CalculatorError
     */
    private BigDecimal computeProd(String str) throws CalculatorError, ArithmeticException
    {
        int bracketCount = 0;
        str = Utility.removeEndBrackets(str);
        BigDecimal prod = new BigDecimal("0");
        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) == '(')
            {
                bracketCount++;
            }
            if (str.charAt(i) == ')')
            {
                bracketCount--;
            }
            if (bracketCount == 0 && str.charAt(i) == '*')
            {
                if (i == 0 || i == str.length() - 1)
                {
                    throw new CalculatorError(ErrorType.missingArg);
                } else
                {
                    prod = compute(str.substring(0, i)).multiply(compute(str.substring(i + 1, str.length())), Math.mc);
                }
                break;
            }
        }
        return prod;
    }

    /**
     * Checks if the String contains any '/' characters outside of brackets.
     * 
     * @param str
     *            - the String to be checked.
     * 
     * @return true if input contains any '/' characters outside of brackets, false otherwise.
     */
    private boolean checkDiv(String str)
    {
        int bracketCount = 0;
        str = Utility.removeEndBrackets(str);
        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) == '(')
            {
                bracketCount++;
            }
            if (str.charAt(i) == ')')
            {
                bracketCount--;
            }
            if (bracketCount == 0 && str.charAt(i) == '/')
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the value of the last division outside of brackets in the String.
     * 
     * @param str
     *            - the String to be evaluated.
     * 
     * @return the value of the last division outside of brackets in the String.
     * @throws CalculatorError
     */
    private BigDecimal computeDiv(String str) throws CalculatorError, ArithmeticException
    {
        int bracketCount = 0;
        str = Utility.removeEndBrackets(str);
        BigDecimal div = new BigDecimal("1");
        for (int i = str.length() - 1; i >= 0; i--)
        {
            if (str.charAt(i) == ')')
            {
                bracketCount++;
            }
            if (str.charAt(i) == '(')
            {
                bracketCount--;
            }
            if (bracketCount == 0 && str.charAt(i) == '/')
            {
                if (i == 0 || i == str.length() - 1)
                {
                    throw new CalculatorError(ErrorType.missingArg);
                } else
                {
                    BigDecimal denom = compute(str.substring(i + 1, str.length()));
                    if (denom.signum() == 0)
                    {
                        throw new CalculatorError(ErrorType.divisionByZero);
                    } else
                    {
                        div = compute(str.substring(0, i)).divide(denom, Math.mc);
                    }
                }
                break;
            }
        }
        return div;
    }

    /**
     * Checks if the String contains any '^' characters outside of brackets.
     * 
     * @param str
     *            - the String to be checked.
     * 
     * @return true if input contains any '^' characters outside of brackets, false otherwise.
     */
    private boolean checkPow(String str)
    {
        int bracketCount = 0;
        str = Utility.removeEndBrackets(str);
        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) == '(')
            {
                bracketCount++;
            }
            if (str.charAt(i) == ')')
            {
                bracketCount--;
            }
            if (bracketCount == 0 && str.charAt(i) == '^')
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the value of the last power outside of brackets in the String.
     * 
     * @param str
     *            - the String to be evaluated.
     * 
     * @return the value of the last power outside of brackets in the String.
     * @throws CalculatorError
     */
    private BigDecimal computePow(String str) throws CalculatorError, ArithmeticException
    {
        int bracketCount = 0;
        str = Utility.removeEndBrackets(str);
        BigDecimal power = new BigDecimal("0");
        for (int i = str.length() - 1; i >= 0; i--)
        {
            if (str.charAt(i) == ')')
            {
                bracketCount++;
            }
            if (str.charAt(i) == '(')
            {
                bracketCount--;
            }
            if (bracketCount == 0 && str.charAt(i) == '^')
            {
                if (i == 0 || i == str.length() - 1)
                {
                    throw new CalculatorError(ErrorType.missingArg);
                } else
                {
                    BigDecimal argument = compute(str.substring(0, i));
                    BigDecimal exponent = compute(str.substring(i + 1, str.length()));
                    if (argument.signum() == 0 && exponent.signum() == -1)
                    {
                        throw new CalculatorError(ErrorType.divisionByZero);
                    } else if ((exponent.subtract(new BigDecimal(exponent.round(Math.mcIntRound).toBigInteger())))
                            .signum() != 0 && argument.signum() == -1)
                    {
                        throw new CalculatorError(ErrorType.nonIntegralPowerNegativeArg);
                    } else
                    {
                        try
                        {
                            int exponentInt = exponent.toBigIntegerExact().intValueExact();
                            power = argument.pow(exponentInt, Math.mc);
                        } catch (ArithmeticException e)
                        {
                            power = Math.pow(argument, exponent);
                        }
                    }
                }
                break;
            }
        }
        return power;
    }

    /**
     * Checks if the String contains any '%' characters outside of brackets.
     * 
     * @param str
     *            - the String to be checked.
     * 
     * @return true if input contains any '%' characters outside of brackets, false otherwise.
     */
    private boolean checkMod(String str)
    {
        int bracketCount = 0;
        str = Utility.removeEndBrackets(str);
        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) == '(')
            {
                bracketCount++;
            }
            if (str.charAt(i) == ')')
            {
                bracketCount--;
            }
            if (bracketCount == 0 && str.charAt(i) == '%')
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the value of the last modulo operation outside of brackets in the String.
     * 
     * @param str
     *            - the String to be evaluated.
     * 
     * @return the value of the last modulo operation outside of brackets in the String.
     * @throws CalculatorError
     */
    private BigDecimal computeMod(String str) throws CalculatorError, ArithmeticException
    {
        int bracketCount = 0;
        str = Utility.removeEndBrackets(str);
        BigDecimal mod = new BigDecimal("1");
        for (int i = str.length() - 1; i >= 0; i--)
        {
            if (str.charAt(i) == ')')
            {
                bracketCount++;
            }
            if (str.charAt(i) == '(')
            {
                bracketCount--;
            }
            if (bracketCount == 0 && str.charAt(i) == '%')
            {
                if (i == 0 || i == str.length() - 1)
                {
                    throw new CalculatorError(ErrorType.missingArg);
                } else
                {
                    BigDecimal divisor = compute(str.substring(i + 1, str.length()));
                    if (divisor.signum() == 0)
                    {
                        throw new CalculatorError(ErrorType.divisionByZero);
                    } else
                    {
                        mod = compute(str.substring(0, i)).remainder(divisor, Math.mc);
                    }
                }
                break;
            }
        }
        return mod;
    }

    /**
     * Checks if the String contains any predefined functions or operations outside of brackets.
     * 
     * @param str
     *            - the String to be checked.
     * 
     * @return true if input contains any predefined functions or operations outside of brackets, false otherwise.
     */
    private boolean checkOther(String str)
    {
        str = Utility.toLowerCase(Utility.removeEndBrackets(str));
        if (str.length() > 3 && str.substring(0, 4).equals("sin("))
        {
            return true;
        } else if (str.length() > 3 && str.substring(0, 4).equals("cos("))
        {
            return true;
        } else if (str.length() > 3 && str.substring(0, 4).equals("tan("))
        {
            return true;
        } else if (str.length() > 3 && str.substring(0, 4).equals("sec("))
        {
            return true;
        } else if (str.length() > 3 && str.substring(0, 4).equals("csc("))
        {
            return true;
        } else if (str.length() > 3 && str.substring(0, 4).equals("cot("))
        {
            return true;
        } else if (str.length() > 3 && str.substring(0, 4).equals("exp("))
        {
            return true;
        } else if (str.length() > 2 && str.substring(0, 3).equals("ln("))
        {
            return true;
        } else if (str.length() > 3 && str.substring(0, 4).equals("abs("))
        {
            return true;
        } else if (str.length() > 6 && str.substring(0, 7).equals("arcsin("))
        {
            return true;
        } else if (str.length() > 6 && str.substring(0, 7).equals("arccos("))
        {
            return true;
        } else if (str.length() > 6 && str.substring(0, 7).equals("arctan("))
        {
            return true;
        } else if (str.length() > 6 && str.substring(0, 7).equals("arcsec("))
        {
            return true;
        } else if (str.length() > 6 && str.substring(0, 7).equals("arccsc("))
        {
            return true;
        } else if (str.length() > 6 && str.substring(0, 7).equals("arccot("))
        {
            return true;
        } else if (str.length() > 4 && str.substring(0, 5).equals("sinh("))
        {
            return true;
        } else if (str.length() > 4 && str.substring(0, 5).equals("cosh("))
        {
            return true;
        } else if (str.length() > 4 && str.substring(0, 5).equals("tanh("))
        {
            return true;
        } else if (str.length() > 4 && str.substring(0, 5).equals("sech("))
        {
            return true;
        } else if (str.length() > 4 && str.substring(0, 5).equals("csch("))
        {
            return true;
        } else if (str.length() > 4 && str.substring(0, 5).equals("coth("))
        {
            return true;
        } else if (str.length() > 7 && str.substring(0, 8).equals("arcsinh("))
        {
            return true;
        } else if (str.length() > 7 && str.substring(0, 8).equals("arccosh("))
        {
            return true;
        } else if (str.length() > 7 && str.substring(0, 8).equals("arctanh("))
        {
            return true;
        } else if (str.length() > 7 && str.substring(0, 8).equals("arcsech("))
        {
            return true;
        } else if (str.length() > 7 && str.substring(0, 8).equals("arccsch("))
        {
            return true;
        } else if (str.length() > 7 && str.substring(0, 8).equals("arccoth("))
        {
            return true;
        } else if (str.length() > 1 && str.substring(0, 2).equals("\u221A("))
        {
            return true;
        } else if (str.length() > 4 && str.substring(0, 5).equals("sqrt("))
        {
            return true;
        } else if (str.length() > 1 && str.substring(0, 2).equals("\u222B("))
        {
            return true;
        } else if (str.length() > 8 && str.substring(0, 9).equals("integral("))
        {
            return true;
        } else if (str.length() > 1 && str.substring(0, 2).equals("\u03A3("))
        {
            return true;
        } else if (str.length() > 3 && str.substring(0, 4).equals("sum("))
        {
            return true;
        } else if (str.length() > 1 && str.substring(0, 2).equals("\u03A0("))
        {
            return true;
        } else if (str.length() > 7 && str.substring(0, 8).equals("product("))
        {
            return true;
        } else if (str.length() > 4 && str.substring(0, 5).equals("rand("))
        {
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * Returns the value of the first predefined function or operation outside of brackets in the String.
     * 
     * @param str
     *            - the String to be evaluated.
     * 
     * @return the value of the first predefined function or operation outside of brackets in the String.
     * @throws CalculatorError
     */
    private BigDecimal computeOther(String str) throws CalculatorError, ArithmeticException
    {
        BigDecimal result = new BigDecimal("0");
        str = Utility.removeEndBrackets(str);

        if (str.length() > 3 && str.substring(0, 4).equals("sin("))
        {
            if (str.length() < 6)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                if (mode == Mode.radians)
                {
                    result = Math.sin(compute(str.substring(3, str.length())));
                } else
                {
                    result = Math.sinDeg(compute(str.substring(3, str.length())));
                }
            }
        } else if (str.length() > 3 && str.substring(0, 4).equals("cos("))
        {
            if (str.length() < 6)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                if (mode == Mode.radians)
                {
                    result = Math.cos(compute(str.substring(3, str.length())));
                } else
                {
                    result = Math.cosDeg(compute(str.substring(3, str.length())));
                }
            }
        } else if (str.length() > 3 && str.substring(0, 4).equals("tan("))
        {
            if (str.length() < 6)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(3, str.length()));
                BigDecimal tempCos;
                if (mode == Mode.radians)
                {
                    tempCos = Math.cos(temp);
                } else
                {
                    tempCos = Math.cosDeg(temp);
                }
                if (tempCos.signum() == 0)
                {
                    throw new CalculatorError(ErrorType.tanUndef);
                } else
                {
                    if (mode == Mode.radians)
                    {
                        result = Math.sin(temp).divide(tempCos, Math.mc);
                    } else
                    {
                        result = Math.sinDeg(temp).divide(tempCos, Math.mc);
                    }
                }
            }
        } else if (str.length() > 3 && str.substring(0, 4).equals("sec("))
        {
            if (str.length() < 6)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(3, str.length()));
                BigDecimal tempCos;
                if (mode == Mode.radians)
                {
                    tempCos = Math.cos(temp);
                } else
                {
                    tempCos = Math.cosDeg(temp);
                }
                if (tempCos.signum() == 0)
                {
                    throw new CalculatorError(ErrorType.secUndef);
                } else
                {
                    result = (new BigDecimal("1")).divide(tempCos, Math.mc);
                }
            }
        } else if (str.length() > 3 && str.substring(0, 4).equals("csc("))
        {
            if (str.length() < 6)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(3, str.length()));
                BigDecimal tempSin;
                if (mode == Mode.radians)
                {
                    tempSin = Math.sin(temp);
                } else
                {
                    tempSin = Math.sinDeg(temp);
                }
                if (tempSin.signum() == 0)
                {
                    throw new CalculatorError(ErrorType.cscUndef);
                } else
                {
                    result = (new BigDecimal("1")).divide(tempSin, Math.mc);
                }
            }
        } else if (str.length() > 3 && str.substring(0, 4).equals("cot("))
        {
            if (str.length() < 6)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(3, str.length()));
                BigDecimal tempSin;
                if (mode == Mode.radians)
                {
                    tempSin = Math.sin(temp);
                } else
                {
                    tempSin = Math.sinDeg(temp);
                }
                if (tempSin.signum() == 0)
                {
                    throw new CalculatorError(ErrorType.cotUndef);
                } else
                {
                    if (mode == Mode.radians)
                    {
                        result = Math.cos(temp).divide(tempSin, Math.mc);
                    } else
                    {
                        result = Math.cosDeg(temp).divide(tempSin, Math.mc);
                    }
                }
            }
        } else if (str.length() > 3 && str.substring(0, 4).equals("exp("))
        {
            if (str.length() < 6)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                result = Math.exp(compute(str.substring(3, str.length())));
            }
        } else if (str.length() > 2 && str.substring(0, 3).equals("ln("))
        {
            if (str.length() < 5)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(2, str.length()));
                if (temp.signum() != 1)
                {
                    throw new CalculatorError(ErrorType.lnUndef);
                } else
                {
                    result = Math.ln(temp);
                }
            }
        } else if (str.length() > 3 && str.substring(0, 4).equals("abs("))
        {
            if (str.length() < 6)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                result = Math.abs(compute(str.substring(3, str.length())));
            }
        } else if (str.length() > 6 && str.substring(0, 7).equals("arcsin("))
        {
            if (str.length() < 9)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(6, str.length()));
                if (temp.compareTo(new BigDecimal("1")) > 0 || temp.compareTo(new BigDecimal("-1")) < 0)
                {
                    throw new CalculatorError(ErrorType.arcsinUndef);
                } else if (mode == Mode.radians)
                {
                    result = Math.arcsin(temp);
                } else
                {
                    result = Math.arcsinDeg(temp);
                }
            }
        } else if (str.length() > 6 && str.substring(0, 7).equals("arccos("))
        {
            if (str.length() < 9)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(6, str.length()));
                if (temp.compareTo(new BigDecimal("1")) > 0 || temp.compareTo(new BigDecimal("-1")) < 0)
                {
                    throw new CalculatorError(ErrorType.arccosUndef);
                } else if (mode == Mode.radians)
                {
                    result = Math.arccos(temp);
                } else
                {
                    result = Math.arccosDeg(temp);
                }
            }
        } else if (str.length() > 6 && str.substring(0, 7).equals("arctan("))
        {
            if (str.length() < 9)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(6, str.length()));
                if (mode == Mode.radians)
                {
                    result = Math.arctan(temp);
                } else
                {
                    result = Math.arctanDeg(temp);
                }
            }
        } else if (str.length() > 6 && str.substring(0, 7).equals("arcsec("))
        {
            if (str.length() < 9)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(6, str.length()));
                if (temp.compareTo(new BigDecimal("1")) < 0 && temp.compareTo(new BigDecimal("-1")) > 0)
                {
                    throw new CalculatorError(ErrorType.arcsecUndef);
                } else if (mode == Mode.radians)
                {
                    result = Math.arcsec(temp);
                } else
                {
                    result = Math.arcsecDeg(temp);
                }
            }
        } else if (str.length() > 6 && str.substring(0, 7).equals("arccsc("))
        {
            if (str.length() < 9)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(6, str.length()));
                if (temp.compareTo(new BigDecimal("1")) < 0 && temp.compareTo(new BigDecimal("-1")) > 0)
                {
                    throw new CalculatorError(ErrorType.arccscUndef);
                } else if (mode == Mode.radians)
                {
                    result = Math.arccsc(temp);
                } else
                {
                    result = Math.arccscDeg(temp);
                }
            }
        } else if (str.length() > 6 && str.substring(0, 7).equals("arccot("))
        {
            if (str.length() < 9)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(6, str.length()));
                if (mode == Mode.radians)
                {
                    result = Math.arccot(temp);
                } else
                {
                    result = Math.arccotDeg(temp);
                }
            }
        } else if (str.length() > 4 && str.substring(0, 5).equals("sinh("))
        {
            if (str.length() < 7)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(4, str.length()));
                result = Math.sinh(temp);
            }
        } else if (str.length() > 4 && str.substring(0, 5).equals("cosh("))
        {
            if (str.length() < 7)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(4, str.length()));
                result = Math.cosh(temp);
            }
        } else if (str.length() > 4 && str.substring(0, 5).equals("tanh("))
        {
            if (str.length() < 7)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(4, str.length()));
                result = Math.tanh(temp);
            }
        } else if (str.length() > 4 && str.substring(0, 5).equals("sech("))
        {
            if (str.length() < 7)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(4, str.length()));
                result = Math.sech(temp);
            }
        } else if (str.length() > 4 && str.substring(0, 5).equals("csch("))
        {
            if (str.length() < 7)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(4, str.length()));
                if (temp.signum() == 0)
                {
                    throw new CalculatorError(ErrorType.cschUndef);
                } else
                {
                    result = Math.csch(temp);
                }
            }
        } else if (str.length() > 4 && str.substring(0, 5).equals("coth("))
        {
            if (str.length() < 7)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(4, str.length()));
                if (temp.signum() == 0)
                {
                    throw new CalculatorError(ErrorType.cothUndef);
                } else
                {
                    result = Math.coth(temp);
                }
            }
        } else if (str.length() > 7 && str.substring(0, 8).equals("arcsinh("))
        {
            if (str.length() < 10)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(7, str.length()));
                result = Math.arcsinh(temp);
            }
        } else if (str.length() > 7 && str.substring(0, 8).equals("arccosh("))
        {
            if (str.length() < 10)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(7, str.length()));
                if (temp.compareTo(new BigDecimal("1")) < 0)
                {
                    throw new CalculatorError(ErrorType.arccoshUndef);
                } else
                {
                    result = Math.arccosh(temp);
                }
            }
        } else if (str.length() > 7 && str.substring(0, 8).equals("arctanh("))
        {
            if (str.length() < 10)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(7, str.length()));
                if (temp.compareTo(new BigDecimal("1")) >= 0 || temp.compareTo(new BigDecimal("-1")) <= 0)
                {
                    throw new CalculatorError(ErrorType.arctanhUndef);
                } else
                {
                    result = Math.arctanh(temp);
                }
            }
        } else if (str.length() > 7 && str.substring(0, 8).equals("arcsech("))
        {
            if (str.length() < 10)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(7, str.length()));
                if (temp.compareTo(new BigDecimal("1")) > 0 || temp.compareTo(new BigDecimal("0")) <= 0)
                {
                    throw new CalculatorError(ErrorType.arcsechUndef);
                } else
                {
                    result = Math.arcsech(temp);
                }
            }
        } else if (str.length() > 7 && str.substring(0, 8).equals("arccsch("))
        {
            if (str.length() < 10)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(7, str.length()));
                if (temp.signum() == 0)
                {
                    throw new CalculatorError(ErrorType.arccschUndef);
                } else
                {
                    result = Math.arccsch(temp);
                }
            }
        } else if (str.length() > 7 && str.substring(0, 8).equals("arccoth("))
        {
            if (str.length() < 10)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(7, str.length()));
                if (temp.compareTo(new BigDecimal("1")) <= 0 && temp.compareTo(new BigDecimal("-1")) >= 0)
                {
                    throw new CalculatorError(ErrorType.arccothUndef);
                } else
                {
                    result = Math.arccoth(temp);
                }
            }
        } else if (str.length() > 1 && str.substring(0, 2).equals("\u221A("))
        {
            if (str.length() < 4)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(1, str.length()));
                if (temp.signum() == -1)
                {
                    throw new CalculatorError(ErrorType.nonIntegralPowerNegativeArg);
                } else
                {
                    result = Math.sqrt(temp);
                }
            }
        } else if (str.length() > 4 && str.substring(0, 5).equals("sqrt("))
        {
            if (str.length() < 7)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                BigDecimal temp = compute(str.substring(4, str.length()));
                if (temp.signum() == -1)
                {
                    throw new CalculatorError(ErrorType.nonIntegralPowerNegativeArg);
                } else
                {
                    result = Math.sqrt(temp);
                }
            }
        } else if (str.length() > 1 && str.substring(0, 2).equals("\u222B("))
        {
            if (str.length() < 8)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                int commaOne = 0, commaTwo = 0;
                int BracketCount = 0;
                for (int i = 2; i < str.length(); i++)
                {
                    if (str.charAt(i) == '(')
                    {
                        BracketCount++;
                    } else if (str.charAt(i) == ')')
                    {
                        BracketCount--;
                    } else if (str.charAt(i) == ',' && BracketCount == 0)
                    {
                        if (commaOne == 0)
                        {
                            commaOne = i;
                        } else if (commaTwo == 0)
                        {
                            commaTwo = i;
                        } else
                        {
                            throw new CalculatorError(ErrorType.invalidIntegralParameters);
                        }
                    }
                }
                if (commaOne == 0 || commaTwo == 0)
                {
                    throw new CalculatorError(ErrorType.invalidIntegralParameters);
                }

                String function = "(" + str.substring(2, commaOne) + ")";
                BigDecimal lowerLimit = compute(str.substring(commaOne + 1, commaTwo));
                BigDecimal upperLimit = compute(str.substring(commaTwo + 1, str.length() - 1));

                result = integral(function, lowerLimit, upperLimit);
            }
        } else if (str.length() > 8 && str.substring(0, 9).equals("integral("))
        {
            if (str.length() < 15)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                int commaOne = 0, commaTwo = 0;
                int BracketCount = 0;
                for (int i = 9; i < str.length(); i++)
                {
                    if (str.charAt(i) == '(')
                    {
                        BracketCount++;
                    } else if (str.charAt(i) == ')')
                    {
                        BracketCount--;
                    } else if (str.charAt(i) == ',' && BracketCount == 0)
                    {
                        if (commaOne == 0)
                        {
                            commaOne = i;
                        } else if (commaTwo == 0)
                        {
                            commaTwo = i;
                        } else
                        {
                            throw new CalculatorError(ErrorType.invalidIntegralParameters);
                        }
                    }
                }
                if (commaOne == 0 || commaTwo == 0)
                {
                    throw new CalculatorError(ErrorType.invalidIntegralParameters);
                }

                String function = "(" + str.substring(9, commaOne) + ")";
                BigDecimal lowerLimit = compute(str.substring(commaOne + 1, commaTwo));
                BigDecimal upperLimit = compute(str.substring(commaTwo + 1, str.length() - 1));

                result = integral(function, lowerLimit, upperLimit);
            }
        } else if (str.length() > 1 && str.substring(0, 2).equals("\u03A3("))
        {
            if (str.length() < 8)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                int commaOne = 0, commaTwo = 0;
                int BracketCount = 0;
                for (int i = 2; i < str.length(); i++)
                {
                    if (str.charAt(i) == '(')
                    {
                        BracketCount++;
                    } else if (str.charAt(i) == ')')
                    {
                        BracketCount--;
                    } else if (str.charAt(i) == ',' && BracketCount == 0)
                    {
                        if (commaOne == 0)
                        {
                            commaOne = i;
                        } else if (commaTwo == 0)
                        {
                            commaTwo = i;
                        } else
                        {
                            throw new CalculatorError(ErrorType.invalidSumParameters);
                        }
                    }
                }
                if (commaOne == 0 || commaTwo == 0)
                {
                    throw new CalculatorError(ErrorType.invalidIntegralParameters);
                }

                String function = "(" + str.substring(2, commaOne) + ")";
                BigDecimal lowerLimit = compute(str.substring(commaOne + 1, commaTwo));
                BigDecimal upperLimit = compute(str.substring(commaTwo + 1, str.length() - 1));
                try
                {
                    lowerLimit.round(Math.mcIntRound).toBigIntegerExact();
                    upperLimit.round(Math.mcIntRound).toBigIntegerExact();
                } catch (ArithmeticException e)
                {
                    throw new CalculatorError(ErrorType.invalidSumParameters);
                }
                if (lowerLimit.compareTo(upperLimit) > 0)
                {
                    throw new CalculatorError(ErrorType.invalidSumParameters);
                }
                result = sum(function, lowerLimit, upperLimit);
            }
        } else if (str.length() > 3 && str.substring(0, 4).equals("sum("))
        {
            if (str.length() < 10)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                int commaOne = 0, commaTwo = 0;
                int BracketCount = 0;
                for (int i = 4; i < str.length(); i++)
                {
                    if (str.charAt(i) == '(')
                    {
                        BracketCount++;
                    } else if (str.charAt(i) == ')')
                    {
                        BracketCount--;
                    } else if (str.charAt(i) == ',' && BracketCount == 0)
                    {
                        if (commaOne == 0)
                        {
                            commaOne = i;
                        } else if (commaTwo == 0)
                        {
                            commaTwo = i;
                        } else
                        {
                            throw new CalculatorError(ErrorType.invalidSumParameters);
                        }
                    }
                }
                if (commaOne == 0 || commaTwo == 0)
                {
                    throw new CalculatorError(ErrorType.invalidIntegralParameters);
                }

                String function = "(" + str.substring(4, commaOne) + ")";
                BigDecimal lowerLimit = compute(str.substring(commaOne + 1, commaTwo));
                BigDecimal upperLimit = compute(str.substring(commaTwo + 1, str.length() - 1));
                try
                {
                    lowerLimit.round(Math.mcIntRound).toBigIntegerExact();
                    upperLimit.round(Math.mcIntRound).toBigIntegerExact();
                } catch (ArithmeticException e)
                {
                    throw new CalculatorError(ErrorType.invalidSumParameters);
                }
                if (lowerLimit.compareTo(upperLimit) > 0)
                {
                    throw new CalculatorError(ErrorType.invalidSumParameters);
                }
                result = sum(function, lowerLimit, upperLimit);
            }
        } else if (str.length() > 1 && str.substring(0, 2).equals("\u03A0("))
        {
            if (str.length() < 8)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                int commaOne = 0, commaTwo = 0;
                int BracketCount = 0;
                for (int i = 2; i < str.length(); i++)
                {
                    if (str.charAt(i) == '(')
                    {
                        BracketCount++;
                    } else if (str.charAt(i) == ')')
                    {
                        BracketCount--;
                    } else if (str.charAt(i) == ',' && BracketCount == 0)
                    {
                        if (commaOne == 0)
                        {
                            commaOne = i;
                        } else if (commaTwo == 0)
                        {
                            commaTwo = i;
                        } else
                        {
                            throw new CalculatorError(ErrorType.invalidProductParameters);
                        }
                    }
                }
                if (commaOne == 0 || commaTwo == 0)
                {
                    throw new CalculatorError(ErrorType.invalidIntegralParameters);
                }

                String function = "(" + str.substring(2, commaOne) + ")";
                BigDecimal lowerLimit = compute(str.substring(commaOne + 1, commaTwo));
                BigDecimal upperLimit = compute(str.substring(commaTwo + 1, str.length() - 1));
                try
                {
                    lowerLimit.round(Math.mcIntRound).toBigIntegerExact();
                    upperLimit.round(Math.mcIntRound).toBigIntegerExact();
                } catch (ArithmeticException e)
                {
                    throw new CalculatorError(ErrorType.invalidProductParameters);
                }
                if (lowerLimit.compareTo(upperLimit) > 0)
                {
                    throw new CalculatorError(ErrorType.invalidProductParameters);
                }
                result = product(function, lowerLimit, upperLimit);
            }
        } else if (str.length() > 7 && str.substring(0, 8).equals("product("))
        {
            if (str.length() < 14)
            {
                throw new CalculatorError(ErrorType.missingArg);
            } else
            {
                int commaOne = 0, commaTwo = 0;
                int BracketCount = 0;
                for (int i = 8; i < str.length(); i++)
                {
                    if (str.charAt(i) == '(')
                    {
                        BracketCount++;
                    } else if (str.charAt(i) == ')')
                    {
                        BracketCount--;
                    } else if (str.charAt(i) == ',' && BracketCount == 0)
                    {
                        if (commaOne == 0)
                        {
                            commaOne = i;
                        } else if (commaTwo == 0)
                        {
                            commaTwo = i;
                        } else
                        {
                            throw new CalculatorError(ErrorType.invalidProductParameters);
                        }
                    }
                }
                if (commaOne == 0 || commaTwo == 0)
                {
                    throw new CalculatorError(ErrorType.invalidIntegralParameters);
                }

                String function = "(" + str.substring(8, commaOne) + ")";
                BigDecimal lowerLimit = compute(str.substring(commaOne + 1, commaTwo));
                BigDecimal upperLimit = compute(str.substring(commaTwo + 1, str.length() - 1));
                try
                {
                    lowerLimit.round(Math.mcIntRound).toBigIntegerExact();
                    upperLimit.round(Math.mcIntRound).toBigIntegerExact();
                } catch (ArithmeticException e)
                {
                    throw new CalculatorError(ErrorType.invalidProductParameters);
                }
                if (lowerLimit.compareTo(upperLimit) > 0)
                {
                    throw new CalculatorError(ErrorType.invalidProductParameters);
                }
                result = product(function, lowerLimit, upperLimit);
            }
        } else if (str.length() > 4 && str.substring(0, 5).equals("rand("))
        {
            if (str.length() < 6)
            {
                throw new CalculatorError(ErrorType.invalidRandParameters);
            } else
            {
                Random rand = new Random();
                if (str.length() == 6)
                {
                    if (str.charAt(5) == ')')
                    {
                        result = new BigDecimal(rand.nextDouble(), Math.mc);
                    } else
                    {
                        throw new CalculatorError(ErrorType.invalidRandParameters);
                    }
                } else
                {
                    BigDecimal temp = compute(str.substring(4, str.length()));
                    BigInteger tempInt = new BigInteger("0");
                    try
                    {
                        tempInt = temp.round(Math.mcIntRound).toBigIntegerExact();
                    } catch (ArithmeticException e)
                    {
                        throw new CalculatorError(ErrorType.invalidRandParameters);
                    }
                    if (tempInt.signum() != 1)
                    {
                        throw new CalculatorError(ErrorType.invalidRandParameters);
                    } else
                    {
                        try
                        {
                            result = new BigDecimal(rand.nextInt(tempInt.intValueExact()));
                        } catch (ArithmeticException e)
                        {
                            throw new CalculatorError(ErrorType.randArgumentTooBig);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns the value of the numeric interpretation of the String.
     * 
     * @param str
     *            - the String to be evaluated.
     * 
     * @return the value of the numeric interpretation of the String.
     * @throws CalculatorError
     */
    private BigDecimal numeric(String str) throws CalculatorError, ArithmeticException
    {
        str = Utility.toLowerCase(Utility.removeEndBrackets(str));
        if (str.equals("ans"))
        {
            return answer;
        } else if (str.equals("mem"))
        {
            return memory;
        } else if (str.equals("e"))
        {
            return Math.E;
        } else if (str.equals("pi") || str.equals("\u03C0"))
        {
            return Math.PI;
        } else if (Utility.isNumerical(str))
        {
            return new BigDecimal(str);
        } else
        {
            throw new CalculatorError(ErrorType.functionUndef);
        }
    }

    /**
     * Returns the numerical value of the integral of the function from lowerLimit to upperLimit computed according to
     * the calculator's integralMode.
     * 
     * @param function
     *            - the function to be integrated.
     * @param lowerLimit
     *            - the lower limit of integration.
     * @param upperLimit
     *            - the upper limit of integration.
     * 
     * @return the numerical value of the integral.
     * @throws CalculatorError
     */
    private BigDecimal integral(String function, BigDecimal lowerLimit, BigDecimal upperLimit) throws CalculatorError,
            ArithmeticException
    {
        if (integralMode == IntegralMode.rectangle)
        {
            function = fReplace(function);
            BigDecimal sum = new BigDecimal("0");
            BigDecimal dx = (upperLimit.subtract(lowerLimit)).divide(new BigDecimal(divisions), Math.mc);
            BigDecimal x = lowerLimit.add(dx);
            String value, tempFunction;
            for (int i = 1; i <= divisions; i++)
            {
                value = x.toString();
                tempFunction = function.replace("x", "(" + value + ")");
                sum = sum.add(compute(tempFunction));
                x = x.add(dx);
            }
            return sum.multiply(dx, Math.mc);
        } else if (integralMode == IntegralMode.midpoint)
        {
            function = fReplace(function);
            BigDecimal sum = new BigDecimal("0");
            BigDecimal dx = (upperLimit.subtract(lowerLimit)).divide(new BigDecimal(divisions), Math.mc);
            BigDecimal x = lowerLimit.add(dx.divide(new BigDecimal("2"), Math.mc));
            String value, tempFunction;
            for (int i = 1; i <= divisions; i++)
            {
                value = x.toString();
                tempFunction = function.replace("x", "(" + value + ")");
                sum = sum.add(compute(tempFunction));
                x = x.add(dx);
            }
            return sum.multiply(dx, Math.mc);
        } else if (integralMode == IntegralMode.trapezoid)
        {
            function = fReplace(function);
            BigDecimal sum = new BigDecimal("0");
            BigDecimal dx = (upperLimit.subtract(lowerLimit)).divide(new BigDecimal(divisions), Math.mc);
            BigDecimal x = lowerLimit;
            String value, tempFunction;
            for (int i = 0; i <= divisions; i++)
            {
                if (i == 0 || i == divisions)
                {
                    value = x.toString();
                    tempFunction = function.replace("x", "(" + value + ")");
                    sum = sum.add(compute(tempFunction));
                } else
                {
                    value = x.toString();
                    tempFunction = function.replace("x", "(" + value + ")");
                    sum = sum.add(compute(tempFunction).multiply(new BigDecimal("2"), Math.mc));
                }
                x = x.add(dx);
            }
            return sum.multiply(dx.divide(new BigDecimal("2"), Math.mc), Math.mc);
        } else if (integralMode == IntegralMode.simpsonQuad)
        {
            function = fReplace(function);
            BigDecimal sum = new BigDecimal("0");
            int totalDivisions = 2 * divisions;
            BigDecimal dx = (upperLimit.subtract(lowerLimit)).divide(new BigDecimal(totalDivisions), Math.mc);
            BigDecimal x = lowerLimit;
            String value, tempFunction;
            for (int i = 0; i <= totalDivisions; i++)
            {
                if (i == 0 || i == totalDivisions)
                {
                    value = x.toString();
                    tempFunction = function.replace("x", "(" + value + ")");
                    sum = sum.add(compute(tempFunction));
                } else if (i % 2 == 0)
                {
                    value = x.toString();
                    tempFunction = function.replace("x", "(" + value + ")");
                    sum = sum.add(compute(tempFunction).multiply(new BigDecimal("2"), Math.mc));
                } else
                {
                    value = x.toString();
                    tempFunction = function.replace("x", "(" + value + ")");
                    sum = sum.add(compute(tempFunction).multiply(new BigDecimal("4"), Math.mc));
                }
                x = x.add(dx);
            }
            return sum.multiply(dx.divide(new BigDecimal("3"), Math.mc), Math.mc);
        } else if (integralMode == IntegralMode.simpsonCube)
        {
            function = fReplace(function);
            BigDecimal sum = new BigDecimal("0");
            int totalDivisions = 3 * divisions;
            BigDecimal dx = (upperLimit.subtract(lowerLimit)).divide(new BigDecimal(totalDivisions), Math.mc);
            BigDecimal x = lowerLimit;
            String value, tempFunction;
            for (int i = 0; i <= totalDivisions; i++)
            {
                if (i == 0 || i == totalDivisions)
                {
                    value = x.toString();
                    tempFunction = function.replace("x", "(" + value + ")");
                    sum = sum.add(compute(tempFunction));
                } else if (i % 3 == 0)
                {
                    value = x.toString();
                    tempFunction = function.replace("x", "(" + value + ")");
                    sum = sum.add(compute(tempFunction).multiply(new BigDecimal("2"), Math.mc));
                } else
                {
                    value = x.toString();
                    tempFunction = function.replace("x", "(" + value + ")");
                    sum = sum.add(compute(tempFunction).multiply(new BigDecimal("3"), Math.mc));
                }
                x = x.add(dx);
            }
            return sum
                    .multiply(dx.multiply(new BigDecimal("3"), Math.mc).divide(new BigDecimal("8"), Math.mc), Math.mc);
        }
        return new BigDecimal("0");
    }

    /**
     * Returns the numerical value of the sum of the function from lowerLimit to upperLimit, where both of these limits
     * are integers.
     * 
     * @param function
     *            - the function to be summed.
     * @param lowerLimit
     *            - the lower limit of summation.
     * @param upperLimit
     *            - the upper limit of summation.
     * 
     * @return the numerical value of the sum.
     * @throws CalculatorError
     */
    private BigDecimal sum(String function, BigDecimal lowerLimit, BigDecimal upperLimit) throws CalculatorError,
            ArithmeticException
    {
        function = fReplace(function);
        BigDecimal sum = new BigDecimal("0");
        BigDecimal x = lowerLimit;
        BigDecimal dx = new BigDecimal("1");
        String value, tempFunction;
        while (x.compareTo(upperLimit) <= 0)
        {
            value = x.toString();
            tempFunction = function.replace("x", "(" + value + ")");
            sum = sum.add(compute(tempFunction));
            x = x.add(dx);
        }
        return sum;
    }

    /**
     * Returns the numerical value of the product of the function from lowerLimit to upperLimit, where both of these
     * limits are integers.
     * 
     * @param function
     *            - the function to take the product of.
     * @param lowerLimit
     *            - the lower limit of the product.
     * @param upperLimit
     *            - the upper limit of the product.
     * 
     * @return the numerical value of the product.
     * @throws CalculatorError
     */
    private BigDecimal product(String function, BigDecimal lowerLimit, BigDecimal upperLimit) throws CalculatorError,
            ArithmeticException
    {
        function = fReplace(function);
        BigDecimal product = new BigDecimal("1");
        BigDecimal x = lowerLimit;
        BigDecimal dx = new BigDecimal("1");
        String value, tempFunction;
        while (x.compareTo(upperLimit) <= 0)
        {
            value = x.toString();
            tempFunction = function.replace("x", "(" + value + ")");
            product = product.multiply(compute(tempFunction), Math.mc);
            x = x.add(dx);
        }
        return product;
    }
}
