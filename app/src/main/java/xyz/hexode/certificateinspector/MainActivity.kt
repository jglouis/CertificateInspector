package xyz.hexode.certificateinspector

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import xyz.hexode.certificateinspector.util.toHex
import java.io.ByteArrayInputStream
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        @Suppress("DEPRECATION")
        @SuppressLint("PackageManagerGetSignatures")
        val signatures = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures
        val signature = signatures.first()
        val certificateStream = ByteArrayInputStream(signature.toByteArray())
        try {
            val certificateFactory = CertificateFactory.getInstance("X509")
            val x509cert = certificateFactory.generateCertificate(certificateStream) as X509Certificate
            Log.d(TAG, "Serial Number: ${x509cert.serialNumber}")
            Log.d(TAG, "Public key: ${x509cert.publicKey}")
            Log.d(TAG, "Issuer: ${x509cert.issuerDN}")
            Log.d(TAG, "Signature Algorithm: ${x509cert.sigAlgName}")
            Log.d(TAG, "Raw Signature: ${x509cert.signature.toHex()}")

            serialNumberTextView.text = "Serial number: ${x509cert.serialNumber}"
            publicKeyTextView.text = "Public key\n${x509cert.publicKey}"
            issuerTextView.text = "Issuer: ${x509cert.issuerDN}"
            subjectTextView.text = "Subject: ${x509cert.subjectDN}"
            validityTextView.text = "Validity\nNotBefore: ${x509cert.notBefore}\nNot After: ${x509cert.notAfter}"
            signatureAlgorithmTextView.text = "Signature Algorithm: ${x509cert.sigAlgName}"
            rawSignatureTextView.text = "Raw Signature\n${x509cert.signature.toHex()}"

        } catch (e: CertificateException) {
            Log.e(TAG, "", e)
        }
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
}
