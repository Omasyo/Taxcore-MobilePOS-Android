package online.taxcore.pos.printers

import android.graphics.BitmapFactory
import android.util.Log
import com.pax.dal.exceptions.PrinterDevException
import com.pax.dal.entity.EFontTypeAscii
import com.pax.dal.entity.EFontTypeExtCode
import online.taxcore.pos.TaxCoreApp

object PaxPrinter: ThermalPrinter {
   override fun print(
       invoiceNumber: String,
       invoiceText: String,
       imageByteArray: ByteArray,
       invoiceFooter: String,
    ) {
        try {
            val printer = TaxCoreApp.dal.printer

            printer.fontSet(EFontTypeAscii.FONT_8_16, EFontTypeExtCode.FONT_16_16)

            printer.printStr(formatInvoiceText(invoiceText), null)

            Log.d("INVoice Text", "printReceipt: $invoiceText")

            val bitmap = BitmapFactory.decodeByteArray(
                imageByteArray,
                0,
                imageByteArray.size,
                BitmapFactory.Options(),
            )
            printer.printBitmap(bitmap)
            printer.printStr(formatInvoiceText(invoiceFooter), null)
            printer.printStr("\n\n\n\n\n\n\n\n", null)
            printer.start()
        } catch (e: PrinterDevException) {
            e.printStackTrace()
        }
    }

    private fun formatInvoiceText(text: String): String {
        val stringBuilder = StringBuilder()
        val lines = text.lines()
        for (line in lines) {
            val padding = (47 - line.length)/2
            val paddedLine = line.padStart(line.length + padding)

            stringBuilder.append("$paddedLine\n")
        }

        return stringBuilder.toString()
    }
}