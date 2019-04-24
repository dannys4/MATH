package matrixAlg;
import java.util.Random;

/**
 * Creates and manipulates complex numbers in both a + bi and
 *      exponential forms
 * 
 * @author Danny Sharp
 * @version 2018.07.17
 *
 */

public class Complex {
    
    /**
     * Real part of the number this object represents
     */
    private double re;
    
    /**
     * Imaginary part of the number this object represents
     */
    private double im;
    
    /**
     * Modulus of the number this object represents
     */
    private double mod;
    
    /**
     * Argument of the number this object represents
     */
    private double arg;
    
    /**
     * Cutoff for rounding for the purposes of this library
     */
    public static final double ROUNDINGCUTOFF = Math.pow(10, -10);
    
    /**
     * Complex field that represents one as a complex number
     */
    public static final Complex ONE = new Complex(1);
    
    /**
     * Complex field that represents zero as a complex number
     */
    public static final Complex ZERO = new Complex(0);
    
    /**
     * Complex field that represents i as a complex number
     */
    public static final Complex I = new Complex(0, 1, false);
    
    /**
     * Calculation of -2*pi
     */
    public static final double NEGTAU = -2*Math.PI;
    
    /**
     * Calculation of 2*pi
     */
    public static final double TAU = 2*Math.PI;
    
    /**
     * Constructs a Complex object from an a + bi  or mod*exp(i*arg) representation based on a boolean
     * @param x             Either the real part of this number or the modulus
     * @param y             Either the imaginary part of this number or the argument
     * @param exp           Will represent number as mod*exp(i*arg) if true, a + bi if false
     */
    public Complex (double x, double y, boolean exp)
    {
        if (Math.abs(x) < Complex.ROUNDINGCUTOFF && Math.abs(y) < Complex.ROUNDINGCUTOFF)
        {
            this.re = 0;
            this.im = 0;
            this.mod = 0;
            this.arg = 0;
        }
        else if (!exp)
        {
            this.re = x;
            this.im = y;
            this.mod = Math.sqrt(x*x + y*y);
            this.arg = Math.atan2(y, x);
        }
        else
        {
            this.mod = x;
            this.arg = y;
            this.re = x*Math.cos(y);
            this.im = x*Math.sin(y);
        }
    }
    
    /**
     * Constructs a Complex object from a real number
     * @param re                Number to be converted into a Complex object
     */
    public Complex (double re)
    {
        this.re = re;
        this.im = 0;
        this.mod = re;
        this.arg = 0;
    }
    
    /**
     * Constructs a Complex object from a real integer
     * @param re                Number to be represented as a Complex object
     */
    public Complex (int re)
    {
        this((double) re);
    }
    
    /**
     * Returns the positive square root of the complex number this represents
     * @return              Positive square root of this complex number
     */
    public Complex sqrt()
    {
        return new Complex(Math.sqrt(this.mod), this.arg/2, true);
    }
    
    /**
     * Returns this Complex object to the ath power. Recommended that a > 1. If not,
     *      this will give one root of it.
     * @param a             Power this object is raised to
     * @return              This object raised to the ath power
     */
    public Complex pow(double a)
    {
        return new Complex(Math.pow(this.mod, a), this.arg*a, true);
    }
    
    /**
     * Returns this Complex object to the ath power, if a is an integer.
     * @param a             Power this object is raised to
     * @return              This object raised to the ath power
     */
    public Complex pow(int a)
    {
        return this.pow((double) a);
    }
    
    /**
     * Sum of two complex numbers
     * @param b             Complex object added to this object
     * @return              Sum of the two objects
     */
    public Complex add (Complex b)
    {
        return new Complex(b.getRe() + this.re, b.getIm() + this.im, false);
    }
    
    /**
     * Sum of this object with a double
     * @param b         Double being added to this object
     * @return          Sum of this object with b
     */
    public Complex add (double b)
    {
        return new Complex(this.re + b, this.im, false);
    }
    
    /**
     * Difference of two complex numbers
     * @param b             Complex object subtracted from this object
     * @return              Difference of the two objects
     */
    public Complex subtract (Complex b)
    {
        return new Complex(this.re - b.getRe(), this.im - b.getIm(), false);
    }
    
    /**
     * Difference of this object from a double
     * @param b         Double being subtracted from this object
     * @return          Difference of b from this object
     */
    public Complex subtract (double b)
    {
        return new Complex(this.re - b, this.im, false);
    }
    
    /**
     * Product of two complex numbers
     * @param b             Complex object multiplying this object
     * @return              Product of the two objects
     */
    public Complex multiply (Complex b)
    {
        return new Complex(this.mod*b.getMod(), this.arg + b.getArg(), true);
    }
    
    /**
     * Product of this object with a double
     * @param b         Double multiplying this object
     * @return          Product of this object with b
     */
    public Complex multiply (double b)
    {
        return new Complex(b*this.mod, this.arg, true);
    }
    
    /**
     * Quotient of two complex numbers
     * @param b             Complex object dividing this object
     * @return              Quotient of the two objects
     */
    public Complex divide (Complex b)
    {
        return new Complex(this.mod/b.getMod(), this.arg - b.getArg(), true);
    }
    
    /**
     * Quotient of this object with a double
     * @param b         Double dividing this object
     * @return          Quotient of this object with b
     */
    public Complex divide (double b)
    {
        return new Complex(this.re/b, this.im/b, false);
    }
    
    /**
     * Retrieves the real part of this object
     * @return              Real part of this object
     */
    public double getRe()
    {
        return this.re;
    }
    
