package uk.co.littlemike.gishgraph

inline fun <T : AutoCloseable, R> T.use(block: (T) -> R): R {
    @Suppress("ConvertTryFinallyToUseCall")
    try {
        return block(this)
    } finally {
        this.close()
    }
}