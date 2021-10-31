package com.mindorks.framework.logapp.com.mindorks.framework.logapp

import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class ClientUtil(address: String, port: Int) {

    private val connection: Socket = Socket(address, port)
    private var connected: Boolean = true

    init {
        println("Connected to server at $address on port $port")
    }

    private val reader: Scanner = Scanner(connection.getInputStream())
    private val writer: OutputStream = connection.getOutputStream()

    fun run(input: String) {
        //thread { read() }
        while (connected) {
            //val input = readLine() ?: ""
            if ("exit" in input) {
                connected = false
                reader.close()
                connection.close()
            } else {
                write(input)
            }
        }

    }

    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }

    private fun read() {
        while (connected)
            println(reader.nextLine())
    }
}