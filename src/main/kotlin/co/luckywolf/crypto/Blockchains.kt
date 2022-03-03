package co.luckywolf.crypto

import co.luckywolf.crypto.BlockchainFunctions.hash
import co.luckywolf.crypto.BlockchainFunctions.mine
import co.luckywolf.crypto.BlockchainFunctions.toHex
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey
import java.time.Instant

object Blockchains {

    //the "nonce" in a bitcoin block is a 32-bit (4-byte) field whose value is adjusted by miners so that
    //the hash of the block will be less than or equal to the current target of the network.
    data class Block(
        val previousHash: String,
        val data: String,
        val timestamp: Long = Instant.now().toEpochMilli(),
        var nonce: Long = 0L,
    ) {
        val hash = toHex(hash(this))
    }

    data class CryptoWallet(val keyPair: KeyPair) {

        fun addTransaction(transaction: Transaction) {

        }

        fun balance(): Float {
            return 9F  //getMyTransactions().sumBy { it.amount }
        }
    }


    //Inputs, which are references to previous transactions that prove the sender has funds to send.
    //Outputs, which shows the amount relevant addresses received in the transaction. ( These outputs are referenced as inputs in new transactions )
    data class Transaction(
        val publicKeyFrom: PublicKey,
        val publicKeyTo: PublicKey,
        val amount: Float
    ) {
        val sequence = 0

        fun sign(privateKey: PrivateKey) {
            BlockchainFunctions.sign(this, privateKey)
        }

//        fun hash(): Either<CryptoFunctions.CryptoError, String> {
//            return hash(this).map { CryptoFunctions.toHex(it) }
//        }
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