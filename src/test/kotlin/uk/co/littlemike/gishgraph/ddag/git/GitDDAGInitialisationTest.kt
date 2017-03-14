package uk.co.littlemike.gishgraph.ddag.git

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.nio.file.Files
import java.nio.file.Path

object GitDDAGInitialisationTest : Spek({
    var workingDirectory : Path

    given("The repository is initialised in a new directory") {
        workingDirectory = Files.createTempDirectory("GitDGraphTest-").resolve("repo")

        beforeGroup {
            GitDDAG(workingDirectory)
        }

        it("Creates a new folder for the working directory") {
            workingDirectory.toFile().isDirectory.should.equal(true)
        }
    }

    given("The repository is initialised in a directory that already exists") {
        workingDirectory = Files.createTempDirectory("GitDGraphTest").resolve("repo")
        GitDDAG(workingDirectory)

        it("Does not fail") {
            workingDirectory.toFile().isDirectory.should.equal(true)
        }
    }
})
