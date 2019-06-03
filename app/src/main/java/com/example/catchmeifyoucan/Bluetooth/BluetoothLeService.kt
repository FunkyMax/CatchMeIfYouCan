package com.example.catchmeifyoucan.Bluetooth

import android.app.Service
import android.bluetooth.*
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.util.*

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */

const val STATE_DISCONNECTED = 0
const val STATE_CONNECTING = 1
const val STATE_CONNECTED = 2
const val HM10_ADDRESS = "34:03:DE:37:AC:D1"

class BluetoothLeService(bluetoothManager: BluetoothManager) : Service() {

    private val serviceUUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")
    private val characteristicUUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")
    private val mBinder = LocalBinder()
    private var mBluetoothManager = bluetoothManager
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mBluetoothDeviceAddress: String? = null
    private var mBluetoothGatt: BluetoothGatt? = null
    private var mConnectionState = STATE_DISCONNECTED
    private var initialized = false

    // Implements callback methods for GATT events that the app cares about.
    private val mGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mConnectionState = STATE_CONNECTED
                mBluetoothGatt!!.discoverServices()

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mConnectionState = STATE_DISCONNECTED
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            println("CONNECTED AND INITIALIZED")
            initialized = true
        }
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    fun initialize(): Boolean {
        mBluetoothAdapter = mBluetoothManager.adapter
        if (mBluetoothAdapter == null) {
            return false
        }
        connect(HM10_ADDRESS)
        while (!initialized) {
            println("CONNECTING TO BLUETOOTH MODULE...")
            if (initialized) {
                return initialized
            }
        }
        return false
    }

    /**
     * Method for transmitting data to the HM10 Soft Bluetooth module.
     */
    fun write(data: ByteArray) {
        if (mBluetoothGatt == null) {
            return
        }
        val service = mBluetoothGatt!!.getService(serviceUUID)
        val characteristic = service.getCharacteristic(characteristicUUID)
        characteristic.value = data
        println(data)
        //mBluetoothGatt!!.setCharacteristicNotification(characteristic, true)
        mBluetoothGatt!!.writeCharacteristic(characteristic)
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * `BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)`
     * callback.
     */
    private fun connect(address: String?): Boolean {
        if (mBluetoothAdapter == null || address == null) {
            return false
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address == mBluetoothDeviceAddress
            && mBluetoothGatt != null
        ) {
            if (mBluetoothGatt!!.connect()) {
                mConnectionState = STATE_CONNECTING
                return true
            } else {
                return false
            }
        }

        val device = mBluetoothAdapter!!.getRemoteDevice(address)
        if (device == null) {
            return false
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback)
        mBluetoothDeviceAddress = address
        mConnectionState = STATE_CONNECTING
        val status = mBluetoothGatt?.discoverServices()
        return status!!
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    private fun close() {
        if (mBluetoothGatt == null) {
            return
        }
        disconnect()
        mBluetoothGatt!!.close()
        mBluetoothGatt = null
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * `BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)`
     * callback.
     */
    private fun disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return
        }
        mBluetoothGatt!!.disconnect()
    }

    inner class LocalBinder : Binder() {
        internal val service: BluetoothLeService
            get() = this@BluetoothLeService
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onUnbind(intent: Intent): Boolean {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close()
        return super.onUnbind(intent)
    }
}