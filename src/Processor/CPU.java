package Processor;

import Memory.*;
import Instruction.*;
import Parsing.*;

public class CPU {
	private boolean regUpdateFlag = false;
	private boolean dateUpdateFlag = false;
	private String[] regUpdates = new String[2];
	private String[] dataUpdates = new String[2];
	private int SREG;
	private int PC;
	private int R1, R2, imm;
	private int cycles;
	private int numOfInstructions = 0;
	private Instruction fetching;
	private Instruction decoding;
	private Instruction executing;
	private Parser Parser;
	private static InstructionMemory InstructionMemory;
	private static String[] DataMemory = new String[2048];
	private static Registers Registers;

	public CPU() {
		Parser = new Parser();
		Parser.readingfile();
		InstructionMemory = Parser.getInstructionMemory();
		for(int i=0;i<2048;i++) {
			DataMemory[i]=null;
			}	
		Registers = Parser.getRegisters();
	}

	public Instruction fetch() {
		if (PC < 0 || PC > 1023 || InstructionMemory.getInstructionMemory()[PC] == null) {
			return null;
		}
		int x = PC;
		PC++;
		return InstructionMemory.getInstructionMemory()[x];
	}

	public String decode(Instruction I) {
		I.decode();
		String process = "";
		switch (I.getIntopcode()) {

		case 0000:
			process = "ADD";
			break;
		case 0001:
			process = "SUB";
			break;
		case 0010:
			process = "MUL";
			break;
		case 0011:
			process = "LDI";
			break;
		case 0100:
			process = "BEQZ";
			break;
		case 0101:
			process = "AND";
			break;
		case 0110:
			process = "OR";
			break;
		case 0111:
			process = "JR";
			break;
		case 1000:
			process = "SLC";
			break;
		case 1001:
			process = "SRC";
			break;
		case 1010:
			process = "LB";
			break;
		case 1011:
			process = "SB";
			break;
		default:
			if (I.getBinaryCode().substring(0, 4).equals("0010"))
				process = "MUL";
			if (I.getBinaryCode().substring(0, 4).equals("0101"))
				process = "AND";
			if (I.getBinaryCode().substring(0, 4).equals("0110"))
				process = "OR";
			if (I.getBinaryCode().substring(0, 4).equals("0011"))
				process = "LDI";
		}
		return process;
	}

	public void execute(Instruction I, String Process) {
		switch (Process) {
		case "ADD":
			this.ADD();
			break;
		case "SUB":
			this.SUB();
			break;
		case "MUL":
			this.MUL();
			break;
		case "LDI":
			this.LDI();
			break;
		case "BEQZ":
			this.BEQZ();
			break;
		case "AND":
			this.AND();
			break;
		case "OR":
			this.OR();
			break;
		case "JR":
			this.JR();
			break;
		case "SLC":
			this.SLC();
			break;
		case "SRC":
			this.SRC();
			break;
		case "LB":
			this.LB();
			break;
		case "SB":
			this.SB();
			break;
		}
	}

	public void run() {

		for (int i = 0; i < InstructionMemory.getInstructionMemory().length; i++) {
			if (InstructionMemory.getInstructionMemory()[i] == null)
				break;
			numOfInstructions++;
		}
		cycles = 3 + ((numOfInstructions - 1) * 1);
		String curProcess = "";
		for (int i = 1; i < cycles + 1; i++) {
			if (i == 1) {
				fetching = fetch();
			} else if (i == 2) {
				decoding = fetching;
				curProcess = decode(decoding);
				fetching = fetch();

			} else if (i == cycles - 1) {
				executing = decoding;
				execute(executing, curProcess);
				decoding = fetching;
				curProcess = decode(decoding);
			} else if (i == cycles) {
				executing = decoding;
				execute(executing, curProcess);
			} else {
				executing = decoding;
				execute(executing, curProcess);
				decoding = fetching;
				curProcess = decode(decoding);
				fetching = fetch();
			}
			printStatus(i, cycles, fetching, decoding, executing);
			String sreg = Integer.toBinaryString(SREG);
			sreg = formatBinaryTo8Bits(sreg);
			System.out.println("SREG : " + sreg + "\n");
		}
	}

