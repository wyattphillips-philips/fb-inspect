package com.wyattphillips.fbinspect

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import javax.swing.Icon

class ImageViewerConfigurationType : ConfigurationType {
    override fun getDisplayName(): String = "FB Inspect Image Viewer"
    
    override fun getConfigurationTypeDescription(): String = 
        "Configuration for FB Inspect image viewer plugin"
    
    override fun getIcon(): Icon? = null // Could add an icon here
    
    override fun getId(): String = "FB_INSPECT_IMAGE_VIEWER"
    
    override fun getConfigurationFactories(): Array<ConfigurationFactory> = 
        arrayOf(ImageViewerConfigurationFactory(this))
}

class ImageViewerConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun getId(): String = "FB_INSPECT_FACTORY"
    
    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return ImageViewerRunConfiguration(project, this, "FB Inspect")
    }
}