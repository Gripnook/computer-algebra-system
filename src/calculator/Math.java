package calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.math.BigInteger;

import calculator.CalculatorError;

/**
 * A class that contains static methods that perform mathematical operations on arguments of type BigDecimal.
 * 
 * @author Andrei Purcarus
 *
 */
public class Math
{

    /**
     * The MathContext applied to all operations in the Math class.
     */
    public static final MathContext mc = new MathContext(32, RoundingMode.HALF_UP);

    /**
     * The MathContext used when checking if a BigDecimal is an integer.
     */
    public static MathContext mcIntRound = new MathContext(21, RoundingMode.HALF_UP);

    /**
     * The maximum error for functions computed using Taylor polynomials.
     */
    public static final BigDecimal MAX_ERROR = new BigDecimal("1e-30");

    /**
     * The mathematical constant e.
     */
    public static final BigDecimal E = new BigDecimal("2.718281828459045235360287471352662497757247093");

    /**
     * A special constant which satisfies E*Y = 2 - Y, implying that multiplying the constant by E will preserve its
     * distance from 1. It is used in the computation of the natural logarithm for this purpose, to obtain faster
     * convergence of the Taylor polynomial.
     */
    public static final BigDecimal Y = new BigDecimal("2").divide(E.add(new BigDecimal("1")), mc);

    /**
     * A special constant which satisfies Z/E = 2 - Z, implying that dividing the constant by E will preserve its
     * distance from 1. It is used in the computation of the natural logarithm for this purpose, to obtain faster
     * convergence of the Taylor polynomial.
     */
    public static final BigDecimal Z = Y.multiply(E, mc);

    /**
     * The square root of 3, used in the computation of arctan.
     */
    public static final BigDecimal SQRT3 = new BigDecimal("1.732050807568877293527446341505872366942805253");

    /**
     * The mathematical constant pi.
     */
    public static final BigDecimal PI = new BigDecimal("3.141592653589793238462643383279502884197169399");

    /**
     * The conversion factor for converting degrees to radians.
     */
    public static final BigDecimal DEG_TO_RAD = PI.divide(new BigDecimal("180"), mc);

    /**
     * Returns the absolute value of the argument.
     * 
     * @param argument
     *            - the argument.
     * @return the absolute value of the argument.
     */
    public static BigDecimal abs(BigDecimal argument)
    {
        return argument.abs();
    }

    /**
     * Returns the exponential function of the argument.
     * 
     * @param argument
     *            - the argument.
     * @return the exponential function of the argument.
     */
    public static BigDecimal exp(BigDecimal argument)
    {
        BigDecimal partialAnswer = new BigDecimal("1");
        boolean negative = false;
        if (argument.signum() == -1)
        {
            argument = argument.negate();
            negative = true;
        }
        while (argument.compareTo(new BigDecimal("1")) > 0)
        {
            argument = argument.subtract(new BigDecimal("1"));
            partialAnswer = partialAnswer.multiply(E, mc);
        }

        BigDecimal term = new BigDecimal("1");
        BigDecimal partialSum = new BigDecimal("1");
        int i = 1;

        do
        {
            term = term.multiply(argument.divide(new BigDecimal(i), mc), mc);
            partialSum = partialSum.add(term);
            i++;
        } while (abs(term).compareTo(MAX_ERROR) >= 0);

        BigDecimal result = partialAnswer.multiply(partialSum, mc);
        if (negative)
        {
            result = (new BigDecimal("1")).divide(result, mc);
        }

        if (abs(result).compareTo(MAX_ERROR) < 0)
        {
            return new BigDecimal("0");
        } else
        {
            return result;
        }
    }