	public void printStatus(int cycleNum, int clockCycles, Instruction fet, Instruction dec, Instruction exe) {
		int fetch = -1;
		int decode = -1;
		int execute = -1;
		if (cycleNum == 1) {
			fetch = 1;
		} else if (cycleNum == 2) {
			fetch = 2;
			decode = 1;
		} else if (cycleNum == clockCycles - 1) {
			decode = cycleNum - 1;
			execute = cycleNum - 2;
		} else if (cycleNum == clockCycles) {
			execute = cycleNum - 2;
		} else {
			fetch = cycleNum;
			decode = cycleNum - 1;
			execute = cycleNum - 2;
		}
		System.out.println("Clock Cycle " + cycleNum + " :- " + "\n");
		if (cycleNum == 1)
			System.out.println("No instruction is being executing" + "\n" + "Instruction 1 is being fetching" + "\n"
					+ "No instruction will be decoding");
		else if (cycleNum == 2) {
			System.out.println("No instruction is being executing" + "\n" + "Instruction 2 is being fetching" + "\n"
					+ "Instruction 1 is being decoding");
		} else if (cycleNum == clockCycles - 1) {
			System.out.println(
					"Instruction " + execute + " is being executing" + "\n" + "No instruction is being fetching" + "\n"
							+ "Instruction " + decode + " is being decoding");
		} else if (cycleNum == clockCycles) {
			System.out.println("Instruction " + execute + " is being executing" + "\n"
					+ "No instruction is being fetching" + "\n" + "No instruction is being decoding");
		} else {
			System.out.println("Instruction " + execute + " is being executing" + "\n" + "Instruction " + fetch
					+ " is being fetching" + "\n" + "Instruction " + decode + " is being decoding");
		}
		if (regUpdateFlag) {
			System.out.println("Register " + regUpdates[0] + " content is updated to " + regUpdates[1]);
		} else
			System.out.println("Registers : No register Updates occurred");
		if (dateUpdateFlag) {
			System.out.println("Data Memory " + dataUpdates[0] + " content is updated to " + dataUpdates[1]);
		} else
			System.out.println("Data Memory : No data memory Updates occurred");
	}

	public void ADD() {
		String add1 = this.executing.getR1();
		String add2 = this.executing.getLastSixBits();
		int address1 = Integer.parseInt(add1, 2);
		int address2 = Integer.parseInt(add2, 2);
		String R1Content = Registers.getRegisters()[address1];
		String R2Content = Registers.getRegisters()[address2];
		int intR1Content = Integer.parseInt(R1Content, 2);
		int intR2Content = Integer.parseInt(R2Content, 2);
		String result = binaryAddition(R1Content, R2Content);
		int intResult = Integer.parseInt(result, 2);
		String updatedAddress = Integer.toString(address1);
		regUpdateFlag = true;
		dateUpdateFlag=false;
		regUpdates[0] = updatedAddress;
		regUpdates[1] = result;
		Registers.getRegisters()[address1] = result;
		boolean carryFlag = ((intR1Content & intR2Content) | ((intR1Content | intR2Content) & ~intResult)) != 0;
		boolean overFlowFlag = ((intR1Content ^ intResult) & (intR2Content ^ intResult)) < 0;
		boolean negativeFlag = (intResult & (1 << 7)) != 0;
		boolean signFlag = negativeFlag ^ overFlowFlag;
		boolean zeroFlag = (intResult == 0);
		if (carryFlag)
			this.SREG |= (1 << 4);
		else if (!carryFlag)
			this.SREG &= ~(1 << 4);
		if (overFlowFlag)
			this.SREG |= (1 << 3);
		else if (!overFlowFlag)
			this.SREG &= ~(1 << 3);
		if (negativeFlag)
			this.SREG |= (1 << 2);
		else if (!negativeFlag)
			this.SREG &= ~(1 << 2);
		if (signFlag)
			this.SREG |= (1 << 1);
		else if (!signFlag)
			this.SREG &= ~(1 << 1);
		if (zeroFlag)
			this.SREG |= 1;
		else if (!zeroFlag)
			this.SREG &= ~1;

	}

