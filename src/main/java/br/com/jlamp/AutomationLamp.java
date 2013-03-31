package br.com.jlamp;

import java.io.IOException;
import java.io.Serializable;



public class AutomationLamp  implements Serializable{

	private static final long serialVersionUID = 1L;
	private SerialComm serial;
	private String on;
	private String off;

	
	public AutomationLamp()  {

		serial = new SerialComm();
		System.out.println(" Ativar Comunicao serial ");
		serial.execute();
		
	}
	

	


	public SerialComm getSerial() {
		return serial;
	}


	public void setSerial(SerialComm serial) {
		this.serial = serial;
	}


	public String getOn()  throws IOException{
		System.out.println("Acender");
		serial.acenderLamp();
		return on;
	}


	public void setOn(String on) throws IOException {
		
		this.on = on;
	}

	public String getOff()  throws IOException{
		System.out.println("Apagar");
		serial.apagarLamp();
		return off;
	}


	public void setOff(String off)  throws IOException{
	
		this.off = off;
	}

	
	
}
	
	
