package uk.co.littlemike.gishgraph.ddag.git

import java.util.*

data class SyncEvent(val id: String, val data: ByteArray) {

    override fun equals(other: Any?): Boolean {
        return other is SyncEvent
                && other.id == id
                && Arrays.equals(other.data, data)
    }

    override fun hashCode(): Int {
        return id.hashCode() + Arrays.hashCode(data)
    }
}
