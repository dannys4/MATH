package matrixAlg;

/**
 * Creates and manipulates column vectors stored as an array of Complex
 *      objects
 * 
 * @author Danny Sharp
 * @version 2018.07.17
 */

public class Vec {
    
    /**
     * Array of Complex objects that represent the numbers in the Vec object
     */
    private Complex[] vector;
    
    /**
     * Array of prime numbers to use for the FFT method
     */
    private static final int[] PRIMES = {1, 2, 3, 5, 7, 11, 13, 17, 19, 23};
    
    /**
     * Construct a Vec object out of an array of Complex objects
     * 
     * @param vector        Representation of vector for this object
     */
    public Vec (Complex[] vector)
    {
        this.vector = vector;
    }
    
    /**
     * Construct a Vec object out of an array of doubles
     * 
     * @param vector        Representation of Vec object
     */
    public Vec (double[] vector)
    {
        this(Complex.toComplexArray(vector));
    }
    
    /**
     * Construct a Vec object out of an array of integers
     * 
     * @param vector        Representation of Vec object
     */
    public Vec (int[] vector)
    {
        this(Complex.toComplexArray(vector));
    }
    
    /**
     * Return the Euclidean norm of this Vec object
     * 
     * @return              Euclidean norm of this Vec object
     */
    public double norm()
    {
        double sumOfSquares = 0;
        for (int i = 0; i < vector.length; i++)
            sumOfSquares = sumOfSquares + vector[i].getMod()*vector[i].getMod();
        return Math.sqrt(sumOfSquares);
    }
    
    /**
     * Return a vector in the same direction as this object
     *      with magnitude one
     * 
     * @return              Unitized version of this Vec object
     */
    public Vec normalize() 
    {
        return this.divide(this.norm());
    }
    
    /**
     * Subtract a Vec object of the same dimension from this
     *      Vec object
     * 
     * @param vector2       Vec object being subtracted
     * @return              Difference of the two Vec objects
     */
    public Vec subtract(Vec vector2)
    {
        if (vector2.length() != vector.length)
            throw new RuntimeException();
        Complex[] vector2Arr = vector2.toArray();
        Complex[] ret = new Complex[vector2Arr.length];
        for (int i = 0; i < vector2Arr.length; i++)
        {
            ret[i] = vector[i].subtract(vector2Arr[i]);
        }
        return new Vec(ret);
    }
    
    /**
     * Add this object with another Vec object of the same dimension
     * 
     * @param vector2       Object being added to this object
     * @return              The sum of the two objects
     */
    public Vec add(Vec vector2)
    {
        if (vector2.length() != vector.length)
            throw new RuntimeException();
        Complex[] vector2Arr = vector2.toArray();
        Complex[] ret = new Complex[vector2Arr.length];
        for (int i = 0; i < vector2Arr.length; i++)
        {
            ret[i] = vector[i].add(vector2Arr[i]);
        }
        return new Vec(ret);
    }
    
    /**
     * Multiply the entire Vec object by a Complex coefficient
     * 
     * @param coeff         Complex object multiplying every element
     * @return              Vec object scaled up by a coefficient
     */
    public Vec multiply(Complex coeff)
    {
        Complex[] ret = new Complex[vector.length];
        for (int i = 0; i < vector.length; i++)
        {
            ret[i] = vector[i].multiply(coeff);
        }
        return new Vec(ret);
    }
    
    /**
     * Multiply every number within the vector this object represents by
     *      a double
     * 
     * @param coeff         Constant multiplier of the vector
     * @return              Vec object after this has been multiplied
     */
    public Vec multiply(double coeff)
    {
        Complex[] ret = new Complex[vector.length];
        for (int i = 0; i < vector.length; i++)
        {
            ret[i] = vector[i].multiply(coeff);
        }
        return new Vec(ret);
    }
    
    /**
     * Divide every number within the vector this object represents by
     *      a nonzero complex number, represented by an object
     * @param coeff         Complex object that multiplies every element of this    
     * @return              The quotient of every element in this with the
     *                          coefficient
     */
    public Vec divide(Complex coeff)
    {
        if (coeff.getMod() < Complex.ROUNDINGCUTOFF)
            throw new ArithmeticException();
        Complex[] ret = new Complex[vector.length];
        for (int i = 0; i < vector.length; i++)
            ret[i] = vector[i].divide(coeff);
        return new Vec(ret);
    }
    
