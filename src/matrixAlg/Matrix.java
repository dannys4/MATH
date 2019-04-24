package matrixAlg;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Creates and manipulates matrices as arrays of vectors or 2D arrays
 *      of Complex objects. Zero-based indexing unless noted otherwise
 * 
 * @author danny
 * @version 2018.07.17
 */

public class Matrix {
    
    /**
     * Matrix represented as an array of Vec objects
     */
    private Vec[] matrix;
    
    /**
     * Matrix represented as a 2D array of Complex objects
     */
    private Complex[][] matrixArr;
    
    /**
     * Matrix object representing the orthogonal basis for this matrix
     */
    private Matrix orthogonalMatrix;
    
    /**
     * Matrix object representing the remainder from the orthogonal basis
     */
    private Matrix remainderMatrix;
    
    /**
     * Creates a Matrix with only one vector
     * 
     * @param vector    Vec in matrix
     */
    public Matrix (Vec vector)
    {
        Vec[] tempVec = {vector};
        this.matrix = tempVec;
        Complex[][] tempArr = {vector.toArray()};
        this.matrixArr = tempArr;
    }
    
    /**
     * Creates a Matrix out of an array of vectors
     * 
     * @param matrix    Array of vectors
     */
    public Matrix (Vec[] matrix)
    {
        this.matrix = matrix;
        this.matrixArr = new Complex[matrix.length][matrix[0].length()];
        for (int i = 0; i < matrix.length; i++)
        {
            matrixArr[i] = matrix[i].toArray();
            if (matrixArr[0].length != matrixArr[i].length)
                throw new RuntimeException();
        }
    }
    
    /**
     * Creates a Matrix out of a 2D array of complex numbers
     * 
     * @param matrix    2D array of Complex Numbers
     */
    public Matrix (Complex[][] matrix)
    {
        this.matrixArr = matrix.clone();
        this.matrix = new Vec[matrixArr.length];
        for (int i = 0; i < matrixArr.length; i++)
        {
            this.matrix[i] = new Vec(matrixArr[i]);
            if (this.matrixArr[0].length != this.matrixArr[i].length)
                throw new RuntimeException();
        }
    }
    
    /**
     * Converts a 2D array of doubles to a 2D array of Complex numbers,
     *      then creates a Matrix object from that
     * 
     * @param matrix    2D array of doubles
     */
    public Matrix (double[][] matrix)
    {
        this(Complex.toComplexArray(matrix));
    }
    
    /**
     * The sum of two Matrix objects. They must match dimensions
     * 
     * @param matrix2   Matrix being added
     * @return          The sum of matM and this Matrix object
     */
    public Matrix add (Matrix matrix2)
    {
        
        Vec[] ret = new Vec[matrix.length];
        Vec[] mat2 = matrix2.toVecArray();
        if (matrix.length != mat2.length || matrix[0].length() != mat2[0].length())
            throw new RuntimeException();
        for (int i = 0; i < ret.length; i++)
            ret[i] = matrix[i].add(mat2[i]);
        return new Matrix(ret);
    }

    /**
     * The difference of a second Matrix from this Matrix object.
     *      They must match dimensions.
     * 
     * @param matrix2   Matrix that is subtracted from this Matrix object
     * @return          Difference of matrix2 from this Matrix object
     */
    public Matrix subtract(Matrix matrix2)
    {
        Vec[] ret = new Vec[matrix.length];
        Vec[] mat2 = matrix2.toVecArray();
        if (matrix.length != mat2.length || matrix[0].length() != mat2[0].length())
            throw new RuntimeException("Dimensions must match!");
        for (int i = 0; i < ret.length; i++)
            ret[i] = matrix[i].subtract(mat2[i]);
        return new Matrix(ret);
    }
    
