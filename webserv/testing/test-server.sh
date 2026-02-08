#!/bin/bash

# Test Script for WebServer Load Testing
# Make sure your Java server is running on port 8181 first

echo "🚀 Starting WebServer Load Tests..."
echo "=================================="

# Test 1: Simple GET
echo "📤 Test 1: Simple GET Request"
curl -w "Response Time: %{time_total}s\n" http://localhost:8181
echo -e "\n---\n"

# Test 2: Small POST
echo "📤 Test 2: Small POST Request"
curl -X POST \
  -H "Content-Type: text/plain" \
  -d "This is a small test message for POST request testing. Hello from curl!" \
  -w "Response Time: %{time_total}s\n" \
  http://localhost:8181
echo -e "\n---\n"

# Test 3: Medium POST (1KB)
echo "📤 Test 3: Medium POST Request (1KB)"
curl -X POST \
  -H "Content-Type: application/json" \
  -d "{\"test\":\"medium_data\",\"size\":\"1KB\",\"data\":\"$(printf 'x%.0s' {1..800})\",\"timestamp\":\"$(date)\",\"random\":\"$(uuidgen)\"}" \
  -w "Response Time: %{time_total}s\n" \
  http://localhost:8181
echo -e "\n---\n"

# Test 4: Large POST (10KB)
echo "📤 Test 4: Large POST Request (10KB)"
LARGE_DATA="LARGE_DATA_TEST_10KB:\n$(printf 'This is a large data test to check server blocking behavior. %.0s' {1..200})\nEND_OF_LARGE_DATA_TEST"
curl -X POST \
  -H "Content-Type: text/plain" \
  -d "$LARGE_DATA" \
  -w "Response Time: %{time_total}s\n" \
  http://localhost:8181
echo -e "\n---\n"

# Test 5: Very Large POST (100KB)
echo "📤 Test 5: Very Large POST Request (100KB)"
VERY_LARGE_DATA="VERY_LARGE_DATA_TEST_100KB:\n$(printf '0123456789%.0s' {1..10000})\nEND_OF_VERY_LARGE_DATA_TEST"
curl -X POST \
  -H "Content-Type: application/octet-stream" \
  -d "$VERY_LARGE_DATA" \
  -w "Response Time: %{time_total}s\n" \
  http://localhost:8181
echo -e "\n---\n"

echo "✅ All tests completed!"
echo "Check your server console for file operations and connection logs."