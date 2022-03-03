package co.luckywolf.crypto

import java.math.BigInteger
import java.security.MessageDigest
import java.security.PrivateKey

object BlockchainFunctions {

    fun sign(transaction: Blockchains.Transaction, privateKey: PrivateKey) {

    }

    fun hash(block: Blockchains.Block): ByteArray {
        val data = "${block.previousHash}${block.data}${block.timestamp}${block.nonce}".toByteArray()
        return hash(data)
    }

    fun hash(transaction: Blockchains.Transaction): ByteArray {
        val data = toHex(transaction.publicKeyFrom.encoded) +
                toHex(transaction.publicKeyTo.encoded) +
                transaction.amount.toString() + transaction.sequence
        return hash(data.toByteArray())
    }

    fun hashCheck(curr: Blockchains.Block, prev: Blockchains.Block): Boolean {
        return curr.hash == toHex(hash(curr)) &&
                curr.previousHash == toHex(hash(prev))
    }

    fun isMined(block: Blockchains.Block, difficulty: Int): Boolean {
        val targetPrefix = "0".repeat(difficulty)
        return block.hash.startsWith(targetPrefix)
    }

    fun hash(
        data: ByteArray,
        algorithm: String = "SHA-256"
    ): ByteArray {
        val md = MessageDigest.getInstance(algorithm)
        md.update(data)
        return md.digest()
    }

    fun toHex(byteArray: ByteArray): String =
        String.format("%064x", BigInteger(1, byteArray))

    fun validate(
        blocks: List<Blockchains.Block>,
        difficulty: Int,
    ): Boolean {
        return validate(blocks, difficulty, 1)
    }

    private fun validate(
        blocks: List<Blockchains.Block>,
        difficulty: Int,
        i: Int,
    ): Boolean {

        if (blocks.isEmpty() || blocks.size == 1) return true

        val prev = blocks[i - 1]
        val curr = blocks[i]

        return when (i) {
            blocks.size - 1 -> hashCheck(curr, prev) && isMined(curr, difficulty)
            else -> validate(blocks, difficulty, (i + 1))
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