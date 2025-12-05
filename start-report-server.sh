#!/bin/bash
# Script to start HTTP server for Allure report
# Usage: ./start-report-server.sh [port]

PORT=${1:-8080}
REPORT_DIR="target/allure-report"

if [ ! -d "$REPORT_DIR" ]; then
    echo "Error: Report directory not found: $REPORT_DIR"
    echo "Please run tests first to generate the report."
    exit 1
fi

echo "Starting HTTP server on port $PORT..."
echo "Report will be available at: http://localhost:$PORT/index.html"
echo "Press Ctrl+C to stop the server"
echo ""

cd "$REPORT_DIR"
python3 -m http.server "$PORT"

