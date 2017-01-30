package calculator;

/**
 * The types of errors that can be encountered by the calculator.
 * 
 * @author Andrei Purcarus
 *
 */
public enum ErrorType
{
    none, divisionByZero, tanUndef, secUndef, cscUndef, cotUndef, lnUndef, arcsinUndef, arccosUndef, arcsecUndef,
    arccscUndef, cschUndef, cothUndef, arccoshUndef, arctanhUndef, arcsechUndef, arccschUndef, arccothUndef,
    nonIntegralPowerNegativeArg, functionUndef, missingArg, bracketMismatch, invalidMode, invalidIntegralParameters,
    invalidSumParameters, invalidProductParameters, invalidRandParameters, randArgumentTooBig, overflow
}
