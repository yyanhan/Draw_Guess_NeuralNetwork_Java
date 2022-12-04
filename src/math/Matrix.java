package src.math;
import java.util.Random;
/**
 * operation
 * 
 *
 */
public class Matrix{
	private double matrix[][];
	private int cols;
	private int rows;
	
	/**
	 * convert a matrix double[][]
	 * 
	 * @param double _matrix[][]
	 * @return nothing
	 */
	//matrx from double 2d array
	public Matrix(double _matrix[][]){
		//matrix[row][cols]
		this.matrix = new double[_matrix.length][_matrix[0].length];
		this.cols=_matrix[0].length;
		this.rows=_matrix.length;
		for(int i = 0; i != this.cols; i++){
			for(int p = 0; p != this.rows; p++){
				this.matrix[p][i]=_matrix[p][i];
			}
		}
	}
	/**
	 * create a matrix double[][] with zero
	 * 
	 * @param int int
	 * 
	 */
	//matrix in _rows * _cols and all value are 0
	public Matrix(int _rows , int _cols){
		this.cols = _cols;
		this.rows = _rows;
		this.matrix = new double[this.rows][this.cols];
		for(int i = 0; i != this.rows; i++){
			for(int p = 0; p != this.cols; p++){
				this.matrix[i][p]= 0;
			}
		}
	}
	
	public Matrix(){
		this.cols = 0;
		this.rows = 0;
		this.matrix = new double[0][0];
	}
	
	
	
	//setter and getter
	   /**
     *  @param nothing
    * @return fint
     */
	public int get_rows(){
		return this.rows;
	}
	   /**
     *  @param nothing
    * @return int
     */
	public int get_cols(){
		return this.cols;
	}
	   /**
     *  @param nothing
    * @return double[][]
     */
	public double[][] get_matrix(){
		return this.matrix;
	}
	   /**
     *  @param int
    * @return double[]
     */
	public double[] getRow(int i) {
        return this.matrix[i];
    }
	   /**
     *  @param nothing
    * @return double[]
     */
	public double[] getCol(int i) {
		double[] arr = new double[this.rows];
		for(int p = 0; p != this.rows; p++){
			arr[p]= this.get_matrix()[p][i];
		}
        return arr;
    }
	 /**
     *  @param int int double
    * @return nothing
     */
	public void setValue(int row, int col, double val) {
        this.matrix[row][col] = val;
    }
	   /**
     *  @param row col
    * @return double
     */
	public double getValue(int row, int col) {
		return this.matrix[row][col];
	}
	/**
	 * helpfunction for matMult
	 * @param double[] 
	 * @param double[] 
	 * @return double
	 */
	//operations
		
	private double arraymulsum(double[] arr1, double arr2[]){
		double result = 0;
		if(arr1.length == arr2.length){
			for(int i = 0; i != arr1.length; i++){
				result += arr1[i] * arr2[i];
			}
			return result;
		}
		return result;
	}
	
