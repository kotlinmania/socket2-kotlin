// Entry point for the native N-API module
// This is loaded by Node.js and provides the socket2 native bindings

try {
    module.exports = require('./build/Release/socket2_native.node');
} catch (err) {
    // Fallback to Debug build if Release is not available
    try {
        module.exports = require('./build/Debug/socket2_native.node');
    } catch (err2) {
        throw new Error(
            'Could not load socket2_native addon. ' +
            'Run `npm install` or `node-gyp rebuild` in native/node-socket directory.\n' +
            'Original error: ' + err.message + '\n' +
            'Debug error: ' + err2.message
        );
    }
}
