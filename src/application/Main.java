package application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.math.MathContext;

import calculator.Calculator;
import calculator.CalculatorError;
import calculator.ErrorType;
import calculator.Utility;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class Main extends Application
{

    /**
     * The types of exponential notation that can be used.
     * 
     * @author Andrei Purcarus
     *
     */
    public enum ExponentialFormat
    {
        scientific, engineering
    }

    /**
     * Sets the primary stage on which the application's GUI is set.
     */
    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            primaryStage.setTitle("Andrei's Computer Algebra System");
            Parent root = FXMLLoader.load(getClass().getResource("Root.fxml"));
            Scene scene = new Scene(root, 318, 620);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Starts the program.
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        Font.loadFont(Main.class.getResource("square.ttf").toExternalForm(), 1);
        launch(args);
    }

    /**
     * The maximum number of lines that can be displayed on the display area of the GUI.
     */
    private static final int MAX_LINES_ON_DISPLAY = 9;

    /**
     * The current number of lines displayed on the display area of the GUI.
     */
    private int numLinesOnDisplay = 0;

    /**
     * The current number of significant figures displayed by the application.
     */
    private static int mcDisplayPrecision = 6;

    /**
     * The MathContext applied to the rounding of answers for the display.
     */
    private static MathContext mcDisplay = new MathContext(6, RoundingMode.HALF_UP);

    /**
     * The current type of exponential notation used by the application.
     */
    private ExponentialFormat ef = ExponentialFormat.scientific;

    /**
     * The calculator used by the application to compute the value of the expressions input by the user.
     */
    private Calculator CAS = new Calculator();

    /**
	 * 
	 */
    @FXML
    private AnchorPane stdWindow, graphWindow;

    /**
     * The TextFields in which the user enters input.
     */
    @FXML
    private TextField textBar, xMin, xMax, yMin, yMax;

    /**
     * The TextAreas on which the application displays the results.
     */
    @FXML
    private TextArea display, graphDisplay;

    /**
     * The Label on which the current mode is printed.
     */
    @FXML
    private Label mode;

    /**
     * The Label on which the graph errors are printed.
     */
    @FXML
    private Label graphLabel;

    @FXML
    private Slider thickness, spacing;

    /**
     * The current functionality of the calculator.
     */
    @FXML
    private CheckMenuItem stdMode, graphMode;

    /**
     * The CheckMenuItems for the significant figures menu.
     */
    @FXML
    private CheckMenuItem SF1, SF2, SF3, SF4, SF5, SF6, SF7, SF8, SF9, SF10, SF11, SF12, SF13, SF14, SF15, SF16, SF17,
            SF18;

    /**
     * The CheckMenuItems for the exponential format menu.
     */
    @FXML
    private CheckMenuItem efScientific, efEngineering;

    /**
     * Closes the program.
     */
    @FXML
    protected void onClose()
    {
        Platform.exit();
    }

    /**
     * Deletes the current contents of the textBar and stores them in the clipboard.
     */
    @FXML
    protected void onCut()
    {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(textBar.getText());
        clipboard.setContent(content);
        textBar.clear();
    }

    /**
     * Copies the current contents of the textBar and stores them in the clipboard.
     */
    @FXML
    protected void onCopy()
    {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(textBar.getText());
        clipboard.setContent(content);
    }

    /**
     * Pastes the current contents of the clipboard to the textBar.
     */
    @FXML
    protected void onPaste()
    {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        textBar.appendText(clipboard.getContent(DataFormat.PLAIN_TEXT).toString());
    }

    /**
     * Sets the functionality to the standard evaluation mode.
     */
    @FXML
    protected void onStdMode()
    {
        if (stdMode.isSelected())
        {
            graphMode.setSelected(false);

            Stage oldStage = (Stage) graphWindow.getScene().getWindow();
            oldStage.close();

            try
            {
                CAS.setMode(Calculator.Mode.radians);
            } catch (CalculatorError e)
            {

            }
            Stage primaryStage = new Stage();
            try
            {
                primaryStage.setTitle("Andrei's Computer Algebra System");
                Parent root = FXMLLoader.load(getClass().getResource("Root.fxml"));
                Scene scene = new Scene(root, 318, 620);
                scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                primaryStage.setScene(scene);
                primaryStage.show();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else
        {
            stdMode.setSelected(true);
        }
    }

    /**
     * Sets the functionality to the graphing mode.
     */
    @FXML
    protected void onGraphMode()
    {
        if (graphMode.isSelected())
        {
            stdMode.setSelected(false);

            Stage oldStage = (Stage) stdWindow.getScene().getWindow();
            oldStage.close();

            try
            {
                CAS.setMode(Calculator.Mode.radians);
            } catch (CalculatorError e)
            {

            }
            Stage primaryStage = new Stage();
            try
            {
                primaryStage.setTitle("Andrei's Computer Algebra System");
                Parent root = FXMLLoader.load(getClass().getResource("Graphing.fxml"));
                Scene scene = new Scene(root, 1000, 620);
                scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                primaryStage.setScene(scene);
                primaryStage.show();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else
        {
            graphMode.setSelected(true);
        }
    }

    /**
     * Unselects all items in the significant figure menu.
     */
    protected void clearSF()
    {
        SF1.setSelected(false);
        SF2.setSelected(false);
        SF3.setSelected(false);
        SF4.setSelected(false);
        SF5.setSelected(false);
        SF6.setSelected(false);
        SF7.setSelected(false);
        SF8.setSelected(false);
        SF9.setSelected(false);
        SF10.setSelected(false);
        SF11.setSelected(false);
        SF12.setSelected(false);
        SF13.setSelected(false);
        SF14.setSelected(false);
        SF15.setSelected(false);
        SF16.setSelected(false);
        SF17.setSelected(false);
        SF18.setSelected(false);
    }

    /**
     * Sets SF1 to selected and sets the current display precision of the application to 1.
     */
    @FXML
    protected void onSF1()
    {
        clearSF();
        SF1.setSelected(true);
        mcDisplayPrecision = 1;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF2 to selected and sets the current display precision of the application to 2.
     */
    @FXML
    protected void onSF2()
    {
        clearSF();
        SF2.setSelected(true);
        mcDisplayPrecision = 2;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF3 to selected and sets the current display precision of the application to 3.
     */
    @FXML
    protected void onSF3()
    {
        clearSF();
        SF3.setSelected(true);
        mcDisplayPrecision = 3;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF4 to selected and sets the current display precision of the application to 4.
     */
    @FXML
    protected void onSF4()
    {
        clearSF();
        SF4.setSelected(true);
        mcDisplayPrecision = 4;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF5 to selected and sets the current display precision of the application to 5.
     */
    @FXML
    protected void onSF5()
    {
        clearSF();
        SF5.setSelected(true);
        mcDisplayPrecision = 5;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF6 to selected and sets the current display precision of the application to 6.
     */
    @FXML
    protected void onSF6()
    {
        clearSF();
        SF6.setSelected(true);
        mcDisplayPrecision = 6;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF7 to selected and sets the current display precision of the application to 7.
     */
    @FXML
    protected void onSF7()
    {
        clearSF();
        SF7.setSelected(true);
        mcDisplayPrecision = 7;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF8 to selected and sets the current display precision of the application to 8.
     */
    @FXML
    protected void onSF8()
    {
        clearSF();
        SF8.setSelected(true);
        mcDisplayPrecision = 8;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF9 to selected and sets the current display precision of the application to 9.
     */
    @FXML
    protected void onSF9()
    {
        clearSF();
        SF9.setSelected(true);
        mcDisplayPrecision = 9;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF10 to selected and sets the current display precision of the application to 10.
     */
    @FXML
    protected void onSF10()
    {
        clearSF();
        SF10.setSelected(true);
        mcDisplayPrecision = 10;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF11 to selected and sets the current display precision of the application to 11.
     */
    @FXML
    protected void onSF11()
    {
        clearSF();
        SF11.setSelected(true);
        mcDisplayPrecision = 11;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF12 to selected and sets the current display precision of the application to 12.
     */
    @FXML
    protected void onSF12()
    {
        clearSF();
        SF12.setSelected(true);
        mcDisplayPrecision = 12;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF13 to selected and sets the current display precision of the application to 13.
     */
    @FXML
    protected void onSF13()
    {
        clearSF();
        SF13.setSelected(true);
        mcDisplayPrecision = 13;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF14 to selected and sets the current display precision of the application to 14.
     */
    @FXML
    protected void onSF14()
    {
        clearSF();
        SF14.setSelected(true);
        mcDisplayPrecision = 14;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF15 to selected and sets the current display precision of the application to 15.
     */
    @FXML
    protected void onSF15()
    {
        clearSF();
        SF15.setSelected(true);
        mcDisplayPrecision = 15;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF16 to selected and sets the current display precision of the application to 16.
     */
    @FXML
    protected void onSF16()
    {
        clearSF();
        SF16.setSelected(true);
        mcDisplayPrecision = 16;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF17 to selected and sets the current display precision of the application to 17.
     */
    @FXML
    protected void onSF17()
    {
        clearSF();
        SF17.setSelected(true);
        mcDisplayPrecision = 17;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Sets SF18 to selected and sets the current display precision of the application to 18.
     */
    @FXML
    protected void onSF18()
    {
        clearSF();
        SF18.setSelected(true);
        mcDisplayPrecision = 18;
        mcDisplay = new MathContext(mcDisplayPrecision, RoundingMode.HALF_UP);
    }

    /**
     * Unselects all items in the exponential format menu.
     */
    protected void clearEf()
    {
        efScientific.setSelected(false);
        efEngineering.setSelected(false);
    }

    /**
     * Sets efScientific to selected and sets the current exponential format to scientific.
     */
    @FXML
    protected void onEfScientific()
    {
        clearEf();
        efScientific.setSelected(true);
        ef = ExponentialFormat.scientific;
    }

    /**
     * Sets efEngineering to selected and sets the current exponential format to engineering.
     */
    @FXML
    protected void onEfEngineering()
    {
        clearEf();
        efEngineering.setSelected(true);
        ef = ExponentialFormat.engineering;
    }

    /**
     * Appends "1" to the textBar;
     */
    @FXML
    protected void on1()
    {
        textBar.appendText("1");
    }

    /**
     * Appends "2" to the textBar;
     */
    @FXML
    protected void on2()
    {
        textBar.appendText("2");
    }

    /**
     * Appends "3" to the textBar;
     */
    @FXML
    protected void on3()
    {
        textBar.appendText("3");
    }

    /**
     * Appends "4" to the textBar;
     */
    @FXML
    protected void on4()
    {
        textBar.appendText("4");
    }

    /**
     * Appends "5" to the textBar;
     */
    @FXML
    protected void on5()
    {
        textBar.appendText("5");
    }

    /**
     * Appends "6" to the textBar;
     */
    @FXML
    protected void on6()
    {
        textBar.appendText("6");
    }

    /**
     * Appends "7" to the textBar;
     */
    @FXML
    protected void on7()
    {
        textBar.appendText("7");
    }

    /**
     * Appends "8" to the textBar;
     */
    @FXML
    protected void on8()
    {
        textBar.appendText("8");
    }

    /**
     * Appends "9" to the textBar;
     */
    @FXML
    protected void on9()
    {
        textBar.appendText("9");
    }

    /**
     * Appends "0" to the textBar;
     */
    @FXML
    protected void on0()
    {
        textBar.appendText("0");
    }

    /**
     * Appends .1" to the textBar;
     */
    @FXML
    protected void onDecimal()
    {
        textBar.appendText(".");
    }

    /**
     * Appends "+" to the textBar;
     */
    @FXML
    protected void onPlus()
    {
        textBar.appendText("+");
    }

    /**
     * Appends "-" to the textBar;
     */
    @FXML
    protected void onMinus()
    {
        textBar.appendText("-");
    }

    /**
     * Appends "*" to the textBar;
     */
    @FXML
    protected void onTimes()
    {
        textBar.appendText("*");
    }

    /**
     * Appends "/" to the textBar;
     */
    @FXML
    protected void onDiv()
    {
        textBar.appendText("/");
    }

    /**
     * Appends "%" to the textBar;
     */
    @FXML
    protected void onMod()
    {
        textBar.appendText("%");
    }

    /**
     * Appends ")" to the textBar;
     */
    @FXML
    protected void onRBracket()
    {
        textBar.appendText(")");
    }

    /**
     * Appends "(" to the textBar;
     */
    @FXML
    protected void onLBracket()
    {
        textBar.appendText("(");
    }

    /**
     * Appends "E" to the textBar;
     */
    @FXML
    protected void onExpNotation()
    {
        textBar.appendText("E");
    }

    /**
     * Appends "e" to the textBar;
     */
    @FXML
    protected void onE()
    {
        textBar.appendText("e");
    }

    /**
     * Appends lower case pi to the textBar;
     */
    @FXML
    protected void onPI()
    {
        textBar.appendText("\u03C0");
    }

    /**
     * Appends "abs(" to the textBar;
     */
    @FXML
    protected void onAbs()
    {
        textBar.appendText("abs(");
    }

    /**
     * Appends "exp(" to the textBar;
     */
    @FXML
    protected void onExp()
    {
        textBar.appendText("exp(");
    }

    /**
     * Appends "ln(" to the textBar;
     */
    @FXML
    protected void onLn()
    {
        textBar.appendText("ln(");
    }

    /**
     * Appends the integral symbol to the textBar;
     */
    @FXML
    protected void onIntegral()
    {
        textBar.appendText("\u222B(");
    }

    /**
     * Appends "^" to the textBar;
     */
    @FXML
    protected void onPow()
    {
        textBar.appendText("^");
    }

    /**
     * Appends the square root symbol to the textBar;
     */
    @FXML
    protected void onSqrt()
    {
        textBar.appendText("\u221A(");
    }

    /**
     * Appends upper case sigma to the textBar;
     */
    @FXML
    protected void onSum()
    {
        textBar.appendText("\u03A3(");
    }

    /**
     * Appends upper case pi to the textBar;
     */
    @FXML
    protected void onProduct()
    {
        textBar.appendText("\u03A0(");
    }

    /**
     * Appends "sin(" to the textBar;
     */
    @FXML
    protected void onSin()
    {
        textBar.appendText("sin(");
    }

    /**
     * Appends "cos(" to the textBar;
     */
    @FXML
    protected void onCos()
    {
        textBar.appendText("cos(");
    }

    /**
     * Appends "tan(" to the textBar;
     */
    @FXML
    protected void onTan()
    {
        textBar.appendText("tan(");
    }

    /**
     * Appends "sec(" to the textBar;
     */
    @FXML
    protected void onSec()
    {
        textBar.appendText("sec(");
    }

    /**
     * Appends "csc(" to the textBar;
     */
    @FXML
    protected void onCsc()
    {
        textBar.appendText("csc(");
    }

    /**
     * Appends "cot(" to the textBar;
     */
    @FXML
    protected void onCot()
    {
        textBar.appendText("cot(");
    }

    /**
     * Appends "arc" to the textBar;
     */
    @FXML
    protected void onArc()
    {
        textBar.appendText("arc");
    }

    /**
     * Appends "sinh(" to the textBar;
     */
    @FXML
    protected void onSinh()
    {
        textBar.appendText("sinh(");
    }

    /**
     * Appends "cosh(" to the textBar;
     */
    @FXML
    protected void onCosh()
    {
        textBar.appendText("cosh(");
    }

    /**
     * Appends "tanh(" to the textBar;
     */
    @FXML
    protected void onTanh()
    {
        textBar.appendText("tanh(");
    }

    /**
     * Appends "sech(" to the textBar;
     */
    @FXML
    protected void onSech()
    {
        textBar.appendText("sech(");
    }

    /**
     * Appends "csch(" to the textBar;
     */
    @FXML
    protected void onCsch()
    {
        textBar.appendText("csch(");
    }

    /**
     * Appends "coth(" to the textBar;
     */
    @FXML
    protected void onCoth()
    {
        textBar.appendText("coth(");
    }

    /**
     * Appends "," to the textBar;
     */
    @FXML
    protected void onComma()
    {
        textBar.appendText(",");
    }

    /**
     * Appends "x" to the textBar;
     */
    @FXML
    protected void onX()
    {
        textBar.appendText("x");
    }

    /**
     * Appends "ans" to the textBar;
     */
    @FXML
    protected void onAns()
    {
        textBar.appendText("ans");
    }

    /**
     * Appends "rand(" to the textBar;
     */
    @FXML
    protected void onRand()
    {
        textBar.appendText("rand(");
    }

    /**
     * Removes the last character in the textBar, if it exists.
     */
    @FXML
    protected void onBackspace()
    {
        String temp = textBar.getText();
        if (temp.length() > 0)
        {
            textBar.setText(temp.substring(0, temp.length() - 1));
        }
    }

    /**
     * Changes the mode of the calculator to the opposite of the one currently used and changes the Label mode to
     * reflect the change.
     */
    @FXML
    protected void onMode()
    {
        if (CAS.getMode() == Calculator.Mode.radians)
        {
            try
            {
                CAS.setMode(Calculator.Mode.degrees);
                mode.setText("DEG");
            } catch (CalculatorError e)
            {
                displayError(e.getError());
            }
        } else if (CAS.getMode() == Calculator.Mode.degrees)
        {
            try
            {
                CAS.setMode(Calculator.Mode.radians);
                mode.setText("RAD");
            } catch (CalculatorError e)
            {
                displayError(e.getError());
            }

        }
    }

    /**
     * Sends the current text written on the textBar to be computed. Also updates the number of lines of output and
     * deletes the first line if it exceeds the maximum.
     */
    @FXML
    protected void onEnter()
    {
        String input = Utility.removeWhiteSpace(textBar.getText());
        input = Utility.addImplicitMultiplication(input);
        if (input.isEmpty())
        {
            // Does nothing.
        } else
        {
            evaluate(input, CAS);
            numLinesOnDisplay++;
            while (numLinesOnDisplay > MAX_LINES_ON_DISPLAY)
            {
                String currentDisplay = display.getText();
                int i = currentDisplay.indexOf('\n');
                display.setText(currentDisplay.substring(i + 1, currentDisplay.length()));
                numLinesOnDisplay--;
            }
        }
    }

    @FXML
    protected void onGraph()
    {
        graphLabel.setVisible(false);
        String function = Utility.removeWhiteSpace(textBar.getText());
        function = Utility.addImplicitMultiplication(function);
        String xMinString = Utility.removeWhiteSpace(xMin.getText());
        xMinString = Utility.addImplicitMultiplication(xMinString);
        String xMaxString = Utility.removeWhiteSpace(xMax.getText());
        xMaxString = Utility.addImplicitMultiplication(xMaxString);
        String yMinString = Utility.removeWhiteSpace(yMin.getText());
        yMinString = Utility.addImplicitMultiplication(yMinString);
        String yMaxString = Utility.removeWhiteSpace(yMax.getText());
        yMaxString = Utility.addImplicitMultiplication(yMaxString);
        if (function.isEmpty() || xMinString.isEmpty() || xMaxString.isEmpty() || yMinString.isEmpty()
                || yMaxString.isEmpty())
        {
            // Does nothing.
        } else
        {
            try
            {
                Graphing.graph(graphDisplay, graphLabel, graphDisplay.getWidth() - 6, graphDisplay.getHeight() - 6,
                        thickness.getValue(), spacing.getValue(), function, xMinString, xMaxString, yMinString,
                        yMaxString, CAS);
            } catch (CalculatorError e)
            {
                graphLabel.setVisible(true);
                displayErrorGraphLabel(e.getError());
            }
        }
    }

    /**
     * Clears the textBar.
     */
    @FXML
    protected void onClear()
    {
        textBar.clear();
    }

    /**
     * Clears the xMin textField.
     */
    @FXML
    protected void onClearXMin()
    {
        xMin.clear();
    }

    /**
     * Clears the xMax textField.
     */
    @FXML
    protected void onClearXMax()
    {
        xMax.clear();
    }

    /**
     * Clears the yMin textField.
     */
    @FXML
    protected void onClearYMin()
    {
        yMin.clear();
    }

    /**
     * Clears the yMax textField.
     */
    @FXML
    protected void onClearYMax()
    {
        yMax.clear();
    }

    /**
     * Clears the display.
     */
    @FXML
    protected void onClearDisplay()
    {
        display.clear();
        numLinesOnDisplay = 0;
    }

    /**
     * Clears the graphDisplay.
     */
    @FXML
    protected void onClearGraphDisplay()
    {
        graphDisplay.clear();
        graphLabel.setVisible(false);
    }

    /**
     * Evaluates the string using the calculator and outputs the value to the display according to the current
     * exponential format of the application.
     * 
     * If an error is reached, it instead calls displayError(ErrorType error) to display the error.
     * 
     * @param str
     *            - the String to be evaluated.
     * @param calc
     *            - the calculator to use to perform the computations.
     */
    private void evaluate(String str, Calculator calc)
    {
        try
        {
            BigDecimal result = calc.compute(str);
            switch (ef)
            {
            case scientific:
                display.setText(display.getText() + result.round(mcDisplay).toString() + "\n");
                break;
            case engineering:
                display.setText(display.getText() + result.round(mcDisplay).toEngineeringString() + "\n");
                break;
            default:
                break;
            }
            calc.setAnswer(result);
        } catch (CalculatorError e)
        {
            displayError(e.getError());
        } catch (ArithmeticException e)
        {
            displayError(ErrorType.overflow);
        }
    }

    /**
     * Displays the current error type.
     * 
     * @param error
     *            - the error to be displayed.
     */
    private void displayError(ErrorType error)
    {
        switch (error)
        {
        case none:
            display.setText(display.getText() + "No errors detected.\n");
            break;
        case divisionByZero:
            display.setText(display.getText() + "Error. Division by 0.\n");
            break;
        case tanUndef:
            display.setText(display.getText() + "Error. Tangent undefined.\n");
            break;
        case secUndef:
            display.setText(display.getText() + "Error. Secant undefined.\n");
            break;
        case cscUndef:
            display.setText(display.getText() + "Error. Cosecant undefined.\n");
            break;
        case cotUndef:
            display.setText(display.getText() + "Error. Cotangent undefined.\n");
            break;
        case lnUndef:
            display.setText(display.getText() + "Error. Ln undefined.\n");
            break;
        case arcsinUndef:
            display.setText(display.getText() + "Error. Arcsin undefined.\n");
            break;
        case arccosUndef:
            display.setText(display.getText() + "Error. Arccos undefined.\n");
            break;
        case arcsecUndef:
            display.setText(display.getText() + "Error. Arcsec undefined.\n");
            break;
        case arccscUndef:
            display.setText(display.getText() + "Error. Arccsc undefined.\n");
            break;
        case cschUndef:
            display.setText(display.getText() + "Error. Csch undefined.\n");
            break;
        case cothUndef:
            display.setText(display.getText() + "Error. Coth undefined.\n");
            break;
        case arccoshUndef:
            display.setText(display.getText() + "Error. Arccosh undefined.\n");
            break;
        case arctanhUndef:
            display.setText(display.getText() + "Error. Arctanh undefined.\n");
            break;
        case arcsechUndef:
            display.setText(display.getText() + "Error. Arcsech undefined.\n");
            break;
        case arccschUndef:
            display.setText(display.getText() + "Error. Arccsch undefined.\n");
            break;
        case arccothUndef:
            display.setText(display.getText() + "Error. Arccoth undefined.\n");
            break;
        case nonIntegralPowerNegativeArg:
            display.setText(display.getText() + "Error. Negative number to non-integral power.\n");
            break;
        case functionUndef:
            display.setText(display.getText() + "Error. Function undefined.\n");
            break;
        case missingArg:
            display.setText(display.getText() + "Error. Missing argument.\n");
            break;
        case bracketMismatch:
            display.setText(display.getText() + "Error. Bracket mismatch.\n");
            break;
        case invalidMode:
            display.setText(display.getText() + "Error. Bracket mismatch.\n");
            break;
        case invalidIntegralParameters:
            display.setText(display.getText() + "Error. Invalid integral arguments.\n");
            break;
        case invalidSumParameters:
            display.setText(display.getText() + "Error. Invalid sum arguments.\n");
            break;
        case invalidProductParameters:
            display.setText(display.getText() + "Error. Invalid product arguments.\n");
            break;
        case invalidRandParameters:
            display.setText(display.getText() + "Error. Invalid rand argument.\n");
            break;
        case randArgumentTooBig:
            display.setText(display.getText() + "Error. Rand argument too big.\n");
            break;
        case overflow:
            display.setText(display.getText() + "Error. Overflow.\n");
            break;
        default:
            break;
        }
    }

    /**
     * Displays the current error type.
     * 
     * @param error
     *            - the error to be displayed.
     */
    private void displayErrorGraphLabel(ErrorType error)
    {
        switch (error)
        {
        case none:
            graphLabel.setText("No errors detected.");
            break;
        case divisionByZero:
            graphLabel.setText("Error. Division by 0.");
            break;
        case tanUndef:
            graphLabel.setText("Error. Tangent undefined.");
            break;
        case secUndef:
            graphLabel.setText("Error. Secant undefined.");
            break;
        case cscUndef:
            graphLabel.setText("Error. Cosecant undefined.");
            break;
        case cotUndef:
            graphLabel.setText("Error. Cotangent undefined.");
            break;
        case lnUndef:
            graphLabel.setText("Error. Ln undefined.");
            break;
        case arcsinUndef:
            graphLabel.setText("Error. Arcsin undefined.");
            break;
        case arccosUndef:
            graphLabel.setText("Error. Arccos undefined.");
            break;
        case arcsecUndef:
            graphLabel.setText("Error. Arcsec undefined.");
            break;
        case arccscUndef:
            graphLabel.setText("Error. Arccsc undefined.");
            break;
        case cschUndef:
            graphLabel.setText("Error. Csch undefined.");
            break;
        case cothUndef:
            graphLabel.setText("Error. Coth undefined.");
            break;
        case arccoshUndef:
            graphLabel.setText("Error. Arccosh undefined.");
            break;
        case arctanhUndef:
            graphLabel.setText("Error. Arctanh undefined.");
            break;
        case arcsechUndef:
            graphLabel.setText("Error. Arcsech undefined.");
            break;
        case arccschUndef:
            graphLabel.setText("Error. Arccsch undefined.");
            break;
        case arccothUndef:
            graphLabel.setText("Error. Arccoth undefined.");
            break;
        case nonIntegralPowerNegativeArg:
            graphLabel.setText("Error. Negative number to non-integral power.");
            break;
        case functionUndef:
            graphLabel.setText("Error. Function undefined.");
            break;
        case missingArg:
            graphLabel.setText("Error. Missing argument.");
            break;
        case bracketMismatch:
            graphLabel.setText("Error. Bracket mismatch.");
            break;
        case invalidMode:
            graphLabel.setText("Error. Bracket mismatch.");
            break;
        case invalidIntegralParameters:
            graphLabel.setText("Error. Invalid integral arguments.");
            break;
        case invalidSumParameters:
            graphLabel.setText("Error. Invalid sum arguments.");
            break;
        case invalidProductParameters:
            graphLabel.setText("Error. Invalid product arguments.");
            break;
        case invalidRandParameters:
            graphLabel.setText("Error. Invalid rand argument.");
            break;
        case randArgumentTooBig:
            graphLabel.setText("Error. Rand argument too big.");
            break;
        case overflow:
            graphLabel.setText("Error. Overflow.");
            break;
        default:
            break;
        }
    }
}