	/**
	 * multiplication A*B
	 * 
	 * @param A
	 * @param B
	 * @return
	 * @throws Exception
	 */
	//multiply 2 matrix
	public Matrix matMult(Matrix ma) {
		if(this.cols == ma.get_rows()){
			int row = this.rows;
			int col = ma.get_cols();
			Matrix result = new Matrix(row,col);
			for(int i = 0; i != row; i++){
				for(int p = 0; p != col; p++){
					result.setValue(i,p,arraymulsum(this.getRow(i),ma.getCol(p)));
				}
			}
			return result;
		}else{
			throw new IllegalArgumentException("The dimensions of the Matrixs aren't multipliable!");
		}
	}
	
	
	/**
	 * matrix *num
	 * 
	 * @param double[]
	 * @return matrix
	 */
	public Matrix matMultNum(double num) {
			int row = this.rows;
			int col = this.get_cols();
			Matrix result = new Matrix(row,col);
			for(int i = 0; i != row; i++){
				for(int p = 0; p != col; p++){
					result.setValue(i,p,this.get_matrix()[i][p]*num);
				}
			}
			return result;
	}
	/**
	 * matrix * arr
	 * 
	 * @param double[]
	 * @return matrix
	 */
	//matrix * arr#
	public Matrix matMultArr(double[] arr){
		Matrix tem = new Matrix().ArraytoMatrix(arr);
		return tem.matMult(new Matrix(this.get_matrix()));
	}
	/**
	 * add two matrixes a+b
	 * 
	 * @param 
	 * @return nothing
	 */
	//add 2 matrix
	public Matrix matAdd(Matrix mA) {
		if(mA.get_rows() == this.rows && mA.get_cols() == this.cols){
			int row = mA.get_rows();
			int col = mA.get_cols();
			Matrix result = new Matrix(row,col);
			for(int i = 0; i != row; i++){
				for(int p = 0; p != col; p++){
					result.setValue(i,p,this.get_matrix()[i][p]+mA.get_matrix()[i][p]);
				}
			}
			return result;
		}
		throw new IllegalArgumentException("The dimensions of the Matrixs aren't addable!");
	}
	