    /**
     * Post-multiplies this Matrix by a second Matrix object. Inner dimensions must match.
     * 
     * @param matrix2       Matrix this Matrix object is being post-multiplied by
     * @return              Product of the two Matrices
     */
    public Matrix multiply(Matrix matrix2)
    {
        Complex[][] mat2 = matrix2.toArray();
        Complex[][] ret = new Complex[mat2.length][this.matrix[0].length()];
        for (int i = 0; i < ret.length; i++)
        {
            for (int j = 0; j < ret[0].length; j++)
            {
                ret[i][j] = Complex.ZERO;
                for (int k = 0; k < this.matrixArr.length; k++)
                    ret[i][j] = ret[i][j].add(this.matrixArr[k][j].multiply(mat2[i][k]));
            }
        }
        return new Matrix(ret);
    }
    
    /**
     * Post-multiplies this Matrix object by a Vec object. The number of columns
     * in the matrix being represented must be equal to the number of elements in the
     * Vec object
     * 
     * @param vecObj        Vec object this Matrix object is being post-multiplied by
     * @return              Product of this Matrix object and the given Vec object
     */
    public Vec multiply(Vec vecObj)
    {
        Complex[] ret = new Complex[matrixArr[0].length];
        Complex[] vecArr = vecObj.toArray();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = Complex.ZERO;
            for (int j = 0; j < vecArr.length; j++)
            {
                ret[i] = ret[i].add(matrixArr[j][i].multiply(vecArr[j]));
            }
        }
        return new Vec(ret);
    }
    
    /**
     * Multiplies every Complex object within this Matrix object by a Complex coefficient
     * 
     * @param coeff         Complex coefficient every object in this Matrix is being multiplied by
     * @return              Product of this Matrix object and the Complex object
     */
    public Matrix multiply(Complex coeff)
    {
        Vec[] ret = new Vec[matrix.length];
        for (int i = 0; i < ret.length; i++)
            ret[i] = this.matrix[i].multiply(coeff);
        return new Matrix(ret);
    }
    
    /**
     * Multiplies every Complex object within this Matrix object by a double coefficient
     * 
     * @param coeff         Double coefficient every object in this Matrix is being multiplied by
     * @return              Product of this Matrix object and the double coefficient
     */
    public Matrix multiply(double coeff)
    {
        Vec[] ret = new Vec[matrix.length];
        for (int i = 0; i < ret.length; i++)
            ret[i] = this.matrix[i].multiply(coeff);
        return new Matrix(ret);
    }
    
    
    /**
     * Returns the transpose of the matrix this object represents
     * 
     * @return              Transpose of the matrix this object represents
     */
    public Matrix transpose()
    {
        Complex[][] transpose = new Complex[matrixArr[0].length][matrixArr.length];
        for (int i = 0; i < matrixArr.length; i++)
        {
            for (int j = 0; j < matrixArr[0].length; j++)
                transpose[j][i] = matrixArr[i][j];
        }
        return new Matrix(transpose);
    }
    
    /**
     * Retrieves the 2D array of Complex objects in this Matrix object
     * 
     * @return              2D array of Complex objects in this Matrix object
     */
    public Complex[][] toArray()
    {
        return this.matrixArr;
    }
    
    /**
     * Retrieves the array of Vec objects in this Matrix object
     * 
     * @return              Array of Vec objects in this Matrix object
     */
    
    public Vec[] toVecArray()
    {
        return this.matrix;
    }
    
    /**
     * Overrides the toString() method of the object class, represents the matrix
     *      this object represents as a string
     * 
     * @return              String containing the matrix this object represents
     */

    public String toString()
    {
        String ret = "";
        for (int i = 0; i < this.matrixArr[0].length; i++)
        {
            for (int j = 0; j < matrixArr.length; j++)
                ret = ret + matrixArr[j][i];
            ret = ret + "\n";
        }
        return ret;
    }
    
    /**
     * Converts this matrix into MATLAB notation
     * 
     * @return              String containing this Matrix object's MATLAB representation
     */
    
    public String toMatlab()
    {
        String ret = "[";
        for (int i = 0; i < this.matrixArr[0].length - 1; i++)
        {
            for (int j = 0; j < matrixArr.length - 1; j++)
                ret += matrixArr[j][i] + ",";
            ret += matrixArr[matrixArr.length - 1][i] + ";";
        }
        for (int j = 0; j < matrixArr.length - 1; j++)
            ret += matrixArr[j][matrixArr[0].length - 1] + ",";
        return ret + matrixArr[matrixArr.length - 1][matrixArr[0].length - 1].toString() + "]";
    }

    /**
     * Appends a second matrix to the bottom of the matrix this object represents. Will
     *      not work if matrices have differing column quantities.
     * 
     * @param matrix2       Matrix object that will be appended to the bottom of this one
     * @return              New Matrix object with this as the (1, 1) block and matrix2
     *                          as the (2, 1) block
     */
    public Matrix appendBottom(Matrix matrix2)
    {
        if (matrix2.toArray().length != matrix.length)
            throw new RuntimeException();
        Vec[] matrix2VecArr = matrix2.toVecArray();
        Vec[] ret = new Vec[matrix.length];
        for (int i = 0; i < matrix.length; i++)
            ret[i] = matrix[i].append(matrix2VecArr[i]);
        return new Matrix(ret);
    }
    
    /**
     * Appends a second matrix to the right of the matrix this object represents. Will
     *      not work if matrices have differing row quantities.
     * 
     * @param matrix2       Matrix object that will be appended to the right of this one
     * @return              New Matrix object with this as the (1, 1) block and matrix2
     *                          as the (1, 2) block
     */
    public Matrix appendRight(Matrix matrix2)
    {
        if (matrix2.toArray()[0].length != matrixArr[0].length)
            throw new RuntimeException();
        Vec[] matrix2VecArr = matrix2.toVecArray();
        Vec[] ret = new Vec[matrix.length + matrix2VecArr.length];
        for (int i = 0; i < ret.length; i++)
            ret[i] = i < matrix.length? matrix[i] : matrix2VecArr[i - matrix.length];
        return new Matrix(ret);
    }

    /**
     * Returns the sub matrix of the current object, inclusive of the numbers. One-
     *      based index.
     * 
     * @param beginRow      First row of the returned matrix
     * @param beginColumn   First column of the returned matrix
     * @param endRow        Last row of the returned matrix
     * @param endColumn     Last column of the returned matrix
     * @return              Sub matrix of current object containing all the rows and columns within the parameters, inclusive
     */
    public Matrix minor(int beginRow, int beginColumn, int endRow, int endColumn)
    {
        Vec[] ret = new Vec[endColumn - beginColumn + 1];
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = matrix[i + beginColumn - 1].clone();
            ret[i] = ret[i].isolateElements(beginRow, endRow);
        }
        return new Matrix(ret);
    }
    
    /**
     * Retrieves the remainder Matrix object from orthogonalization of this Matrix's
     *      orthogonalization only after the corresponding method has been called
     * 
     * @return              Matrix object representing an upper triangular matrix
     *                          created when orthogonalizing this Matrix object
     */
    
    public Matrix getR()
    {
        if (remainderMatrix == null)
            throw new NullPointerException();
        return remainderMatrix;
    }
    
    /**
     * Retrieves the orthogonal Matrix object from orthogonalization of this Matrix's
     *      orthogonalization only after the corresponding method has been called
     * 
     * @return              Matrix object representing the orthogonal basis for
     *                          this Matrix object
     */
    
    public Matrix getQ()
    {
        if (orthogonalMatrix == null)
            throw new RuntimeException();
        return orthogonalMatrix;
    }
    
    /**
     * Helper method for orthogonalization method. Retrieves the submatrix made up of all
     *      but the first row & column of the matrix this object represents
     * 
     * @return              First bottom right minor of this Matrix object
     */
    
    private Matrix firstMinor()
    {
        if (this.matrix.length == 2)
            return new Matrix(this.matrix[1].removeFirstElement());
        else
        {
            Vec[] ret = new Vec[matrix.length - 1];
            for (int i = 0; i < ret.length; i++)
                ret[i] = matrix[i + 1].removeFirstElement();
            return new Matrix(ret);
        }
    }
    
    /**
     * Helper method for orthogonalization method. Increases both dimensions of the matrix
     *      this object represents by one, with all zeros in the new first row & column except
     *      the number 1 at element 1, 1.
     * 
     * @return              Expanded version of this Matrix object
     */
    private Matrix expand()
    {
        Vec[] ret = new Vec[matrix.length + 1];
        ret[0] = Vec.e1(matrixArr[0].length + 1);
        for (int i = 1; i < ret.length; i++)
            ret[i] = matrix[i - 1].expand();
        return new Matrix(ret);
    }
    
    /**
     * Creates a matrix with an orthonormal basis for the matrix this object represents
     *      as its columns, Q,  and one that is upper triangular, R, such that Q*R will
     *      give the matrix this object represents. Only works if the rows of this Matrix
     *      object are greater than or equal to its columns.
     */
    public void orthogonalize()
    {
        if (matrix.length > matrixArr[0].length)
            throw new RuntimeException();
        Vec[] workingMatrix = this.matrix;
        Matrix[] orthogonals = new Matrix[Math.min(matrix.length, matrixArr[0].length - 1)];        
        for (int i = 0; i < orthogonals.length; i++) {
            Vec workingVector = workingMatrix[0];
            Matrix q = Matrix.householderReflection(workingVector);
            if (workingMatrix.length > 1)
                workingMatrix = q.multiply(new Matrix(workingMatrix)).firstMinor().toVecArray();
            for (int j = i; j > 0; j--)
                q = q.expand();
            orthogonals[i] = q;
        }
        
        Matrix qT = orthogonals[0];
        for (int i = 1; i < orthogonals.length; i++)
        {
            qT = orthogonals[i].multiply(qT);
        }
        this.remainderMatrix = qT.multiply(new Matrix(matrix));
        this.orthogonalMatrix = qT.transpose();
    }
    
    /**
     * Solves a system of linear equations, Ax = b, for x where A is the matrix this object
     *      represents, using QR factorization. Can only be done after using the orthogonalize
     *      method, when the system is either critically or overdetermined (which makes it an
     *      approximation), and when the number of rows of A is the number of elements in the
     *      object b
     * 
     * @param b             Right side of the linear system of equations
     * @return              Solution to the linear system of equations
     */
    public Vec solve(Vec b) {
        if (orthogonalMatrix == null)
            this.orthogonalize();
        Complex[][] rem = this.remainderMatrix.toArray();
        Complex[] y = this.orthogonalMatrix.transpose().multiply(b).toArray();
        Complex[] x = new Complex[matrix.length];
        for (int i = x.length - 1; i >= 0; i--)
        {
            Complex ans = y[i];
            for (int j = rem.length - 1; j > i; j--)
            {
                ans = ans.subtract(rem[j][i].multiply(x[j]));
            }
            x[i] = ans.divide(rem[i][i]);
        }
        return new Vec(x);
    }
    
    /**
     * Returns a Householder reflection of a given vector
     * 
     * @param inputVec      Vec object to take the reflection of    
     * @return
     */
    public static Matrix householderReflection(Vec inputVec)
    {
        Complex[] vecArr = inputVec.toArray();
        Complex alpha = (vecArr[0].signum()).multiply(inputVec.norm());
        Vec ret;
        try {
            ret = inputVec.add(Vec.e1(inputVec.length()).multiply(alpha));
            ret = ret.normalize();
            return Matrix.identity(ret.length()).subtract(ret.outerProd(ret.multiply(2)));
        }
        catch (Exception e) {
            System.out.println("Trying to divide by zero");
            throw new RuntimeException("Can't do that");
        }
    }

    /**
     * Create an nxn identity Matrix object
     * 
     * @param n             Number of columns and rows of the identity Matrix object
     * @return              Matrix object representing an nxn identity matrix
     */
    public static Matrix identity(int n)
    {
        if (n < 1)
            throw new RuntimeException();
        Complex[][] eye = new Complex[n][n];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
                eye[i][j] = i == j? Complex.ONE : Complex.ZERO;
        }
        return new Matrix(eye);
    }
    
    /**
     * Create an empty mxn Matrix object
     * 
     * @param m             Row count of the new Matrix object
     * @param n             Column count of the new Matrix object
     * @return              Matrix object of all zeros
     */
    public static Matrix zeros(int m, int n)
    {
        if (n < 1 || m < 1)
            throw new RuntimeException();
        Complex[][] ret = new Complex[n][m];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < m; j++)
                ret[i][j] = Complex.ZERO;
        }
        return new Matrix(ret);
    }

    /**
     * Creates nxn Matrix object made up of twiddle numbers, exp(-2*pi*i/n),
     *      to increasingly large powers
     * @param n             Column and row count of the Fourier Matrix object
     * @return              Fourier Matrix object
     */
    public static Matrix fourierMatrix(int n)
    {
        if (n < 1)
            throw new RuntimeException();
        Complex[][] fourier = new Complex[n][n];
        Complex omega = new Complex(1, (Complex.NEGTAU)/n, true);
        for (int i = 0; i < n; i++)
        {
            fourier[i][i] = omega.pow(Math.pow(i, 2));
            for (int j = 0; j < i; j++)
            {
                fourier[j][i] = omega.pow(j*i);
                fourier[i][j] = fourier[j][i];
            }
        }
        return new Matrix(fourier);
    }
    
    /**
     * Creates a permutation matrix that will switch switchRow1 with
     *      switchRow2 when post multiplied by this Matrix object
     *      or switch the same indexed columns when premultiplied.
     *      One-based index for rows/columns
     * 
     * @param switchRow1            Index of first row/column that will be switched
     * @param switchRow2            Index of second row/column that will be switched
     * @param n                     Size of permutation matrix
     * @return                      Permutation matrix
     */
    public static Matrix permutationMatrix(int switchRow1, int switchRow2, int n)
    {
        if (n < 1 || switchRow1 < 1 || switchRow2 < 1 || switchRow2 > n || switchRow1 > n)
            throw new RuntimeException();
        if (n <= 2)
            return Matrix.identity(n);
        switchRow1--;
        switchRow2--;
        Complex[][] ret = Matrix.identity(n).toArray();
        ret[switchRow1][switchRow1] = Complex.ZERO;
        ret[switchRow2][switchRow2] = Complex.ZERO;
        ret[switchRow1][switchRow2] = Complex.ONE;
        ret[switchRow2][switchRow1] = Complex.ONE;
        return new Matrix(ret);
    }
    
    /**
     * Checks if a matrix is hermitian
     * @return boolean     Whether the matrix is hermitian
     */
    public boolean isHermitian() {
        if (this.matrixArr.length != this.matrixArr[0].length) {
            return false;
        }
        for (int i = 0; i < matrixArr.length; i++) {
            for (int j = i; j < matrixArr[i].length; j++) {
                if (!matrixArr[i][j].equals(matrixArr[j][i].conjugate()))
                    return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if a matrix is upper triangular
     * @return boolean     Whether matrix is upper triangular
     */
    public boolean isUpperTriangle() {
        for (int i = 0; i < matrixArr.length; i++) {
            for (int j = i + 1; j < matrixArr[i].length; j++) {
                if (matrixArr[i][j].getMod() > 100*Complex.ROUNDINGCUTOFF) 
                    return false;
                else
                    matrixArr[i][j] = Complex.ZERO;
            }
        }
        return true;
    }
    
    /**
     * Checks if a matrix is lower triangular
     * @return boolean     Whether matrix is lower triangular
     */
    public boolean isLowerTriangle() {
        for (int j = 0; j < matrixArr[0].length; j++) {
            for (int i = j + 1; i < matrixArr.length; i++) {
                if (matrixArr[i][j].getMod() > 100*Complex.ROUNDINGCUTOFF) 
                    return false;
                else
                    matrixArr[i][j] = Complex.ZERO;
            }
        }
        return true;
    }
    
    /**
     * Solves for the eigenvalues and eigenvectors of a symmetric complex matrix
     * @return Matrix[2]       Contains eigenvalues and eigenvectors
     * Matrix[0], d, is matrix containing eigenvalues on the diagonal
     * Matrix[1], q, is matrix containing the appropriate eigenvectors
     * Given matrix A, this returns Aq = qd
     */
    public Matrix[] powerMethod() {
        if (!this.isHermitian())
            throw new RuntimeException("Cannot use this with a nonsymmetric matrix");
        int n = matrixArr.length;
        Matrix Q = Matrix.identity(n);
        Matrix D = this.clone();
        int i = 0;
        while ( !D.isUpperTriangle() || !D.isLowerTriangle()) {
            D.orthogonalize();
            Matrix Que = D.getQ();
            Q = Q.multiply(Que);
            D = D.getR().multiply(Que);
            i++;
        }
        System.out.println("QR Iterations: " + i);
        Matrix[] ret = {D,Q};
        return ret;
    }
    
    /**
     * Solves for the eigenvalues and eigenvectors of a symmetric complex
     *       matrix using hessenberg transformation initially
     * @return Matrix[2]       Contains eigenvalues and eigenvectors
     * Matrix[0], d, is matrix containing eigenvalues on the diagonal
     * Matrix[1], q, is matrix containing the appropriate eigenvectors
     * Given matrix A, this returns Aq = qd
     */
    public Matrix[] hessPowerMethod() {
        if (!this.isHermitian())
            throw new RuntimeException("Cannot use this with a nonsymmetric matrix");
        Matrix[] hess = this.hessTransform();
        Matrix h = hess[0];
        Matrix q = hess[1];
        Matrix Q = q;
        Matrix D = h;
        int i = 0;
        while ( !D.isUpperTriangle() || !D.isLowerTriangle()) {
            D.orthogonalize();
            Matrix Que = D.getQ();
            Q = Q.multiply(Que);
            D = D.getR().multiply(Que);
            i++;
        }
        System.out.println("Hess QR Iterations: " + i);
        Matrix[] ret = {D,Q};
        return ret;
    }
    
    /**
     * Use similarity transformations to transform
     * this into an upper Hessenberg matrix
     * @return Array of matrices [h,q]
     * h- Upper hessenberg matrix
     * q- orthogonal matrix such that q*Aq = h
     */
    public Matrix[] hessTransform() {
        int m = matrixArr[0].length;
        int n = matrixArr.length;
        if (m != n)
            throw new RuntimeException("Must be square matrix");
        Matrix h = this.clone();
        Matrix q = Matrix.identity(n);
        for (int k = 0; k < n - 2; k++) {
            Vec[] hVec = h.toVecArray();
            Vec reflectorVec = hVec[k].isolateElements(k+2, n);
            Matrix reflector = Matrix.householderReflection(reflectorVec);
            for (int j = 0; j <= k; j++)
                reflector = reflector.expand();
            h = reflector.multiply(h).multiply(reflector);
            q = q.multiply(reflector);
        }
        Matrix[] ret = {h,q};
        return ret;
    }
    
    /**
     * Creates the Singular Value Decomposition (SVD or PCA for short)
     * @returns Matrix[]   Contains an array {u,s,v} of the decomposition where:
     * u:  Unitary matrix containing the orthogonal eigenvectors of AA^T
     * s:  Matrix containing nonzero eigenvalues of AA^T or A^TA
     * v:  Unitary matrix containing the orthogonal eigenvectors of A^TA
     */
    public Matrix[] svd() {
        if(matrix.length > matrix[0].length()) {
            Matrix[] vsu = this.transpose().svd();
            Matrix[] usv = {vsu[2], vsu[1], vsu[0]};
            return usv;
        }
        Matrix ata = this.transpose().multiply(this);
        Matrix[] eigsAta = ata.qrShiftEigs();
        Complex[][] s2 = eigsAta[0].toArray();
        Matrix zeros = Matrix.zeros(s2[0].length, s2.length);
        Complex[][] sArr = zeros.toArray();
        int r = sArr[0].length;
        for(int i = 0; i < sArr.length; i++) {
            Complex si = s2[i][i];
            if(si.equals(Complex.ZERO))  {
                r = i;
                break;
            }
            sArr[i][i] = si.sqrt();
        }
        Matrix s = new Matrix(sArr);
        s = s.minor(1,1,r,r);
        Vec[] vArr = eigsAta[1].toVecArray();
        vArr = Arrays.copyOf(vArr, r);
        Vec[] uArr = new Vec[r];
        for(int i = 0; i < r; i++) {
            uArr[i] = this.multiply(vArr[i]).divide(sArr[i][i]);
        }
        Matrix[] ret = {new Matrix(uArr), s, new Matrix(vArr)};
        return ret;
    }
    
    /**
     * Implementation of the QR algorithm with shifting
     * @returns Matrix[2] d,q. d is diagonal matrix, q is orthogonal where
     * A = q*d*q'
     */
    public Matrix[] qrShiftEigs() {
        Matrix[] hess = this.hessTransform();
        Matrix[] dq = qrShiftEigsHelper(hess[0]);
        Matrix d = dq[0];
        Matrix p = d.sortDiags();
        d = p.transpose().multiply(d).multiply(p);
        Matrix q = hess[1].multiply(dq[1]);
        q = q.multiply(p);
        Matrix[] ret = {d, q};
        return ret;
    }
    
    /**
     * Opposite of expand(), so it adds another zero column and row at ends with
     *     a one in the m+1xn+1 position
     * @return Matrix  Same as this matrix with an extra near empty column and row
     *     1 in m+1xn+1 entry
     */
    private Matrix expandBottom() {
            Vec[] ret = new Vec[matrix.length + 1];
            ret[matrix.length] = Vec.e1(matrix[0].length() + 1).reverse();
            for (int i = 0; i < matrix.length; i++)
                ret[i] = matrix[i].expandBottom();
            return new Matrix(ret);
    }
    
    /**
     * Recursive helper method for qr shift algorithm
     * @param d        Matrix we try to upper-triangularize
     * @return Matrix  Upper triangularized input
     */
    private Matrix[] qrShiftEigsHelper(Matrix d) {
        int n = d.toVecArray().length;
        if (n <= 1) {
            double[] one1 = {1};
            double[][] one2 = {one1};
            Matrix[] ret = {d, new Matrix(one2)};
            return ret;
        }
        Complex[][] dArr = d.toArray();
        double eps = dArr[n-1][n-2].getMod();
        Matrix I = Matrix.identity(n);
        Matrix Q = Matrix.identity(n);
        while (eps > Complex.ROUNDINGCUTOFF) {
            Complex mu = dArr[n-1][n-1];
            Matrix shift = I.multiply(mu);
            Matrix dWShift = d.subtract(shift);
            Matrix[] qr = dWShift.getQR();
            d = qr[1].multiply(qr[0]).add(shift);
            dArr = d.toArray();
            Q = Q.multiply(qr[0]);
            if (eps - dArr[n-1][n-2].getMod() < Complex.ROUNDINGCUTOFF) {
                dArr[n-1][n-2] = Complex.ZERO;
                eps = 0;
            }
            else
                eps = dArr[n-1][n-2].getMod();
        }
        Matrix dPrinciple = d.minor(1, 1, n-1, n-1);
        Matrix[] dq = qrShiftEigsHelper(dPrinciple);
        dq[1] = dq[1].expandBottom();
        Matrix q = Q.multiply(dq[1]);
        Matrix dPrime = dq[0];
        dPrime = dPrime.expandBottom();
        Vec[] newDVecArr = dPrime.toVecArray();
        Vec[] dVecArr = d.toVecArray();
        newDVecArr[dVecArr.length - 1] = dVecArr[dVecArr.length - 1];
        Matrix[] ret = {new Matrix(newDVecArr), q};
        return ret;
    }
    
    /**
     * Decides whether a matrix is of upper-triangular form with one subdiagonal
     * of nonzero entries
     * @return Whether the matrix is "hessenberg"
     */
    public boolean isHessenberg() {
        for (int i = 0; i < matrix.length - 2; i++) {
            for (int j = 2; j < matrixArr[i].length; j++) {
                if (matrixArr[i][j].getMod() > Complex.ROUNDINGCUTOFF) 
                    return false;
            }
        }
        return true;
    }

    /**
     * Returns array of Q,R matrices after orthogonalization
     * @return  Matrix[]       {q,r} array
     */
    public Matrix[] getQR() {
        if (orthogonalMatrix == null)
            this.orthogonalize();
        Matrix[] qr = {orthogonalMatrix, remainderMatrix};
        return qr;
    }
    
    /**
     * Returns an array of the mods of the diagonal entries
     * @returns     double[] of the mods of the diagonal
     */
    public Double[] diagMod() {
        int n = Math.min(matrix.length, matrix[0].length());
        Double[] diags = new Double[n];
        for(int i = 0; i < n; i++) {
              diags[i] = (Double) matrixArr[i][i].getMod();
        }
        return diags;
    }
    
    /**
     * Returns a permutation matrix to sort the 
     * eigenvalues and eigenvectors
     * @returns Matrix   Permutation matrix that sorts eigs
     */
    private Matrix sortDiags() {
        Vec[] I = Matrix.identity(matrix.length).toVecArray();
        Double[] diags = this.diagMod();
        DiagComparator comp = new DiagComparator(diags);
        Integer[] diagInds = comp.makeInds();
        Arrays.sort(diagInds, comp);
        Vec[] perm = new Vec[matrix.length];
        for(int i = 0; i < matrix.length; i++) {
            perm[i] = I[diagInds[i]];
        }
        return new Matrix(perm);
    }
    
    /**
     * Makes deep copy of matrix
     * @return  Matrix      Copy of this matrix
     */
    public Matrix clone() {
        Vec[] mat = new Vec[matrixArr.length];
        for (int i = 0; i < mat.length; i++) {
            mat[i] = matrix[i].clone();
        }
        return new Matrix(mat);
    }
    
    /**
     * Creates a random real matrix
     * @param m                     Rows of new matrix
     * @param n                     Columns of new matrix
     * @return Matrix           Random matrix
     */
    public static Matrix rand(int m, int n) {
        Vec[] mat = new Vec[n];
        for (int i = 0; i < n; i++)
            mat[i] = Vec.rand(m);
        return new Matrix(mat);
    }
    
    /**
     * Creates a random complex matrix
     * @param m                     Rows of the new Matrix
     * @param n                     Columns of new Matrix
     * @return Matrix           Random Complex Matrix
     */
    public static Matrix randComplex(int m, int n) {
        Vec[] mat = new Vec[n];
        for (int i = 0; i < n; i++)
            mat[i] = Vec.randComplex(m);
        return new Matrix(mat);
    }
   private class DiagComparator implements Comparator<Integer>{
        private final Double[] diags; 
        public DiagComparator(Double[] diags) {
            this.diags = diags;
        }
        public Integer[] makeInds() {
            Integer[] inds = new Integer[diags.length];
            for(int i = 0; i < diags.length; i++) {
                inds[i] = i;
            }
            return inds;
        }
        public int compare(Integer ind1, Integer ind2) {
            return -diags[ind1].compareTo(diags[ind2]);
        }
    }
}