	public void SUB() {
		String add1 = this.executing.getR1();
		String add2 = this.executing.getLastSixBits();
		int address1 = Integer.parseInt(add1, 2);
		int address2 = Integer.parseInt(add2, 2);
		String R1Content = Registers.getRegisters()[address1];
		String R2Content = Registers.getRegisters()[address2];
		int intR1Content = Integer.parseInt(R1Content, 2);
		int intR2Content = Integer.parseInt(R2Content, 2);
		String result = binarySubtraction(R1Content, R2Content);
		int intResult = Integer.parseInt(result, 2);
		String updatedAddress = Integer.toString(address1);
		regUpdateFlag = true;
		dateUpdateFlag=false;
		regUpdates[0] = updatedAddress;
		regUpdates[1] = result;
		Registers.getRegisters()[address1] = result;
		boolean overFlowFlag = ((intR1Content ^ intResult) & (intR2Content ^ intResult)) < 0;
		boolean negativeFlag = (intResult & (1 << 7)) != 0;
		boolean signFlag = negativeFlag ^ overFlowFlag;
		boolean zeroFlag = (intResult == 0);
		if (overFlowFlag)
			this.SREG |= (1 << 3);
		else if (!overFlowFlag)
			this.SREG &= ~(1 << 3);
		if (negativeFlag)
			this.SREG |= (1 << 2);
		else if (!negativeFlag)
			this.SREG &= ~(1 << 2);
		if (signFlag)
			this.SREG |= (1 << 1);
		else if (!signFlag)
			this.SREG &= ~(1 << 1);
		if (zeroFlag)
			this.SREG |= 1;
		else if (!zeroFlag)
			this.SREG &= ~1;
	}

	public void MUL() {
		String add1 = this.executing.getR1();
		String add2 = this.executing.getLastSixBits();
		int address1 = Integer.parseInt(add1, 2);
		int address2 = Integer.parseInt(add2, 2);
		String R1Content = Registers.getRegisters()[address1];
		String R2Content = Registers.getRegisters()[address2];
		String result = binaryMultiplication(R1Content, R2Content);
		result = formatBinaryTo8Bits(result);
		int intResult = Integer.parseInt(result, 2);
		String updatedAddress = Integer.toString(address1);
		regUpdateFlag = true;
		dateUpdateFlag=false;
		regUpdates[0] = updatedAddress;
		regUpdates[1] = result;
		Registers.getRegisters()[address1] = result;
		boolean negativeFlag = (intResult & (1 << 7)) != 0;
		boolean zeroFlag = (intResult == 0);
		if (negativeFlag)
			this.SREG |= (1 << 2);
		else if (!negativeFlag)
			this.SREG &= ~(1 << 2);
		if (zeroFlag)
			this.SREG |= 1;
		else if (!zeroFlag)
			this.SREG &= ~1;
	}

	public void LDI() {
		String add1 = this.executing.getR1();
		String imm = this.executing.getLastSixBits();
		//boolean isNeg = isNegative(imm);
		int address1 = Integer.parseInt(add1, 2);
		imm = formatBinaryTo8Bits(imm);
		String updatedAddress = Integer.toString(address1);
		regUpdateFlag = true;
		dateUpdateFlag=false;
		regUpdates[0] = updatedAddress;
		regUpdates[1] = imm;
		Registers.getRegisters()[address1] = imm;
	}

	public void BEQZ(){
		imm = this.executing.getIntlastSixBits();
		if (this.executing.getIntR1() == 0) {
			PC = PC + 1 + imm;
		}
	}