	/**
	 * print the matrix
	 * @param int[][]
	*/
	//matrix as output 
	public void matOut() {
		int row = this.rows;
		int col = this.cols;
		for(int i = 0; i!=row ; i++){
			for(int p = 0; p!=col; p++){
				System.out.printf("[%.2f] ",this.getValue(i,p));
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
 * sigmoid 
 * 
 * @param int[][]A
 * @return double[][]neu
 */
	//sigmoid of matrix ...
	public Matrix sigmoid() {
		Matrix result = new Matrix(this.get_matrix());
        for (int i = 0; i < this.get_rows(); i++)
            for (int p = 0; p < this.get_cols(); p++)
                result.setValue(i, p,
                        (double) (1.0 / (1.0 + Math.exp(-1 * result.getValue(i,p)))));
        return result;
    }

	/**
	 * add a number to the matrix	
	 * 
	 * @param num
	 * @return matrix
	 */
	
	public Matrix matrix_addNum(double num){
		Matrix matrix = new Matrix(this.get_matrix());
		int cols=this.cols;
		int rows=this.rows;
		for(int i = 0; i != rows; i++){
			for(int p = 0; p != cols; p++){
				matrix.get_matrix()[i][p]+=num;
			}
		}
		return matrix;
	}
	/**
	 *convert array to matrix with row = 1
	 * 
	 * @param double[]
	 * @return matrix
	 */
	
	public  Matrix ArraytoMatrix(double[] a){
		
        double[][] matrixarr = new double[1][a.length];
        for(int i = 0; i < a.length;i++){
            matrixarr[0][i] = a[i];
        }
		Matrix result = new Matrix(matrixarr);
		return result;
    }
	/**
	 *convert array to matrix with col = 1
	 * 
	 * @param double[]
	 * @return matrix
	 */
	
	public  Matrix ArraytoMatrix2(double[] a){
		
        double[][] matrixarr = new double[a.length][1];
        for(int i = 0; i < a.length;i++){
            matrixarr[i][0] = a[i];
        }
		Matrix result = new Matrix(matrixarr);
		return result;
    }
	

	/**
	 * convert matrix to array
	 * 
	 * @param nothing
	 * @return double[]
	 */
	
	public double[] MatrixtoArray(){
		int row = this.rows;
		int col = this.cols;
		int length = row * col;
		double[] result = new double[length];
		int count = 0;
		for(int i = 0; i != row; i++){
			for(int p = 0; p != col; p++){
				result[count]=this.get_matrix()[i][p];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * create a matrix, Rows and columns are random numbers between 0 and 1
	 * 
	 * @param length1 length2
	 * @return double[][]
	 */
	// randomMatrix
	public Matrix randomMatrix(int row, int column){
		Matrix result = new Matrix();
		result.rows = row;
		result.cols = column;
		Random rand = new Random();
		result.matrix = new double[result.rows][result.cols];
		for(int i = 0; i != result.rows; i++){
			for(int p = 0; p != result.cols; p++){
				double plusorminus = rand.nextDouble();
				if(plusorminus > 0.5){
					result.matrix[i][p]= rand.nextDouble();
				}else{
					result.matrix[i][p]= rand.nextDouble() *-1;
				}
			}
		}
		return result;
	}

	/**
	 * create a matrix, Rows and columns are random numbers between 0 and n
	 * 
	 * @param length1 length2 length3
	 * @return matrix
	 */
	public Matrix randomMatrixN(int row, int column, int n){
		Random rand = new Random();
		Matrix result = new Matrix(row, column);
		for(int i = 0; i != result.rows; i++){
			for(int p = 0; p != result.cols; p++){
				double plusorminus = rand.nextDouble();
				if(plusorminus > 0.5){
					result.matrix[i][p]= rand.nextDouble()*n;
				}else{
					result.matrix[i][p]= rand.nextDouble()*n *-1;
				}
			}
		}
		return result;
	}
	/**
	 * Subtraction of two matrices
	 * 
	 * @param matrix
	 * @return
	 * @throws Exception
	 */
	public Matrix matSub(Matrix mA) {
		if(mA.get_rows() == this.rows && mA.get_cols() == this.cols){
			int row = mA.get_rows();
			int col = mA.get_cols();
			Matrix result = new Matrix(row,col);
			for(int i = 0; i != row; i++){
				for(int p = 0; p != col; p++){
					result.setValue(i,p,this.get_matrix()[i][p]-mA.get_matrix()[i][p]);
				}
			}
			return result;
		}
		throw new IllegalArgumentException("The dimensions of the Matrixs aren't addable!");
	}
	/**
	 *transpose a matrix
	 * 
	 * @param nothing
	 * @return matrix
	 *
	 */
	public Matrix transpose(){
		double[][] values = new double[this.cols][this.rows];

        for (int i = 0; i < this.rows; i++) {
            for (int p = 0; p < this.cols; p++) {
                values[p][i] = this.matrix[i][p];
            }
        }
        return new Matrix(values);
	}
	/**
	 * Dot with two matrixes
	 * 
	 * @param matrix
	 * @return nothing
	 * @exception
	 */
	public Matrix Dot(Matrix mA) {
		if(mA.get_rows() == this.rows && mA.get_cols() == this.cols){
			int row = mA.get_rows();
			int col = mA.get_cols();
			Matrix result = new Matrix(row,col);
			for(int i = 0; i != row; i++){
				for(int p = 0; p != col; p++){
					result.setValue(i,p,this.get_matrix()[i][p]*mA.get_matrix()[i][p]);
				}
			}
			return result;
		}
		throw new IllegalArgumentException("The dimensions of the Matrixs aren't addable!");
	}
	/**
	 * Dot with a matrix and number
	 * 
	 * @param double
	 * @return nothing
	 */
	public Matrix Dot(double bla) {
		Matrix result = new Matrix(this.matrix);
		for(int i = 0; i != result.rows; i++){
			for(int p = 0; p != result.cols; p++){
				result.setValue(i,p,this.get_matrix()[i][p]* bla);
			}
		}
		return result;
	}
	/**
	 * sigmoid function
	 * 
	 */
	public Matrix sigmoidDerivation(){

        Matrix sig = new Matrix(this.matrix).sigmoid();
        Matrix m = new Matrix(this.get_rows(), this.get_cols());

        for (int i = 0; i < m.get_rows(); i++)
            for (int p = 0; p < m.get_cols(); p++)
                m.setValue(i, p, 1 - sig.getValue(i, p));

        return sig.matMult(m.transpose());
    }
}