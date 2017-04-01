package uk.co.littlemike.gishgraph.ddag.git

import org.eclipse.jgit.api.Git
import java.nio.file.Files
import java.nio.file.Path

class GitDDag(workingDirectory: Path, myRemote: Remote) {
    private val git = Git.init().setDirectory(workingDirectory.toFile()).call()
    private val myCommits = workingDirectory.resolve(myRemote.id)

    init {
        Files.createDirectories(myCommits)
    }

    fun createInitialCommit(commitId: String, data: ByteArray) {
        myCommits.resolve(commitId).toFile().writeBytes(data)
        git.add().addFilepattern(".").call()
        git.commit()
                .setMessage(commitId)
                .call()
    }
}
