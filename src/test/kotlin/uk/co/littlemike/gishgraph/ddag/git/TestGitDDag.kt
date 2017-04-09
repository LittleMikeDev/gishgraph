package uk.co.littlemike.gishgraph.ddag.git

import java.nio.file.Path

class TestGitDDag(rootDirectory: Path, val id: String) {
    val remoteRepo = GitRepository(rootDirectory.resolve("remote"))
    val workingDirectory: Path = rootDirectory.resolve("local")
    val localRepo = GitRepository(workingDirectory)
    val ddag = GitDDag(localRepo.workingDirectory, remoteRepo.asRemote(id))

    fun remote() = remoteRepo.asRemote(id)

    fun createInitialEvent(eventId: String) = ddag.createInitialEvent(eventId, utf8(eventId))

    fun sync(eventId: String, other: TestGitDDag) = ddag.sync(eventId, utf8(eventId), other.remote())

    private fun utf8(string: String) = string.toByteArray(Charsets.UTF_8)
}