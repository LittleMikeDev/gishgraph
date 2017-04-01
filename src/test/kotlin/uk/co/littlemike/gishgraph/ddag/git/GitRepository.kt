package uk.co.littlemike.gishgraph.ddag.git

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevWalk
import uk.co.littlemike.gishgraph.extensions.use
import java.nio.file.Path

class GitRepository(val workingDirectory: Path) {
    val git : Git = Git.init().setDirectory(workingDirectory.toFile()).call()

    fun isClean() = git.status().call().isClean

    fun asRemote(id: String) = workingDirectory.remote(id)

    fun refExists(refName: String) = git.repository.findRef(refName) != null

    fun findCommit(refName: String): RevCommit? {
        val commitId = git.repository.findRef(refName)?.leaf?.objectId ?: return null
        RevWalk(git.repository).use {
            return it.parseCommit(commitId)
        }
    }
}