	public void AND() {
		String add1 = this.executing.getR1();
		String add2 = this.executing.getLastSixBits();
		int address1 = Integer.parseInt(add1, 2);
		int address2 = Integer.parseInt(add2, 2);
		String R1Content = Registers.getRegisters()[address1];
		String R2Content = Registers.getRegisters()[address2];
		String result = binaryAND(R1Content, R2Content);
		int intResult = Integer.parseInt(result, 2);
		String updatedAddress = Integer.toString(address1);
		regUpdateFlag = true;
		dateUpdateFlag=false;
		regUpdates[0] = updatedAddress;
		regUpdates[1] = result;
		Registers.getRegisters()[address1] = result;
		boolean negativeFlag = (intResult & (1 << 7)) != 0;
		boolean zeroFlag = (intResult == 0);
		if (negativeFlag)
			this.SREG |= (1 << 2);
		else if (!negativeFlag)
			this.SREG &= ~(1 << 2);
		if (zeroFlag)
			this.SREG |= 1;
		else if (!zeroFlag)
			this.SREG &= ~1;

	}

	public void OR() {
		String add1 = this.executing.getR1();
		String add2 = this.executing.getLastSixBits();
		int address1 = Integer.parseInt(add1, 2);
		int address2 = Integer.parseInt(add2, 2);
		String R1Content = Registers.getRegisters()[address1];
		String R2Content = Registers.getRegisters()[address2];
		String result = binaryOR(R1Content, R2Content);
		int intResult = Integer.parseInt(result, 2);
		String updatedAddress = Integer.toString(address1);
		regUpdateFlag = true;
		dateUpdateFlag=false;
		regUpdates[0] = updatedAddress;
		regUpdates[1] = result;
		Registers.getRegisters()[address1] = result;
		boolean negativeFlag = (intResult & (1 << 7)) != 0;
		boolean zeroFlag = (intResult == 0);
		if (negativeFlag)
			this.SREG |= (1 << 2);
		else if (!negativeFlag)
			this.SREG &= ~(1 << 2);
		if (zeroFlag)
			this.SREG |= 1;
		else if (!zeroFlag)
			this.SREG &= ~1;
	}

	public void JR() {
		R1 = this.executing.getIntR1();
		R2 = this.executing.getIntlastSixBits();
		String Str1 = Integer.toBinaryString(R1);
		String Str2 = Integer.toBinaryString(R2);
		String res = Str1 + Str2;
		PC = (int) Long.parseLong(res);
	}

	public void SLC() {
		String add1 = this.executing.getR1();
		String strImm = this.executing.getLastSixBits();
		int address1 = Integer.parseInt(add1, 2);
		int imm = Integer.parseInt(strImm, 2);
		String R1Content = Registers.getRegisters()[address1];
		int intR1Content = Integer.parseInt(R1Content, 2);
		intR1Content = intR1Content << imm | intR1Content >>> 8 - imm;
		String result = leftCircularShift(R1Content, imm);
		result = formatBinaryTo8Bits(result);
		String updatedAddress = Integer.toString(address1);
		regUpdateFlag = true;
		dateUpdateFlag=false;
		regUpdates[0] = updatedAddress;
		regUpdates[1] = result;
		Registers.getRegisters()[address1] = result;
		boolean negativeFlag = (intR1Content & (1 << 7)) != 0;
		boolean zeroFlag = (intR1Content == 0);
		if (negativeFlag)
			this.SREG |= (1 << 2);
		else if (!negativeFlag)
			this.SREG &= ~(1 << 2);
		if (zeroFlag)
			this.SREG |= 1;
		else if (!zeroFlag)
			this.SREG &= ~1;

	}

