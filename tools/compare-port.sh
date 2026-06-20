#!/bin/bash
# Compares Rust socket2 implementation with Kotlin port

echo "=== Socket2 Port Analysis ==="
echo ""

# Extract Rust public methods
echo "📋 Rust socket2 public methods:"
grep -E "^\s*pub fn" /tmp/socket2-rust/src/socket.rs | \
  sed 's/^\s*pub fn //' | \
  sed 's/(.*$//' | \
  sort > /tmp/rust-methods.txt

# Extract Kotlin public methods
echo "📋 Kotlin socket2 public methods:"
grep -E "^\s*public fun" src/commonMain/kotlin/io/github/kotlinmania/socket2/Socket.kt | \
  sed 's/^\s*public fun //' | \
  sed 's/(.*$//' | \
  sed 's/://g' | \
  sort > /tmp/kotlin-methods.txt

echo ""
echo "Statistics:"
echo "-----------"
RUST_COUNT=$(wc -l < /tmp/rust-methods.txt | tr -d ' ')
KOTLIN_COUNT=$(wc -l < /tmp/kotlin-methods.txt | tr -d ' ')
echo "Rust methods:   $RUST_COUNT"
echo "Kotlin methods: $KOTLIN_COUNT"
echo "Coverage:       $(( KOTLIN_COUNT * 100 / RUST_COUNT ))%"

echo ""
echo "✅ Implemented in Kotlin:"
echo "------------------------"
comm -12 /tmp/rust-methods.txt /tmp/kotlin-methods.txt

echo ""
echo "🚧 Missing from Kotlin (Priority APIs):"
echo "---------------------------------------"
comm -23 /tmp/rust-methods.txt /tmp/kotlin-methods.txt | head -30

echo ""
echo "Full missing list: $(comm -23 /tmp/rust-methods.txt /tmp/kotlin-methods.txt | wc -l | tr -d ' ') methods"
