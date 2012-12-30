/**
 * Copyright (c) 2008 Martin Wood-Mitrovski
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.relivethefuture.osc.transport;

import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.relivethefuture.osc.data.OscPacket;

public class OscClient extends IoHandlerAdapter implements
		IoFutureListener<IoFuture> {
	final Logger logger = LoggerFactory.getLogger(OscClient.class);

	private IoConnector connector;
	private IoSession session;
	private List<OscPacket> packetQueue;
	private OscDataEncoder encoder;

	private InetSocketAddress address;

	private boolean udp = true;

	public OscClient(boolean isUDP) {
		packetQueue = new ArrayList<OscPacket>();
		udp = isUDP;
		connector = createConnector();

		connector.setHandler(this);
		encoder = new OscDataEncoder(udp);
	}

	private IoConnector createConnector() {
		if (udp) {
			return new NioDatagramConnector();
		} else {
			return new NioSocketConnector();
		}
	}

	public void connect(InetSocketAddress addr) {
		address = addr;
		logger.debug("Connect : " + address.getHostName() + " : "
				+ address.getPort());
		ConnectFuture connFuture = connector.connect(address);

		logger.debug("About to wait.");
		connFuture.awaitUninterruptibly();

		connFuture.addListener(this);
	}

	public void operationComplete(IoFuture future) {
		ConnectFuture connFuture = (ConnectFuture) future;
		if (connFuture.isConnected()) {
			session = future.getSession();
			logger.debug("Client Connected " + packetQueue.size());
			if (packetQueue.size() > 0) {
				for (OscPacket packet : packetQueue) {
					try {
						sendPacket(packet);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			System.out.println("Not connected...exiting");
		}
	}

	public void sendPacket(OscPacket packet) throws InterruptedException {
		logger.debug("Trying to send packet " + packetQueue.size());

		if (session == null) {
			packetQueue.add(packet);
			logger
					.debug("Session is null, queued packet "
							+ packetQueue.size());
		} else {
			int size = packet.getSize();

			logger.debug("sending packet " + size);

			IoBuffer buffer = IoBuffer.allocate(size);
			try {
				encoder.encodePacket(packet, buffer, !udp);
				buffer.flip();
				session.write(buffer);
			} catch (CharacterCodingException e) {
				e.printStackTrace();
			}
		}
	}

	public void exceptionCaught(IoSession arg0, Throwable arg1)
			throws Exception {
		logger.warn("Exception Caught ");
		if (arg1 instanceof PortUnreachableException) {
			logger.warn("Cant connect to server " + address.getHostName()
					+ " : " + address.getPort());
		} else {
			arg1.printStackTrace();
		}
	}

	public void messageReceived(IoSession arg0, Object arg1) throws Exception {
		logger.info("Message Received");
	}

	public void messageSent(IoSession arg0, Object arg1) throws Exception {
		logger.info("Message Sent");
	}

	public void sessionClosed(IoSession arg0) throws Exception {
		logger.info("Session Closed");

	}

	public void sessionCreated(IoSession arg0) throws Exception {
		logger.info("Session Created");
	}

	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
		logger.info("Session Idle");
	}

	public void sessionOpened(IoSession arg0) throws Exception {
		logger.info("Session Opened");
	}

	
	public void disconnect() {
	//	session.close();
	}
}