    /**
     * Divide every number within the vector this object represents by
     *      a nonzero double
     * @param coeff         Double which divides every element in this
     * @return              Vec of every element in this divided by
     *                          the coefficient
     */
    public Vec divide(double coeff)
    {
        if (Math.abs(coeff) < Complex.ROUNDINGCUTOFF)
            throw new ArithmeticException();
        Complex[] ret = new Complex[vector.length];
        for (int i = 0; i < vector.length; i++)
            ret[i] = vector[i].divide(coeff);
        return new Vec(ret);
    }
    
    /**
     * Retrieve the Complex object array contained in this Vec object
     * 
     * @return              This object in array form
     */
    public Complex[] toArray()
    {
        return vector;
    }
    
    /**
     * Find the inner product of the vector this represents and another
     *      vector of the same dimension
     * 
     * @param vector2       Second Vec object in the inner product
     * @return              Inner product of this with another object
     */
    public Complex innerProd(Vec vector2)
    {
        if (vector2.length() != vector.length)
            throw new RuntimeException();
        Complex[] vector2Arr = vector2.toArray();
        Complex sum = Complex.ZERO;
        for (int i = 0; i < vector2Arr.length; i++)
            sum = sum.add(vector2Arr[i].multiply(vector[i].conjugate()));
        return sum;
    }
    
    /**
     * Find the outer product of this Vec object with another Vec
     *      object of the any dimension
     * @param secVec
     * @return
     */
    public Matrix outerProd(Vec secVec)
    {
        Complex[] vec2 = secVec.toArray();
        Vec[] ret = new Vec[vec2.length];
        for (int i = 0; i < ret.length; i++)
        {
            Complex[] loopVec = new Complex[vector.length];
            for (int j = 0; j < vector.length; j++)
                loopVec[j] = vec2[i].conjugate().multiply(vector[j]);
            ret[i] = new Vec(loopVec);
        }
        return new Matrix(ret);
    }
    
    /**
     * Isolate the elements between the a and b elements of this Vec
     *      object, inclusive. One based indexing. b must be greater than
     *      a which must be greater than zero
     * 
     * @param a             Current index of the new Vec's first element
     * @param b             Current index of the new Vec's last element
     * @return              Vec containing only elements between a & b
     */
    public Vec isolateElements(int a, int b)
    {
        if (b < a || a < 1)
            throw new RuntimeException();
        Complex[] ret = new Complex[b - a + 1];
        for (int i = 0; i < ret.length; i++)
            ret[i] = vector[i + a - 1].clone();
        return new Vec(ret);
    }

    /**
     * Return an object representing if the first element were excised from
     *      this object, assuming this is larger than one element
     * 
     * @return              Shortened version of this Vec object
     */
    public Vec removeFirstElement()
    {
        if (vector.length == 1)
            throw new RuntimeException();
        Complex[] ret = new Complex[vector.length - 1];
        for (int i = 0; i < ret.length; i++)
            ret[i] = vector[i + 1].clone();
        return new Vec(ret);
    }
    
    /**
     * Create a new Vec object with all the same elements shifted down
     *      one index and the new first element being a zero
     * 
     * @return              Expanded new Vec object
     */
    protected Vec expand() {
        Complex[] ret = new Complex[vector.length + 1];
        ret[0] = Complex.ZERO;
        for (int i = 1; i < ret.length; i++)
            ret[i] = vector[i - 1].clone();
        return new Vec(ret);
    }
    
    /**
     * Create a new Vec object with all the same elements but a zero at the
     *     last index
     *  @return            Expanded new Vec object
     */
    protected Vec expandBottom() {
        Complex[] ret = new Complex[vector.length + 1];
        for (int i = 0; i < vector.length; i++)
            ret[i] = vector[i].clone();
        ret[vector.length] = Complex.ZERO;
        return new Vec(ret);
    }
    
    /**
     * Reverses a vector
     * 
     * @return         returns an upside-down vector
     */
    protected Vec reverse() {
        Complex[] ret = new Complex[vector.length];
        for (int i = 0; i < vector.length; i++)
            ret[i] = vector[vector.length-i-1].clone();
        return new Vec(ret);
    }
    
