package uk.co.littlemike.gishgraph.ddag.git

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.nio.file.Files

object GitDDagCommitTests : Spek({
    val root = Files.createTempDirectory("GitDGraphTest-")
    val localDirectory = root.resolve("local")
    val remoteDirectory = root.resolve("remote")
    val myRemote = Remote("my-id", remoteDirectory.toAbsolutePath().toUri().path)
    val ddag = GitDDag(localDirectory, myRemote)
    val remoteRepository = GitRepository(root.resolve("remote"))

    on ("Initialisation of repo") {
        val myCommits = localDirectory.resolve(myRemote.id)

        it("Creates a folder for local commits") {
            myCommits.toFile().isDirectory.should.equal(true)
        }
    }
})