    /**
     * Returns the natural logarithmic function of the argument.
     * 
     * @param argument
     *            - the argument.
     * @return the natural logarithmic function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal ln(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        if (argument.signum() != 1)
        {
            throw new CalculatorError(ErrorType.lnUndef);
        } else if (argument.compareTo(new BigDecimal("1")) == 0)
        {
            return new BigDecimal("0");
        } else
        {
            int count = 0;
            while (argument.compareTo(Z) > 0)
            {
                argument = argument.divide(E, mc);
                count++;
            }
            while (argument.compareTo(Y) < 0)
            {
                argument = argument.multiply(E, mc);
                count--;
            }

            BigDecimal tempArgument = argument.subtract(new BigDecimal("1"));
            BigDecimal termNumerator = tempArgument;
            BigDecimal term = tempArgument;
            BigDecimal partialSum = tempArgument;
            int i = 2;

            do
            {
                termNumerator = termNumerator.multiply(tempArgument.negate(), mc);
                term = termNumerator.divide(new BigDecimal(i), mc);
                partialSum = partialSum.add(term);
                i++;
            } while (abs(term).compareTo(MAX_ERROR) >= 0);

            BigDecimal result = (partialSum.add(new BigDecimal(count)));
            if (abs(result).compareTo(MAX_ERROR) < 0)
            {
                return new BigDecimal("0");
            } else
            {
                return result;
            }
        }
    }

    /**
     * Returns argument to the power of exponent
     * 
     * @param argument
     *            - the argument
     * @param exponent
     *            - the exponent
     * @return argument to the power of exponent
     * @throws CalculatorError
     */
    public static BigDecimal pow(BigDecimal argument, BigDecimal exponent) throws CalculatorError, ArithmeticException
    {
        if (exponent.signum() == 0)
        {
            return new BigDecimal("1");
        } else
        {
            if (argument.signum() == 1)
            {
                return exp(exponent.multiply(ln(argument), mc));
            } else if (argument.signum() == 0)
            {
                if (exponent.signum() == 1)
                {
                    return new BigDecimal("0");
                } else
                {
                    throw new CalculatorError(ErrorType.divisionByZero);
                }
            } else
            {
                if ((exponent.subtract(new BigDecimal(exponent.round(mcIntRound).toBigInteger()))).signum() == 0)
                {
                    if (exponent.round(mcIntRound).toBigInteger().remainder(new BigInteger("2")).signum() == 0)
                    {
                        return exp(exponent.multiply(ln(abs(argument)), mc));
                    } else
                    {
                        return (exp(exponent.multiply(ln(abs(argument)), mc))).negate();
                    }
                } else
                {
                    throw new CalculatorError(ErrorType.nonIntegralPowerNegativeArg);
                }
            }
        }
    }

    /**
     * Returns argument to the power of 0.5.
     * 
     * @param argument
     *            - the argument
     * @return argument to the power of exponent
     * @throws CalculatorError
     */
    public static BigDecimal sqrt(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        if (argument.signum() == -1)
        {
            throw new CalculatorError(ErrorType.nonIntegralPowerNegativeArg);
        } else if (argument.signum() == 0)
        {
            return new BigDecimal("0");
        } else
        {
            return exp((new BigDecimal("0.5")).multiply(ln(argument), mc));
        }
    }

    /**
     * Returns the sine function of the argument. The argument is assumed to be in radians.
     * 
     * @param argument
     *            - the argument.
     * @return the sine function of the argument.
     */
    public static BigDecimal sin(BigDecimal argument)
    {
        // Use better reduction for higher numbers.
        boolean negative = false;
        if (argument.signum() == -1)
        {
            argument = argument.negate();
            negative = true;
        }

        BigDecimal tempArgument = argument.divide((new BigDecimal("2")).multiply(PI, mc), mc);
        argument =
                (tempArgument.subtract(new BigDecimal(tempArgument.toBigInteger().toString()))).multiply(
                        (new BigDecimal("2")).multiply(PI, mc), mc);
        // while (argument.compareTo((new BigDecimal("2")).multiply(PI, mc)) >= 0) {
        // argument = argument.subtract((new BigDecimal("2")).multiply(PI, mc));
        // }
        // while (argument.compareTo(new BigDecimal("0")) < 0) {
        // argument = argument.add((new BigDecimal("2")).multiply(PI, mc));
        // }

        BigDecimal argumentSquared = argument.pow(2, mc);
        BigDecimal term = argument;
        BigDecimal partialSum = argument;
        int i = 2;

        do
        {
            term = term.multiply((argumentSquared.divide(new BigDecimal(i * (i + 1)), mc)).negate(), mc);
            partialSum = partialSum.add(term);
            i += 2;
        } while (abs(term).compareTo(MAX_ERROR) >= 0);

        if (abs(partialSum).compareTo(MAX_ERROR) < 0)
        {
            return new BigDecimal("0");
        } else
        {
            return negative ? partialSum.negate() : partialSum;
        }
    }

