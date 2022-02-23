package co.luckywolf.crypto

import co.luckywolf.crypto.BlockchainFunctions.formatBitcoinHash
import co.luckywolf.crypto.BlockchainFunctions.hash
import co.luckywolf.crypto.BlockchainFunctions.mine
import java.time.Instant

typealias Func<A, B> = (A) -> B
typealias Func2<A, B, C> = (A, B) -> C

object Blockchains {

    //the "nonce" in a bitcoin block is a 32-bit (4-byte) field whose value is adjusted by miners so that
    //the hash of the block will be less than or equal to the current target of the network.
    data class Block(
        val previousHash: String,
        val data: String,
        val timestamp: Long = Instant.now().toEpochMilli(),
        var nonce: Long = 0L,
    ) {
        val hash = formatBitcoinHash(hash(this))
    }


    class BlockChainLedger {

        var blocks = mutableListOf<Block>()

        fun append(block: Block, difficulty: Int): Block {
            val mined = mine(block, difficulty)
            blocks.add(mined)
            return mined
        }
    }
}

fun main(args: Array<String>) {

}