package uk.co.littlemike.gishgraph.ddag.git

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.internal.storage.file.FileRepository
import java.io.File
import java.nio.file.Path


class GitDDAG(workingDirectory: Path) {
    val git = Git.init().setDirectory(workingDirectory.toFile()).call()
}