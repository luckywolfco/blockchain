package co.luckywolf.crypto

import arrow.core.*
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.math.BigInteger
import java.security.*
import java.security.spec.ECGenParameterSpec


object CryptoFunctions {

    sealed class CryptoError(error: String) {
        data class UnsupportedProvider(val error: String) : CryptoError(error)
        data class GenerateKeyError(val error: String) : CryptoError(error)
        data class SignatureError(val error: String) : CryptoError(error)
        data class HashError(val error: String) : CryptoError(error)
    }

    fun isProvider(provider: String): Option<Provider> {
        val prv = Security.getProvider(provider)
        return when {
            prv != null -> Some(prv)
            else -> none()
        }
    }

    fun useProvider(provider: String = "sunJCE"): Provider {
        return when (provider) {
            "BC" -> {
                val bouncy = BouncyCastleProvider()
                Security.addProvider(bouncy)
                bouncy
            }
            else -> {
                Security.getProvider(provider)
            }
        }
    }

    fun generateEcdsaKeyPair(provider: Provider, curve: String = "prime192v1"): KeyPair {

        val generator = KeyPairGenerator.getInstance("ECDSA", provider)
        val random = SecureRandom.getInstance("SHA1PRNG")
        val parameterSpec = ECGenParameterSpec(curve)
        generator.initialize(parameterSpec, random) //256 bytes provides an acceptable security level
        return generator.generateKeyPair()
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


    fun sign(
        provider: Provider,
        privateKey: PrivateKey,
        data: ByteArray,
        algorithm: String = "ECDSA"
    ): ByteArray {

        val signature = Signature.getInstance(algorithm, provider).apply {
            initSign(privateKey)
            update(data)
        }
        return signature.sign()
    }

    fun verifySignature(
        provider: Provider,
        publicKey: PublicKey,
        signature: ByteArray,
        data: ByteArray,
        algorithm: String = "ECDSA"
    ): Boolean {

        val verification = Signature.getInstance(algorithm, provider).apply {
            initVerify(publicKey)
            update(data)
        }
        return verification.verify(signature)

    }


}