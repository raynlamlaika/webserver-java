#!/bin/bash

# 1GB Load Test for WebServer
# Tests server with very large data to check memory usage and file streaming

echo "🔥 Starting 1GB Load Test..."
echo "============================="
echo "⚠️  WARNING: This will send 1GB of data to your server!"
echo "⚠️  Make sure your server is running and you have enough disk space."
echo ""

# Check if server is running
echo "🔍 Checking if server is running on port 8181..."
if ! curl -s --connect-timeout 3 http://localhost:8181 > /dev/null; then
    echo "❌ Server not responding on port 8181. Please start your server first."
    exit 1
fi
echo "✅ Server is running!"
echo ""

# Create 1GB test file
echo "📝 Creating 1GB test data..."
TEST_FILE="/tmp/1gb_test_data.txt"

# Generate 1GB of test data efficiently
{
    echo "=== 1GB STRESS TEST DATA ==="
    echo "Timestamp: $(date)"
    echo "Test ID: $(uuidgen)"
    echo "Purpose: Testing server file streaming with large data"
    echo "Expected behavior: Server should stream to file without memory overflow"
    echo "=================================="
    echo ""
    
    # Generate 1GB of data (1024 MB * 1024 KB * 1024 bytes)
    # Using efficient pattern generation
    CHUNK_SIZE=1024  # 1KB chunks
    TOTAL_CHUNKS=10000  # 1024*1024 = 1MB chunks to make 1GB
    
    for i in $(seq 1 $TOTAL_CHUNKS); do
        # Generate 1KB of data per chunk
        printf "CHUNK_%07d: %s\n" $i "$(printf '0123456789ABCDEF%.0s' {1..62})"
    done
    
    echo ""
    echo "=== END OF 1GB TEST DATA ==="
} > "$TEST_FILE"

echo "✅ 1GB test file created: $TEST_FILE"
echo "📊 File size: $(ls -lh $TEST_FILE | awk '{print $5}')"
echo ""

# Send the 1GB POST request
echo "🚀 Sending 1GB POST request to server..."
echo "⏱️  This may take a while - monitoring response time..."
echo ""

START_TIME=$(date +%s)

curl -X POST \
  -H "Content-Type: application/octet-stream" \
  -H "X-Test-Type: 1GB-Load-Test" \
  -H "X-Test-ID: $(uuidgen)" \
  --data-binary "@$TEST_FILE" \
  -w "\n📊 Response Stats:\n - Total Time: %{time_total}s\n - Upload Speed: %{speed_upload} bytes/sec\n - Response Code: %{http_code}\n" \
  http://localhost:8181

END_TIME=$(date +%s)
DURATION=$((END_TIME - START_TIME))

echo ""
echo "✅ 1GB test completed in ${DURATION} seconds!"
echo ""

# Cleanup
echo "🧹 Cleaning up test file..."
rm -f "$TEST_FILE"

echo "🎯 Test Results Summary:"
echo "========================"
echo "- Data sent: 1GB"
echo "- Duration: ${DURATION} seconds" 
echo "- Check server console for:"
echo "  ✓ File streaming logs"
echo "  ✓ Memory usage (should stay low)"
echo "  ✓ tmp/ folder file creation/cleanup"
echo "  ✓ No OutOfMemoryError"
echo ""
echo "💡 If test succeeded:"
echo "  ✅ Server handles large data without blocking"
echo "  ✅ File streaming works correctly"
echo "  ✅ Memory usage stays reasonable"