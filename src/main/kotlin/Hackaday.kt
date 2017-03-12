import org.eclipse.jgit.api.Git
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File
import java.time.Instant

fun greeting(): String {
    return "Hello world!"
}

class Member {

}

class Event(val owner: Member, val parent: Event, val stepParent: Event, val signature: Any?) {
    val timestamp = Instant.now()

    fun tryingThings() {
        val repo = FileRepositoryBuilder().build()
        val git = Git(repo)
        val fetchResult = git.fetch()
                .setCheckFetchedObjects(true)
                .setRemote("Some remote")
                .call()
        val advertisedRefs = fetchResult.advertisedRefs // Member HEADs as this remote sees them. Can this be used in conjunction with our own member HEADs to build the list of new events?

    }
}

interface GitAdaptor

class JGitAdaptor(filesystem: WorkingDirectory) : GitAdaptor

class WorkingDirectory(repositoryLocation: File)

class GitDGraph(
        private val workingDirectory: WorkingDirectory,
        private val localRepository: GitAdaptor
) {
    fun doIt() {
        
    }
}