    /**
     * Override Java.lang.Object class toString method
     */
    public String toString()
    {
        String ret = "";
        for (int i = 0; i < vector.length - 1; i++)
            ret = ret + vector[i] + "\n";
        return ret + vector[vector.length - 1];
    }
    
    /**
     * Creates a string that can be pasted into MATLAB and used
     * 
     * @return MATLAB code for a column vector
     */
    
    public String toMatlab()
    {
        String ret = "[";
        for (int i = 0; i < vector.length - 1; i++)
            ret += vector[i] + ";";
        ret += vector[vector.length - 1] + "]";
        return ret;
    }
    
    /**
     * Create an elementary Vec object with a one as the first element
     *      and zeros elsewhere. rows must be greater than zero
     * @param rows
     * @return
     */
    public static Vec e1 (int rows)
    {
        if (rows < 1)
            throw new RuntimeException();
        Complex[] e1 = new Complex[rows];
        e1[0] = Complex.ONE;
        for (int i = 1; i < e1.length; i++)
            e1[i] = Complex.ZERO;
        return new Vec(e1);
    }
    
    /**
     * Create column vector of zeros
     * @param n  Dimension of vector
     * @return Vec  Vector of all zeros
     */
    public static Vec zeros(int n) {
        if (n < 1)
            throw new RuntimeException("n must be a natural number");
        Complex[] ret = new Complex[n];
        for (int i = 0; i < n; i++)
            ret[i] = Complex.ZERO;
        return new Vec(ret);
    }
    
    /**
     * Retrieve the length of this Vec object
     * 
     * @return              Length of this Vec object
     */
    public int length()
    {
        return vector.length;
    }
    
    /**
     * Calculate the Discrete Fourier Transform of this Vec object
     * 
     * @return              This Vec object represented in the spectral
     *                          domain
     */
    private Complex[] DFT()
    {
        Complex[] dft = new Complex[vector.length];
        int N = dft.length;
        for (int freq = 0; freq <= N - 1; freq++)
        {
            dft[freq] = Complex.ZERO;
            for (int n = 0; n <= N - 1; n++)
                dft[freq] = dft[freq].add((new Complex(1, -2*Math.PI*freq*n/N, true)).multiply(vector[n]));
        }
        return dft;
    }
    
    /**
     * Calculate the Inverse Discrete Fourier Transform of this Vec
     *      object
     * 
     * @return              Vec object representing this in the time
     *                          domain
     */
    private Complex[] IDFT()
    {
        Complex[] idft = new Complex[vector.length];
        int N = idft.length;
        for (int n = 0; n <= N - 1; n++)
        {
            idft[n] = Complex.ZERO;
            for (int freq = 0; freq <= N - 1; freq++)
                idft[n] = idft[n].add((new Complex(1, 2*Math.PI*freq*n/N, true)).multiply(vector[freq]));
        }
        return idft;
    }
    
    /**
     * Zero pad the front end of this Vec object until the size is the
     *      smallest power of two greater than this Vec object's current
     *      length
     * 
     * @return              Zero padded version of this vector
     */
    public Vec powerOfTwoExpansion()
    {
        double adjustment = Complex.log(2, vector.length);
        adjustment = vector.length*(Math.pow(2, Math.ceil(adjustment) - adjustment) - 1);
        Vec ret = new Vec(vector);
        for (int i = 0; i < adjustment; i++)
            ret = ret.expand();
        return ret;
    }
    
    /**
     * Calculate the Discrete Fourier Transform of this using the Fourier
     *      Matrix
     * 
     * @return              This Vec object represented in the spectral
     *                          domain
     */
    public Complex[] matrixDFT()
    {
        int N = vector.length;
        return Matrix.fourierMatrix(N).multiply(this).toArray();
    }
    
    /**
     * Append a second Vec object to the end of this object
     * 
     * @param vector2           Vec object being appended to this
     * @return                  Larger Vec object representing both
     *                              vectors
     */
    public Vec append(Vec vector2)
    {
        Complex[] vector2Arr = vector2.toArray();
        Complex[] ret = new Complex[vector.length + vector2Arr.length];
        for (int i = 0; i < ret.length; i++)
            ret[i] = i < vector.length? vector[i] : vector2Arr[i - vector.length];
        return new Vec(ret);
    }
    
