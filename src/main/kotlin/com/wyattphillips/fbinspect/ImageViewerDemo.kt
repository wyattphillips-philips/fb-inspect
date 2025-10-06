package com.wyattphillips.fbinspect

import javax.swing.SwingUtilities

/**
 * Demo class to test the image viewer functionality without IntelliJ integration
 */
object ImageViewerDemo {
    
    @JvmStatic
    fun main(args: Array<String>) {
        SwingUtilities.invokeLater {
            // Create a demo with default dimensions
            val action = ShowAsImageAction()
            
            // Show dimensions dialog
            val dimensionsDialog = action.createDimensionsDialog()
            
            if (dimensionsDialog.showAndGet()) {
                val width = dimensionsDialog.width
                val height = dimensionsDialog.height  
                val channels = dimensionsDialog.channels
                
                println("Selected dimensions: ${width}x${height}x${channels}")
                
                // Show the image viewer with a sample variable
                action.showImageFromVariable("sampleImageData", width, height, channels)
            } else {
                println("Dialog cancelled")
                System.exit(0)
            }
        }
    }
}