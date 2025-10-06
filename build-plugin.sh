#!/bin/bash

# Simple build script for FB Inspect Plugin
# This script helps build the plugin when Gradle has compatibility issues

set -e

echo "Building FB Inspect Plugin..."

# Create build directory
mkdir -p build/classes/kotlin
mkdir -p build/plugin

# Set classpath for IntelliJ platform (you'll need to adjust this path)
# INTELLIJ_HOME should point to your IntelliJ IDEA installation
INTELLIJ_HOME="${INTELLIJ_HOME:-/Applications/IntelliJ IDEA.app/Contents}"

if [ ! -d "$INTELLIJ_HOME" ]; then
    echo "Warning: IntelliJ home not found at $INTELLIJ_HOME"
    echo "Set INTELLIJ_HOME environment variable to your IntelliJ installation"
    echo "For testing core functionality without IntelliJ platform:"
    echo ""
    
    # Test core functionality
    echo "Testing core image processing functionality..."
    kotlinc src/main/kotlin/com/wyattphillips/fbinspect/DebugVariable.kt \
           src/main/kotlin/com/wyattphillips/fbinspect/ImageProcessingTest.kt \
           -include-runtime -d build/fb-inspect-test.jar
    
    echo "Running tests..."
    java -jar build/fb-inspect-test.jar
    
    echo ""
    echo "Core functionality test completed successfully!"
    echo "To build the full plugin, set INTELLIJ_HOME and re-run this script."
    exit 0
fi

# Build with IntelliJ platform classpath
IDEA_JAR="$INTELLIJ_HOME/lib/idea.jar"
PLATFORM_API_JAR="$INTELLIJ_HOME/lib/platform-api.jar"

if [ ! -f "$IDEA_JAR" ]; then
    echo "Error: Could not find IntelliJ platform jars"
    echo "Expected: $IDEA_JAR"
    exit 1
fi

echo "Compiling Kotlin sources..."
kotlinc -cp "$IDEA_JAR:$PLATFORM_API_JAR" \
        src/main/kotlin/com/wyattphillips/fbinspect/*.kt \
        -d build/classes/kotlin

echo "Creating plugin structure..."
cp -r src/main/resources/* build/plugin/

# Create plugin jar
echo "Packaging plugin..."
(cd build/classes/kotlin && jar cf ../../plugin/lib/fb-inspect.jar *)

# Create final plugin zip
echo "Creating plugin distribution..."
(cd build && zip -r ../fb-inspect-plugin.zip plugin/)

echo "Plugin built successfully: fb-inspect-plugin.zip"
echo ""
echo "To install:"
echo "1. Open Android Studio"
echo "2. Go to File > Settings > Plugins"  
echo "3. Click gear icon > Install Plugin from Disk..."
echo "4. Select fb-inspect-plugin.zip"
echo "5. Restart Android Studio"