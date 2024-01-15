package Memory;

import Instruction.Instruction;

public class InstructionMemory {
	private Instruction[] instructionMemory;
	private int lastInstruction = -1;

	public InstructionMemory() {
		instructionMemory = new Instruction[1024];
	}

	public Instruction[] getInstructionMemory() {
		return instructionMemory;
	}

	public void addInstruction(Instruction I) {
		lastInstruction++;
		instructionMemory[lastInstruction] = I;
	}

	public int getLastInstruction() {
		return lastInstruction;
	}

	public void setLastInstruction(int lastInstruction) {
		this.lastInstruction = lastInstruction;
	}

}