	public void SRC() {
		String add1 = this.executing.getR1();
		String strImm = this.executing.getLastSixBits();
		int address1 = Integer.parseInt(add1, 2);
		int imm = Integer.parseInt(strImm, 2);
		String R1Content = Registers.getRegisters()[address1];
		int intR1Content = Integer.parseInt(R1Content, 2);
		intR1Content = intR1Content << imm | intR1Content >>> 8 - imm;
		String result = rightCircularShift(R1Content, imm);
		result = formatBinaryTo8Bits(result);
		String updatedAddress = Integer.toString(address1);
		regUpdateFlag = true;
		dateUpdateFlag=false;
		regUpdates[0] = updatedAddress;
		regUpdates[1] = result;
		Registers.getRegisters()[address1] = result;
		boolean negativeFlag = (intR1Content & (1 << 7)) != 0;
		boolean zeroFlag = (intR1Content == 0);
		if (negativeFlag)
			this.SREG |= (1 << 2);
		else if (!negativeFlag)
			this.SREG &= ~(1 << 2);
		if (zeroFlag)
			this.SREG |= 1;
		else if (!zeroFlag)
			this.SREG &= ~1;
	}

	public void LB() {
		imm = this.executing.getIntlastSixBits();
		String i = Integer.toString(imm);
		int IMM = Integer.parseInt(i, 2);
		String add1 = this.executing.getR1();
		int address1 = Integer.parseInt(add1, 2);
		String Add = Integer.toString(address1);
		Registers.getRegisters()[address1] = this.DataMemory[IMM];
		regUpdateFlag = true;
		dateUpdateFlag=false;
		regUpdates[0] = Add;
		regUpdates[1] = this.DataMemory[IMM];
	}

	public void SB() {
		String add1 = this.executing.getR1();
		imm = this.executing.getIntlastSixBits();
		int address1 = Integer.parseInt(add1, 2);
		String R1Content = Registers.getRegisters()[address1];
		String i = Integer.toString(imm);
		int IMM = Integer.parseInt(i, 2);
		String upd = Integer.toString(IMM);
		int ByteR1 = this.executing.getIntR1() & 0xFF;
		String R1 = Integer.toString(ByteR1);
		R1=formatBinaryTo8Bits(R1);
		this.DataMemory[IMM] = R1Content;
		regUpdateFlag=false;
		dateUpdateFlag=true;
		dataUpdates[0]=upd;
		dataUpdates[1]=R1Content;
	}

	public static void printContents() {
		int x = 0;
		int y = 0;
		int z = -1;
		System.out.println("Instruction Memory :-");
		if (InstructionMemory.getInstructionMemory() != null) {
			for (int i = 0; i < InstructionMemory.getInstructionMemory().length; i++) {
				x++;
				if (InstructionMemory.getInstructionMemory()[i] != null) {
					System.out.println("Instruction " + x + " : " + InstructionMemory.getInstructionMemory()[i]);
				}
				else if (InstructionMemory.getInstructionMemory()[i] == null) {
					System.out.println("Instruction " + x + " : " +"There is no instruction");
					
				}
			}
		} else
			System.out.println("Instruction Memory is empty !");
		System.out.println("\n" + "Data Memory :-");
		if (DataMemory != null) {
			for (int i = 0; i < DataMemory.length; i++) {
				y++;
				if (DataMemory[i] != null) {
					System.out.println("Data " + y + " : " + DataMemory[i]);
				}
				else if (DataMemory[i] == null) {
					System.out.println("Data " + y + " : " +"There is no data");
					
				}
			}
		} else
			System.out.println("Data Memory is empty !");
		System.out.println("\n" + "Registers :-");
		if (Registers.getRegisters() != null) {
			for (int i = 0; i < Registers.getRegisters().length; i++) {
				if (Registers.getRegisters()[i] != null)
					z++;
				else if (Registers.getRegisters()[i] == null)
					break;
				System.out.println("Register " + z + " : " + Registers.getRegisters()[i]);
			}
		} else
			System.out.println("Registers is empty !");
	}

