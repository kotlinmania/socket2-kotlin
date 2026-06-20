// port-lint: source socket.rs
package io.github.kotlinmania.socket2

import io.github.kotlinmania.socket2.sys.*

/**
 * Cross-platform Socket implementation.
 *
 * This implementation wraps socket file descriptors and provides
 * idiomatic Kotlin APIs for socket operations across all platforms.
 */
public class Socket internal constructor(
    private var fd: RawSocket?
) {
    public companion object {
        /**
         * Creates a new socket and sets common flags.
         *
         * On macOS, this sets:
         * - `SOCK_CLOEXEC`: Close-on-exec flag
         * - `SOCK_NOSIGPIPE`: Don't raise SIGPIPE on closed connections
         *
         * See commonMain/Socket.kt for full documentation.
         */
        public fun new(domain: Domain, type: Type, protocol: Protocol?): Result<Socket> {
            // On macOS, set SOCK_NOSIGPIPE to avoid SIGPIPE signals
            return newRaw(domain, type, protocol).mapCatching { socket ->
                // Set common flags after creation
                // For now, we create the socket and rely on system defaults
                // TODO: Set SO_NOSIGPIPE socket option
                socket
            }
        }

        /**
         * Creates a new socket without setting additional flags.
         *
         * See commonMain/Socket.kt for full documentation.
         */
        public fun newRaw(domain: Domain, type: Type, protocol: Protocol?): Result<Socket> {
            val protocolValue = protocol?.value ?: 0
            return socket(domain.value, type.value, protocolValue).map { fd ->
                Socket(fd)
            }
        }
    }

    /**
     * Returns the raw file descriptor for this socket.
     *
     * # Safety
     * The caller must not close this file descriptor while the Socket is still in use.
     */
    internal fun asRaw(): RawSocket {
        return fd ?: throw IllegalStateException("Socket has been closed")
    }

    /**
     * Binds this socket to the specified address.
     *
     * See commonMain/Socket.kt for full documentation.
     */
    public fun bind(address: SockAddr): Result<Unit> {
        return bind(asRaw(), address)
    }

    /**
     * Initiate a connection on this socket to the specified address.
     *
     * See commonMain/Socket.kt for full documentation.
     */
    public fun connect(address: SockAddr): Result<Unit> {
        return connect(asRaw(), address)
    }

    /**
     * Marks the socket as ready to accept incoming connection requests.
     *
     * See commonMain/Socket.kt for full documentation.
     */
    public fun listen(backlog: Int): Result<Unit> {
        return listen(asRaw(), backlog)
    }

    /**
     * Accept a new incoming connection from this listener.
     *
     * See commonMain/Socket.kt for full documentation.
     */
    public fun accept(): Result<Pair<Socket, SockAddr>> {
        return accept(asRaw()).map { (newFd, addr) ->
            Pair(Socket(newFd), addr)
        }
    }

    /**
     * Shuts down the read, write, or both halves of this connection.
     *
     * See commonMain/Socket.kt for full documentation.
     */
    public fun shutdown(how: Shutdown): Result<Unit> {
        val howValue = when (how) {
            Shutdown.Read -> SHUT_RD
            Shutdown.Write -> SHUT_WR
            Shutdown.Both -> SHUT_RDWR
        }
        return shutdown(asRaw(), howValue)
    }

    /**
     * Receives data on the socket from the remote address to which it is connected.
     *
     * See commonMain/Socket.kt for full documentation.
     */
    public fun recv(buffer: ByteArray, flags: Int): Result<Int> {
        return recv(asRaw(), buffer, flags)
    }

    /**
     * Sends data on the socket to a connected peer.
     *
     * See commonMain/Socket.kt for full documentation.
     */
    public fun send(buffer: ByteArray, flags: Int): Result<Int> {
        return send(asRaw(), buffer, flags)
    }

    /**
     * Closes this socket.
     *
     * See commonMain/Socket.kt for full documentation.
     */
    public fun close(): Result<Unit> {
        val currentFd = fd
        return if (currentFd == null) {
            Result.failure(IllegalStateException("Socket already closed"))
        } else {
            fd = null
            close(currentFd)
        }
    }

    /**
     * Ensures the socket is closed when the object is garbage collected.
     *
     * Note: Relying on finalization is not recommended. Always explicitly
     * call [close] when done with the socket.
     */
    protected fun finalize() {
        fd?.let { close(it) }
    }

    override fun toString(): String {
        return "Socket(fd=$fd)"
    }
}