    /**
     * Returns the cosine function of the argument. The argument is assumed to be in radians.
     * 
     * @param argument
     *            - the argument.
     * @return the cosine function of the argument.
     */
    public static BigDecimal cos(BigDecimal argument)
    {
        // Use better reduction for higher numbers.
        if (argument.signum() == -1)
        {
            argument = argument.negate();
        }

        BigDecimal tempArgument = argument.divide((new BigDecimal("2")).multiply(PI, mc), mc);
        argument =
                (tempArgument.subtract(new BigDecimal(tempArgument.toBigInteger().toString()))).multiply(
                        (new BigDecimal("2")).multiply(PI, mc), mc);
        // while (argument.compareTo((new BigDecimal("2")).multiply(PI, mc)) >= 0) {
        // argument = argument.subtract((new BigDecimal("2")).multiply(PI, mc));
        // }
        // while (argument.compareTo(new BigDecimal("0")) < 0) {
        // argument = argument.add((new BigDecimal("2")).multiply(PI, mc));
        // }

        BigDecimal argumentSquared = argument.pow(2, mc);
        BigDecimal term = new BigDecimal("1");
        BigDecimal partialSum = new BigDecimal("1");
        int i = 1;

        do
        {
            term = term.multiply((argumentSquared.divide(new BigDecimal(i * (i + 1)), mc)).negate(), mc);
            partialSum = partialSum.add(term);
            i += 2;
        } while (abs(term).compareTo(MAX_ERROR) >= 0);

        if (abs(partialSum).compareTo(MAX_ERROR) < 0)
        {
            return new BigDecimal("0");
        } else
        {
            return partialSum;
        }
    }

    /**
     * Returns the tangent function of the argument. The argument is assumed to be in radians.
     * 
     * @param argument
     *            - the argument.
     * @return the tangent function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal tan(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        BigDecimal tempCos = cos(argument);
        if (tempCos.signum() == 0)
        {
            throw new CalculatorError(ErrorType.tanUndef);
        } else
        {
            return sin(argument).divide(tempCos, mc);
        }
    }

    /**
     * Returns the secant function of the argument. The argument is assumed to be in radians.
     * 
     * @param argument
     *            - the argument.
     * @return the secant function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal sec(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        BigDecimal tempCos = cos(argument);
        if (tempCos.signum() == 0)
        {
            throw new CalculatorError(ErrorType.secUndef);
        } else
        {
            return (new BigDecimal("1")).divide(tempCos, mc);
        }
    }

    /**
     * Returns the cosecant function of the argument. The argument is assumed to be in radians.
     * 
     * @param argument
     *            - the argument.
     * @return the cosecant function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal csc(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        BigDecimal tempSin = sin(argument);
        if (tempSin.signum() == 0)
        {
            throw new CalculatorError(ErrorType.cscUndef);
        } else
        {
            return (new BigDecimal("1")).divide(tempSin, mc);
        }
    }

    /**
     * Returns the cotangent function of the argument. The argument is assumed to be in radians.
     * 
     * @param argument
     *            - the argument.
     * @return the cotangent function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal cot(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        BigDecimal tempSin = sin(argument);
        if (tempSin.signum() == 0)
        {
            throw new CalculatorError(ErrorType.cotUndef);
        } else
        {
            return cos(argument).divide(tempSin, mc);
        }
    }

    /**
     * Returns the sine function of the argument. The argument is assumed to be in degrees.
     * 
     * @param argument
     *            - the argument.
     * @return the sine function of the argument.
     */
    public static BigDecimal sinDeg(BigDecimal argument)
    {
        return sin(DEG_TO_RAD.multiply(argument, mc));
    }

    /**
     * Returns the cosine function of the argument. The argument is assumed to be in degrees.
     * 
     * @param argument
     *            - the argument.
     * @return the cosine function of the argument.
     */
    public static BigDecimal cosDeg(BigDecimal argument)
    {
        return cos(DEG_TO_RAD.multiply(argument, mc));
    }

