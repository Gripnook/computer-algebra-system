# Computer Algebra System
An advanced graphing calculator developed as a port of [cas](https://github.com/Gripnook/cas) to Java in order to use JavaFX for UI.

## Usage

The program can be executed by simply running the `ComputerAlgebraSystem.jar` program. Note that this requires a local JRE to be installed.

## Features

The calculator has two modes, standard mode and graphing mode, which can be toggled in the view tab.
In standard mode, the calculator computes numeric results. In graphing mode, the calculator uses an ASCII graph to display functions of the independent variable x. Note that this graphing method is slow and inefficient and will only work well for simple functions.

The calculator also supports scientific and engineering exponential formats, with a number of significant figures from 1 to 18.

<<<<<<< HEAD
Predefined functions:
=======
###### Predefined functions:
>>>>>>> 75cac639fe3acd140569cdd8b2b284e93cda1a69

```
+, -, *, /, %, ^ : Arithmetic operators.

abs(x) : Absolute value function.

√(x)
sqrt(x) : Square root function.

exp(x) : Natural exponential function. Not functional. Use the ^ operator instead.

ln(x) : Natural logarithm function.

sin(x), cos(x), tan(x), sec(x), csc(x), cot(x) : Trigonometric functions.

arcsin(x), arccos(x), arctan(x)
arcsec(x), arccsc(x), arccot(x) : Inverse trigonometric functions.

sinh(x), cosh(x), tanh(x), sech(x), csch(x), coth(x) : Hyperbolic trigonometric functions.

arcsinh(x), arccosh(x), arctanh(x)
arcsech(x), arccsch(x), arccoth(x) : Inverse hyperbolic trigonometric functions.

∫(f(x), a, b)
integral(f(x), a, b) : Definite integral of f(x) from x = a to x = b.

Σ(f(x), a, b)
sum(f(x), a, b) : Sum of f(x) from x = a to x = b.

Π(f(x), a, b)
product(f(x), a, b) : Product of f(x) from x = a to x = b.

rand(n) : Random number in the range [0, n), where n is an integer.
```

<<<<<<< HEAD
Special characters:
=======
###### Special characters:
>>>>>>> 75cac639fe3acd140569cdd8b2b284e93cda1a69

```
e     : 2.71828
π, pi : 3.14159
x     : Independent variable for function evaluation.
ans   : Previous answer.
```
