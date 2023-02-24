package online.taxcore.pos.utils

import android.graphics.BitmapFactory
import com.pax.dal.IPrinter
import com.pax.dal.exceptions.PrinterDevException
import com.pax.neptunelite.api.NeptuneLiteUser
import online.taxcore.pos.TaxCoreApp

class PaxPrinter {
    companion object {
        fun printReceipt(

            invoiceNumber: String,
            invoiceText: String = "",
            imageByteArray: ByteArray,
            invoiceFooter: String = "",
        ) {
            try {
                val printer = TaxCoreApp.dal.printer

                printer.printStr(invoiceText, null)

                val bitmap = BitmapFactory.decodeByteArray(
                    imageByteArray,
                    0,
                    imageByteArray.size,
                    BitmapFactory.Options(),
                )
                printer.printBitmap(bitmap)
                printer.printStr(invoiceFooter, null)
                printer.printStr("\n\n\n\n\n\n\n", null)
                printer.start()
            } catch (e: PrinterDevException) {
                e.printStackTrace()
            }
        }
    }
}