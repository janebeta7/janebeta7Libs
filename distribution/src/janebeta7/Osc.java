package janebeta7;

import com.relivethefuture.osc.data.BasicOscListener;
import com.relivethefuture.osc.data.OscMessage;
import com.relivethefuture.osc.transport.OscServer;

public class Osc {
	public static final int DEFAULT_OSC_PORT = 8000;
	private OscServer server;
	private int oscPort = DEFAULT_OSC_PORT;
	private int argVal = 1;

	public Osc(int port) {
		oscPort = port;
		System.out.println("janebeta7Libs > OSC > listening! on: "+oscPort);
		startListening() ;
	}

	/***
	 * This listens for incoming OSC messages.
	 * 
	 * For testing, it just prints the message, but you can do anything you want
	 * with it!
	 * 
	 * It is run in a separate thread, so if you are going to update the UI
	 * you'll need to use a Handler.
	 * 
	 * @author odbol
	 * 
	 */
	public class LooperListener extends BasicOscListener {

		/***
		 * This is the main place where you will handle individual osc messages
		 * as they come in.
		 * 
		 * The message's address specifies what the user wants to change in your
		 * application: think of it as an API call. The message's arguments
		 * specify what to change those things to. You can accept multiple
		 * arguments of any primitive types.
		 */
		@Override
		public void handleMessage(OscMessage msg) {
			// get all the arguments for the message. in this case we're
			// assuming one and only one argument.
			int val = (Integer) msg.getArguments().get(0);
			//System.out.println("* aColor > val:" + val);
			argVal = val;

		}
	}

	/***
	 * This starts your app listening for OSC messages.
	 * 
	 * You want to call this once your user chooses to start the OSC server - it
	 * probably shouldn't be started by default since it will block that port
	 * for any other apps.
	 */
	private void startListening() {
		stopListening(); // unbind from port

		try {
			System.out.println("janebeta7Libs > OSC > new OscServer");
			server = new OscServer(oscPort);
			server.setUDP(true); // as of now, the TCP implementation of OSCLib
									// is broken (getting buffer overflows!), so
									// we have to use UDP.
			server.start();
		} catch (Exception e) {

			return;
		}
		server.addOscListener(new LooperListener());

	}

	/***
	 * This is the main place where you will handle individual osc messages as
	 * they come in.
	 * 
	 * The message's address specifies what the user wants to change in your
	 * application: think of it as an API call. The message's arguments specify
	 * what to change those things to. You can accept multiple arguments of any
	 * primitive types.
	 */

	private void stopListening() {
		if (server != null) {
			server.stop();
			server = null;
		}
	}
	/***
	 * devolvemos el valor del parametro color a la clase principal
	 */
	public int getOsc() {
		return argVal;
	}
}
