package ch.emilycares.loganaintegration

import ch.emilycares.loganaintegration.controller.ErrorController
import ch.emilycares.loganaintegration.model.LoganaMessage
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.guessModuleDir
import com.intellij.openapi.roots.FileIndexFacade
import com.intellij.openapi.ui.Messages
import java.nio.file.Path

class LoadLoganaReportAction : AnAction() {
    private val helpLine =
        "In order for the logana extension to read the reports file. Please open a file of that project that you want to see the error's from."
    private val helpTitle = "PLEASE OPEN A FILE"

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project;
        if (project == null) {
            Messages.showErrorDialog(helpLine, helpTitle)
            return
        }
        val selectedEditor = FileEditorManager.getInstance(project)
            .selectedEditor;

        if (selectedEditor == null) {
            Messages.showErrorDialog(helpLine, helpTitle)
            return
        }
        val file = selectedEditor.file;
        val module = FileIndexFacade.getInstance(project).getModuleForFile(file)
        val projectDirectory = Path.of(module!!.guessModuleDir()!!.path)

        val messages: List<LoganaMessage> = parseReportFile(projectDirectory)

        ErrorController.instance!!.update(project, messages)
    }
}