package uk.co.littlemike.gishgraph.ddag.git

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.MergeCommand
import org.eclipse.jgit.transport.RefSpec
import org.eclipse.jgit.transport.URIish
import java.nio.file.Path

class GitDDag(workingDirectory: Path, private val myRemote: Remote) {
    private val git = Git.init().setDirectory(workingDirectory.toFile()).call()
    private val commitFormat = CommitFormat(git, myRemote)
    private val branchDiffer = BranchDiffer(git.repository)

    init {
        commitFormat.createInitialCommit()
        git.checkout()
                .setCreateBranch(true)
                .setName(myRemote.id)
                .call()
        git.branchDelete().setBranchNames("master").call()
        addRemote(myRemote)
    }

    fun createInitialEvent(commitId: String, data: ByteArray) {
        commitFormat.write(commitId, data)
        push()
    }

    fun createEvent(commitId: String, data: ByteArray, remote: Remote) {
        git.merge()
                .setCommit(false)
                .setFastForward(MergeCommand.FastForwardMode.NO_FF)
                .include(git.repository.exactRef(remote.branchRefName()))
                .call()
        commitFormat.write(commitId, data)
        push()
    }

    private fun push() {
        git.push().setRemote(myRemote.id).setRefSpecs(myRemote.pushRefSpec()).call()
    }

    private fun addRemote(remote: Remote) {
        git.remoteAdd().apply {
            setUri(URIish(remote.url))
            setName(remote.id)
        }.call()
    }

    fun fetch(remote: Remote): FetchResult {
        addRemote(remote)
        git.fetch().setRemote(remote.id).call()
        val fetchedCommits = branchDiffer.diff(myRemote.branchRefName(), remote.branchRefName())
        return FetchResult(commitFormat.parseAll(fetchedCommits, ::FetchedEvent))
    }

    fun Remote.branchRefName() = "refs/remotes/$id/$id"

    fun Remote.pushRefSpec() = RefSpec(id)
}
