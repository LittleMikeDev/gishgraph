package uk.co.littlemike.gishgraph.ddag.git

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.api.dsl.xgiven
import java.nio.file.Files
import java.nio.file.Path

object GitDDAGInitialisationTests : Spek({
    var workingDirectory : Path

    given("The working directory does not exist") {
        workingDirectory = Files.createTempDirectory("GitDGraphTest-").resolve("repo")

        on("Initialisation of the repo") {
            GitDDAG(workingDirectory)
        }

        it("Initializes in the new working directory $workingDirectory") {
            workingDirectory.resolve(".git").toFile().isDirectory.should.equal(true)
            GitRepository(workingDirectory).isInitialized().should.equal(true)
        }
    }

    given("The working directory exists") {
        workingDirectory = Files.createTempDirectory("GitDGraphTest-").resolve("repo")
        GitRepository(workingDirectory).init()

        on("Initialisation of the repo") {
            GitDDAG(workingDirectory)
        }

        it("Initializes in the existing working directory") {
            workingDirectory.resolve(".git").toFile().isDirectory.should.equal(true)
            GitRepository(workingDirectory).isInitialized().should.equal(true)
        }
    }
})
