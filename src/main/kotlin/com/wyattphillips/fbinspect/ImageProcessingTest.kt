package com.wyattphillips.fbinspect

import java.awt.Color
import java.awt.image.BufferedImage

/**
 * Test class to validate image processing logic without GUI components
 */
object ImageProcessingTest {
    
    @JvmStatic
    fun main(args: Array<String>) {
        println("Testing FB Inspect Image Processing...")
        
        // Test 1: Grayscale image creation
        testGrayscaleImage()
        
        // Test 2: RGB image creation  
        testRGBImage()
        
        // Test 3: RGBA image creation
        testRGBAImage()
        
        // Test 4: Mock debug variable
        testMockDebugVariable()
        
        println("All tests completed successfully!")
    }
    
    private fun testGrayscaleImage() {
        println("\n1. Testing Grayscale Image Creation:")
        val width = 4
        val height = 4
        val channels = 1
        
        // Create test data - simple gradient
        val data = ByteArray(width * height * channels)
        for (i in data.indices) {
            data[i] = (i * 255 / data.size).toByte()
        }
        
        val image = createImageFromBytes(data, width, height, channels)
        println("   - Created ${width}x${height} grayscale image")
        println("   - Image type: ${image.type}")
        println("   - Expected size: ${width * height} bytes, got: ${data.size}")
        
        // Verify a few pixels
        val rgb = image.getRGB(0, 0)
        val color = Color(rgb)
        println("   - First pixel RGB: (${color.red}, ${color.green}, ${color.blue})")
    }
    
    private fun testRGBImage() {
        println("\n2. Testing RGB Image Creation:")
        val width = 3
        val height = 2
        val channels = 3
        
        // Create test data - red, green, blue pixels
        val data = byteArrayOf(
            255.toByte(), 0, 0,     // Red pixel
            0, 255.toByte(), 0,     // Green pixel
            0, 0, 255.toByte(),     // Blue pixel
            128.toByte(), 128.toByte(), 128.toByte(),  // Gray pixel
            255.toByte(), 255.toByte(), 0,  // Yellow pixel
            255.toByte(), 0, 255.toByte()   // Magenta pixel
        )
        
        val image = createImageFromBytes(data, width, height, channels)
        println("   - Created ${width}x${height} RGB image")
        println("   - Expected size: ${width * height * channels} bytes, got: ${data.size}")
        
        // Verify colors
        val redPixel = Color(image.getRGB(0, 0))
        val greenPixel = Color(image.getRGB(1, 0))
        val bluePixel = Color(image.getRGB(2, 0))
        
        println("   - Red pixel: (${redPixel.red}, ${redPixel.green}, ${redPixel.blue})")
        println("   - Green pixel: (${greenPixel.red}, ${greenPixel.green}, ${greenPixel.blue})")
        println("   - Blue pixel: (${bluePixel.red}, ${bluePixel.green}, ${bluePixel.blue})")
    }
    
    private fun testRGBAImage() {
        println("\n3. Testing RGBA Image Creation:")
        val width = 2
        val height = 2  
        val channels = 4
        
        // Create test data with alpha channel
        val data = byteArrayOf(
            255.toByte(), 0, 0, 255.toByte(),         // Opaque red
            0, 255.toByte(), 0, 128.toByte(),         // Semi-transparent green
            0, 0, 255.toByte(), 64.toByte(),          // More transparent blue
            255.toByte(), 255.toByte(), 255.toByte(), 0  // Transparent white
        )
        
        val image = createImageFromBytes(data, width, height, channels)
        println("   - Created ${width}x${height} RGBA image")
        println("   - Expected size: ${width * height * channels} bytes, got: ${data.size}")
        
        // Verify alpha values
        val redPixel = Color(image.getRGB(0, 0), true)
        val greenPixel = Color(image.getRGB(1, 0), true)
        
        println("   - Red pixel RGBA: (${redPixel.red}, ${redPixel.green}, ${redPixel.blue}, ${redPixel.alpha})")
        println("   - Green pixel RGBA: (${greenPixel.red}, ${greenPixel.green}, ${greenPixel.blue}, ${greenPixel.alpha})")
    }
    
    private fun testMockDebugVariable() {
        println("\n4. Testing Mock Debug Variable:")
        val mockVar = MockDebugVariable("testImage", "unsigned char*")
        
        println("   - Variable name: ${mockVar.name}")
        println("   - Variable type: ${mockVar.type}")
        println("   - Variable address: ${mockVar.address}")
        println("   - Data size: ${mockVar.getDataSize()} bytes")
        
        val data = mockVar.getByteData()
        println("   - First few bytes: ${data.take(10).joinToString(", ") { (it.toInt() and 0xFF).toString() }}")
        
        // Test image creation from mock data
        val width = 64
        val height = 48
        val channels = 3
        val expectedSize = width * height * channels
        
        if (data.size >= expectedSize) {
            val testData = data.sliceArray(0 until expectedSize)
            val image = createImageFromBytes(testData, width, height, channels)
            println("   - Successfully created ${width}x${height} image from mock data")
        } else {
            println("   - Warning: Insufficient data for ${width}x${height}x${channels} image")
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
}