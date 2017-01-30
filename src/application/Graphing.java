package application;

import java.math.BigDecimal;

import calculator.Calculator;
import calculator.CalculatorError;
import calculator.Math;
import calculator.ErrorType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class Graphing
{
    public static void graph(TextArea display, Label graphLabel, double width, double height, double thickness,
            double spacing, String function, String xMin, String xMax, String yMin, String yMax, Calculator calc)
            throws CalculatorError
    {
        display.clear();

        // Checks for valid xMax, xMin, yMax, yMin.
        BigDecimal xMinValue, xMaxValue, yMinValue, yMaxValue;
        try
        {
            xMinValue = calc.compute(xMin);
        } catch (CalculatorError e)
        {
            graphLabel.setVisible(true);
            graphLabel.setText("Invalid xmin value.");
            return;
        }
        try
        {
            xMaxValue = calc.compute(xMax);
        } catch (CalculatorError e)
        {
            graphLabel.setVisible(true);
            graphLabel.setText("Invalid xmax value.");
            return;
        }
        try
        {
            yMinValue = calc.compute(yMin);
        } catch (CalculatorError e)
        {
            graphLabel.setVisible(true);
            graphLabel.setText("Invalid ymin value.");
            return;
        }
        try
        {
            yMaxValue = calc.compute(yMax);
        } catch (CalculatorError e)
        {
            graphLabel.setVisible(true);
            graphLabel.setText("Invalid ymax value.");
            return;
        }

        // Checks for xMax > xMin
        if (xMaxValue.compareTo(xMinValue) <= 0)
        {
            graphLabel.setVisible(true);
            graphLabel.setText("xmax must be greater than xmin.");
            return;
        }

        // Checks for yMax > yMin
        if (yMaxValue.compareTo(yMinValue) <= 0)
        {
            graphLabel.setVisible(true);
            graphLabel.setText("ymax must be greater than ymin.");
            return;
        }

        // Graph the function
        BigDecimal dx = (xMaxValue.subtract(xMinValue)).divide(new BigDecimal(width), Math.mc);
        BigDecimal dy = (yMaxValue.subtract(yMinValue)).divide(new BigDecimal(height), Math.mc);

        // String tempGraph = "";
        char[][] tempGraph = new char[(int) height + 1][(int) width + 1];

        BigDecimal x = xMinValue;
        for (int i = 0; i <= (int) (width); i += (int) spacing)
        {
            // String line = DrawCurve((new BigDecimal((thickness/2)+0.1)), new BigDecimal(spacing), x, yMinValue,
            // yMaxValue, dy, (int) height, function, dx, dy, calc);
            char[] line =
                    DrawCurve((new BigDecimal((thickness / 2) + 0.1)), new BigDecimal(spacing), x, dx, yMinValue,
                            yMaxValue, dy, (int) height, function, dx, dy, calc);

            for (int j = 0; j < (int) spacing; j++)
            {
                for (int k = 0; k < line.length; k++)
                {
                    tempGraph[k][i + j] = line[k];
                }
            }
            x = x.add(dx.multiply(new BigDecimal((int) spacing), Math.mc));
        }

        // Invert x and y.
        // String graph = "";

        // for (int j = 0; j <= (int) (height); j++) {
        // for (int i = 0; i <= (int) (width); i++) {
        // graph = graph + tempGraph.charAt(j + i*((int) (height + 1)));
        // }
        // graph = graph + "\n";
        // }
        char[] graph = new char[(int) (height + 1) * (int) (width + 2)];
        for (int j = 0; j <= (int) (height); j++)
        {
            for (int k = 0; k <= (int) (width); k++)
            {
                graph[j * (int) (width + 2) + k] = tempGraph[j][k];
            }
            graph[j * (int) (width + 2) + (int) (width + 1)] = '\n';
        }
        display.setText(new String(graph));
    }

    // private static final BigDecimal HALF_THICKNESS = new BigDecimal("1.6");
    private static final BigDecimal AXIS_HALF_THICKNESS = new BigDecimal("0.6");

    // Draws the curve for the value of x and all values of y in range.
    private static char[] DrawCurve(BigDecimal HALF_THICKNESS, BigDecimal SPACING, BigDecimal x, BigDecimal dx,
            BigDecimal yMin, BigDecimal yMax, BigDecimal dy, int height, String function, BigDecimal pixelLengthX,
            BigDecimal pixelLengthY, Calculator calc) throws CalculatorError
    {
        // String result = "";
        char[] result = new char[height + 1];
        boolean checkFunction = true;

        BigDecimal valueOfFunctionMinus = new BigDecimal("0");
        BigDecimal valueOfFunctionPlus = new BigDecimal("0");
        try
        {
            function = calc.fReplace(function);
            String functionMinus =
                    function.replace("x", "(" + (x.subtract(dx.divide(new BigDecimal("2"), Math.mc))).toString() + ")");
            valueOfFunctionMinus = calc.compute(functionMinus);
            String functionPlus =
                    function.replace("x", "(" + (x.add(dx.divide(new BigDecimal("2"), Math.mc))).toString() + ")");
            valueOfFunctionPlus = calc.compute(functionPlus);
        } catch (CalculatorError e)
        {
            ErrorType error = e.getError();
            if (error == ErrorType.functionUndef || error == ErrorType.missingArg || error == ErrorType.bracketMismatch
                    || error == ErrorType.invalidMode || error == ErrorType.invalidIntegralParameters
                    || error == ErrorType.invalidSumParameters || error == ErrorType.invalidProductParameters
                    || error == ErrorType.invalidRandParameters || error == ErrorType.randArgumentTooBig
                    || error == ErrorType.overflow)
            {
                throw new CalculatorError(error);
            } else
            {
                checkFunction = false;
            }
        }

        BigDecimal y = yMax;
        boolean maxGreaterThanMin = true;
        BigDecimal difference = new BigDecimal("0");
        if (checkFunction)
        {
            difference = valueOfFunctionPlus.subtract(valueOfFunctionMinus);
            if (difference.signum() <= 0)
            {
                maxGreaterThanMin = false;
            }
        }
        if (checkFunction)
        {
            if (maxGreaterThanMin)
            {
                for (int i = 0; i <= height; i++)
                {
                    if (x.compareTo(pixelLengthX.multiply(AXIS_HALF_THICKNESS, Math.mc)) < 0
                            && x.compareTo(pixelLengthX.multiply(AXIS_HALF_THICKNESS, Math.mc).negate()) > 0
                            && y.compareTo(pixelLengthY.multiply(AXIS_HALF_THICKNESS.multiply(SPACING, Math.mc),
                                    Math.mc)) < 0
                            && y.compareTo(pixelLengthY.multiply(AXIS_HALF_THICKNESS.multiply(SPACING, Math.mc),
                                    Math.mc).negate()) > 0)
                    {
                        result[i] = '+'; // The origin.
                    } else if (x.compareTo(pixelLengthX.multiply(AXIS_HALF_THICKNESS, Math.mc)) < 0
                            && x.compareTo(pixelLengthX.multiply(AXIS_HALF_THICKNESS, Math.mc).negate()) > 0)
                    {
                        result[i] = '-';
                    } else if (y.compareTo(pixelLengthY.multiply(AXIS_HALF_THICKNESS.multiply(SPACING, Math.mc),
                            Math.mc)) < 0
                            && y.compareTo(pixelLengthY.multiply(AXIS_HALF_THICKNESS.multiply(SPACING, Math.mc),
                                    Math.mc).negate()) > 0)
                    {
                        result[i] = '|';
                    } else if (y.compareTo(valueOfFunctionPlus.add(pixelLengthY.multiply(
                            HALF_THICKNESS.multiply(SPACING, Math.mc), Math.mc))) <= 0
                            && y.compareTo(valueOfFunctionMinus.subtract(pixelLengthY.multiply(
                                    HALF_THICKNESS.multiply(SPACING, Math.mc), Math.mc))) >= 0)
                    {
                        result[i] = '#';
                    } else
                    {
                        result[i] = ' ';
                    }
                    y = y.subtract(dy);
                }
            } else
            {
                for (int i = 0; i <= height; i++)
                {
                    if (x.compareTo(pixelLengthX.multiply(AXIS_HALF_THICKNESS, Math.mc)) < 0
                            && x.compareTo(pixelLengthX.multiply(AXIS_HALF_THICKNESS, Math.mc).negate()) > 0
                            && y.compareTo(pixelLengthY.multiply(AXIS_HALF_THICKNESS.multiply(SPACING, Math.mc),
                                    Math.mc)) < 0
                            && y.compareTo(pixelLengthY.multiply(AXIS_HALF_THICKNESS.multiply(SPACING, Math.mc),
                                    Math.mc).negate()) > 0)
                    {
                        result[i] = '+'; // The origin.
                    } else if (x.compareTo(pixelLengthX.multiply(AXIS_HALF_THICKNESS, Math.mc)) < 0
                            && x.compareTo(pixelLengthX.multiply(AXIS_HALF_THICKNESS, Math.mc).negate()) > 0)
                    {
                        result[i] = '-';
                    } else if (y.compareTo(pixelLengthY.multiply(AXIS_HALF_THICKNESS.multiply(SPACING, Math.mc),
                            Math.mc)) < 0
                            && y.compareTo(pixelLengthY.multiply(AXIS_HALF_THICKNESS.multiply(SPACING, Math.mc),
                                    Math.mc).negate()) > 0)
                    {
                        result[i] = '|';
                    } else if (y.compareTo(valueOfFunctionPlus.subtract(pixelLengthY.multiply(
                            HALF_THICKNESS.multiply(SPACING, Math.mc), Math.mc))) >= 0
                            && y.compareTo(valueOfFunctionMinus.add(pixelLengthY.multiply(
                                    HALF_THICKNESS.multiply(SPACING, Math.mc), Math.mc))) <= 0)
                    {
                        result[i] = '#';
                    } else
                    {
                        result[i] = ' ';
                    }
                    y = y.subtract(dy);
                }
            }
        } else
        {
            for (int i = 0; i <= height; i++)
            {
                if (x.compareTo(pixelLengthX.multiply(AXIS_HALF_THICKNESS, Math.mc)) < 0
                        && x.compareTo(pixelLengthX.multiply(AXIS_HALF_THICKNESS, Math.mc).negate()) > 0
                        && y.compareTo(pixelLengthY.multiply(AXIS_HALF_THICKNESS.multiply(SPACING, Math.mc), Math.mc)) < 0
                        && y.compareTo(pixelLengthY.multiply(AXIS_HALF_THICKNESS.multiply(SPACING, Math.mc), Math.mc)
                                .negate()) > 0)
                {
                    result[i] = '+'; // The origin.
                } else if (x.compareTo(pixelLengthX.multiply(AXIS_HALF_THICKNESS, Math.mc)) < 0
                        && x.compareTo(pixelLengthX.multiply(AXIS_HALF_THICKNESS, Math.mc).negate()) > 0)
                {
                    result[i] = '-';
                } else if (y.compareTo(pixelLengthY.multiply(AXIS_HALF_THICKNESS.multiply(SPACING, Math.mc), Math.mc)) < 0
                        && y.compareTo(pixelLengthY.multiply(AXIS_HALF_THICKNESS.multiply(SPACING, Math.mc), Math.mc)
                                .negate()) > 0)
                {
                    result[i] = '|';
                } else
                {
                    result[i] = ' ';
                }
                y = y.subtract(dy);
            }
        }

        return result;
    }
}
