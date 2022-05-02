import java.math.BigDecimal;

public class Convertor {
	public static String IEEE754(double d) {
		int signBit = d < 0 ? 1 : 0;
		int power = 0;
		StringBuilder fractionBinary = new StringBuilder();
		if (Math.abs(d) < 1) {
			double[] mantissaAndPower = getMantissaAndPowerFromDouble(d);
			power = (int) mantissaAndPower[1];
			fractionBinary.append(doubleToBinary(mantissaAndPower[0]).substring(2));
		} else {
			double[] splitDouble = splitDouble(d);
			StringBuilder intBinary = intToBinary((int) splitDouble[0]);
			StringBuilder shiftBinary = new StringBuilder();
			for (int i = 0; i < intBinary.length(); i++) {
				if (intBinary.charAt(i) == '1') {
					shiftBinary.append(intBinary.substring(i + 1));
					break;
				}
			}
			power = shiftBinary.length();
			fractionBinary = shiftBinary.append(doubleToBinary(splitDouble[1]).substring(2));
		}
		int shiftExponent = 127 + power;
		StringBuilder shiftExponentBinary = intToBinary(shiftExponent);
		return signBit + " | " + shiftExponentBinary + " | " + getFormattedFraction(fractionBinary);
	}

	private static double[] getMantissaAndPowerFromDouble(double d) {
		// Example; 0.085 = 1.36 x 2 ^-4 returns {0.36, -4}
		int power = 0;
		double temp = d;
		while (temp < 1) {
			power -= 1;
			temp = Math.abs(d) / Math.pow(2, power);
		}
		double[] arr = { splitDouble(temp)[1], power };
		return arr;
	}

	private static String getFormattedFraction(StringBuilder fractionBinary) {
		if (fractionBinary.length() >= 23)
			return fractionBinary.substring(0, 23);
		while (fractionBinary.length() < 23)
			fractionBinary.append(0);
		return fractionBinary.toString();
	}

	private static String doubleToBinary(double d) {
		double[] splitDouble = splitDouble(d);
		StringBuilder binary = intToBinary((int) splitDouble[0]);
		double temp = Math.abs(splitDouble[1]);
		if (temp > 0.0) {
			StringBuilder tempBinary = new StringBuilder();
			while (temp != 0) {
				if (tempBinary.length() == 23)
					break;
				temp *= 2;
				double[] s = splitDouble(temp);
				tempBinary.append((int) s[0]);
				temp = s[1];
			}
			binary.append(".").append(tempBinary.toString());
		}
		return binary.toString();
	}

	private static double[] splitDouble(double d) {
		BigDecimal bigDecimal = new BigDecimal(String.valueOf(d));
		int intValue = bigDecimal.intValue();
		double fraction = bigDecimal.subtract(new BigDecimal(intValue)).doubleValue();
		double arr[] = { intValue, fraction };
		return arr;
	}

	private static StringBuilder intToBinary(int value) {
		int temp = Math.abs(value);
		StringBuilder binary = new StringBuilder();
		if (value == 0)
			return binary.append(0);
		int bitLength = 0;
		while (temp > 0 || bitLength < 8) {
			binary.append(temp % 2);
			temp /= 2;
			bitLength++;
		}
		return binary.reverse();
	}

}
