package Memory;

public class DataMemory {
	private String[] dataMemory;

	public DataMemory(){
		String[] dataMemory = new String[2048];
		for(int i=0;i<2048;i++) {
			dataMemory[i]="000000000000000000";
			}
	}

	public String[] getDataMemory() {
		return dataMemory;
	}

	public void setDataMemory(String[] dataMemory) {
		this.dataMemory = dataMemory;
	}

}
