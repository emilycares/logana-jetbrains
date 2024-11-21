package ch.emilycares.loganaintegration.controller

import ch.emilycares.loganaintegration.model.LoganaMessage
import com.intellij.analysis.problemsView.FileProblem
import com.intellij.analysis.problemsView.ProblemsProvider
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

class ProblemConverter private constructor() {
    private val problemsProviderHashMap = HashMap<Project, ProblemsProvider>()

    private fun getProblemProvider(project: Project): ProblemsProvider {
        val provider = problemsProviderHashMap[project]

        if (provider == null) {
            val problemsProvider: ProblemsProvider = object : ProblemsProvider {
                override fun dispose() {
                    ErrorController.instance!!.dispose(project)
                }

                override val project: Project
                    get() = project
            }
            problemsProviderHashMap[project] = problemsProvider
            return problemsProvider
        }
        return provider
    }

    fun toProblem(project: Project, message: LoganaMessage): FileProblem {
        return object : FileProblem {
            override val line: Int
                get() = message.row

            override val file: VirtualFile
                get() = LocalFileSystem.getInstance().findFileByNioFile(message.path)!!

            override val column: Int
                get() = message.col

            override val provider: ProblemsProvider
                get() = getProblemProvider(project)

            override val text: String
                get() = message.text

            override val group: String?
                get() = null

            override val description: String?
                get() = null

            override val icon: Icon
                get() = AllIcons.General.Error
        }
    }

    companion object {
        var instance: ProblemConverter? = null
            get() {
                if (field == null) {
                    field = ProblemConverter()
                }

                return field
            }
            private set
    }
}