	public static String binaryAddition(String binary1, String binary2) {
		// Make sure the two binary strings have the same length
		int maxLength = Math.max(binary1.length(), binary2.length());
		binary1 = String.format("%" + maxLength + "s", binary1).replace(' ', '0');
		binary2 = String.format("%" + maxLength + "s", binary2).replace(' ', '0');

		StringBuilder result = new StringBuilder();
		int carry = 0;

		// Perform addition for each bit position
		for (int i = maxLength - 1; i >= 0; i--) {
			int bit1 = binary1.charAt(i) - '0';
			int bit2 = binary2.charAt(i) - '0';

			// Calculate sum and carry
			int sum = bit1 + bit2 + carry;
			int sumBit = sum % 2;
			carry = sum / 2;

			// Append sum bit to result string
			result.append(sumBit);
		}

		// Append remaining carry if any
		if (carry > 0) {
			result.append(carry);
		}

		// Reverse the result string
		return result.reverse().toString();
	}

	public static String binarySubtraction(String binary1, String binary2) {
		// Make sure the two binary strings have the same length
		int maxLength = Math.max(binary1.length(), binary2.length());
		binary1 = String.format("%" + maxLength + "s", binary1).replace(' ', '0');
		binary2 = String.format("%" + maxLength + "s", binary2).replace(' ', '0');

		StringBuilder result = new StringBuilder();
		int borrow = 0;

		// Perform subtraction for each bit position
		for (int i = maxLength - 1; i >= 0; i--) {
			int bit1 = binary1.charAt(i) - '0';
			int bit2 = binary2.charAt(i) - '0';

			// Apply borrow if necessary
			bit1 -= borrow;
			borrow = 0;

			// Adjust bit1 and borrow if bit1 < bit2
			if (bit1 < bit2) {
				bit1 += 2;
				borrow = 1;
			}

			// Calculate difference
			int difference = bit1 - bit2;

			// Append difference bit to result string
			result.append(difference);
		}

		// Reverse the result string
		return result.reverse().toString();
	}

	public static String binaryMultiplication(String binary1, String binary2) {
		int length1 = binary1.length();
		int length2 = binary2.length();
		int maxLength = length1 + length2;

		// Initialize an array to store intermediate multiplication results
		int[] intermediateResults = new int[maxLength];

		// Perform multiplication for each bit of binary2
		for (int i = length2 - 1; i >= 0; i--) {
			int bit2 = binary2.charAt(i) - '0';

			if (bit2 == 1) {
				int carry = 0;

				// Multiply binary1 by the current bit of binary2 and add to intermediate result
				for (int j = length1 - 1; j >= 0; j--) {
					int bit1 = binary1.charAt(j) - '0';
					int product = (bit1 * bit2) + carry + intermediateResults[i + j + 1];
					intermediateResults[i + j + 1] = product % 2;
					carry = product / 2;
				}

				// Handle remaining carry if any
				if (carry > 0) {
					intermediateResults[i] += carry;
				}
			}
		}

		// Convert intermediate results to a string
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < maxLength; i++) {
			result.append(intermediateResults[i]);
		}

		// Trim leading zeros
		while (result.length() > 1 && result.charAt(0) == '0') {
			result.deleteCharAt(0);
		}

