package uk.co.littlemike.gishgraph.ddag.git

import org.eclipse.jgit.api.Git
import java.nio.file.Files
import java.nio.file.Path


class GitDDag(workingDirectory: Path, myRemote: Remote) {
    val git = Git.init().setDirectory(workingDirectory.toFile()).call()

    init {
        Files.createDirectories(workingDirectory.resolve(myRemote.id))
    }
}
