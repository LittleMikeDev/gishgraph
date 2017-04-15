package uk.co.littlemike.gishgraph.ddag.git

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.treewalk.TreeWalk
import uk.co.littlemike.gishgraph.extensions.use
import java.io.ByteArrayOutputStream
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
                .setMessage("${myRemote.id}/$commitId")
                .call()
    }

    fun createInitialCommit() {
        git.commit().setMessage("Created repo for ${myRemote.id}").call()
    }

    fun <T> parse(commit: RevCommit, builder: (String, ByteArray) -> T): T {
        val (remote, id) = commit.fullMessage.split("/")
        val data = TreeWalk.forPath(git.repository, "$remote/$id", commit.tree).use {
            val dataStream = ByteArrayOutputStream()
            git.repository.open(it.getObjectId(0)).copyTo(dataStream)
            dataStream.toByteArray()
        }
        return builder.invoke(id, data)
    }

    fun <T> parseAll(commits: List<RevCommit>, builder: (String, ByteArray) -> T) = commits
            .filter { !it.isInitialCommit() }
            .map { parse(it, builder) }

    fun RevCommit.isInitialCommit() = parentCount == 0
}