		return result.toString();
	}

	public static String binaryAND(String binary1, String binary2) {
		int length = Math.max(binary1.length(), binary2.length());

		// Pad the binary numbers with leading zeros if necessary
		binary1 = String.format("%" + length + "s", binary1).replace(' ', '0');
		binary2 = String.format("%" + length + "s", binary2).replace(' ', '0');

		StringBuilder result = new StringBuilder();

		// Perform the bitwise AND operation for each bit position
		for (int i = 0; i < length; i++) {
			char bit1 = binary1.charAt(i);
			char bit2 = binary2.charAt(i);

			// Perform the AND operation
			char resultBit = (bit1 == '1' && bit2 == '1') ? '1' : '0';

			// Append the result bit to the result string
			result.append(resultBit);
		}

		return result.toString();
	}

	public static String binaryOR(String binary1, String binary2) {
		int length = Math.max(binary1.length(), binary2.length());

		// Pad the binary numbers with leading zeros if necessary
		binary1 = String.format("%" + length + "s", binary1).replace(' ', '0');
		binary2 = String.format("%" + length + "s", binary2).replace(' ', '0');

		StringBuilder result = new StringBuilder();

		// Perform the bitwise OR operation for each bit position
		for (int i = 0; i < length; i++) {
			char bit1 = binary1.charAt(i);
			char bit2 = binary2.charAt(i);

			// Perform the OR operation
			char resultBit = (bit1 == '1' || bit2 == '1') ? '1' : '0';

			// Append the result bit to the result string
			result.append(resultBit);
		}

		return result.toString();
	}

	public static String leftCircularShift(String binaryNumber, int positions) {
		int length = binaryNumber.length();
		int shiftAmount = positions % length;

		// Extract the bits to be shifted
		String shiftedBits = binaryNumber.substring(0, shiftAmount);

		// Shift the remaining bits to the left
		String remainingBits = binaryNumber.substring(shiftAmount);

		// Combine the shifted bits and remaining bits
		String shiftedNumber = remainingBits + shiftedBits;

		return shiftedNumber;
	}

	public static String rightCircularShift(String binaryNumber, int positions) {
		int length = binaryNumber.length();
		int shiftAmount = positions % length;

		// Extract the bits to be shifted
		String shiftedBits = binaryNumber.substring(length - shiftAmount);

		// Shift the remaining bits to the right
		String remainingBits = binaryNumber.substring(0, length - shiftAmount);

		// Combine the shifted bits and remaining bits
		String shiftedNumber = shiftedBits + remainingBits;

		return shiftedNumber;
	}

	public static String formatBinaryTo8Bits1(String binaryNumber) {
	    int length = binaryNumber.length();

	    // Check if the binary number represents a negative value
	    boolean isNegative = binaryNumber.charAt(0) == '1';

	    // If the length is less than 8, pad it with leading zeros or ones depending on the sign
	    if (length < 8) {
	        StringBuilder paddedNumber = new StringBuilder();
	        int numOfZeros = 8 - length;

	        // Add leading zeros if positive, or leading ones if negative
	        char leadingBit = isNegative ? '1' : '0';
	        for (int i = 0; i < numOfZeros; i++) {
	            paddedNumber.append(leadingBit);
	        }

	        // Append the original binary number
	        paddedNumber.append(binaryNumber);

	        return paddedNumber.toString();
	    }

	    // If the length is already 8 or more, return the original binary number
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
		return formatBinaryTo8Bits2(binaryNumber);
	}
	public static String formatBinaryTo8Bits2(String binaryNumber) {
	    int length = binaryNumber.length();
	    // Check if the binary number represents a negative value
	    boolean isNegative = binaryNumber.charAt(0) == '1';
         if(isNegative) {
        	 for(int i=length;i<8;i++) {
        		 binaryNumber ="1"+binaryNumber;
        	 }
         }
         else {
        	 for(int i=length;i<8;i++) {
        		 binaryNumber ="0"+binaryNumber;
        	 }
         }
      
       return binaryNumber;
	}
	public static String byteToBinaryString(byte value) {
	    StringBuilder binary = new StringBuilder();
	    for (int i = 7; i >= 0; i--) {
	        binary.append((value >> i) & 1);
	    }
	    return binary.toString();
	}

	public static boolean isNegative(String operand) {
		boolean isNegative = operand.startsWith("-");
		return isNegative;
	}

	public static String handleNegatives(String operand) {
		int x = Integer.parseInt(operand);
		String num = Integer.toBinaryString(x & 0xFF);
		return num;
	}

	public static void main(String[] args) {
		CPU C = new CPU();
		C.run();
		printContents();
	}

}