    /**
     * Returns the tangent function of the argument. The argument is assumed to be in degrees.
     * 
     * @param argument
     *            - the argument.
     * @return the tangent function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal tanDeg(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        return tan(DEG_TO_RAD.multiply(argument, mc));
    }

    /**
     * Returns the secant function of the argument. The argument is assumed to be in degrees.
     * 
     * @param argument
     *            - the argument.
     * @return the secant function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal secDeg(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        return sec(DEG_TO_RAD.multiply(argument, mc));
    }

    /**
     * Returns the cosecant function of the argument. The argument is assumed to be in degrees.
     * 
     * @param argument
     *            - the argument.
     * @return the cosecant function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal cscDeg(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        return csc(DEG_TO_RAD.multiply(argument, mc));
    }

    /**
     * Returns the cotangent function of the argument. The argument is assumed to be in degrees.
     * 
     * @param argument
     *            - the argument.
     * @return the cotangent function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal cotDeg(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        return cot(DEG_TO_RAD.multiply(argument, mc));
    }

    /**
     * Returns the arc sine function of the argument. The answer is given in radians.
     * 
     * @param argument
     *            - the argument.
     * @return the arc sine function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal arcsin(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        if (argument.compareTo(new BigDecimal("1")) > 0 || argument.compareTo(new BigDecimal("-1")) < 0)
        {
            throw new CalculatorError(ErrorType.arcsinUndef);
        } else
        {
            if (argument.signum() == 0)
            {
                return new BigDecimal("0");
            }
            if (argument.compareTo(new BigDecimal("1")) == 0)
            {
                return PI.divide(new BigDecimal("2"), mc);
            }
            if (argument.compareTo(new BigDecimal("-1")) == 0)
            {
                return (PI.divide(new BigDecimal("2"), mc)).negate();
            }
            return arctan(argument.divide(sqrt((new BigDecimal("1")).subtract(argument.pow(2, mc))), mc));
        }
    }

    /**
     * Returns the arc cosine function of the argument. The answer is given in radians.
     * 
     * @param argument
     *            - the argument.
     * @return the arc cosine function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal arccos(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        if (argument.compareTo(new BigDecimal("1")) > 0 || argument.compareTo(new BigDecimal("-1")) < 0)
        {
            throw new CalculatorError(ErrorType.arccosUndef);
        } else
        {
            return (PI.divide(new BigDecimal("2"), mc)).subtract(arcsin(argument));
        }
    }

    /**
     * Returns the arc tangent function of the argument. The answer is given in radians.
     * 
     * @param argument
     *            - the argument.
     * @return the arc tangent function of the argument.
     */
    public static BigDecimal arctan(BigDecimal argument)
    {
        if (argument.signum() == 0)
        {
            return new BigDecimal("0");
        }

        boolean negative = false;
        if (argument.signum() == -1)
        {
            argument = argument.negate();
            negative = true;
        }

        boolean greaterThanOne = false;
        if (argument.compareTo(new BigDecimal("1")) > 0)
        {
            argument = (new BigDecimal("1")).divide(argument, mc);
            greaterThanOne = true;
        }

        boolean greaterThanBound = false; // Compares the argument to 2 - sqrt(3)
                                          // Which is approximately 0.267949192431122706.
        if (argument.compareTo(new BigDecimal("0.267949192431122706")) > 0)
        {
            argument =
                    (((SQRT3).multiply(argument, mc)).subtract(new BigDecimal("1"))).divide((SQRT3).add(argument), mc);
            greaterThanBound = true;
        }

        BigDecimal argumentSquared = argument.pow(2, mc);
        BigDecimal termNumerator = argument;
        BigDecimal term = argument;
        BigDecimal partialSum = argument;
        int i = 3;

        do
        {
            termNumerator = termNumerator.multiply(argumentSquared.negate(), mc);
            term = termNumerator.divide(new BigDecimal(i), mc);
            partialSum = partialSum.add(term);
            i += 2;
        } while (abs(term).compareTo(MAX_ERROR) >= 0);

        BigDecimal result = partialSum;
        if (abs(result).compareTo(MAX_ERROR) < 0)
        {
            result = new BigDecimal("0");
        }

        if (greaterThanBound)
        {
            result = (PI.divide(new BigDecimal("6"), mc)).add(result);
        }

        if (greaterThanOne)
        {
            result = (PI.divide(new BigDecimal("2"))).subtract(result);
        }

        if (negative)
        {
            result = result.negate();
        }

        return result;
    }

