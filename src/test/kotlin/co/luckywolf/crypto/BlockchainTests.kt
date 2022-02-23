package co.luckywolf.crypto

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue
import co.luckywolf.crypto.BlockChains as chain

class BlockchainTests {

    @Test
    fun verify_ledger() {

        val ledger = chain.BlockChainLedger()
        val genesis = ledger.append(chain.Block(previousHash = "0", data = "Genesis"), 2)

        val exodus = ledger.append(
            chain.Block(
                previousHash = genesis.hash,
                data = "Exodus"
            ), 2
        )

        val leviticus = ledger.append(
            chain.Block(
                previousHash = exodus.hash,
                data = "Leviticus"
            ), 2
        )

        assertTrue(BlockchainFunctions.validate(ledger.blocks, 2))

    }
}