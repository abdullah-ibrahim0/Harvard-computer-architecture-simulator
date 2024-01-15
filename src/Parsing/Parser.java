package Parsing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import Instruction.Instruction;
import Memory.*;

public class Parser {
	private static InstructionMemory InstructionMemory;
	private static Registers registers;
	private static DataMemory DataMemory;
	
	public Parser() {
		this.registers = new Registers();
		this.InstructionMemory = new InstructionMemory();
		this.DataMemory = new DataMemory();
		
	}

	public void readingfile() {
		String filePath = "instructions.txt";

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				parsing(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void parsing(String Line) {
		String[] parts = Line.split(" ");
		switch (parts[0]) {
		case "ADD":
			String x1 = parts[1].substring(1); // reg1 number
			String y1 = parts[2].substring(1); // reg2 number
			int m1 = Integer.parseInt(x1);
			int n1 = Integer.parseInt(y1);
			String mm1 = Integer.toBinaryString(m1);
			String nn1 = Integer.toBinaryString(n1);
			String a1 = formatBinaryTo6Bits(mm1);
			String b1 = formatBinaryTo6Bits(nn1);
			String opcode1 = "0000";
			String inst1 = opcode1 + a1 + b1;
			Instruction I1 = new Instruction(inst1);
			InstructionMemory.addInstruction(I1);
			;
			break;
		case "SUB":
			String x2 = parts[1].substring(1); // reg1 number
			String y2 = parts[2].substring(1); // reg2 number
			int m2 = Integer.parseInt(x2);
			int n2 = Integer.parseInt(y2);
			String mm2 = Integer.toBinaryString(m2);
			String nn2 = Integer.toBinaryString(n2);
			String a2 = formatBinaryTo6Bits(mm2);
			String b2 = formatBinaryTo6Bits(nn2);
			String opcode2 = "0001";
			String inst2 = opcode2 + a2 + b2;
			Instruction I2 = new Instruction(inst2);
			InstructionMemory.addInstruction(I2);
			;
			break;
		case "MUL":
			String x3 = parts[1].substring(1); // reg1 number
			String y3 = parts[2].substring(1); // reg2 number
			int m3 = Integer.parseInt(x3);
			int n3 = Integer.parseInt(y3);
			String mm3 = Integer.toBinaryString(m3);
			String nn3 = Integer.toBinaryString(n3);
			String a3 = formatBinaryTo6Bits(mm3);
			String b3 = formatBinaryTo6Bits(nn3);
			String opcode3 = "0010";
			String inst3 = opcode3 + a3 + b3;
			Instruction I3 = new Instruction(inst3);
			InstructionMemory.addInstruction(I3);
			;
			break;
		case "LDI":
			String x4 = parts[1].substring(1);
			String y4 = parts[2];
			
			int regNum = Integer.parseInt(x4);
			String k = convertTo6BitBinary(regNum);
			boolean b4 = isNegative(y4);
			int n4 = Integer.parseInt(y4);
			String nn4 = Integer.toBinaryString(n4);
			nn4 = formatBinaryTo6Bits(nn4);
			String opcode4 = "0011";
			if (b4) {
				nn4 = handleNegatives(y4);
			}
			String inst4 = opcode4 + k + nn4;
			Instruction I4 = new Instruction(inst4);
			InstructionMemory.addInstruction(I4);
			break;
		case "BEQZ":
			String x5 = parts[1].substring(1);
			String y5 = parts[2];
			boolean a5 = isNegative(x5);
			boolean b5 = isNegative(y5);
			int m5 = Integer.parseInt(x5);
			int n5 = Integer.parseInt(y5);
			String mm5 = Integer.toBinaryString(m5);
			String nn5 = Integer.toBinaryString(n5);
			if (b5)
				nn5 = handleNegatives(y5);
			String opcode5 = "0100";
			String inst5 = opcode5 + mm5 + nn5;
			Instruction I5 = new Instruction(inst5);
			InstructionMemory.addInstruction(I5);
			;
			break;
		case "AND":
			String x6 = parts[1].substring(1);
			String y6 = parts[2].substring(1);
			boolean a6 = isNegative(x6);
			boolean b6 = isNegative(y6);
			int m6 = Integer.parseInt(x6);
			int n6 = Integer.parseInt(y6);
			String mm6 = Integer.toBinaryString(m6);
			String nn6 = Integer.toBinaryString(n6);
			mm6 = formatBinaryTo6Bits(mm6);
			nn6 = formatBinaryTo6Bits(nn6);
			String opcode6 = "0101";
			if (a6) {
				mm6 = handleNegatives(x6);
				mm6 = formatBinaryTo6Bits(mm6);
			}
			if (b6) {
				nn6 = handleNegatives(y6);
				nn6 = formatBinaryTo6Bits(nn6);
			}
			String inst6 = opcode6 + mm6 + nn6;
			Instruction I6 = new Instruction(inst6);
			InstructionMemory.addInstruction(I6);
			;
			break;
		case "OR":
			String x7 = parts[1].substring(1);
			String y7 = parts[2].substring(1);
			boolean a7 = isNegative(x7);
			boolean b7 = isNegative(y7);
			int m7 = Integer.parseInt(x7);
			int n7 = Integer.parseInt(y7);
			String mm7 = Integer.toBinaryString(m7);
			String nn7 = Integer.toBinaryString(n7);
			mm7 = formatBinaryTo6Bits(mm7);
			nn7 = formatBinaryTo6Bits(nn7);
			String opcode7 = "0110";
			if (a7) {
				mm7 = handleNegatives(x7);
				mm7 = formatBinaryTo6Bits(mm7);
			}
			if (b7) {
				nn7 = handleNegatives(y7);
				nn7 = formatBinaryTo6Bits(nn7);
			}
			String inst7 = opcode7 + mm7 + nn7;
			Instruction I7 = new Instruction(inst7);
			InstructionMemory.addInstruction(I7);
			;
			break;
		case "JR":
			String x8 = parts[1].substring(1);
			String y8 = parts[2].substring(1);
			boolean a8 = isNegative(x8);
			boolean b8 = isNegative(y8);
			int m8 = Integer.parseInt(x8);
			int n8 = Integer.parseInt(y8);
			String mm8 = Integer.toBinaryString(m8);
			String nn8 = Integer.toBinaryString(n8);
			mm8 = formatBinaryTo6Bits(mm8);
			nn8 = formatBinaryTo6Bits(nn8);
			String opcode8 = "0111";
			if (a8) {
				mm8 = handleNegatives(x8);
				mm8 = formatBinaryTo6Bits(mm8);
			}
			if (b8) {
				nn8 = handleNegatives(y8);
				nn8 = formatBinaryTo6Bits(nn8);
			}
			String inst8 = opcode8 + mm8 + nn8;
			Instruction I8 = new Instruction(inst8);
			InstructionMemory.addInstruction(I8);
			;
			break;
		case "SLC":
			String x9 = parts[1].substring(1);
			String y9 = parts[2];
			boolean a9 = isNegative(x9);
			boolean b9 = isNegative(y9);
			int m9 = Integer.parseInt(x9);
			int n9 = Integer.parseInt(y9);
			String mm9 = Integer.toBinaryString(m9);
			String nn9 = Integer.toBinaryString(n9);
			mm9 = formatBinaryTo6Bits(mm9);
			nn9 = formatBinaryTo6Bits(nn9);
			String opcode9 = "1000";
			if (a9) {
				mm9 = handleNegatives(x9);
				mm9 = formatBinaryTo6Bits(mm9);
			}
			if (b9) {
				nn9 = handleNegatives(y9);
				nn9 = formatBinaryTo6Bits(nn9);
			}
			String inst9 = opcode9 + mm9 + nn9;
			Instruction I9 = new Instruction(inst9);
			InstructionMemory.addInstruction(I9);
			;
			break;
		case "SRC":
			String x10 = parts[1].substring(1);
			String y10 = parts[2];
			boolean a10 = isNegative(x10);
			boolean b10 = isNegative(y10);
			int m10 = Integer.parseInt(x10);
			int n10 = Integer.parseInt(y10);
			String mm10 = Integer.toBinaryString(m10);
			String nn10 = Integer.toBinaryString(n10);
			mm10 = formatBinaryTo6Bits(mm10);
			nn10 = formatBinaryTo6Bits(nn10);
			String opcode10 = "1001";
			if (a10) {
				mm10 = handleNegatives(x10);
				mm10 = formatBinaryTo6Bits(mm10);
			}
			if (b10) {
				nn10 = handleNegatives(y10);
				nn10 = formatBinaryTo6Bits(nn10);
			}
			String inst10 = opcode10 + mm10 + nn10;
			Instruction I10 = new Instruction(inst10);
			InstructionMemory.addInstruction(I10);
			;
			break;
		case "LB":
			String x11 = parts[1].substring(1);
			String y11 = parts[2];
			boolean a11 = isNegative(x11);
			boolean b11 = isNegative(y11);
			int m11 = Integer.parseInt(x11);
			int n11 = Integer.parseInt(y11);
			String mm11 = Integer.toBinaryString(m11);
			String nn11 = Integer.toBinaryString(n11);
			mm11 = formatBinaryTo6Bits(mm11);
			nn11 = formatBinaryTo6Bits(nn11);
			String opcode11 = "1010";
			if (a11) {
				mm11 = handleNegatives(x11);
				mm11 = formatBinaryTo6Bits(mm11);
			}
			if (b11) {
				nn11 = handleNegatives(y11);
				nn11 = formatBinaryTo6Bits(nn11);
			}
			String inst11 = opcode11 + mm11 + nn11;
			Instruction I11 = new Instruction(inst11);
			InstructionMemory.addInstruction(I11);
			;
			break;
		case "SB":
			String x12 = parts[1].substring(1);
			String y12 = parts[2];
			boolean a12 = isNegative(x12);
			boolean b12 = isNegative(y12);
			int m12 = Integer.parseInt(x12);
			int n12 = Integer.parseInt(y12);
			String mm12 = Integer.toBinaryString(m12);
			String nn12 = Integer.toBinaryString(n12);
			mm12 = formatBinaryTo6Bits(mm12);
			nn12 = formatBinaryTo6Bits(nn12);
			String opcode12 = "1011";
			if (a12) {
				mm12 = handleNegatives(x12);
				mm12 = formatBinaryTo6Bits(mm12);
			}
			if (b12) {
				nn12 = handleNegatives(y12);
				nn12 = formatBinaryTo6Bits(nn12);
			}
			String inst12 = opcode12 + mm12 + nn12;
			Instruction I12 = new Instruction(inst12);
			InstructionMemory.addInstruction(I12);
			;
			break;

		}

	}

	public static boolean isNegative(String operand) {
		boolean isNegative = operand.startsWith("-");
		return isNegative;
	}

	public static String handleNegatives(String operand) {
	    int x = Integer.parseInt(operand);
	    String num = Integer.toBinaryString(x & 0x3F); // Masking with 0x3F to ensure 6 bits
	    int length = num.length();
	    
	    // Pad the binary number with leading zeros to ensure a fixed length of 6 bits
	    if (length < 6) {
	        StringBuilder paddedNumber = new StringBuilder();
	        int numOfZeros = 6 - length;
	        
	        // Add leading zeros
	        for (int i = 0; i < numOfZeros; i++) {
	            paddedNumber.append('0');
	        }
	        
	        // Append the original binary number
	        paddedNumber.append(num);
	        
	        return paddedNumber.toString();
	    }
	    
	    // If the length is already 6 or more, return the original binary number
	    return num;
	}



	public static String convertTo6BitBinary(int number) {
		// Ensure the number is within the valid range of 6-bit binary representation
		if (number < -32 || number > 31) {
			throw new IllegalArgumentException("Number out of range for 6-bit binary representation");
		}

		// Convert the number to binary representation
		String binary = Integer.toBinaryString(number);

		// Check if the binary number has less than 6 bits
		if (binary.length() < 6) {
			// Add leading zeros if necessary
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 6 - binary.length(); i++) {
				sb.append("0");
			}
			binary = sb.toString() + binary;
		}

		return binary;
	}

	public static String formatBinaryTo6Bits(String binaryNumber) {
	    int length = binaryNumber.length();

	    // If the length is less than 6, pad it with leading zeros
	    if (length < 6) {
	        StringBuilder paddedNumber = new StringBuilder();
	        int numOfZeros = 6 - length;

	        // Add leading zeros
	        for (int i = 0; i < numOfZeros; i++) {
	            paddedNumber.append('0');
	        }

	        // Append the original binary number
	        paddedNumber.append(binaryNumber);

	        return paddedNumber.toString();
	    }

	    // If the length is already 6 or more, return the original binary number
	    return binaryNumber;
	}


	public static String formatBinaryTo8Bits(String binaryNumber) {
		int length = binaryNumber.length();

		// If the length is less than 8, pad it with leading zeros
		if (length < 8) {
			StringBuilder paddedNumber = new StringBuilder();
			int numOfZeros = 8 - length;

			// Add leading zeros
			for (int i = 0; i < numOfZeros; i++) {
				paddedNumber.append('0');
			}

			// Append the original binary number
			paddedNumber.append(binaryNumber);

			return paddedNumber.toString();
		}

		// If the length is already 8 or more, return the original binary number
		return binaryNumber;
	}
	

	public InstructionMemory getInstructionMemory() {
		return InstructionMemory;
	}

	public Registers getRegisters() {
		return registers;
	}

	public DataMemory getDataMemory() {
		return DataMemory;
	}

	public static void main(String[] args) {
		/*Parser P = new Parser();
		int x=0;
		for (int i = 0; i < 7; i++) {
			
			if(InstructionMemory.getInstructionMemory()[i]!=null)
				x++;
			System.out.println(x);
			System.out.println(InstructionMemory.getInstructionMemory()[i]);
		}*/

	}
}
