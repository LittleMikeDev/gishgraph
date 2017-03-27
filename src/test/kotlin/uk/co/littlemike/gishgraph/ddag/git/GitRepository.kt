package uk.co.littlemike.gishgraph.ddag.git

import org.eclipse.jgit.api.Git
import java.nio.file.Path

class GitRepository(val workingDirectory: Path) {
    val git : Git = Git.init().setDirectory(workingDirectory.toFile()).call()

    fun isInitialized() = git.status().call().isClean

    fun asRemote(id: String) = workingDirectory.asRemote(id)
}