    /**
     * Returns the arc secant function of the argument. The answer is given in radians.
     * 
     * @param argument
     *            - the argument.
     * @return the arc secant function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal arcsec(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        if (argument.compareTo(new BigDecimal("1")) < 0 && argument.compareTo(new BigDecimal("-1")) > 0)
        {
            throw new CalculatorError(ErrorType.arcsecUndef);
        } else
        {
            return arccos((new BigDecimal("1")).divide(argument, mc));
        }
    }

    /**
     * Returns the arc cosecant function of the argument. The answer is given in radians.
     * 
     * @param argument
     *            - the argument.
     * @return the arc cosecant function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal arccsc(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        if (argument.compareTo(new BigDecimal("1")) < 0 && argument.compareTo(new BigDecimal("-1")) > 0)
        {
            throw new CalculatorError(ErrorType.arccscUndef);
        } else
        {
            return arcsin((new BigDecimal("1")).divide(argument, mc));
        }
    }

    /**
     * Returns the arc cotangent function of the argument. The answer is given in radians.
     * 
     * @param argument
     *            - the argument.
     * @return the arc cotangent function of the argument.
     */
    public static BigDecimal arccot(BigDecimal argument)
    {
        if (argument.signum() != 0)
        {
            return arctan((new BigDecimal("1")).divide(argument, mc));
        } else
        {
            return PI.divide(new BigDecimal("2"), mc);
        }
    }

    /**
     * Returns the arc sine function of the argument. The answer is given in degrees.
     * 
     * @param argument
     *            - the argument.
     * @return the arc sine function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal arcsinDeg(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        return arcsin(argument).divide(DEG_TO_RAD, mc);
    }

    /**
     * Returns the arc cosine function of the argument. The answer is given in degrees.
     * 
     * @param argument
     *            - the argument.
     * @return the arc cosine function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal arccosDeg(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        return arccos(argument).divide(DEG_TO_RAD, mc);
    }

    /**
     * Returns the arc tangent function of the argument. The answer is given in degrees.
     * 
     * @param argument
     *            - the argument.
     * @return the arc tangent function of the argument.
     */
    public static BigDecimal arctanDeg(BigDecimal argument)
    {
        return arctan(argument).divide(DEG_TO_RAD, mc);
    }

    /**
     * Returns the arc secant function of the argument. The answer is given in degrees.
     * 
     * @param argument
     *            - the argument.
     * @return the arc secant function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal arcsecDeg(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        return arcsec(argument).divide(DEG_TO_RAD, mc);
    }

    /**
     * Returns the arc cosecant function of the argument. The answer is given in degrees.
     * 
     * @param argument
     *            - the argument.
     * @return the arc cosecant function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal arccscDeg(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        return arccsc(argument).divide(DEG_TO_RAD, mc);
    }

    /**
     * Returns the arc cotangent function of the argument. The answer is given in degrees.
     * 
     * @param argument
     *            - the argument.
     * @return the arc cotangent function of the argument.
     */
    public static BigDecimal arccotDeg(BigDecimal argument)
    {
        if (argument.signum() != 0)
        {
            return arccot(argument).divide(DEG_TO_RAD, mc);
        } else
        {
            return new BigDecimal("90");
        }
    }

    /**
     * Returns the hyperbolic sine function of the argument.
     * 
     * @param argument
     *            - the argument.
     * @return the hyperbolic sine function of the argument.
     */
    public static BigDecimal sinh(BigDecimal argument)
    {
        BigDecimal tempExp = exp(argument);
        return (tempExp.subtract((new BigDecimal("1")).divide(tempExp, mc)).divide(new BigDecimal("2"), mc));
    }

    /**
     * Returns the hyperbolic cosine function of the argument.
     * 
     * @param argument
     *            - the argument.
     * @return the hyperbolic cosine function of the argument.
     */
    public static BigDecimal cosh(BigDecimal argument)
    {
        BigDecimal tempExp = exp(argument);
        return (tempExp.add((new BigDecimal("1")).divide(tempExp, mc)).divide(new BigDecimal("2"), mc));
    }

    /**
     * Returns the hyperbolic tangent function of the argument.
     * 
     * @param argument
     *            - the argument.
     * @return the hyperbolic tangent function of the argument.
     */
    public static BigDecimal tanh(BigDecimal argument)
    {
        BigDecimal positiveExponential = exp(argument);
        BigDecimal negativeExponential = (new BigDecimal("1")).divide(positiveExponential, mc);
        return (positiveExponential.subtract(negativeExponential)).divide(positiveExponential.add(negativeExponential),
                mc);
    }