    /**
     * Calculate the Fourier Transform using a Radix-2 Cooley-Tukey based
     *      algorithm.
     * 
     * @return              This Vec object represented in the spectral
     *                          domain
     */
    private Complex[] baseTwoFFT()
    {
        if (vector.length == 2)
        {
            return this.DFT();
        }
        else
        {
            int N = vector.length;
            Vec w = this.skip(2);
            Complex twiddle = new Complex(1, Complex.NEGTAU/N, true);
            Vec evens = w.isolateElements(1, N/2);
            Vec odds = w.isolateElements(N/2 + 1, N);
            Complex[] evenFT = evens.baseTwoFFT();
            Complex[] oddFT = odds.baseTwoFFT();
            Complex[] ret = new Complex[N];
            for (int i = 0; i < N/2; i++)
            {
                ret[i] = evenFT[i].add(twiddle.pow(i).multiply(oddFT[i]));
                ret[i + evenFT.length] = evenFT[i].subtract(twiddle.pow(i).multiply(oddFT[i]));
            }
            return ret;
        }
    }

    /**
     * Calculate the Inverse Fourier Transform using a Radix-2 Cooley-
     *      Tukey based algorithm
     * 
     * @return              This Vec object represented in the time
     *                          domain
     */
    private Complex[] baseTwoIFFT()
    {
        if (vector.length == 2)
        {
            return this.IDFT();
        }
        else
        {
            int N = vector.length;
            Vec w = this.skip(2);
            Complex twiddle = new Complex(1, Complex.TAU/N, true);
            Vec evens = w.isolateElements(1, N/2);
            Vec odds = w.isolateElements(N/2 + 1, N);
            Complex[] evenIFT = evens.baseTwoIFFT();
            Complex[] oddIFT = odds.baseTwoIFFT();
            Complex[] ret = new Complex[N];
            for (int i = 0; i < N/2; i++)
            {
                ret[i] = evenIFT[i].add(twiddle.pow(i).multiply(oddIFT[i]));
                ret[i + evenIFT.length] = evenIFT[i].subtract(twiddle.pow(i).multiply(oddIFT[i]));
            }
            return ret;
        }
    }
    
    /**
     * Calculate the Fourier Transform using a variable-radix Cooley-
     *      Tukey based algorithm
     * 
     * @return              This Vec object represented in the
     *                          spectral domain
     */
    private Complex[] genericFFT()
    {
        boolean breakOut1 = false;
        boolean breakOut2 = false;
        int i = 0;
        if (vector.length != 2 && Complex.isPowerOfTwo(vector.length))
            return this.baseTwoFFT();
        
        while (!breakOut1 && i < PRIMES.length)
        {
            breakOut1 = vector.length == PRIMES[i];
            i++;
        }
        
        if (breakOut1)
            return this.DFT();
        i = 0;
        while (!breakOut2 && i < PRIMES.length)
        {
            i++;
            breakOut2 = (vector.length % PRIMES[i]) == 0;
        }
        if (!breakOut2)
            return this.DFT();
        else
            return this.generalFFT(PRIMES[i]);
    }
    
    /**
     * Helper method for Vec.genericFFT()
     * 
     * @param S             Radix of algorithm
     * @return              This Vec object represented in the
     *                          spectral domain
     */
    private Complex[] generalFFT(int S)
    {
        int N = vector.length;
        Vec w = this.skip(S);
        int NS = N/S;
        Complex[][] fourierTransforms = new Complex[S][];
        for (int i = 0; i < S; i++)
            fourierTransforms[i] = w.isolateElements((i*NS) + 1, (i + 1)*NS).genericFFT();
        
        Complex[] ret = new Complex[N];
        for (int j = 0; j < S; j++)
        {
            for (int k = 0; k < NS; k++)
            {
                int kpjNS = (int) k + j*N/S;
                ret[kpjNS] = Complex.ZERO;
                Complex twiddle = new Complex(1, (Complex.NEGTAU)*kpjNS/N, true);
                for (int s = 0; s < S; s++)
                    ret[kpjNS] = ret[kpjNS].add(twiddle.pow(s).multiply(fourierTransforms[s][k]));
            }
        }
        return ret;
    }
    
