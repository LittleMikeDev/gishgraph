package uk.co.littlemike.gishgraph.ddag.git

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.lib.Constants
import java.nio.file.Path

class GitRepository(val workingDirectory: Path) {
    val git = Git.init().setDirectory(workingDirectory.toFile()).call()

    fun isInitialized() = git.status().call().isClean

    fun init() {
        // Do nothing, it is always initialized by JGit
    }

    fun asRemote(id: String) = workingDirectory.asRemote(id)
}
