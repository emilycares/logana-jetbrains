package ch.emilycares.loganaintegration.controller

import ch.emilycares.loganaintegration.model.LoganaMessage
import com.intellij.analysis.problemsView.Problem
import com.intellij.analysis.problemsView.ProblemsCollector
import com.intellij.openapi.project.Project
import java.util.stream.Collectors

class ErrorController {
    var oldMessages = mutableListOf<Problem>()
    var messages = mutableListOf<Problem>()


    fun dispose(project: Project) {
        oldMessages.clear()
        messages.clear()
    }

    fun update(project: Project, newMessages: List<LoganaMessage>) {
        val problemsCollector = ProblemsCollector.getInstance(project)

        this.oldMessages = this.messages
        this.messages = newMessages.stream()
            .map { m -> ProblemConverter.instance!!.toProblem(project, m) }
            .collect(Collectors.toList())

        for (problem in this.oldMessages) {
            problemsCollector.problemDisappeared(problem)
        }
        for (problem in this.messages) {
            problemsCollector.problemAppeared(problem)
        }
    }


    companion object {
        @JvmStatic
        var instance: ErrorController? = null
            get() {
                if (field == null) {
                    field = ErrorController()
                }

                return field
            }
            private set
    }
}
