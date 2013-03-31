package br.com.jlamp;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/*
 *
 * @author Desiree Santos
 */
public class SerialComm implements SerialPortEventListener {

	InputStream inputStream ;
	OutputStream outputStream;
	private final int ACENDER = 1;
	private final int APAGAR = 0;

	public SerialComm() {

		inputStream = new InputStream() {
			
			@Override
			public int read() throws IOException {
				return 0;
			}
		};
		
		
		outputStream = new OutputStream() {
			
			@Override
			public void write(int arg0) throws IOException {
				
			}
		};
		
	  execute();
		
	}

	public void execute() {

		String portName = getPortNameByOS();

		CommPortIdentifier portId = getPortIdentifier(portName);
		if (portId != null) {

			try {

				SerialPort serialPort = (SerialPort) portId.open(this
						.getClass().getName(), 2000);

				inputStream = serialPort.getInputStream();
				outputStream = serialPort.getOutputStream();

				serialPort.addEventListener(this);
				serialPort.notifyOnDataAvailable(true);
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			} catch (PortInUseException e) {
			}

			catch (IOException e) {
			}

			catch (UnsupportedCommOperationException e) {
				e.printStackTrace();
			} catch (TooManyListenersException e) {
			}

		} else {
			System.out.println("Porta Serial n‹o dispon’vel");
		}
	}

	/**
	 * Obter porta serial Multiplataforma(Windows-Linux-Mac)
	 **/
	private String getPortNameByOS() {

		String soName = System.getProperty("os.name", "").toLowerCase();

		if (soName.startsWith("windows")) {
			// windows
			return "COM14";
		} else if (soName.startsWith("linux")) {
			// linux
			return "/dev/ttyS0";
		} else if (soName.startsWith("mac")) {
			// mac
			return "/dev/tty.usbserial-A900IRGL";
		} else {
			System.out.println("N‹o suporta o Sistema Operacional Corrente !");
			System.exit(1);
			return null;
		}

	}

	/**
	 * Identificar porta serial ativa
	 **/
	@SuppressWarnings("unused")
	private CommPortIdentifier getPortIdentifier(String portName) {
		@SuppressWarnings("rawtypes")
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		Boolean portFound = false;
		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier) portList
					.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				System.out.println("Available port: " + portId.getName());
				if (portId.getName().equals(portName)) {
					System.out.println("Found port: " + portName);
					portFound = true;
					return portId;
				}
			}

		}

		return null;

	}

	/**
	 * Ler dados da porta serial
	 **/
	public void serialEvent(SerialPortEvent event) {

		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:

			byte[] readBuffer = new byte[20];
			try {
				int numBytes = 0;
				while (inputStream.available() > 0) {
					numBytes = inputStream.read(readBuffer);
				}
				String result = new String(readBuffer);
				result = result.substring(1, numBytes);

				System.out.println("Read: " + result);

			} catch (IOException e) {
			}

			break;
		}
	}

	public void acenderLamp() {

		try {
			
			outputStream.write(ACENDER);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void apagarLamp() {

		try {
			
			outputStream.write(APAGAR);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}