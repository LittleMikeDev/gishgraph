package uk.co.littlemike.gishgraph.ddag.git

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.MergeCommand
import org.eclipse.jgit.transport.RefSpec
import org.eclipse.jgit.transport.URIish
import java.nio.file.Files
import java.nio.file.Path

class GitDDag(workingDirectory: Path, private val myRemote: Remote) {
    private val git = Git.init().setDirectory(workingDirectory.toFile()).call()
    private val myCommits = workingDirectory.resolve(myRemote.id)

    init {
        Files.createDirectories(myCommits)
        git.commit().setMessage("Created repo for ${myRemote.id}").call()
        git.checkout()
                .setCreateBranch(true)
                .setName(myRemote.id)
                .call()
        git.branchDelete().setBranchNames("master").call()
        addRemote(myRemote)
    }

    fun createInitialCommit(commitId: String, data: ByteArray) {
        writeCommit(commitId, data)
        git.commit().setMessage(commitId).call()
        push()
    }

    fun commit(commitId: String, data: ByteArray, remote: Remote) {
        git.merge()
                .setCommit(false)
                .setFastForward(MergeCommand.FastForwardMode.NO_FF)
                .include(git.repository.exactRef(remote.branchRefName()))
                .call()
        writeCommit(commitId, data)
        git.commit()
                .setMessage(commitId)
                .call()
        push()
    }

    private fun push() {
        git.push().setRemote(myRemote.id).setRefSpecs(myRemote.pushRefSpec()).call()
    }

    private fun writeCommit(commitId: String, data: ByteArray) {
        myCommits.resolve(commitId).toFile().writeBytes(data)
        git.add().addFilepattern(".").call()
    }

    private fun addRemote(remote: Remote) {
        git.remoteAdd().let {
            it.setUri(URIish(remote.url))
            it.setName(remote.id)
            it
        }.call()
    }

    fun fetch(remote: Remote) {
        addRemote(remote)
        git.fetch().setRemote(remote.id).call()
    }

    fun Remote.branchRefName() = "refs/remotes/$id/$id"

    fun Remote.pushRefSpec() = RefSpec(id)
}
