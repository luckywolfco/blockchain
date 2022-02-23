package co.luckywolf.crypto

import java.math.BigInteger
import java.security.MessageDigest

object BlockchainFunctions {

    val hash: Func<Blockchains.Block, ByteArray> by lazy { { hash(it.data, it.previousHash, it.timestamp, it.nonce) } }
    val formatBitcoinHash: Func<ByteArray, String> = { toHex(it) }

    val validate: Func2<List<Blockchains.Block>, Int, Boolean> = { blocks, difficulty ->
        validate(blocks, difficulty, 1, matchHash, isMined)
    }
    val matchHash: Func2<Blockchains.Block, Blockchains.Block, Boolean> = { curr, prev ->
        curr.hash == formatBitcoinHash(hash(curr)) &&
                curr.previousHash == formatBitcoinHash(hash(prev))
    }
    val isMined: Func2<Blockchains.Block, Int, Boolean> = { c, d ->
        val targetPrefix = "0".repeat(d)
        c.hash.startsWith(targetPrefix)
    }

    fun hash(
        previousHash: String,
        data: String,
        timestamp: Long,
        nonce: Long,
        algorithm: String = "SHA-256"
    ): ByteArray {
        val md = MessageDigest.getInstance(algorithm)
        md.update("$previousHash$data$timestamp$nonce".toByteArray())
        return md.digest()
    }

    fun toHex(byteArray: ByteArray): String =
        String.format("%064x", BigInteger(1, byteArray))

    fun validate(
        blocks: List<Blockchains.Block>,
        difficulty: Int,
        i: Int,
        hashCheck: (Blockchains.Block, Blockchains.Block) -> Boolean,
        targetCheck: (Blockchains.Block, Int) -> Boolean
    ): Boolean {

        if (blocks.isEmpty() || blocks.size == 1) return true

        val prev = blocks[i - 1]
        val curr = blocks[i]

        return when (i) {
            blocks.size - 1 -> hashCheck(curr, prev) && targetCheck(curr, difficulty)
            else -> validate(blocks, difficulty, (i + 1), hashCheck, targetCheck)
        }
    }

    fun mine(block: Blockchains.Block, difficulty: Int): Blockchains.Block {

        if (isMined(block, difficulty))
            return block

        var mined = block.copy()
        while (!isMined(mined, difficulty)) {
            mined = mined.copy(nonce = mined.nonce + 1)
        }

        return mined
    }
}