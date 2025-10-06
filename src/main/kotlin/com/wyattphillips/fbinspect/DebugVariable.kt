package com.wyattphillips.fbinspect

/**
 * Interface representing a debug variable in the native debugger
 */
interface DebugVariable {
    val name: String
    val type: String
    val address: String?
    
    /**
     * Get the raw byte data from this variable
     * This would integrate with LLDB to read memory at the variable's address
     */
    fun getByteData(): ByteArray
    
    /**
     * Get the size of the data in bytes
     */
    fun getDataSize(): Int
}

/**
 * Mock implementation for testing and demonstration
 */
class MockDebugVariable(
    override val name: String,
    override val type: String,
    override val address: String? = "0x7fff12345678"
) : DebugVariable {
    
    override fun getByteData(): ByteArray {
        // Generate sample image data - a simple gradient pattern
        val width = 640
        val height = 480
        val channels = 3
        val data = ByteArray(width * height * channels)
        
        for (y in 0 until height) {
            for (x in 0 until width) {
                val baseIdx = (y * width + x) * channels
                // Create a gradient pattern
                val r = ((x.toFloat() / width) * 255).toInt().toByte()
                val g = ((y.toFloat() / height) * 255).toInt().toByte()
                val b = (((x + y).toFloat() / (width + height)) * 255).toInt().toByte()
                
                data[baseIdx] = r
                data[baseIdx + 1] = g
                data[baseIdx + 2] = b
            }
        }
        
        return data
    }
    
    override fun getDataSize(): Int {
        return getByteData().size
    }
}

/**
 * Real implementation would integrate with LLDB debugger
 */
class LLDBDebugVariable(
    override val name: String,
    override val type: String,
    override val address: String?,
    private val size: Int
) : DebugVariable {
    
    override fun getByteData(): ByteArray {
        // This would use LLDB API to read memory
        // For now, return empty array as placeholder
        TODO("Implement LLDB integration")
    }
    
    override fun getDataSize(): Int {
        return size
    }
}