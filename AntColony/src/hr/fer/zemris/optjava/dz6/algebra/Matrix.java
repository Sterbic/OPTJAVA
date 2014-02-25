package hr.fer.zemris.optjava.dz6.algebra;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Klasa koja modelira matricu realnih brojeva uz popratne metode
 * @author Luka Sterbic
 * @version 0.3
 */
public class Matrix {
	
	private static final double EPSILON = 1E-9;
	
	private int rows;
	private int cols;
	private double[][] matrix;
	private boolean isDecomposed;
	
	/**
	 * Stvori jedinicnu matricu zadane dimenzije
	 * @param n dimenzija matrice
	 */
	public Matrix(int n) {
		this(n, n);
		
		for(int i = 0; i < n; i++) {
			matrix[i][i] = 1;
		}
	}
	
	/**
	 * Stvori matricu zadanih dimenzija, elementi matrice su u nuli
	 * @param rows broj redaka
	 * @param cols broj stupaca
	 */
	public Matrix(int rows, int cols) {
		initRowsAndCols(rows, cols);
		this.matrix = new double[rows][cols];
	}
	
	/**
	 * Stvori matricu iz 2D polja realnih brojeva
	 * @param matrix 2D polje doubleova
	 */
	public Matrix(double[][] matrix) {
		if(matrix == null) {
			throw new IllegalArgumentException("Polje podataka ne smije biti null!");
		}
		
		if(matrix.length < 1 || matrix[0].length < 1) {
			throw new IllegalArgumentException("Polje podataka je lose formatirano!");
		}
		
		this.isDecomposed = false;
		this.rows = matrix.length;
		this.cols = matrix[0].length;		
		this.matrix = matrix;
	}
	
	/**
	 * Stvori matricu iz zadane matrice, dobiva se prava kopija matrice
	 * @param copyFrom zadana matrica
	 */
	public Matrix(Matrix copyFrom) {
		this(copyFrom.rows, copyFrom.cols);
		this.isDecomposed = copyFrom.isDecomposed;
		
		for(int i = 0; i < this.rows; i++) {
			for(int j = 0; j < this.cols; j++) {
				this.matrix[i][j] = copyFrom.matrix[i][j];
			}
		}
	}
	
	/**
	 * Stvori stupcastu matricu iz vektora realnih brojeva
	 * @param vector vektor realnih brojeva
	 */
	public Matrix(double[] vector) {
		if(vector == null) {
			throw new IllegalArgumentException("Vektor ne smije biti null!");
		}
		
		initRowsAndCols(vector.length, 1);
		
		this.matrix = new double[vector.length][1];
		for(int i = 0; i < vector.length; i++) {
			this.matrix[i][0] = vector[i];
		}
	}
	
	/**
	 * Vrati broj redaka
	 * @return broj redaka
	 */
	public int getRows() {
		return rows;
	}
	
	/**
	 * Vrati broj stupaca
	 * @return broj stupaca
	 */
	public int getCols() {
		return cols;
	}
	