    /**
     * Calculate the Inverse Fourier Transform using a variable-radix
     *      Cooley-Tukey based algorithm
     * 
     * @return              This Vec object represented in the time
     *                          domain
     */
    private Complex[] genericIFFT()
    {
        boolean breakOut1 = false;
        boolean breakOut2 = false;
        int i = 0;
        if (vector.length < 1)
            throw new RuntimeException();
        
        if (vector.length != 2 && Complex.isPowerOfTwo(vector.length))
            return this.baseTwoIFFT();
        
        while (!breakOut1 && i < PRIMES.length)
        {
            breakOut1 = vector.length == PRIMES[i];
            i++;
        }
        if (breakOut1)
            return this.IDFT();
        i = 0;
        while (!breakOut2 && i < PRIMES.length)
        {
            i++;
            breakOut2 = (vector.length % PRIMES[i]) == 0;
        }
        if (!breakOut2)
            return this.IDFT();
        else
            return this.generalIFFT(PRIMES[i]);
    }
    
    /**
     * Helper method for Vec.genericIFFT
     * 
     * @param S             Radix of algorithm
     * @return              This Vec object represented in the time
     *                          domain
     */
    private Complex[] generalIFFT(int S)
    {
        int N = vector.length;
        Vec w = this.skip(S);
        int NS = N/S;
        Complex[][] invFourierTransforms = new Complex[S][];
        
        for (int i = 0; i < S; i++)
            invFourierTransforms[i] = w.isolateElements((i*NS) + 1, (i + 1)*NS).genericIFFT();
        
        Complex[] ret = new Complex[N];
        for (int j = 0; j < S; j++)
        {
            for (int k = 0; k < NS; k++)
            {
                int kpjNS = (int) k + j*N/S;
                ret[kpjNS] = Complex.ZERO;
                Complex twiddle = new Complex(1, (Complex.TAU)*kpjNS/N, true);
                for (int s = 0; s < S; s++)
                    ret[kpjNS] = ret[kpjNS].add(twiddle.pow(s).multiply(invFourierTransforms[s][k]));
            }
        }
        return ret;
    }
    
    /**
     * Retrieves the Fourier Transform of this Vec object
     * 
     * @return              Fourier Transform of this Vec object
     */
    public Vec FFT()
    {
        if (Complex.isPowerOfTwo(vector.length))
            return new Vec(this.baseTwoFFT());
        else
            return new Vec(this.genericFFT());
    }
    
    /**
     * Retrieves the Inverse Fourier Transform of this Vec object
     * 
     * @return              Inverse Fourier Transform of this Vec
     *                          object
     */
    public Vec IFFT()
    {
        if (Complex.isPowerOfTwo(vector.length))
            return new Vec(this.baseTwoIFFT()).divide(vector.length);
        else
            return new Vec(this.genericIFFT()).divide(vector.length);
    }
    
    /**
     * Creates a rearranged version of this Vec object. S := 2
     *      would give the bit-reversed order, for example.
     * 
     * @param S         Radix of this Vec object's length
     * @return          Rearranged version of this Vec object
     */
    private Vec skip(int S)
    {
        int N = vector.length;
        int NS = N/S;
        Complex[][] reArr = new Complex[S][NS];
        for (int i = 0; i < S; i++)
        {
            for (int j = 0; j < NS; j++)
                reArr[i][j] = vector[j*S + i];
        }
        Vec[] vecArr = (new Matrix(reArr)).toVecArray();
        Vec ret = vecArr[0];
        for (int i = 1; i < S; i++)
        {
            ret = ret.append(vecArr[i]);
        }
        return ret;
    }
    
    /**
     * Creates a deep copy of this vector
     * 
     * @return Vec      Vec identical to this
     */
    public Vec clone() {
        Complex[] vec = new Complex[vector.length];
        for (int i = 0; i < vec.length; i++)
            vec[i] = vector[i].clone();
        return new Vec(vec);
    }
    
    /**
     * Creates a random real vector using Complex.rand
     * @param length    Length of the vector
     * @return Vec       Random real vector
     */
    public static Vec rand(int length) {
        Complex[] vec = new Complex[length];
        for (int i = 0; i < length; i++) {
            vec[i] = Complex.rand();
        }
        return new Vec(vec);
    }
    
    /**
     * Creates a random complex vector using Complex.randComplex
     * @param length    Length of the vector
     * @return Vec       Random complex vector
     */
    public static Vec randComplex(int length) {
        Complex[] vec = new Complex[length];
        for (int i = 0; i < length; i++) {
            vec[i] = Complex.randComplex();
        }
        return new Vec(vec);
    }
}