    /**
     * Returns the hyperbolic secant function of the argument.
     * 
     * @param argument
     *            - the argument.
     * @return the hyperbolic secant function of the argument.
     */
    public static BigDecimal sech(BigDecimal argument)
    {
        return (new BigDecimal("1")).divide(cosh(argument), mc);
    }

    /**
     * Returns the hyperbolic cosecant function of the argument.
     * 
     * @param argument
     *            - the argument.
     * @return the hyperbolic cosecant function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal csch(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        if (argument.signum() == 0)
        {
            throw new CalculatorError(ErrorType.cschUndef);
        } else
        {
            return (new BigDecimal("1")).divide(sinh(argument), mc);
        }
    }

    /**
     * Returns the hyperbolic cotangent function of the argument.
     * 
     * @param argument
     *            - the argument.
     * @return the hyperbolic cotangent function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal coth(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        if (argument.signum() == 0)
        {
            throw new CalculatorError(ErrorType.cothUndef);
        } else
        {
            BigDecimal positiveExponential = exp(argument);
            BigDecimal negativeExponential = (new BigDecimal("1")).divide(positiveExponential, mc);
            return (positiveExponential.add(negativeExponential)).divide(
                    positiveExponential.subtract(negativeExponential), mc);
        }
    }

    /**
     * Returns the arc hyperbolic sine function of the argument.
     * 
     * @param argument
     *            - the argument.
     * @return the arc hyperbolic sine function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal arcsinh(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        return ln(argument.add(sqrt(argument.pow(2, mc).add(new BigDecimal("1")))));
    }

    /**
     * Returns the arc hyperbolic cosine function of the argument.
     * 
     * @param argument
     *            - the argument.
     * @return the arc hyperbolic cosine function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal arccosh(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        if (argument.compareTo(new BigDecimal("1")) < 0)
        {
            throw new CalculatorError(ErrorType.arccoshUndef);
        } else
        {
            return ln(argument.add(sqrt(argument.pow(2, mc).subtract(new BigDecimal("1")))));
        }
    }

    /**
     * Returns the arc hyperbolic tangent function of the argument.
     * 
     * @param argument
     *            - the argument.
     * @return the arc hyperbolic tangent function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal arctanh(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        if (argument.compareTo(new BigDecimal("1")) >= 0 || argument.compareTo(new BigDecimal("-1")) <= 0)
        {
            throw new CalculatorError(ErrorType.arctanhUndef);
        } else
        {
            return (new BigDecimal("0.5")).multiply(ln(((new BigDecimal("2")).divide(
                    (new BigDecimal("1")).subtract(argument), mc)).subtract(new BigDecimal("1"))), mc);
        }
    }

    /**
     * Returns the arc hyperbolic secant function of the argument.
     * 
     * @param argument
     *            - the argument.
     * @return the arc hyperbolic secant function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal arcsech(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        if (argument.compareTo(new BigDecimal("1")) > 0 || argument.compareTo(new BigDecimal("0")) <= 0)
        {
            throw new CalculatorError(ErrorType.arcsechUndef);
        } else
        {
            return arccosh((new BigDecimal("1")).divide(argument, mc));
        }
    }

    /**
     * Returns the arc hyperbolic cosecant function of the argument.
     * 
     * @param argument
     *            - the argument.
     * @return the arc hyperbolic cosecant function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal arccsch(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        if (argument.signum() == 0)
        {
            throw new CalculatorError(ErrorType.arccschUndef);
        } else
        {
            return arcsinh((new BigDecimal("1")).divide(argument, mc));
        }
    }

    /**
     * Returns the arc hyperbolic cotangent function of the argument.
     * 
     * @param argument
     *            - the argument.
     * @return the arc hyperbolic cotangent function of the argument.
     * @throws CalculatorError
     */
    public static BigDecimal arccoth(BigDecimal argument) throws CalculatorError, ArithmeticException
    {
        if (argument.compareTo(new BigDecimal("1")) <= 0 && argument.compareTo(new BigDecimal("-1")) >= 0)
        {
            throw new CalculatorError(ErrorType.arccothUndef);
        } else
        {
            return arctanh((new BigDecimal("1")).divide(argument, mc));
        }
    }
}
