package uk.co.littlemike.gishgraph.ddag.git

import org.eclipse.jgit.api.Git
import java.nio.file.Files

class CommitFormat(private val git: Git, private val myRemote: Remote) {
    private val workingDirectory = git.repository.workTree.toPath()
    private val myCommits = workingDirectory.resolve(myRemote.id)

    init {
        Files.createDirectories(myCommits)
    }

    fun write(commitId: String, data: ByteArray) {
        myCommits.resolve(commitId).toFile().writeBytes(data)
        git.add().addFilepattern(".").call()
        git.commit()
                .setMessage(commitId)
                .call()
    }

    fun createInitialCommit() {
        git.commit().setMessage("Created repo for ${myRemote.id}").call()
    }
}