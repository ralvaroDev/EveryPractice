package com.example.everypractice.ui.izi

import android.app.*
import android.content.*
import android.os.*
import androidx.activity.result.contract.*
import androidx.appcompat.app.*
import com.example.everypractice.databinding.*
import pe.izipay.izipaysdk.baseclass.*
import pe.izipay.izipaysdk.baseclass.Constants.Currency.SOLES
import pe.izipay.izipaysdk.baseclass.Constants.Environment.RELEASE
import pe.izipay.izipaysdk.baseclass.Constants.Operation
import pe.izipay.izipaysdk.entities.*
import pe.izipay.izipaysdk.entities.OperationResult.TypeResult
import pe.izipay.izipaysdk.entities.OperationResult.TypeResult.ERROR
import pe.izipay.izipaysdk.entities.OperationResult.TypeResult.LOGON
import pe.izipay.izipaysdk.views.ui.*
import timber.log.*

class IziActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIziBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIziBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnInfo.setOnClickListener {
            info()
        }
        binding.btnImei.setOnClickListener {
            imei()
        }
        binding.btnCompra.setOnClickListener { purchase("1") }
        binding.btnReport.setOnClickListener { detailReport() }
        binding.btnVoid.setOnClickListener { void("1", binding.etRef.text.toString()) }


    }

    private fun purchase(amount: String) {
        sendDataToSDK(Operation.PURCHASE, amount)
    }

    private fun purchaseWithTip(tipAmount: String, waiterCode: String, amount: String) {
        sendDataToSDK(
            Operation.PURCHASE, amount, tipAmount, waiterCode = waiterCode
        )
    }

    private fun void(amount: String, transactionReferenceNumber: String) {
        sendDataToSDK(
            Operation.VOID, amount,
            referenceCode = transactionReferenceNumber
        )
    }

    private fun totalReport() {
        sendDataToSDK(Operation.TOTAL_REPORT)
    }

    private fun detailReport() {
        sendDataToSDK(Operation.DETAILED_REPORT)
    }

    private fun waiterReport() {
        sendDataToSDK(Operation.WAITER_REPORT)
    }

    private fun reprint(transactionReferenceNumber: String) {
        sendDataToSDK(Operation.VOID, referenceCode = transactionReferenceNumber)
    }

    private fun imei() {
        sendDataToSDK(Operation.IMEI)
    }

    private fun info() {
        sendDataToSDK(Operation.INFO)
    }

    private fun sendDataToSDK(
        operationType: Operation,
        amount: String = "",
        tipAmount: String = "",
        currency: Constants.Currency = SOLES,
        waiterCode: String = "",
        referenceCode: String = "",
        popUp: Boolean = true
    ) {

        val info = OperationInfo()
        info.apply {
            this.operationType = operationType
            isPopUp = popUp
            phoneIMEI = "111100000001720"
            merchantCode = "8827547"
        }
        amount.takeIf { it.isNotBlank() }?.let { info.amount = it.toDouble() }
        tipAmount.takeIf { it.isNotBlank() }?.let { info.tipAmount = it.toDouble() }
        info.currency = currency
        waiterCode.takeIf { it.isNotBlank() }?.let { info.waiterCode = it }
        referenceCode.takeIf { it.isNotBlank() }?.let { info.referenceCode = it }
        info.environment = RELEASE

        val intent = Intent(this, IzipaySDK::class.java)
        val bundle = Bundle().apply {
            putSerializable(OperationInfo.OPERATION_INFO, info)
        }
        intent.putExtras(bundle)

        launcherIzi.launch(intent)
    }

    private val launcherIzi = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data: Intent? = it.data
            processResult(data)
        }
    }

    private fun processResult(data: Intent?) {

        data?.let { bundle ->
            bundle.extras?.let {
                val results = StringBuilder()
                val opRes = it.get(OperationResult.OPERATION_RESULT) as OperationResult
                when (opRes.typeResult) {
                    ERROR -> {
                        results.append("ResponseCode = ${opRes.responseCode}\n")
                        results.append("ResponseMessage = ${opRes.responseMessage}\n")
                    }
                    LOGON -> {
                        results.apply {
                            append("ResponseCode = ${opRes.responseCode}\n")
                            append("ResponseMessage = ${opRes.responseMessage}\n")
                            append("TipOn = ${opRes.isTipOn}\n")
                            append("DollarsOn = ${opRes.isDollarsOn}\n")
                            append("iziJrOn = ${opRes.isIziJrOn}\n")
                        }
                    }
                    TypeResult.PURCHASE, TypeResult.VOID -> {
                        results.apply {
                            append("ResponseCode = ${opRes.responseCode}\n")
                            append("ResponseMessage = ${opRes.responseMessage}\n")
                            append("TransactionResult = ${opRes.transactionResult}\n")
                            append("MerchantName = ${opRes.merchantName}\n")
                            append("MaskedCard = ${opRes.maskedCard}\n")
                            append("CardBin = ${opRes.cardBin}\n")
                            append("CardBrand = ${opRes.cardBrand}\n")
                            append("BuyerNAme = ${opRes.buyerName}\n")
                            append("Currency = ${opRes.currency}\n")
                            append("Amount = ${opRes.amount}\n")
                            append("ReferenceNumber = ${opRes.referenceNumber}\n")
                            append("Installments = ${opRes.installments}\n")
                            append("TransactionId = ${opRes.transactionId}\n")
                            append("PhoneIMEI = ${opRes.phoneIMEI}\n")
                            append("TerminalId = ${opRes.terminalID}\n")
                            append("TransactionDate = ${opRes.transactionDate}\n")
                            append("TransactionTime = ${opRes.transactionTime}\n")
                            append("AID = ${opRes.aid}\n")
                            append("AppLabel = ${opRes.appLabel}\n")
                            //results.append("Crypto = ${opRes.c}\n")
                            append("BatchNumber = ${opRes.batchNumber}\n")
                            append("Voucher = ${opRes.voucher}\n")
                            append("SignatureInBase64 = ${opRes.signatureInBase64}\n")
                        }
                    }
                    /*TypeResult.INFO -> {}
                    REPORTS -> {}
                    TypeResult.REPRINT -> {}*/
                    else -> {
                        results.append("Voucher = ${opRes.voucher}\n")
                        results.append("ResponseCode = ${opRes.responseCode}\n")
                        results.append("ResponseMessage = ${opRes.responseMessage}\n")
                    }
                }
                Timber.d("Results: $results")
            }
        }

    }
}