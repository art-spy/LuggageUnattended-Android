package com.example.leluun;

import java.io.IOException;
import java.io.InputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BluetoothSocketListener implements Runnable {
	private BluetoothSocket btsocket;
	private Handler handler;
	private String msg;
	public BluetoothSocketListener(BluetoothSocket socket,
	Handler handler) {
	btsocket = socket;
	this.handler = handler;
	}
	
	
	public void run() {
		
		int bufferSize = 1024;
		long time;
		long timet;
		byte[] buffer = new byte[bufferSize];
		try {

		InputStream instream = btsocket.getInputStream();
		int bytesRead = -1;
		String message = "";
		while (true) {
		message = "";
		bytesRead = instream.read(buffer);
		if (bytesRead != -1) {
		time = System.nanoTime();
		while ((bytesRead==bufferSize)&&(buffer[bufferSize-1] != 0)) {
		message = message + new String(buffer, 0, bytesRead);
		bytesRead = instream.read(buffer);
		}
		msg = message + new String(buffer, 0, bytesRead - 1);
		timet = Long.parseLong(msg);

		threadMsg(time - timet);
		btsocket.getInputStream();
		}
		}
		} catch (IOException e) {
		Log.d("BLUETOOTH_COMMS", e.getMessage());
		}
	}
	
	
	 private void threadMsg(long msg) {
		 
         if (msg!=0){
             Message msgObj = handler.obtainMessage();
             Bundle b = new Bundle();
             b.putLong("message", msg);
             msgObj.setData(b);
             handler.sendMessage(msgObj);
         }
     }
}
