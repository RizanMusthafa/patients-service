#!/bin/bash

# Script to build client, move to server resources, and create JAR file
set -e  # Exit on any error

echo "🚀 Starting build process..."

# Get the script directory (root of the project)
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CLIENT_DIR="${SCRIPT_DIR}/client"
SERVER_DIR="${SCRIPT_DIR}/server"
DASHBOARD_DIR="${SERVER_DIR}/src/main/resources/static/dashboard"

# Step 1: Build the client
echo "📦 Building client application..."
cd "${CLIENT_DIR}"

# Check if yarn is available, otherwise use npm
if command -v yarn &> /dev/null; then
    echo "Using yarn to build..."
    yarn install
    yarn build
else
    echo "Yarn not found, using npm..."
    npm install
    npm run build
fi

# Check if build was successful
if [ ! -d "dist" ]; then
    echo "❌ Error: Client build failed - dist directory not found"
    exit 1
fi

echo "✅ Client build completed successfully"

# Step 2: Clean existing dashboard directory
echo "🧹 Cleaning existing dashboard directory..."
rm -rf "${DASHBOARD_DIR}"
mkdir -p "${DASHBOARD_DIR}"

# Step 3: Copy client build to server resources
echo "📋 Copying client build to server resources..."
cp -r "${CLIENT_DIR}/dist/"* "${DASHBOARD_DIR}/"

echo "✅ Client files copied to server resources"

# Step 4: Build the JAR file
echo "🔨 Building JAR file..."
cd "${SERVER_DIR}"

# Use Maven wrapper if available, otherwise use system maven
if [ -f "./mvnw" ]; then
    echo "Using Maven wrapper..."
    chmod +x ./mvnw
    ./mvnw clean package -DskipTests
else
    echo "Maven wrapper not found, using system maven..."
    mvn clean package -DskipTests
fi

# Check if JAR was created successfully
JAR_FILE=$(find "${SERVER_DIR}/target" -name "*.jar" ! -name "*-sources.jar" ! -name "*-javadoc.jar" | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "❌ Error: JAR file not found after build"
    exit 1
fi

echo "✅ JAR file created successfully: ${JAR_FILE}"

echo ""
echo "🎉 Build process completed successfully!"
echo "📦 JAR location: ${JAR_FILE}"
echo ""
echo "To run the application, use:"
echo "  java -jar ${JAR_FILE}"
echo ""
echo "The dashboard will be available at: http://localhost:8083/dashboard"
