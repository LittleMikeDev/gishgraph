package uk.co.littlemike.gishgraph.ddag.git

import java.nio.file.Path

class TestGitDDag(rootDirectory: Path, val id: String) {
    val remoteRepo = GitRepository(rootDirectory.resolve("remote"))
    val workingDirectory: Path = rootDirectory.resolve("local")
    val localRepo = GitRepository(workingDirectory)
    val ddag = GitDDag(localRepo.workingDirectory, remoteRepo.asRemote(id))

    fun remote() = remoteRepo.asRemote(id)

    fun createInitialEvent(eventId: String) = ddag.createInitialEvent(eventId, utf8(eventId))

    fun sync(eventId: String, other: TestGitDDag): FetchResult {
        val fetchResult = fetch(other)
        ddag.createEvent(eventId, utf8(eventId), other.remote())
        return fetchResult
    }

    fun fetch(other: TestGitDDag) = ddag.fetch(other.remote())

    private fun utf8(string: String) = string.toByteArray(Charsets.UTF_8)
}