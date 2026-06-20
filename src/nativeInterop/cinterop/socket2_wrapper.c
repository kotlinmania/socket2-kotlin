#include "socket2_wrapper.h"
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include <stdlib.h>

// Internal structure - wraps sockaddr_storage
struct Socket2AddrStorage {
    struct sockaddr_storage storage;
};

Socket2AddrStorage* socket2_addr_storage_new() {
    Socket2AddrStorage* addr = (Socket2AddrStorage*)malloc(sizeof(Socket2AddrStorage));
    if (addr) {
        memset(&addr->storage, 0, sizeof(addr->storage));
    }
    return addr;
}

void socket2_addr_storage_free(Socket2AddrStorage* storage) {
    free(storage);
}

uint16_t socket2_addr_storage_get_family(const Socket2AddrStorage* storage) {
    return storage->storage.ss_family;
}

void socket2_addr_storage_set_family(Socket2AddrStorage* storage, uint16_t family) {
    storage->storage.ss_family = family;
}

struct sockaddr* socket2_addr_storage_as_sockaddr(Socket2AddrStorage* storage) {
    return (struct sockaddr*)(&storage->storage);
}

struct sockaddr_storage* socket2_addr_storage_as_storage(Socket2AddrStorage* storage) {
    return &storage->storage;
}

void socket2_addr_storage_from_raw(Socket2AddrStorage* dest, const struct sockaddr_storage* src, uint32_t len) {
    memcpy(&dest->storage, src, len);
}

// Socket syscalls - direct pass-through
int socket2_socket(int domain, int type, int protocol) {
    return socket(domain, type, protocol);
}

int socket2_bind(int sockfd, Socket2AddrStorage* addr, uint32_t addrlen) {
    return bind(sockfd, socket2_addr_storage_as_sockaddr(addr), addrlen);
}

int socket2_connect(int sockfd, Socket2AddrStorage* addr, uint32_t addrlen) {
    return connect(sockfd, socket2_addr_storage_as_sockaddr(addr), addrlen);
}

int socket2_listen(int sockfd, int backlog) {
    return listen(sockfd, backlog);
}

int socket2_accept(int sockfd, Socket2AddrStorage* addr, uint32_t* addrlen) {
    socklen_t len = *addrlen;
    int result = accept(sockfd, socket2_addr_storage_as_sockaddr(addr), &len);
    *addrlen = len;
    return result;
}

int socket2_shutdown(int sockfd, int how) {
    return shutdown(sockfd, how);
}

int64_t socket2_recv(int sockfd, void* buf, uint64_t len, int flags) {
    return recv(sockfd, buf, len, flags);
}

int64_t socket2_send(int sockfd, const void* buf, uint64_t len, int flags) {
    return send(sockfd, buf, len, flags);
}

int socket2_close(int fd) {
    return close(fd);
}

int socket2_get_errno() {
    return errno;
}

const char* socket2_get_error_string(int errnum) {
    return strerror(errnum);
}
