# Installation Instructions for FB Inspect Plugin

## Prerequisites

- Android Studio Arctic Fox (2020.3.1) or later  
- Java 17 or later
- Kotlin 1.9.0 or later
- Native C++ debugging capabilities enabled

## Building the Plugin

### Option 1: Using IntelliJ IDEA/Android Studio (Recommended)

1. Clone the repository:
   ```bash
   git clone https://github.com/wyattphillips-philips/fb-inspect.git
   cd fb-inspect
   ```

2. Open the project in IntelliJ IDEA or Android Studio

3. Ensure you have the IntelliJ Plugin Development plugin enabled

4. Build the plugin:
   - In the IDE: Run → Run 'runIde' or use Gradle task `./gradlew runIde`
   - This will start a new IDE instance with the plugin installed

5. Package for distribution:
   ```bash
   ./gradlew buildPlugin
   ```
   The packaged plugin will be in `build/distributions/fb-inspect-1.0.0.zip`

### Option 2: Command Line Build (Alternative)

If you encounter Gradle compatibility issues, you can build individual components:

1. Test the core functionality:
   ```bash
   kotlinc src/main/kotlin/com/wyattphillips/fbinspect/DebugVariable.kt src/main/kotlin/com/wyattphillips/fbinspect/ImageProcessingTest.kt -include-runtime -d fb-inspect-test.jar
   java -jar fb-inspect-test.jar
   ```

2. For the plugin, you'll need to resolve the Gradle/IntelliJ platform version compatibility in `build.gradle.kts`

## Installing the Plugin

### From Local Build

1. Build the plugin using the steps above
2. In Android Studio: `File > Settings > Plugins`
3. Click the gear icon → `Install Plugin from Disk...`
4. Select the `.zip` file from `build/distributions/`
5. Restart Android Studio

### From Plugin Repository (Future)

Once published to the JetBrains Plugin Repository:
1. `File > Settings > Plugins`
2. Search for "FB Inspect" 
3. Click Install

## Configuration

The plugin works automatically when:
1. You're debugging native C++ code with LLDB
2. You have byte array variables in scope
3. You right-click on a variable and select "Show as Image"

## Troubleshooting

### Build Issues

1. **Gradle Compatibility**: The plugin requires specific versions of Gradle and the IntelliJ plugin. If you encounter issues:
   - Try updating to IntelliJ IDEA 2023.2+
   - Use Gradle 8.2+
   - Check the `build.gradle.kts` for compatible versions

2. **Missing Dependencies**: Ensure you have:
   - IntelliJ Plugin Development support
   - Native debugging plugins enabled
   - Proper JDK version (17+)

### Runtime Issues

1. **Action Not Visible**: 
   - Ensure you're in a debugging session
   - Check that LLDB debugger is active
   - Verify the variable is a byte array type

2. **Image Not Displaying**:
   - Check that dimensions match your data size
   - Verify channels parameter (1=Gray, 3=RGB, 4=RGBA)
   - Ensure sufficient memory data is available

### Plugin Development

For developers wanting to modify the plugin:

1. The main action is in `ShowAsImageAction.kt`
2. Image processing logic is in `ImageViewerDialog.kt`
3. Debug variable interface is in `DebugVariable.kt`
4. Plugin configuration is in `src/main/resources/META-INF/plugin.xml`

## Version Compatibility

- **Android Studio**: 2020.3+ (Arctic Fox or newer)
- **IntelliJ Platform**: 2022.3+
- **LLDB**: Version included with Android Studio
- **JDK**: 17+ (required by modern IntelliJ platform)

## Support

If you encounter issues:
1. Check the IDE logs: `Help > Show Log in Files`  
2. Look for plugin-related error messages
3. Report issues on the GitHub repository
4. Include your Android Studio version and system details