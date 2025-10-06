package com.wyattphillips.fbinspect

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import javax.swing.JComponent
import javax.swing.JPanel

class ImageViewerRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<Any>(project, factory, name) {
    
    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return ImageViewerSettingsEditor()
    }
    
    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        return null // Not used for this configuration
    }
}

class ImageViewerSettingsEditor : SettingsEditor<ImageViewerRunConfiguration>() {
    private val panel = JPanel()
    
    override fun resetEditorFrom(configuration: ImageViewerRunConfiguration) {
        // No specific settings to reset for now
    }
    
    override fun applyEditorTo(configuration: ImageViewerRunConfiguration) {
        // No specific settings to apply for now
    }
    
    override fun createEditor(): JComponent = panel
}