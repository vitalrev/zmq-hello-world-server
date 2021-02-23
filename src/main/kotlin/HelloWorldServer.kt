import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMQ

fun main(args: Array<String>) {
    ZContext().use { context ->
        println("Start server..")
        // Socket to talk to clients
        val socket: ZMQ.Socket = context.createSocket(SocketType.REP)
        socket.bind("tcp://*:9702")

        while (!Thread.currentThread().isInterrupted) {
            // Block until a message is received
            val reply: ByteArray = socket.recv(0)

            // Print the message
            println(
                "Received: [" + String(reply, ZMQ.CHARSET) + "]"
            )

            // Send a response
            val response = "Hello, world!"
            socket.send(response.toByteArray(ZMQ.CHARSET), 0)
        }
    }
}
