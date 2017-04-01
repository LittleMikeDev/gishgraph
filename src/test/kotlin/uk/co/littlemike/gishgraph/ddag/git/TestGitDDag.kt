package uk.co.littlemike.gishgraph.ddag.git

import java.nio.file.Path

class TestGitDDag(rootDirectory: Path, val id: String) {
    val remoteRepo = GitRepository(rootDirectory.resolve("remote"))
    val workingDirectory: Path = rootDirectory.resolve("local")
    val localRepo = GitRepository(workingDirectory)
    val ddag = GitDDag(localRepo.workingDirectory, remoteRepo.asRemote(id))

    fun asRemote() = remoteRepo.asRemote(id)
}