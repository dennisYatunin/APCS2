public class Recursion {

	public String name() {
		return "Yatunin,Dennis";
	}

	public int fact(int n) {
		if (n > 0)
			return fact(n - 1) * n;
		else if (n == 0)
			return 1;
		else throw new java.lang.IllegalArgumentException();
	}

	public int fib_help(int x, int y, int n) {
		if (n > 0)
			return fib_help(y, x + y, n - 1);
		else return x;
	}
	public int fib(int n) {
		if (n >= 0)
			return fib_help(0, 1, n);
		else throw new java.lang.IllegalArgumentException();
	}

	public double sqrt_help(double n, double sqrt_n) {
		if (sqrt_n * sqrt_n - n < 0.0000000001 && sqrt_n * sqrt_n - n > -0.0000000001)
			// Precise enough?
			return sqrt_n;
		else return sqrt_help(n, (n / sqrt_n + sqrt_n) / 2);
	}
	public double sqrt(double n) {
		if (n >= 0)
			return sqrt_help(n, 1);
		else throw new java.lang.IllegalArgumentException();
	}

}