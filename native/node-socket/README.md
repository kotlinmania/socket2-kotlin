# socket2-kotlin Native Node.js Bindings

This directory contains N-API bindings that provide direct POSIX socket syscalls to Node.js, following the same architecture as our Kotlin/Native implementation.

## Architecture

Unlike using Node's built-in `net` module (which abstracts away the raw syscalls), these bindings provide **direct access to POSIX socket functions**:

- `socket(2)` - Create a socket
- `bind(2)` - Bind to an address
- `connect(2)` - Connect to a remote address
- `listen(2)` - Mark socket as listening
- `accept(2)` - Accept incoming connections
- `shutdown(2)` - Shutdown part of connection
- `close(2)` - Close socket
- `recv(2)` - Receive data
- `send(2)` - Send data

This gives us the same level of control as:
- **Kotlin/Native**: Direct C bindings via `@CName`
- **JVM**: Java NIO (which uses JNI internally)
- **Node.js**: N-API bindings (this module)

## Building

### Prerequisites

```bash
npm install
```

This will:
1. Install `node-addon-api` and `node-gyp`
2. Automatically run `node-gyp rebuild` to compile the native module

### Manual Build

```bash
# Clean build
npm run clean

# Build
npm run build

# Or rebuild
node-gyp rebuild
```

## Usage from Kotlin/JS

The Kotlin/JS Socket implementation uses these bindings via external declarations:

```kotlin
@JsModule("@socket2-kotlin/native-bindings")
@JsNonModule
external object Socket2Native {
    fun socket(domain: Int, type: Int, protocol: Int): Int
    fun bind(fd: Int, address: dynamic): Unit
    fun connect(fd: Int, address: dynamic): Unit
    fun listen(fd: Int, backlog: Int): Unit
    fun accept(fd: Int): dynamic
    fun shutdown(fd: Int, how: Int): Unit
    fun close(fd: Int): Unit
    fun recv(fd: Int, len: Int, flags: Int): dynamic
    fun send(fd: Int, buffer: dynamic, flags: Int): Int

    val AF_INET: Int
    val AF_INET6: Int
    val SOCK_STREAM: Int
    val SOCK_DGRAM: Int
    val SHUT_RD: Int
    val SHUT_WR: Int
    val SHUT_RDWR: Int
}
```

## Platform Support

Currently supports:
- macOS (x64, ARM64)
- Linux (x64, ARM64, ARM)
- Windows (via WSL or native with modifications)

## Development

The C++ code in `src/socket_bindings.cpp` mirrors the structure of `src/macosArm64Main/kotlin/io/github/kotlinmania/socket2/sys/Unix.kt` to maintain consistency across platforms.
