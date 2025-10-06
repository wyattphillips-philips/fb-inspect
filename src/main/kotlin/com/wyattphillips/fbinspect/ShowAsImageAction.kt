package com.wyattphillips.fbinspect

import java.awt.Dimension
import javax.swing.*

// Simplified version for core functionality demonstration
class ShowAsImageAction {
    
    fun showImageFromVariable(variableName: String, width: Int, height: Int, channels: Int) {
        // Get the selected variable from the debugger context
        val selectedVariable = MockDebugVariable(variableName, "unsigned char*")
        
        // Create and show the image viewer
        SwingUtilities.invokeLater {
            val imageViewer = ImageViewerDialog(selectedVariable, width, height, channels)
            imageViewer.isVisible = true
        }
    }
    
    fun createDimensionsDialog(): ImageDimensionsDialog {
        return ImageDimensionsDialog()
    }
}

class ImageDimensionsDialog : JDialog() {
    private val widthField = JTextField("640")
    private val heightField = JTextField("480")
    private val channelsField = JTextField("3")
    private var result = false
    
    val width: Int get() = widthField.text.toIntOrNull() ?: 640
    val height: Int get() = heightField.text.toIntOrNull() ?: 480
    val channels: Int get() = channelsField.text.toIntOrNull() ?: 3
    
    init {
        title = "Image Dimensions"
        isModal = true
        setupUI()
    }
    
    private fun setupUI() {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        
        // Add form fields
        panel.add(createFieldPanel("Width:", widthField))
        panel.add(createFieldPanel("Height:", heightField))
        panel.add(createFieldPanel("Channels (1=Gray, 3=RGB, 4=RGBA):", channelsField))
        
        // Add buttons
        val buttonPanel = JPanel()
        val okButton = JButton("OK")
        val cancelButton = JButton("Cancel")
        
        okButton.addActionListener { 
            if (validateInput()) {
                result = true
                dispose()
            }
        }
        cancelButton.addActionListener { dispose() }
        
        buttonPanel.add(okButton)
        buttonPanel.add(cancelButton)
        panel.add(buttonPanel)
        
        add(panel)
        pack()
        setLocationRelativeTo(null)
    }
    
    private fun createFieldPanel(label: String, field: JTextField): JPanel {
        val panel = JPanel()
        panel.add(JLabel(label))
        panel.add(field)
        return panel
    }
    
    private fun validateInput(): Boolean {
        if (width <= 0) {
            JOptionPane.showMessageDialog(this, "Width must be positive")
            return false
        }
        if (height <= 0) {
            JOptionPane.showMessageDialog(this, "Height must be positive")
            return false
        }
        if (channels !in 1..4) {
            JOptionPane.showMessageDialog(this, "Channels must be 1, 3, or 4")
            return false
        }
        return true
    }
    
    fun showAndGet(): Boolean {
        isVisible = true
        return result
    }
}