	/**
	 * Promijeni velicinu matrice, ako je nova matrica veca od
	 * originalne ne definirani elementi ce biti postavljeni na 0
	 * @param rows novi broj redaka
	 * @param cols novi broj stupaca
	 */
	public void resize(int rows, int cols) {
		if(rows < 1 || cols < 1) {
			throw new IllegalArgumentException("Broj redaka i stupaca mora biti veci od 1!");
		}
		
		double[][] newMatrix = new double[rows][cols];
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				newMatrix[i][j] = matrix[i][j];
			}
		}
		
		matrix = newMatrix;
	}
	
	/**
	 * Vrati element na zadanom indeksu
	 * @param row indeks retka
	 * @param col indeks stupca
	 * @return element na [row, col]
	 */
	public double get(int row, int col) {
		checkRowIndex(row);
		checkColumnIndex(col);
		
		return matrix[row][col];
	}
	
	/**
	 * Postavi vrijednost elementa na zadanom indeksu
	 * @param row indeks retka
	 * @param col indeks stupca
	 * @param value nova vrijednost elementa
	 */
	public void set(int row, int col, double value) {
		checkRowIndex(row);
		checkColumnIndex(col);
		
		matrix[row][col] = value;
	}
	
	/**
	 * Povecaj vrijednost elementa na zadanom indeksu
	 * @param row indeks retka
	 * @param col indeks stupca
	 * @param value nova vrijednost elementa
	 */
	public void setAdd(int row, int col, double value) {
		checkRowIndex(row);
		checkColumnIndex(col);
		
		matrix[row][col] += value;
	}
	
	/**
	 * Izvadi stupac matrice kao zasebnu matricu
	 * @param col indeks stupca
	 * @return stupcasta matrica
	 */
	public Matrix columnAsMatrix(int col) {
		checkColumnIndex(col);
		
		Matrix m = new Matrix(rows, 1);
		
		for(int i = 0; i < rows; i++) {
			m.matrix[i][0] = matrix[i][col];
		}
		
		return m;
	}
	
	/**
	 * Vrati zadani stupac kao vektor realnih brojeva
	 * @param col index stupca
	 * @return polje doubleova
	 */
	public double[] columnAsArray(int col) {
		checkColumnIndex(col);
		double[] array = new double[rows];
		
		for(int i = 0; i < rows; i++) {
			array[i] = matrix[i][col];
		}
		
		return array;
	}
	
	/**
	 * Kopiraj elemente iz stupcaste matrice u zadani stupac
	 * @param columnMatrix stupcasta matrica
	 * @param col indeks stupca
	 */
	public void setColumn(Matrix columnMatrix, int col) {
		checkColumnIndex(col);
		
		if(columnMatrix == null) {
			throw new IllegalArgumentException("Matrica ne smije biti null!");
		}
		
		if(columnMatrix.cols != 1 || columnMatrix.rows != rows) {
			throw new IllegalArgumentException("Matirce nemaju kompatibilne dimenzije!");
		}
		
		for(int i = 0; i < rows; i++) {
			matrix[i][col] = columnMatrix.matrix[i][0];
		}
	}
	
	/**
	 * Zbroji ovu matricu s drugom matricom,
	 * mijenja stanje ove matice
	 * @param other druga matrica
	 * @return this
	 */
	public Matrix addToThis(Matrix other) {
		if(rows != other.rows || cols != other.cols) {
			throw new IllegalArgumentException("Matirce nemaju kompatibilne dimenzije!");
		}
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				matrix[i][j] += other.matrix[i][j];
			}
		}
		
		return this;
	}
	
	/**
	 * Zbroji ovu matricu s drugom matricom,
	 * ne mijenja stanje ove matice
	 * @param other druga matrica
	 * @return nova matrica
	 */
	public Matrix add(Matrix other) {
		return this.copy().addToThis(other);
	}
	
	/**
	 * Oduzmi zadanu matricu od ove matrice,
	 * mijenja stanje ove matice
	 * @param other druga matrica
	 * @return this
	 */
	public Matrix subtractToThis(Matrix other) {
		if(rows != other.rows || cols != other.cols) {
			throw new IllegalArgumentException("Matirce nemaju kompatibilne dimenzije!");
		}
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				matrix[i][j] -= other.matrix[i][j];
			}
		}
		
		return this;
	}
	
	/**
	 * Oduzmi zadanu matricu od ove matrice,
	 * ne mijenja stanje ove matice
	 * @param other druga matrica
	 * @return nova matrica
	 */
	public Matrix subtract(Matrix other) {
		return this.copy().subtractToThis(other);
	}
	
	/**
	 * Pomnozi ovu matricu sa zadanom matricom
	 * @param other druga matrica
	 * @return umnozak matrica
	 */
	public Matrix multiply(Matrix other) {
		if(cols != other.rows) {
			throw new IllegalArgumentException("Matirce nemaju kompatibilne dimenzije!"); 
		}
		
		Matrix m = new Matrix(rows, other.cols);
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < other.cols; j++) {
				m.matrix[i][j] = 0.0;
				
				for(int k = 0; k < cols; k++) {
					m.matrix[i][j] += matrix[i][k] * other.matrix[k][j];
				}
			}
		}
		
		return m;
	}
	
	/**
	 * Transponiraj ovu matricu
	 * @return transponirana matrica
	 */
	public Matrix transpose() {
		Matrix m = new Matrix(cols, rows);
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				m.matrix[j][i] = matrix[i][j];
			}
		}
		
		return m;
	}

	/**
	 * Pomnozi ovu matricu sa skalarom,
	 * mijenja unutarnje stanje matrice
	 * @param scalar skalarna vrijednost
	 * @return this
	 */
	public Matrix multiplyToThis(double scalar) {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				matrix[i][j] *= scalar;
			}
		}
		
		return this;
	}
	
	/**
	 * Pomnozi ovu matricu sa skalarom,
	 * ne mijenja unutarnje stanje matrice
	 * @param scalar skalarna vrijednost
	 * @return nova matrica
	 */
	public Matrix multiply(double scalar) {
		return this.copy().multiplyToThis(scalar);
	}
	
	/**
	 * Supstitucija unaprijed
	 * @param b vektor b (permutirani ako se koristila LUP dekompozicija)
	 * @return vektor y
	 */
	public Matrix forwardSubstitution(Matrix b) {
		if(rows != cols) {
			throw new IllegalArgumentException("Matrica nije kvadratna!");
		}
		
		if(b.rows != rows || b.cols != 1) {
			throw new IllegalArgumentException("Krivi format vektora!");
		}
		
		double[] y = new double[rows];
		
		for(int i = 0; i < rows; i++) {
			y[i] = b.get(i, 0);
			
			for(int j = 0; j <= i - 1; j++) {
				y[i] -= y[j] * matrix[i][j];
			}
		}
		
		return new Matrix(y);
	}
	
	/**
	 * Supstitucija unatrag
	 * @param y vektor y
	 * @return vektor x
	 */
	public Matrix backSubstitution(Matrix y) {
		if(rows != cols) {
			throw new IllegalArgumentException("Matrica nije kvadratna!");
		}
		
		if(y.rows != rows || y.cols != 1) {
			throw new IllegalArgumentException("Krivi format vektora!");
		}
		
		double[] x = new double[rows];
		
		for(int i = rows - 1; i >= 0; i--) {
			x[i] = y.get(i, 0);
			
			for(int j = i + 1; j < rows; j++) {
				x[i] -= x[j] * matrix[i][j];
			}
			
			x[i] /= matrix[i][i];
		}
		
		return new Matrix(x);
	}
	
	/**
	 * Dekomponiraj ovu matricu na L i U
	 * @param LUP true za LUP postupak, false inace
	 * @return vektor permutacija P, null u slucaju neuspjeha
	 */
	public Matrix decomposeLU(boolean LUP) {
		if(rows != cols) {
			throw new IllegalArgumentException("Matrica nije kvadratna!");
		}
		
		Matrix P = new Matrix(rows);
		
		for(int i = 0; i < rows - 1; i++) {
			if(LUP) {
				// ako je LUP postupak odabran napravi pivotiranje po stupcima
				int pivotIndex = i;
				
				for(int j = pivotIndex + 1; j < rows; j++) {
					if(Math.abs(matrix[j][i]) > Math.abs(matrix[pivotIndex][i])) {
						pivotIndex = j;
					}
				}
				
				this.swapRows(i, pivotIndex);
				P.swapRows(i, pivotIndex);
			}
			
			double pivotValue = matrix[i][i];
			
			if(isZero(pivotValue)) {
				System.err.println("Pivot je priblizno nula! Pivot: " + pivotValue);
				return null;
			}
			
			for(int j = i + 1; j < rows; j++) {
				matrix[j][i] /= pivotValue;
				
				for(int k = i + 1; k < cols; k++) {
					matrix[j][k] -= matrix[j][i] * matrix[i][k];
				}
			}	
		}
		
		double lastPivot = matrix[rows - 1][cols - 1];
		if(isZero(lastPivot)) {
			System.err.println("LU[N, N] je priblizno nula! Pivot: " + lastPivot);
			return null;
		}
		
		isDecomposed = true;
		
		return P;
	}
	
	/**
	 * Vrati kopiju matrice L
	 * @return matrica L
	 */
	public Matrix getL() {
		if(!isDecomposed) {
			throw new IllegalArgumentException("Matrica nije dekomponirana!");
		}
		
		Matrix L = new Matrix(rows);
		
		for(int i = 1; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				if(i > j) {
					L.matrix[i][j] = matrix[i][j];
				}
			}
		}
		
		return L;
	}
	
	/**
	 * Vrati kopiju matrice U
	 * @return matrica U
	 */
	public Matrix getU() {
		if(!isDecomposed) {
			throw new IllegalArgumentException("Matrica nije dekomponirana!");
		}
		
		Matrix U = new Matrix(rows);
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				if(i <= j) {
					U.matrix[i][j] = matrix[i][j];
				}
			}
		}
		
		return U;
	}
	
	/**
	 * Invertiraj ovu matricu, ne mijenja unutarnje stanje
	 * @return invertirana matrica, null u slucaju neuspjeha
	 */
	public Matrix inverse() {
		Matrix LU = this.copy();
		Matrix P = LU.decomposeLU(true);
		
		if(P == null) {
			System.err.println("Invezna matrica ne moze se izracunati!");
			return null;
		}
		
		Matrix inverse = new Matrix(rows, cols);
		
		for(int j = 0; j < cols; j++) {
			Matrix y = LU.forwardSubstitution(P.columnAsMatrix(j));
			Matrix x = LU.backSubstitution(y);
			
			inverse.setColumn(x, j);
		}
		
		return inverse;
	}
	
	/**
	 * Provjerava da li je matrica vektor zadane velicine
	 * @param size velicina vektora
	 * @return trua ako je matrica vektor, false inace
	 */
	public boolean isVector(int size) {
		if(rows == size && cols == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Vrati kopiju ove matrice
	 * @return kopija
	 */
	public Matrix copy() {
		return new Matrix(this);
	}
	
	/**
	 * Isprintaj matricu na standardni izlaz
	 */
	public void print() {
		System.out.println(this.toString());
	}
	
	/**
	 * Spremi matricu u datoteku
	 * @param path staza do datoteke
	 * @return true u slucaju uspjeha, false inace
	 */
	public boolean save(String path) {
		if(path == null) {
			throw new IllegalArgumentException("Staza datoteke ne smije biti null!");
		}
		
		try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path)))) {
			writer.write(this.toString());
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				sb.append(matrix[i][j]);
				
				if(j != cols - 1) {
					sb.append("\t");
				}
			}
			
			if(i != rows - 1) {
				sb.append("\n");
			}
		}
		
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix other = (Matrix) obj;
		if (cols != other.cols)
			return false;
		if (rows != other.rows)
			return false;
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				if(!isZero(matrix[i][j] - other.matrix[i][j])) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Staticka metoda za ucitavanje matice iz datoteke
	 * @param path staza datoteke
	 * @return ucitana matica, null u slucaju neuspjeha
	 */
	public static Matrix load(String path) {
		Matrix m = null;
		
		try {
			m = load(Files.readAllLines(Paths.get(path), Charset.defaultCharset()));
		} catch(Exception ignorable) {}
		
		return m;
	}
	
	/**
	 * Staticka metoda za ucitavanje matice iz liste stringova
	 * @param lines lista redaka matrice
	 * @return ucitana matrica, null u slucaju neuspjeha
	 */
	public static Matrix load(List<String> lines) {
		Matrix m = null;
		
		try {
			int rows = lines.size();
			int cols = lines.get(0).trim().split("\\s+").length;
			
			double[][] matrix = new double[rows][cols];
			
			for(int i = 0; i < rows; i++) {
				String[] parts = lines.get(i).trim().split("\\s+");
				
				if(parts.length != cols) {
					throw new Exception("Nekonzistentan broj elemenata po retku!");
				}
				
				for(int j = 0; j < cols; j++) {
					matrix[i][j] = Double.parseDouble(parts[j]);
				}
			}
			
			m = new Matrix(matrix);
		} catch(Exception ignorable) {
			ignorable.printStackTrace();
		} 
		
		return m;
	}
	
	/**
	 * Inicijaliziraj retke i stupce
	 * @param rows broj redaka
	 * @param cols broj stupaca
	 */
	private void initRowsAndCols(int rows, int cols) {
		if(rows < 1 || cols < 1) {
			throw new IllegalArgumentException("Broj redaka i stupaca mora biti veci od 1!");
		}
		
		this.rows = rows;
		this.cols = cols;
		this.isDecomposed = false;
	}
	
	/**
	 * Provjeri da li je realni broj priblizno jednak nuli
	 * @param value realni broj
	 * @return trua ako je priblizno jednak nuli, flase inace
	 */
	private boolean isZero(double value) {
		if(Math.abs(value) < EPSILON) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Zamijeni dva retka u matrici
	 * @param first indeks prvog retka
	 * @param second indeks drugog retka
	 */
	private void swapRows(int first, int second) {
		checkRowIndex(first);
		checkRowIndex(second);
		
		double[] tmp = matrix[first];
		matrix[first] = matrix[second];
		matrix[second] = tmp;
	}
	
	/**
	 * Provjeri da li je zadani indeks retka valjan
	 * @param row indeks retka
	 */
	private void checkRowIndex(int row) {
		if(row < 0 || row >= rows) {
			throw new IllegalArgumentException("Ilegalni indeks retka!");
		}
	}
	
	/**
	 * Provjeri da li je zadani indeks stupca valjan
	 * @param col indeks stupca
	 */
	private void checkColumnIndex(int col) {
		if(col < 0 || col >= cols) {
			throw new IllegalArgumentException("Ilegalni indeks retka!");
		}
	}

}