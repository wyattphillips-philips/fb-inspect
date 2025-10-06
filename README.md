# FB Inspect - Image Viewer Plugin for Android Studio

A plugin for Android Studio that provides the ability to visualize byte arrays as images during native C++ debugging with LLDB.

## Features

- View byte arrays as images in the debugger
- Configurable width, height, and channels
- Support for common image formats:
  - Grayscale (1 channel)  
  - RGB (3 channels)
  - RGBA (4 channels)
- Interactive dialog for specifying image dimensions
- Real-time image preview from memory data

## Installation

1. Build the plugin using Gradle:
   ```bash
   ./gradlew buildPlugin
   ```

2. Install the generated `.zip` file in Android Studio:
   - Go to `File > Settings > Plugins`
   - Click the gear icon and select `Install Plugin from Disk...`
   - Choose the generated `.zip` file from `build/distributions/`

## Usage

1. Set a breakpoint in your native C++ code
2. Start debugging your Android application
3. When stopped at a breakpoint with a byte array variable:
   - Right-click on the variable in the debugger
   - Select "Show as Image" from the context menu
   - Enter the width, height, and number of channels
   - View the rendered image

## Supported Variable Types

- `unsigned char*`
- `char*` 
- `uint8_t*`
- Any pointer to byte-sized data

## Image Format Interpretation

The plugin interprets raw byte data as images using the following formats:

- **1 Channel (Grayscale)**: Each byte represents a pixel intensity (0-255)
- **3 Channels (RGB)**: Three consecutive bytes per pixel (Red, Green, Blue)
- **4 Channels (RGBA)**: Four consecutive bytes per pixel (Red, Green, Blue, Alpha)

## Development

### Project Structure

```
src/main/kotlin/com/wyattphillips/fbinspect/
├── ShowAsImageAction.kt          # Main action for "Show as Image"
├── ImageViewerDialog.kt          # Image display dialog
├── DebugVariable.kt              # Interface for debugger variables
├── ImageViewerConfigurationType.kt # Plugin configuration
└── ImageViewerDemo.kt            # Standalone demo
```

### Building and Testing

The project includes a demo mode for testing the image viewer without IntelliJ:

```bash
# Compile and run the demo
kotlinc -cp "." src/main/kotlin/com/wyattphillips/fbinspect/*.kt -include-runtime -d demo.jar
java -jar demo.jar
```

### Plugin Integration Points

The plugin integrates with Android Studio through:

1. **Action Registration**: Adds "Show as Image" to debugger context menus
2. **LLDB Integration**: Reads memory data from native debugger
3. **Variable Detection**: Identifies byte array variables automatically
4. **UI Components**: Provides dialogs for dimension input and image display

## Requirements

- Android Studio Arctic Fox (2020.3.1) or later
- Active LLDB debugging session  
- Native C++ code with byte array variables

## License

This project is open source. See LICENSE file for details.