    /**
     * Retrieves the imaginary part of this object
     * @return              Imaginary part of this object
     */
    public double getIm()
    {
        return this.im;
    }
    
    /**
     * Retrieves the modulus of this object
     * @return              Modulus of this object
     */
    public double getMod()
    {
        return this.mod;
    }
    
    /**
     * Retrieves the argument of this object
     * @return              Argument of this object
     */
    public double getArg()
    {
        return this.arg;
    }
    
    /**
     * Calculates the conjugate of this object
     * @return              Conjugate of this object
     */
    public Complex conjugate()
    {
        return new Complex(re, -1*im, false);
    }
    
    /**
     * Converts this object to a string. Overrides toString() method of the
     *      Java.lang.Object class
     */
    public String toString()
    {
        String ret = "";
        if (Math.abs(re)>Complex.ROUNDINGCUTOFF && Math.abs(im)>Complex.ROUNDINGCUTOFF)
            ret = im > 0? re + " + " + im + "i    " : re + " - " + Math.abs(im) + "i    ";
        else if (Math.abs(re)>Complex.ROUNDINGCUTOFF || Math.abs(im)>Complex.ROUNDINGCUTOFF)
            ret = Math.abs(re)>Complex.ROUNDINGCUTOFF? re + "  " : im + "i ";
        else
            ret = "0    ";
        return ret;
    }
    
    /**
     * Calculates the signum of this object
     * @return              Signum of this object
     */
    public Complex signum()
    {
        return new Complex(1, this.arg, true);
    }
    
    public static double signum(double a)
    {
        return a < -1*Complex.ROUNDINGCUTOFF? -1 : a > Complex.ROUNDINGCUTOFF? 1 : 0;
    }
    
    /**
     * Converts an array of doubles into an array of Complex objects
     * @param arr           Array of doubles being converted into Complex array
     * @return              Array of Complex objects equivalent to the input array
     */
    public static Complex[] toComplexArray(double[] arr)
    {
        Complex[] ret = new Complex[arr.length];
        for (int i = 0; i < arr.length; i++)
            ret[i] = new Complex(arr[i]);
        return ret;
    }
    
    /**
     * Converts an array of integers into an array of Complex objects
     * 
     * @param arr           Array of integers to be converted into Complex objects
     * @return              Equivalent array of Complex objects to the input
     */
    public static Complex[] toComplexArray(int[] arr)
    {
        Complex[] ret = new Complex[arr.length];
        for (int i = 0; i < arr.length; i++)
            ret[i] = new Complex(arr[i]);
        return ret;
    }
    
    /**
     * Converts a 2D array of doubles into an array of Complex objects
     * @param arr           2D array of doubles to be converted into Complex objects
     * @return              2D array of Complex objects equivalent to the input
     */
    public static Complex[][] toComplexArray(double[][] arr)
    {
        Complex[][] ret = new Complex[arr.length][];
        for (int i = 0; i < arr.length; i++)
            ret[i] = Complex.toComplexArray(arr[i]);
        return ret;
    }
    
    /**
     * Converts a 2D array of integers into an array of Complex objects
     * @param arr           2D array of integers to be converted into Complex objects
     * @return              2D array of Complex objects equivalent to the input
     */
    public static Complex[][] toComplexArray(int[][] arr)
    {
        Complex[][] ret = new Complex[arr.length][];
        for (int i = 0; i < arr.length; i++)
            ret[i] = Complex.toComplexArray(arr[i]);
        return ret;
    }
    
    /**
     * Takes the logarithm of any double with any base as a double
     * 
     * @param a             Logarithmic base
     * @param b             Number to take the logarithm of
     * @return              Calculated logarithm of b base a
     */
    public static double log(double a, double b)
    {
        return (Math.log(b))/(Math.log(a));
    }
    
    /**
     * To check if a given number is a power of two
     * 
     * @param a             Input to check
     * @return              Whether a is a power of two
     */
    public static boolean isPowerOfTwo(double a)
    {
        double b = Complex.log(2, a);
        double c = Math.floor(b);
        return Math.abs(b - c) < Complex.ROUNDINGCUTOFF;
    }
    
    /**
     * Find the factorial of an integer
     * 
     * @param a             Integer to take factorial of
     * @return              a!
     */
    public static double factorial(int a)
    {
        if (a < 0)
            throw new RuntimeException();
        if (a < 2)
            return 1;
        double ret = a;
        for (int i = a - 1; i > 0; i--)
            ret *= i;
        return ret;
    }
    
    /**
     * Clones this object
     * 
     * @return Complex     Identical complex number
     */
    public Complex clone() {
        return new Complex(re, im, false);
    }
    
    /**
     * Check if two complex numbers are equal
     * 
     * @param comp2        Another Complex Number
     * @returns boolean    Whether the two are equal
     */
    public boolean equals(Complex comp2) {
        return Math.abs(comp2.getRe() - re) < Complex.ROUNDINGCUTOFF &&
                    Math.abs(comp2.getIm() - im) < Complex.ROUNDINGCUTOFF;
    }
    
    /**
     * Return random real numbers between 0 and 1
     * @return Complex   Random real number between 0 and 1
     */
    public static Complex rand() {
        Random rand = new Random();
        return new Complex(rand.nextDouble());
    }
    
    /**
     * Return random complex numbers with magnitude of 1
     * @return Complex  Random complex number with magnitude 1
     */
    public static Complex randComplex() {
        Random rand = new Random();
        double re = rand.nextDouble();
        double im = rand.nextDouble();
        double mag = Math.sqrt(re*re + im*im);
        re = re/mag;
        im = im/mag;
        return new Complex(re, im, false);
    }
}
