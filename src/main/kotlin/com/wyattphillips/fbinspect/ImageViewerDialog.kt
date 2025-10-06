package com.wyattphillips.fbinspect

import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.*

class ImageViewerDialog(
    private val variable: DebugVariable,
    private val width: Int,
    private val height: Int,
    private val channels: Int
) : JFrame("Image Viewer - ${variable.name}") {
    
    private lateinit var imagePanel: ImagePanel
    
    init {
        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        setupUI()
        loadAndDisplayImage()
        pack()
        setLocationRelativeTo(null)
    }
    
    private fun setupUI() {
        imagePanel = ImagePanel()
        val scrollPane = JScrollPane(imagePanel)
        scrollPane.preferredSize = Dimension(800, 600)
        
        val panel = JPanel(BorderLayout())
        panel.add(JLabel("Variable: ${variable.name} (${variable.type})"), BorderLayout.NORTH)
        panel.add(scrollPane, BorderLayout.CENTER)
        
        // Add close button
        val buttonPanel = JPanel()
        val closeButton = JButton("Close")
        closeButton.addActionListener { dispose() }
        buttonPanel.add(closeButton)
        panel.add(buttonPanel, BorderLayout.SOUTH)
        
        add(panel)
    }
    
    private fun loadAndDisplayImage() {
        try {
            // Get byte data from the variable
            val imageData = variable.getByteData()
            
            if (imageData.size < width * height * channels) {
                showError("Insufficient data: expected ${width * height * channels} bytes, got ${imageData.size}")
                return
            }
            
            val image = createImageFromBytes(imageData, width, height, channels)
            imagePanel.setImage(image)
            
        } catch (e: Exception) {
            showError("Failed to create image: ${e.message}")
        }
    }
    
    private fun createImageFromBytes(data: ByteArray, width: Int, height: Int, channels: Int): BufferedImage {
        val imageType = when (channels) {
            1 -> BufferedImage.TYPE_BYTE_GRAY
            3 -> BufferedImage.TYPE_3BYTE_BGR
            4 -> BufferedImage.TYPE_4BYTE_ABGR
            else -> throw IllegalArgumentException("Unsupported channel count: $channels")
        }
        
        val image = BufferedImage(width, height, imageType)
        
        when (channels) {
            1 -> {
                // Grayscale
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        val idx = y * width + x
                        if (idx < data.size) {
                            val gray = data[idx].toInt() and 0xFF
                            val rgb = Color(gray, gray, gray).rgb
                            image.setRGB(x, y, rgb)
                        }
                    }
                }
            }
            3 -> {
                // RGB
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        val baseIdx = (y * width + x) * 3
                        if (baseIdx + 2 < data.size) {
                            val r = data[baseIdx].toInt() and 0xFF
                            val g = data[baseIdx + 1].toInt() and 0xFF
                            val b = data[baseIdx + 2].toInt() and 0xFF
                            val rgb = Color(r, g, b).rgb
                            image.setRGB(x, y, rgb)
                        }
                    }
                }
            }
            4 -> {
                // RGBA
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        val baseIdx = (y * width + x) * 4
                        if (baseIdx + 3 < data.size) {
                            val r = data[baseIdx].toInt() and 0xFF
                            val g = data[baseIdx + 1].toInt() and 0xFF
                            val b = data[baseIdx + 2].toInt() and 0xFF
                            val a = data[baseIdx + 3].toInt() and 0xFF
                            val rgb = Color(r, g, b, a).rgb
                            image.setRGB(x, y, rgb)
                        }
                    }
                }
            }
        }
        
        return image
    }
    
    private fun showError(message: String) {
        imagePanel.showError(message)
    }
}

class ImagePanel : JPanel() {
    private var image: BufferedImage? = null
    private var errorMessage: String? = null
    
    init {
        background = Color.WHITE
    }
    
    fun setImage(image: BufferedImage) {
        this.image = image
        this.errorMessage = null
        preferredSize = Dimension(image.width, image.height)
        revalidate()
        repaint()
    }
    
    fun showError(message: String) {
        this.errorMessage = message
        this.image = null
        preferredSize = Dimension(400, 200)
        revalidate()
        repaint()
    }
    
    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        
        when {
            image != null -> {
                g2d.drawImage(image, 0, 0, null)
            }
            errorMessage != null -> {
                g2d.color = Color.RED
                g2d.drawString(errorMessage!!, 20, 50)
            }
            else -> {
                g2d.color = Color.GRAY
                g2d.drawString("No image data", 20, 50)
            }
        }
    }
}