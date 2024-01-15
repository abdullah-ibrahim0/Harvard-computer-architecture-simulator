package Instruction;

public class Instruction {
	
	private String binaryCode;
	private String opcode;
	private String R1;
	private String lastSixBits;
	private Long bCode;
	private int intopcode;
	private int intR1;
	private int intlastSixBits;

	public Instruction(String binaryCode) {
		this.binaryCode = binaryCode;
		
	}
	
	public void decode(){
	    opcode=binaryCode.substring(0,4);
	    R1=binaryCode.substring(5,10);
	    lastSixBits=binaryCode.substring(11,16);
	}

	public String getBinaryCode() {
		return binaryCode;
	}

	public void setBinaryCode(String binaryCode) {
		this.binaryCode = binaryCode;
	}

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}

	public String getR1() {
		return R1;
	}

	public void setR1(String r1) {
		R1 = r1;
	}

	public String getLastSixBits() {
		return lastSixBits;
	}

	public void setLastSixBits(String lastSixBits) {
		this.lastSixBits = lastSixBits;
	}

	public Long getbCode() {
		bCode = Long.parseLong(binaryCode);
		return bCode;
	}

	public void setbCode(Long bCode) {
		this.bCode = bCode;
	}

	public int getIntopcode() {
		intopcode=Integer.parseInt(opcode);
		return intopcode;
	}

	public void setIntopcode(int intopcode) {
		this.intopcode = intopcode;
	}

	public int getIntR1() {
		intR1=Integer.parseInt(R1);
		return intR1;
	}

	public void setIntR1(int intR1) {
		this.intR1 = intR1;
	}

	public int getIntlastSixBits() {
		intlastSixBits=Integer.parseInt(lastSixBits);
		return intlastSixBits;
	}

	public void setIntlastSixBits(int intlastSixBits) {
		this.intlastSixBits = intlastSixBits;
	}
	public String toString() {
        return binaryCode;
    }
	
}
