package uk.co.littlemike.gishgraph.ddag.git

import java.nio.file.Files
import java.nio.file.Path


class GitDDAG(
        workingDirectory: Path
) {
    init {
        Files.createDirectories(workingDirectory)
    }
}
